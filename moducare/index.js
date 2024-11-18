// /**
//  * @format
//  */

// import {AppRegistry} from 'react-native';
// import App from './src/App';
// import {name as appName} from './app.json';
// import 'react-native-gesture-handler';
// import notifee from '@notifee/react-native';
// import {EventType} from '@notifee/react-native';

// notifee.onBackgroundEvent(({type, detail}) => {
//   const {notification, pressAction} = detail;

//   console.log('이게뭐임', pressAction);
//   if (type === EventType.PRESS && pressAction) {
//     // pressAction이 존재할 때만 처리하도록 추가 검증
//     if (pressAction.id === 'challengeNoti') {
//       console.log('백그라운드 클릭');
//       // 추가적인 처리를 여기에서 진행
//     }
//   } else {
//     console.log('pressAction이 없음');
//   }
// });

// AppRegistry.registerComponent(appName, () => App);
import {AppRegistry, Linking} from 'react-native';
import App from './src/App';
import {name as appName} from './app.json';
import 'react-native-gesture-handler';
import notifee from '@notifee/react-native';
import {EventType} from '@notifee/react-native';

notifee.onBackgroundEvent(async ({type, detail}) => {
  try {
    const {notification, pressAction} = detail;
    // pressAction이 null이나 undefined인지 체크
    if (type === EventType.PRESS && pressAction) {
      // pressAction이 존재하고 id가 유효한 경우에만 처리
      if (pressAction.id === 'challengeNoti') {
        // console.log('백그라운드 클릭');
        Linking.openURL('moducare://챌린지');
        // 추가적인 처리를 여기에서 진행
      }
      // Remove the notification
      await notifee.cancelNotification(notification.id);
    }
  } catch (error) {
    console.log('에러발생', error);
    throw error;
  }
});

AppRegistry.registerComponent(appName, () => App);
