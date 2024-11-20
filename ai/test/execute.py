import torch
import torch.nn.functional as F  # Softmax를 사용하려면 추가
from PIL import Image
from torchvision import transforms

model_path = '../scalp_weights/'  # 예측하려는 이미지 경로
scalp_image_path = 'scalp_image.jpg'

# 모델 로드
device = torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu')
model = torch.load(model_path + 'aram_model7.pt', map_location=device)  # 학습된 모델 로드
model.eval()  # 모델을 평가 모드로 전환

# 이미지 전처리
transform = transforms.Compose([
    transforms.Resize([600, 600]),  # 훈련 시 사용된 크기와 동일한 크기
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])  # 훈련에 사용된 정규화 값
])

# 이미지 로딩
image = Image.open(scalp_image_path)
image = transform(image).unsqueeze(0)  # 배치 차원을 추가 (1, C, H, W)

# 모델에 이미지 입력하여 예측 수행
image = image.to(device)
with torch.no_grad():  # 예측 시에는 gradient 계산을 하지 않음
    output = model(image)

# 출력값 (로짓) 확인
print("Raw output (logits):", output)

# Softmax 적용하여 각 클래스에 대한 확률 계산
probabilities = F.softmax(output, dim=1)  # dim=1은 클래스 차원에 대해 Softmax 적용

# 예측된 클래스 인덱스 및 확률
_, predicted = torch.max(output, 1)  # 가장 높은 확률의 클래스 인덱스 찾기
predicted_class = predicted.item()

# 예측된 클래스와 확률 출력
print(f'Predicted class: {predicted_class}')
print(f'Probabilities for each class: {probabilities}')  # 각 클래스의 확률 출력

# 예측된 클래스 이름 출력
# class_names = ['Class 0', 'Class 1', 'Class 2', 'Class 3']  # 두피 중증도 4가지
class_names = ['Class 0', 'Class 1']  # 두피 여부 o/x
print(f'Predicted class name: {class_names[predicted_class]}')
