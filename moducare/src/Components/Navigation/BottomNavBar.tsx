import * as React from 'react';
import {
  Dimensions,
  View,
  StyleSheet,
  TouchableOpacity,
  Text,
  Modal,
  Pressable,
} from 'react-native';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import MainPage from '../../Pages/MainPage';
import DiagnosisPage from '../../Pages/AIDiagnosis/DiagnosisPage';
import ChallengeMainPage from '../../Pages/Challenge/ChallengeMainPage';
import ReportNav from './ReportNav';
import EditUserPage from '../../Pages/User/EditUserPage';
import Octicons from 'react-native-vector-icons/Octicons';
import Feather from 'react-native-vector-icons/Feather';
import SimpleLineIcons from 'react-native-vector-icons/SimpleLineIcons';
import {useState} from 'react';
import SlideModal from '../Common/SlideModal';
import CustomButton from '../Common/CustomButton';
import {useNavigation} from '@react-navigation/native';
import {getEncryptStorage} from '../../util';
import useAuth from '../../hook/useAuth';
import LottieView from 'lottie-react-native';
import CustomText from '../Common/CustomText';
import {confirmMark, Alert, Loading} from '../../assets/lottie';
import {colors} from '../../constants';
import Entypo from 'react-native-vector-icons/Entypo';

const Tab = createBottomTabNavigator();

const HEIGHT = Dimensions.get('window').height;
const WIDTH = Dimensions.get('window').width;

const HomeIcon = ({color, size}: {color: string; size: number}) => (
  <Octicons name="home" color={color} size={size} />
);

const ChallengeIcon = ({color, size}: {color: string; size: number}) => (
  <Feather name="check-circle" color={color} size={size} />
);

const DiagnosisIcon = () => (
  <View style={styles.walkButton}>
    <SimpleLineIcons name="camera" color="white" size={40} />
  </View>
);

const ReportIcon = ({color, size}: {color: string; size: number}) => (
  <Feather name="clipboard" color={color} size={size} />
);

function CustomTabBarButton() {
  const navigation = useNavigation<any>();
  const [moreOpen, setMoreOpen] = useState(false);
  const {delUserMutation} = useAuth();

  // popup modal 관련
  const [popupVisible, setPopupVisible] = useState(false);
  const [popupOption, setPopupOption] = useState<
    'Alert' | 'confirmMark' | 'Loading'
  >('Alert');
  const [popupContent, setPopupContent] = useState('');
  const [button, setButton] = useState<(() => void) | null>(null);

  const withdrawUser = async (): Promise<void> => {
    console.log('회원 탈퇴 시작');
    // await deleteMember();
    delUserMutation.mutate();
  };
  const {logoutMutation} = useAuth();
  const logout = async () => {
    const fcmToken = await getEncryptStorage('fcmToken');
    logoutMutation.mutate(fcmToken);
  };

  const lottieSource =
    popupOption === 'Alert'
      ? Alert
      : popupOption === 'confirmMark'
      ? confirmMark
      : Loading;

  // PopUP modal 우선 여기 수정하고 나중에 넣을겡
  const openLogoutModal = () => {
    setPopupOption('Alert');
    setPopupContent('정말로 로그아웃 하시겠어요?!');
    setPopupVisible(true);
    setButton(() => logout);
  };
  const openWithdrawModal = () => {
    setPopupOption('Alert');
    setPopupContent('정말로 회원 탈퇴 하시겠어요?!');
    setPopupVisible(true);
    setButton(() => withdrawUser);
  };

  const [themeMode, setThemeMode] = useState('light');

  const handleThemePick = (type: string) => {
    setThemeMode(type);
  };

  return (
    <>
      <TouchableOpacity
        style={styles.moreButton}
        onPress={() => setMoreOpen(!moreOpen)}>
        <Feather name="more-horizontal" color="#8E8E8E" size={25} />
        <Text style={styles.moreText}>더보기</Text>
      </TouchableOpacity>
      <SlideModal visible={moreOpen} onClose={() => setMoreOpen(!moreOpen)}>
        <Text style={styles.ThemeTitle}>화면테마</Text>
        <View style={styles.moreModal}>
          <View style={styles.ThemeArea}>
            <Pressable
              style={styles.ThemeBtn}
              onPress={() => handleThemePick('system')}>
              <CustomText label="시스템설정" size={18} />
              {themeMode === 'system' && (
                <Entypo name="check" size={18} color={colors.NEGATIVE} />
              )}
            </Pressable>
            <Pressable
              style={styles.ThemeBtn}
              onPress={() => handleThemePick('light')}>
              <CustomText label="라이트" size={18} />
              {themeMode === 'light' && (
                <Entypo name="check" size={18} color={colors.NEGATIVE} />
              )}
            </Pressable>
            <Pressable
              style={styles.ThemeBtn}
              onPress={() => handleThemePick('dark')}>
              <CustomText label="다크" size={18} />
              {themeMode === 'dark' && (
                <Entypo name="check" size={18} color={colors.NEGATIVE} />
              )}
            </Pressable>
          </View>
          <CustomButton
            label="내 정보 수정"
            onPress={() => {
              setMoreOpen(false);
              navigation.navigate('회원 정보 수정');
            }}
          />
          <CustomButton label="로그아웃" onPress={openLogoutModal} />
          <CustomButton label="회원 탈퇴" onPress={openWithdrawModal} />
        </View>
        <Modal
          animationType="fade"
          transparent={true}
          visible={popupVisible}
          onRequestClose={() => setPopupVisible(false)}
          // 모달이 전체 화면을 덮을 수 있도록 설정
          statusBarTranslucent>
          <View style={styles.overlay}>
            <View style={styles.modalContainer}>
              <LottieView
                source={lottieSource}
                autoPlay
                loop={popupOption === 'Loading'}
                style={[styles.confirmMark]}
              />
              <CustomText label={popupContent} />
              {popupOption !== 'Loading' && (
                <View style={styles.buttonContainer}>
                  <CustomButton label="확인" onPress={button} size="small" />
                  <CustomButton
                    label="취소"
                    onPress={() => setPopupVisible(false)}
                    size="small"
                  />
                </View>
              )}
            </View>
          </View>
        </Modal>
      </SlideModal>
    </>
  );
}

export default function BottomNavBar({navigation}: {navigation: any}) {
  return (
    <Tab.Navigator
      initialRouteName="홈"
      screenOptions={{
        headerShown: false,
        tabBarActiveTintColor: '#B9834E',
        tabBarStyle: {
          borderTopWidth: 1,
          shadowColor: 'transparent',
          height: HEIGHT * 0.07,
          position: 'absolute',
          paddingTop: 5,
        },
        tabBarLabelStyle: {
          paddingBottom: 8,
          fontSize: 12,
        },
      }}>
      <Tab.Screen
        name="홈"
        component={MainPage}
        options={{
          tabBarIcon: HomeIcon,
        }}
      />
      <Tab.Screen
        name="챌린지"
        component={ChallengeMainPage}
        options={{
          tabBarIcon: ChallengeIcon,
          unmountOnBlur: true,
        }}
      />
      <Tab.Screen
        name="Diagnosis"
        component={DiagnosisPage}
        options={{
          tabBarLabel: () => null,
          tabBarIcon: DiagnosisIcon,
        }}
        listeners={() => ({
          tabPress: e => {
            e.preventDefault();
            navigation.navigate('ai');
          },
        })}
      />
      <Tab.Screen
        name="리포트"
        component={ReportNav}
        options={{
          tabBarIcon: ReportIcon,
          unmountOnBlur: true,
        }}
      />
      <Tab.Screen
        name="더보기"
        component={EditUserPage}
        options={{
          tabBarButton: () => CustomTabBarButton(),
        }}
      />
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  walkButton: {
    width: WIDTH * 0.17,
    height: WIDTH * 0.17,
    borderRadius: WIDTH * 0.09,
    top: HEIGHT * -0.025,
    backgroundColor: '#A28E81',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {width: 0, height: 2},
    shadowOpacity: 0.5,
    shadowRadius: 4,
    elevation: 5,
  },
  moreButton: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  moreText: {
    marginTop: 4,
    paddingBottom: 8,
    fontSize: 12,
    color: '#8E8E8E',
  },
  moreModal: {
    // flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    gap: 15,
  },

  // popup modal 관련
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    gap: 20,
    backgroundColor: 'white',
    borderRadius: 12,
    padding: 20,
    width: '80%',
    maxWidth: 500,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  confirmMark: {
    width: 60,
    height: 60,
    alignSelf: 'center',
  },
  buttonContainer: {
    flexDirection: 'row',
    gap: 10,
  },
  ThemeTitle: {
    fontSize: 22,
    color: colors.MAIN,
    fontFamily: 'Pretendard-ExtraBold',
    marginBottom: 10,
  },
  ThemeArea: {
    width: '100%',
    justifyContent: 'space-around',
    flexDirection: 'row',
  },
  ThemeBtn: {
    width: 100,
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'row',
  },
});
