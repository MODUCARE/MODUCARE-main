import * as React from 'react';
import {
  Image,
  Pressable,
  PressableProps,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import CustomText from '../Common/CustomText';
import SvgIconAtom from '../Common/SvgIconAtom';
import {colors} from '../../constants/colors';

interface ListProps extends PressableProps {
  isPhoto?: boolean;
}

const BigList = ({isPhoto = false, ...props}: ListProps) => {
  return (
    <>
      <Pressable style={styles.container} {...props}>
        {isPhoto ? (
          <SvgIconAtom name="Basic" size={80} />
        ) : (
          <>
            <Image
              style={styles.ImgArea}
              source={require('../../assets/test.png')}
            />
          </>
        )}
        <View style={styles.detailArea}>
          <CustomText label="찬 바람으로 머리말리기" size={18} />
          <View style={styles.dateArea}>
            <Text style={styles.dateArea}>5 / 10</Text>
          </View>
        </View>
      </Pressable>
      <View style={styles.line} />
    </>
  );
};

export default BigList;

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignContent: 'center',
    justifyContent: 'space-between',
    marginBottom: 15,
    paddingHorizontal: 20,
  },
  ImgArea: {
    width: 80,
    height: 80,
    borderRadius: 100,
  },
  detailArea: {
    justifyContent: 'space-between',
  },
  dateArea: {
    justifyContent: 'flex-end',
    textAlign: 'right',
    fontFamily: 'Pretendard-Bold',
    color: colors.BLACK,
    fontSize: 12,
  },
  line: {
    marginHorizontal: 20,
    height: 1,
    backgroundColor: colors.WHITE_GRAY,
    marginVertical: 10,
  },
});