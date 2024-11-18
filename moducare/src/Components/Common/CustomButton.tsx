import React from 'react';
import {
  Pressable,
  StyleSheet,
  Text,
  PressableProps,
  Dimensions,
} from 'react-native';
import {colors} from '../../constants/colors';

interface CustomButtomProps extends PressableProps {
  label: string;
  variant?: 'filled' | 'outlined';
  size?: 'large' | 'medium' | 'small';
  inValid?: boolean;
}

const deviceHeight = Dimensions.get('screen').height;
const CustomButtom = ({
  label,
  variant = 'filled',
  size = 'large',
  inValid = false,
  ...props
}: CustomButtomProps) => {
  return (
    <Pressable
      disabled={inValid}
      style={[
        styles.container,
        inValid && styles.inVaild,
        styles[variant],
        styles[size],
      ]}
      {...props}>
      <Text style={[styles.text, styles[`${variant}Text`]]}>{label}</Text>
    </Pressable>
  );
};

const styles = StyleSheet.create({
  container: {
    borderRadius: 10,
    justifyContent: 'center',
    marginHorizontal: 'auto',
  },
  inVaild: {
    opacity: 0.5,
  },
  filled: {
    backgroundColor: colors.MAIN,
  },
  outlined: {
    borderColor: colors.MAIN,
    borderWidth: 3,
  },
  large: {
    width: '100%',
    paddingVertical: deviceHeight > 700 ? 15 : 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  medium: {
    width: '50%',
    paddingVertical: deviceHeight > 700 ? 12 : 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  small: {
    width: '30%',
    paddingVertical: deviceHeight > 700 ? 12 : 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    fontSize: 20,
    fontWeight: '100',
  },
  filledText: {
    color: colors.WHITE,
    fontFamily: 'Pretendard-SemiBold',
  },
  outlinedText: {
    color: colors.MAIN,
    fontFamily: 'Pretendard-SemiBold',
  },
});

export default CustomButtom;
