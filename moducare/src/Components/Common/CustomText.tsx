import React from 'react';
import {StyleSheet, Text, TextProps} from 'react-native';
import {colors} from '../../constants/colors';
// import {colors} from '../../constants/colors';

interface CustomTextProps extends TextProps {
  label: string | undefined;
  variant?: 'bold' | 'regular';
  size?: number;
  color?: string;
}

const CustomText = ({
  label,
  variant = 'bold',
  size = 15,
  color = colors.BLACK,
  ...props
}: CustomTextProps) => {
  return (
    <Text
      style={[styles[`${variant}Text`], {fontSize: size, color: color}]}
      {...props}>
      {label}
    </Text>
  );
};

const styles = StyleSheet.create({
  boldText: {
    fontFamily: 'Pretendard-Bold',
    verticalAlign: 'middle',
  },
  regularText: {
    fontFamily: 'Pretendard-Medium',
    verticalAlign: 'middle',
  },
});

export default CustomText;
