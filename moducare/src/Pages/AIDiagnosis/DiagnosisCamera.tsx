import React, {useRef, useState} from 'react';
import {Pressable, StyleSheet, View, Image} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {Camera, useCameraDevice} from 'react-native-vision-camera';
import {
  PinchGestureHandler,
  GestureHandlerRootView,
  GestureHandlerGestureEvent,
} from 'react-native-gesture-handler';
import CustomText from '../../Components/Common/CustomText';
import CustomButtom from '../../Components/Common/CustomButton';
import {colors} from '../../constants/colors';
import usePermission from '../../hook/usePermission';
import {usePopup} from '../../hook/usePopup';
import PopupModal from '../../Components/Common/PopupModal';

const DiagnosisCamera = ({navigation}: {navigation: any}) => {
  const {checkPermission} = usePermission('CAM');
  const {visible, option, content, showPopup, hidePopup} = usePopup();
  const [res, setRes] = useState();
  const cameraRef = useRef<Camera>(null);
  const device = useCameraDevice('back');
  const [imageUri, setImageUri] = useState<string | null>(null);

  // 줌 상태 관리 (줌 값 초기화는 1)
  const [zoom, setZoom] = useState(1);

  // 핀치 제스처에서 사용되는 scale 값 (줌 인/아웃)
  let lastScale = useRef(1); // lastScale은 ref로 관리하여 계속 값을 유지합니다.

  // 핀치 제스처 처리
  const handlePinchGesture = (event: GestureHandlerGestureEvent) => {
    const scale = event.nativeEvent.scale;

    // scale 값에 누적된 lastScale을 곱해서 줌 상태를 업데이트
    let newZoom = lastScale.current * scale;

    // 줌 값 제한 (최소 1, 최대 16)
    newZoom = Math.max(1, Math.min(newZoom, 10));

    setZoom(newZoom); // 상태로 줌 값 업데이트
  };

  // 핀치 제스처 끝났을 때 lastScale을 갱신
  const handlePinchEnd = () => {
    lastScale.current = zoom; // 현재 줌 값을 마지막 scale로 설정
  };

  // 사진 촬영 함수
  const handleTakePhoto = async () => {
    const hasPermission = await checkPermission();
    if (!hasPermission) {
      return;
    }

    if (cameraRef.current) {
      try {
        showPopup({
          option: 'Loading',
          content: '잠시만 기다려주세요.',
        });
        const photo = await cameraRef.current.takePhoto({
          flash: 'off',
        });
        hidePopup();
        setImageUri(`file://` + photo.path); // 사진 URI 설정
        console.log(photo.path);
      } catch (error) {
        console.error('사진 촬영 실패:', error);
      }
    }
  };

  // 검사 진행 함수
  const handleAiDiagnosis = async () => {
    const formData = new FormData();
    formData.append('file', {
      uri: imageUri,
      type: 'image/jpeg',
      name: 'photo.jpg',
    });
    setZoom(1);
    setImageUri(null);
    navigation.navigate('aiLoading', {
      file: formData,
      type: 'Camera',
    });
  };

  // 이미지 삭제 함수
  const handleImageDelete = () => {
    setZoom(1);
    setImageUri(null);
  };

  return (
    <SafeAreaView style={styles.container}>
      <CustomText label="두피가 잘 나오게 사진을 찍어주세요." size={20} />
      {imageUri ? (
        <>
          <View style={styles.cameraArea}>
            <Image source={{uri: imageUri}} style={styles.capturedImage} />
          </View>
          <View style={styles.PhotoBtnArea}>
            <CustomButtom
              label="재 촬영"
              size="small"
              onPress={handleImageDelete}
            />
            <CustomButtom
              label="검사 진행"
              size="small"
              onPress={handleAiDiagnosis}
            />
          </View>
        </>
      ) : (
        <>
          <GestureHandlerRootView style={styles.cameraArea}>
            <PinchGestureHandler
              onGestureEvent={handlePinchGesture}
              onHandlerStateChange={handlePinchEnd}>
              <View style={styles.cameraArea}>
                <Camera
                  ref={cameraRef}
                  style={styles.cameraStyle}
                  device={device}
                  isActive={true}
                  focusable={true}
                  photo={true}
                  zoom={zoom} // 줌 값 적용
                />
              </View>
            </PinchGestureHandler>
          </GestureHandlerRootView>
          <CustomText label={`카메라 줌 : ${zoom.toFixed(1)} x`} />
          <View style={styles.zoomArea}>
            <View style={[styles.zoomTest, {width: `${zoom * 10}%`}]} />
          </View>
          <Pressable style={styles.BtnArea} onPress={handleTakePhoto} />
        </>
      )}
      <PopupModal
        visible={visible}
        option={option}
        onClose={() => {
          if (option === 'confirmMark') {
            hidePopup();
            navigation.navigate('aiResult', {
              type: 'diagnosis',
              diagnosisResult: res,
            });
          }
        }}
        content={content}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.WHITE,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 10,
  },
  cameraArea: {
    borderWidth: 1,
    borderColor: colors.WHITE,
    width: '100%',
    height: 300,
    marginVertical: 10,
    justifyContent: 'center',
    alignItems: 'center', // 카메라 영역 가운데 정렬
  },
  cameraStyle: {
    width: '100%',
    height: '100%',
    borderWidth: 1,
  },
  BtnArea: {
    width: 80,
    height: 80,
    borderWidth: 8,
    borderRadius: 50,
    borderColor: colors.MAIN,
    backgroundColor: colors.WHITE,
    marginTop: 50,
    elevation: 4,
    justifyContent: 'center',
    alignItems: 'center',
  },
  capturedImage: {
    width: '100%',
    height: '100%',
    resizeMode: 'cover', // 이미지 크기에 맞게 조정
  },
  PhotoBtnArea: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 50,
    marginTop: 50,
    elevation: 4,
  },
  zoomArea: {
    width: 300,
    height: 10,
    backgroundColor: colors.WHITE_GRAY,
  },
  zoomTest: {
    backgroundColor: colors.MAIN,
    height: 10,
    borderRadius: 10,
  },
});

export default DiagnosisCamera;
