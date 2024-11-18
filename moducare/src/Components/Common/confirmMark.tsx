import React from 'react';
import {StyleSheet, View, Text} from 'react-native';
import LottieView from 'lottie-react-native';
import confirmMark from '../../assets/lottie/confirmMark.json';
import {colors} from '../../constants/colors';

export default function ConfirmMark() {
  return (
    <View style={style.container}>
      <Text style={style.doneText}>생성 완료</Text>
      <LottieView
        source={confirmMark}
        autoPlay
        loop={false}
        style={style.confirmMark}
      />
    </View>
  );
}

const style = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    gap: 40,
  },
  confirmMark: {
    width: 50,
    height: 50,
  },
  doneText: {
    fontSize: 40,
    fontWeight: '900',
    color: colors.MAIN,
    fontFamily: 'Pretendard-Medium',
    verticalAlign: 'middle',
  },
});
