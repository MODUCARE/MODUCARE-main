import React, {useState} from 'react';
import {StyleSheet, ToastAndroid} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {colors} from '../../constants/colors';
import CustomText from '../../Components/Common/CustomText';
import {useFocusEffect} from '@react-navigation/native';
import {useAiDiagnosisMutation} from '../../quires/useReportsQuery';
import LottieView from 'lottie-react-native';
import {confirmMark, robot} from '../../assets/lottie';
import {ResponseAiDiagnosis} from '../../api/ai-api';
import CustomButtom from '../../Components/Common/CustomButton';
import {BackHandler} from 'react-native';

const Loading = ({route, navigation}) => {
  const [isFinish, setFinish] = useState(false);
  const [data, setData] = useState<ResponseAiDiagnosis>();
  const {mutateAsync} = useAiDiagnosisMutation();
  const {file, type} = route.params;

  const postAiData = async () => {
    console.log('넘어온파일', file);
    try {
      const res = await mutateAsync(file);
      setData(res);
      setFinish(true);
    } catch (error) {
      navigation.navigate('aiFail', {type});
    }
  };

  const handleMovePage = () => {
    setFinish(false);
    navigation.navigate('aiResult', {
      type: 'diagnosis',
      diagnosisResult: data,
    });
  };
  useFocusEffect(
    React.useCallback(() => {
      postAiData();

      let backPress = 0;
      const backAction = () => {
        if (backPress < 1) {
          backPress++;
          ToastAndroid.show(
            '한번 더 누르시면 앱이 종료됩니다.',
            ToastAndroid.SHORT,
          );
          setTimeout(() => {
            backPress = 0;
          }, 2000);
          return true;
        } else {
          BackHandler.exitApp();
        }
        return true;
      };
      BackHandler.addEventListener('hardwareBackPress', backAction);

      return () => {
        BackHandler.removeEventListener('hardwareBackPress', backAction);
      };
    }, []),
  );

  return (
    <SafeAreaView style={styles.container}>
      {isFinish ? (
        <>
          <LottieView
            source={confirmMark}
            autoPlay
            loop={false}
            style={styles.confirmMark}
          />
          <CustomText
            label="AI가 결과를 보내드렸어요! 확인해볼까요?"
            size={20}
          />
          <CustomButtom label="결과보기" onPress={handleMovePage} />
        </>
      ) : (
        <>
          <LottieView
            source={robot}
            autoPlay
            loop={true}
            style={styles.robot}
          />
          <CustomText label="AI가 두피를 들여다 보고 있어요!" size={20} />
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
    gap: 20,
  },
  robot: {
    width: 250,
    height: 250,
    alignSelf: 'center',
  },
  confirmMark: {
    width: 100,
    height: 100,
    alignSelf: 'center',
  },
});

export default Loading;
