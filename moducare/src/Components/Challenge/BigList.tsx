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
  uri?: string;
  title?: string;
  user?: number;
}

const BigList = ({uri, title = '', user = 0, ...props}: ListProps) => {
  return (
    <>
      <Pressable style={styles.container} {...props}>
        {uri === null ? (
          <SvgIconAtom name="Basic" size={80} />
        ) : (
          <>
            <Image style={styles.ImgArea} source={{uri}} />
          </>
        )}
        <View style={styles.detailArea}>
          <CustomText label={title} size={18} />
          <View style={styles.dateArea}>
            <Text style={styles.dateArea}>{user} / 10</Text>
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
