import React from 'react';
import {Linking, SafeAreaView, StyleSheet, View} from 'react-native';
import WebView, {WebViewMessageEvent} from 'react-native-webview';
import {colors} from '../constants/colors';
import useAuth from '../hook/useAuth';
import {getEncryptStorage, setEncryptStorage} from '../util';
import {setHeader} from '../util/headers';
import {postLogin} from '../api/login-api';
import Config from 'react-native-config';
import axios from 'axios';

const KakaoLoginPage = ({navigation}) => {
  const handleRedirect = url => {
    console.log('url', url);
    if (url.startsWith(Config.KAKAO_REDIRECT)) {
      // URL에서 인증 코드 추출
      const code = url.replace(`${Config.KAKAO_REDIRECT}?code=`, '');
      console.log('code', code);
      // requestkakao(code);
      navigation.navigate('ok', {code});
    }
  };

  const handleOnMessage = (event: WebViewMessageEvent) => {
    if (event.nativeEvent.url.includes(`${Config.KAKAO_REDIRECT}?code=`)) {
      const code = event.nativeEvent.url.replace(
        `${Config.KAKAO_REDIRECT}?code=`,
        '',
      );
      console.log(code);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <WebView
        source={{
          uri: `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${Config.KAKAO_API}&redirect_uri=${Config.KAKAO_REDIRECT}`,
        }}
        injectedJavaScript="window.ReactNativeWebView.postMessage('')"
        onMessage={handleOnMessage}
        onNavigationStateChange={navState => handleRedirect(navState.url)}
      />
    </SafeAreaView>
  );
};
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.WHITE,
  },
});

export default KakaoLoginPage;
