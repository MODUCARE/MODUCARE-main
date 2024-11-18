import React, {useRef, useState} from 'react';
import {Pressable, StyleSheet, View, Image, Alert} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {colors} from '../../constants/colors';
import CustomText from '../../Components/Common/CustomText';
import WebView from 'react-native-webview';
import ViewShot from 'react-native-view-shot';
import CustomButtom from '../../Components/Common/CustomButton';

const DiagnosisIOT = ({navigation}) => {
  const [capturedImage, setCapturedImage] = useState<string | null>(null);
  const viewShotRef = useRef<ViewShot | null>(null); // ViewShot의 ref

  const captureScreen = () => {
    // ViewShot으로 캡쳐
    if (viewShotRef.current) {
      viewShotRef.current.capture().then(uri => {
        // 캡쳐된 이미지의 URI를 상태에 저장
        setCapturedImage(uri);
        console.log(uri);
      });
    }
  };

  const handleAiDiagnosis = async () => {
    //검사진행
    const formData = new FormData();
    formData.append('file', {
      uri: capturedImage, // 사진 URI
      type: 'image/jpeg', // 이미지 파일 타입 (예시로 jpeg 사용)
      name: 'photo.jpg', // 파일명
    });
    setCapturedImage(null);
    navigation.navigate('aiLoading', {
      file: formData,
      type: 'IOT',
    });
  };

  const handleImageDelete = () => {
    setCapturedImage(null);
  };

  const handleWebViewError = () => {
    navigation.navigate('aiFail', {
      type: 'IOTError',
      content: '먼저 기기와의 연결을 진행해주세요!',
    });
  };

  return (
    <SafeAreaView style={styles.container}>
      <CustomText label="두피가 잘 나오게 사진을 찍어주세요." size={20} />

      {capturedImage ? (
        <>
          <View style={styles.capturedImageContainer}>
            <Image source={{uri: capturedImage}} style={styles.capturedImage} />
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
          <ViewShot ref={viewShotRef} options={{format: 'jpg', quality: 0.9}}>
            <View style={styles.cameraArea}>
              <WebView
                source={{uri: 'http://192.168.137.225:8000'}}
                onError={handleWebViewError}
              />
            </View>
          </ViewShot>
          <Pressable style={styles.BtnArea} onPress={captureScreen} />
        </>
      )}
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
    width: 400,
    height: 300,
    marginVertical: 10,
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
  },
  capturedImageContainer: {
    marginTop: 20,
    alignItems: 'center',
    width: 400,
    height: 300,
  },
  capturedImage: {
    width: 400,
    height: 300,
  },
  PhotoBtnArea: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 50,
    marginTop: 50,
  },
});

export default DiagnosisIOT;
