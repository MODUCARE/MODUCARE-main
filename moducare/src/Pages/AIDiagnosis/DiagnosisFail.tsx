import React from 'react';
import {StyleSheet, ToastAndroid} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {colors} from '../../constants/colors';
import CustomText from '../../Components/Common/CustomText';
import {useFocusEffect} from '@react-navigation/native';
import LottieView from 'lottie-react-native';
import {fail} from '../../assets/lottie';
import CustomButtom from '../../Components/Common/CustomButton';
import {BackHandler} from 'react-native';

const DiagnosisFail = ({route, navigation}) => {
  const {type, content} = route.params;

  const handleMovePage = () => {
    if (type === 'IOT') {
      navigation.navigate('aiIOT');
    } else if (type === 'IOTError') {
      navigation.navigate('ai');
    } else {
      navigation.navigate('aiCamera');
    }
  };
  useFocusEffect(
    React.useCallback(() => {
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
      <LottieView source={fail} autoPlay loop={false} style={styles.robot} />
      <CustomText
        label={content ? content : '두피 이미지가 아니에요! 재촬영 해주세요!'}
        size={20}
      />
      <CustomButtom
        label={content ? '이전으로' : '재촬영하기'}
        onPress={handleMovePage}
      />
      <CustomButtom
        label="메인으로"
        onPress={() => navigation.navigate('bottomNavigate')}
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
    gap: 20,
  },
  robot: {
    width: 100,
    height: 100,
    alignSelf: 'center',
  },
  confirmMark: {
    width: 100,
    height: 100,
    alignSelf: 'center',
  },
});

export default DiagnosisFail;
