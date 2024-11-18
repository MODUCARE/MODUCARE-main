import React from 'react';
import {
  Alert,
  Dimensions,
  Image,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import CustomButtom from '../../Components/Common/CustomButton';
import {colors} from '../../constants/colors';
import SvgIconAtom from '../../Components/Common/SvgIconAtom';
import {
  CameraOptions,
  launchImageLibrary,
  ImageLibraryOptions,
} from 'react-native-image-picker';
import {postFeedChallenge} from '../../api/challenge-api';
import {View} from 'react-native';
import FontAwesome from 'react-native-vector-icons/FontAwesome';
import {useNavigation} from '@react-navigation/native';
import {usePopup} from '../../hook/usePopup';
import PopupModal from '../../Components/Common/PopupModal';

interface Action {
  title: string;
  type: 'capture' | 'library';
  options: CameraOptions | ImageLibraryOptions;
}

const options: Action = {
  title: 'Select Image',
  type: 'library',
  options: {
    selectionLimit: 1,
    mediaType: 'photo',
    includeBase64: false,
  },
};

const HEIGHT = Dimensions.get('window').height;

const ChallengeWritePage = ({route}) => {
  const navigation = useNavigation();
  const {id} = route.params;
  const [challengeContent, setChallengeContent] = React.useState<string>('');
  const [imgConfig, setImgConfig] = React.useState<any>(null);
  const {visible, option, content, showPopup, hidePopup} = usePopup();

  const openImageLibrary = async () => {
    const images = await launchImageLibrary(options);
    if (images.assets) {
      setImgConfig(images);
    }
  };

  const imageUpload = async () => {
    if (imgConfig === null) {
      showPopup({option: 'Alert', content: '사진을 첨부해주세요!'});
      return;
    }
    showPopup({option: 'Loading', content: '인증피드 작성 중입니다...'});
    try {
      const file = imgConfig.assets[0];
      const uri = file.uri; // 이미지 URI
      const fileName = file.fileName; // 이미지 파일명
      const type = file.type; // 이미지 타입
      const formData = new FormData();
      formData.append('content', challengeContent);
      formData.append('file', {
        uri: uri,
        type: type,
        name: fileName,
      });

      await postFeedChallenge(id, formData);
      // 초기화
      setImgConfig(null);
      setChallengeContent('');
      hidePopup();
      showPopup({
        option: 'confirmMark',
        content: '인증피드 작성 완료!',
      });
      // 페이지 전환
    } catch (error) {
      showPopup({option: 'Alert', content: '인증피드 작성에 실패했습니다!'});
    }
  };

  const handleImgDelete = () => {
    setImgConfig(null);
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false}>
        {imgConfig !== null ? (
          <View style={styles.imageContainer}>
            <Pressable style={styles.closeBtn} onPress={handleImgDelete}>
              <FontAwesome name="close" size={20} color={'#ffffff'} />
            </Pressable>
            <Image
              source={{uri: imgConfig?.assets?.[0].uri}}
              style={{width: 300, height: 300}}
            />
          </View>
        ) : (
          <Pressable
            style={styles.UploadArea}
            onPress={() => openImageLibrary()}>
            <SvgIconAtom name="Camera" style={{margin: 'auto'}} />
            <Text style={styles.UploadText}>
              챌린지 인증에 사용할 사진을 남에게 공유해요.
            </Text>
          </Pressable>
        )}
        <TextInput
          style={styles.InputArea}
          placeholder="내용을 입력하세요."
          value={challengeContent}
          onChangeText={setChallengeContent}
          textAlignVertical="top"
          multiline={true}
        />
        <CustomButtom label="인증 완료" onPress={imageUpload} />
      </ScrollView>
      <PopupModal
        visible={visible}
        option={option}
        onClose={() => {
          if (option === 'confirmMark') {
            hidePopup();
            navigation.goBack();
          } else {
            hidePopup();
          }
        }}
        content={content}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.WHITE,
    padding: 20,
  },
  UploadArea: {
    marginVertical: 20,
    height: 300,
    alignItems: 'center',
    justifyContent: 'center',
  },
  UploadText: {
    textAlign: 'center',
    color: colors.MAIN,
    fontSize: 14,
    fontFamily: 'Pretendard-Medium',
  },
  InputArea: {
    backgroundColor: colors.WHITE_GRAY,
    borderRadius: 10,
    paddingHorizontal: 10,
    flex: 0.5,
    marginBottom: 30,
    height: HEIGHT * 0.2,
  },
  imageContainer: {
    position: 'relative',
    width: 300,
    alignItems: 'center',
    margin: 10,
    marginVertical: 20,
    marginHorizontal: 'auto',
  },
  closeBtn: {
    width: 30,
    height: 30,
    borderRadius: 50,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: colors.MAIN,
    position: 'absolute',
    right: 0,
    margin: 5,
    zIndex: 1000,
  },
});

export default ChallengeWritePage;
