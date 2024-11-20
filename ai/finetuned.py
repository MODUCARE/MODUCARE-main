import copy
import os

import torch
import torch.nn as nn
import torch.optim as optim
from efficientnet_pytorch import EfficientNet
from torch.utils.data import DataLoader
from torchvision import transforms, datasets
from tqdm import tqdm

os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"] = "3"
#
device = torch.device("cuda")
random_seed = 100
torch.manual_seed(random_seed)

train_name = 'model1'
PATH = './scalp_weights/'

# 데이터 경로
data_train_path = './' + train_name + '/train'
data_val_path = './' + train_name + '/validation'


def main():
    # 데이터 증강
    transforms_train = transforms.Compose([
        transforms.Resize([600, 600]),
        transforms.RandomHorizontalFlip(p=0.5),
        transforms.RandomRotation(15),
        transforms.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])

    transforms_val = transforms.Compose([
        transforms.Resize([600, 600]),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
    ])

    # 데이터셋 및 로더
    train_data_set = datasets.ImageFolder(data_train_path, transform=transforms_train)
    val_data_set = datasets.ImageFolder(data_val_path, transform=transforms_val)

    dataloaders = {
        'train': DataLoader(train_data_set, batch_size=6, shuffle=True, num_workers=4),
        'val': DataLoader(val_data_set, batch_size=6, shuffle=False, num_workers=4)
    }

    # 모델 로드
    model1 = EfficientNet.from_name('efficientnet-b7')  # EfficientNet 모델 로드
    num_classes = 4  # 클래스 개수 (저장 시 정의한 값)
    model1._fc = nn.Linear(model1._fc.in_features, num_classes)

    # state_dict 불러오기
    state_dict_path = PATH + 'state_dict_' + train_name + '.pt'
    state_dict = torch.load(state_dict_path, map_location=device)

    # state_dict를 모델에 로드
    model1.load_state_dict(state_dict)

    # 모델을 디바이스로 이동
    model1 = model1.to(device)

    # 출력층만 학습 가능하도록 설정 (1단계)
    model1._fc.requires_grad = True  # 출력층만 학습 가능

    # Loss 및 Optimizer
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model1._fc.parameters(), lr=1e-4)  # 출력층만 최적화
    scheduler = optim.lr_scheduler.StepLR(optimizer, step_size=7, gamma=0.1)

    # 학습 함수
    def train_model(model, criterion, optimizer, scheduler, num_epochs=25):
        best_model_wts = copy.deepcopy(model.state_dict())
        best_acc = 0.0

        for epoch in range(num_epochs):
            print(f"Epoch {epoch}/{num_epochs - 1}")
            print('-' * 10)

            for phase in ['train', 'val']:
                if phase == 'train':
                    model.train()
                else:
                    model.eval()

                running_loss = 0.0
                running_corrects = 0

                for inputs, labels in tqdm(dataloaders[phase]):
                    inputs, labels = inputs.to(device), labels.to(device)
                    optimizer.zero_grad()

                    with torch.set_grad_enabled(phase == 'train'):
                        outputs = model(inputs)
                        _, preds = torch.max(outputs, 1)
                        loss = criterion(outputs, labels)

                        if phase == 'train':
                            loss.backward()
                            optimizer.step()

                    running_loss += loss.item() * inputs.size(0)
                    running_corrects += torch.sum(preds == labels.data)

                epoch_loss = running_loss / len(dataloaders[phase].dataset)
                epoch_acc = running_corrects.double() / len(dataloaders[phase].dataset)

                print(f"{phase} Loss: {epoch_loss:.4f} Acc: {epoch_acc:.4f}")

                # 검증 성능 개선 시 모델 저장
                if phase == 'val' and epoch_acc > best_acc:
                    best_acc = epoch_acc
                    best_model_wts = copy.deepcopy(model.state_dict())

            scheduler.step()

        print(f"Best val Acc: {best_acc:.4f}")
        model.load_state_dict(best_model_wts)
        return model

    # 1단계: 출력층만 학습
    num_epochs = 10
    model1 = train_model(model1, criterion, optimizer, scheduler, num_epochs=num_epochs)

    # 2단계: 전체 모델 파인튜닝
    for param in model1.parameters():
        param.requires_grad = True  # 모든 파라미터 학습 가능

    optimizer = optim.Adam(model1.parameters(), lr=1e-5)  # 전체 파라미터 최적화
    scheduler = optim.lr_scheduler.StepLR(optimizer, step_size=7, gamma=0.1)

    # 전체 파라미터로 추가 학습
    num_epochs = 15
    model1 = train_model(model1, criterion, optimizer, scheduler, num_epochs=num_epochs)

    # 파인튜닝된 모델 저장
    torch.save(model1.state_dict(), PATH + 'state_dict_' + train_name + '.pt')


def saveStateDict():
    # 모델의 state_dict 저장
    # 모델 전체를 저장한 경우
    model1 = torch.load(PATH + 'aram_' + train_name + '.pt', map_location=device)
    model1 = model1.to(device)

    torch.save(model1.state_dict(), PATH + 'state_dict_' + train_name + '.pt')


if __name__ == '__main__':
    main()
