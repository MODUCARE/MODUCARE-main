import React from 'react';
import {Alert, Pressable, StyleSheet, Text, View} from 'react-native';
import {colors} from '../../constants/colors';
import SvgIconAtom from './SvgIconAtom';

interface FeedNavProps {
  onPress: () => void;
  onExit: () => void;
}
const FeedNav = ({onPress, onExit}: FeedNavProps) => {
  const handleExit = () => {
    Alert.alert(
      '챌린지를 나가시겠습니까?',
      '챌린지 인원이 없을 경우 챌린지는 소멸합니다.',
      [
        {
          text: '나가기',
          onPress: onExit,
        },
        {
          text: '취소',
          style: 'cancel',
        },
      ],
    );
  };
  return (
    <View style={styles.container}>
      <Pressable style={[styles.BtnArea, styles.PickArea]}>
        <SvgIconAtom name="List" />
        <Text style={[styles.text, {color: colors.WHITE}]}>인증목록</Text>
      </Pressable>
      <View style={styles.line} />
      <Pressable style={styles.BtnArea} onPress={onPress}>
        <SvgIconAtom name="Ok" />
        <Text style={styles.text}>인증하기</Text>
      </Pressable>
      <View style={styles.line} />
      <Pressable style={styles.BtnArea} onPress={handleExit}>
        <SvgIconAtom name="Exit" />
        <Text style={styles.text}>나가기</Text>
      </Pressable>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    backgroundColor: colors.WHITE,
    flexDirection: 'row',
    justifyContent: 'center',
    alignContent: 'center',
    marginHorizontal: 'auto',
    marginVertical: 30,
    paddingHorizontal: 5,
    paddingVertical: 5,
    borderColor: colors.MAIN,
    borderWidth: 1,
    borderRadius: 20,
    elevation: 20,
  },
  line: {
    width: 1,
    backgroundColor: colors.GRAY,
    marginHorizontal: 5,
  },
  BtnArea: {
    padding: 10,
    borderRadius: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  PickArea: {
    backgroundColor: colors.MAIN,
  },
  text: {
    fontSize: 16,
    fontFamily: 'Pretendard-Medium',
    color: colors.MAIN,
    alignSelf: 'center',
    marginLeft: 10,
  },
});

export default FeedNav;
