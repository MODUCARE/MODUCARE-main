import React from 'react';
import {Pressable, StyleSheet, Text, View} from 'react-native';
import {colors} from '../../constants/colors';
import SvgIconAtom from './SvgIconAtom';

interface FeedNavProps {
  onPress: () => void;
}
const FeedNav = ({onPress}: FeedNavProps) => {
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
      <Pressable style={styles.BtnArea}>
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