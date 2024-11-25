import * as React from 'react';
import {View, Text, StyleSheet, SafeAreaView, Pressable} from 'react-native';
import CustomButtom from '../../Components/Common/CustomButton';
import CustomText from '../../Components/Common/CustomText';
import SvgIconAtom from '../../Components/Common/SvgIconAtom';
import {colors} from '../../constants/colors';
import {useFocusEffect} from '@react-navigation/native';
import {usePopup} from '../../hook/usePopup';
import PopupModal from '../../Components/Common/PopupModal';

export default function DiagnosisPage({navigation}: {navigation: any}) {
  const {
    visible,
    option,
    content,
    popupConfirm,
    popupCancel,
    showPopup,
    hidePopup,
  } = usePopup();
  const [product, setProduct] = React.useState<string>('');

  const handlePress = (device: string) => {
    setProduct(device);
  };

  const handleMovePage = () => {
    product === 'camera'
      ? navigation.navigate('aiCamera')
      : navigation.navigate('aiIOT');
  };

  useFocusEffect(
    //하단 nav로 이동했을 때
    React.useCallback(() => {
      return () => {
        setProduct('');
      };
    }, []),
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.MainArea}>
        <Pressable
          style={({pressed}) => [
            styles.pickArea,
            product === 'iot' && styles.pick,
            pressed && product !== 'iot' && styles.pick,
          ]}
          onPress={() => handlePress('iot')}>
          <View>
            <Text style={styles.text}>MODU CARE 기기</Text>
            <Text style={[styles.text, styles.subText]}>
              보다 정확한 진단이 가능해요
            </Text>
          </View>
          <SvgIconAtom name="Modu" />
        </Pressable>
        <Pressable
          style={({pressed}) => [
            styles.pickArea,
            product === 'camera' && styles.pick,
            pressed && product !== 'camera' && styles.pick,
          ]}
          onPress={() => handlePress('camera')}>
          <View>
            <Text style={styles.text}>핸드폰 카메라</Text>
            <Text style={[styles.text, styles.subText]}>
              간편하고 검사해보아요
            </Text>
          </View>
          <SvgIconAtom name="Phone" />
        </Pressable>
        <View style={styles.TextArea}>
          <CustomText label="핸드폰 카메라 촬영법" />
          <CustomText label="  1. 카메라 줌을 알맞게 설정해주세요." />
          <CustomText label="  2. 초점을 맞추고 두피에 밀착해주세요." />
          <CustomText label="  3. 촬영 후 AI 검사를 진행해주세요." />
        </View>
      </View>
      <View style={styles.BtnArea}>
        {product !== '' ? (
          <CustomButtom label="두피 촬영하기" onPress={handleMovePage} />
        ) : (
          <CustomButtom
            label="두피 촬영하기"
            variant="outlined"
            onPress={() =>
              showPopup({
                option: 'Alert',
                content: '촬영 기기를 선택해주세요.',
                confirm: () => {
                  hidePopup();
                },
              })
            }
          />
        )}
      </View>
      <PopupModal
        visible={visible}
        onClose={hidePopup}
        content={content}
        confirm={popupConfirm}
        cancel={popupCancel}
        option={option}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
    height: '100%',
    backgroundColor: '#fff',
    padding: 10,
  },
  MainArea: {
    flex: 0.8,
  },
  BtnArea: {
    flex: 0.1,
    width: '100%',
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  pickArea: {
    width: '100%',
    flexDirection: 'row',
    justifyContent: 'space-between',
    borderColor: colors.WHITE_GRAY,
    borderWidth: 3,
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 10,
    marginVertical: 5,
  },
  pick: {
    borderColor: colors.MAIN,
  },
  TextArea: {
    width: '100%',
    marginVertical: 10,
    paddingHorizontal: 10,
  },
  text: {
    fontFamily: 'Pretendard-Bold',
    color: colors.BLACK,
    fontSize: 15,
  },
  subText: {
    fontFamily: 'Pretendard-Bold',
    color: colors.GRAY,
    fontSize: 10,
  },
});
