import * as React from 'react';
import {View, StyleSheet, Dimensions, ViewStyle} from 'react-native';

const {width} = Dimensions.get('window');

export default function ItemBox({
  children,
  style,
}: {
  children: React.ReactNode;
  style?: ViewStyle;
}) {
  return (
    <View style={[styles.container, style]}>
      <View>{children}</View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    elevation: 5,
    width: width * 0.9,
    borderRadius: 10,
    backgroundColor: '#fff',
    padding: 15,
  },
});
