import React from 'react';
import {Image, StyleSheet} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {colors} from '../constants/colors';
import {PERMISSIONS, requestMultiple} from 'react-native-permissions';

requestMultiple([
  PERMISSIONS.ANDROID.CAMERA,
  PERMISSIONS.ANDROID.READ_EXTERNAL_STORAGE,
  PERMISSIONS.ANDROID.WRITE_EXTERNAL_STORAGE,
  PERMISSIONS.ANDROID.READ_MEDIA_IMAGES,
]).then(result => {
  console.log(result);
});

const SplashPage = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Image style={styles.img} source={require('../assets/Splash.png')} />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.MAIN,
    justifyContent: 'center',
    alignContent: 'center',
  },
  img: {
    margin: 'auto',
  },
});

export default SplashPage;
