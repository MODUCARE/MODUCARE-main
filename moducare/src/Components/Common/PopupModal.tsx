import React from 'react';
import {Modal, View, StyleSheet} from 'react-native';
import CustomText from './CustomText';
import LottieView from 'lottie-react-native';
import {confirmMark, Alert, Loading} from '../../assets/lottie';

import CustomButton from './CustomButton';

interface PopupModalProps {
  visible: boolean;
  onClose: () => void;
  content: string;
  option: 'Alert' | 'confirmMark' | 'Loading';
  confirm?: () => void | 'none';
  cancel?: () => void | 'none';
}

const PopupModal = ({
  visible,
  onClose,
  content,
  option,
  confirm,
  cancel,
}: PopupModalProps) => {
  const lottieSource =
    option === 'Alert'
      ? Alert
      : option === 'confirmMark'
      ? confirmMark
      : Loading;

  const handleConfirm = () => {
    confirm && confirm();
    onClose();
  };

  const handleCancel = () => {
    cancel && cancel();
    onClose();
  };

  return (
    <Modal
      animationType="fade"
      transparent={true}
      visible={visible}
      onRequestClose={onClose}
      // 모달이 전체 화면을 덮을 수 있도록 설정
      statusBarTranslucent>
      <View style={styles.overlay}>
        <View style={styles.modalContainer}>
          <LottieView
            source={lottieSource}
            autoPlay
            loop={option === 'Loading'}
            style={[styles.confirmMark]}
          />
          <CustomText label={content} />
          <View style={styles.buttonArea}>
            {confirm !== 'none' && (
              <CustomButton label="확인" onPress={handleConfirm} size="small" />
            )}
            {cancel !== 'none' && (
              <CustomButton label="취소" onPress={handleCancel} size="small" />
            )}
          </View>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
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
    // 그림자 효과
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
  buttonArea: {
    flexDirection: 'row',
    gap: 10,
  },
});

export default PopupModal;
