import React from 'react';
import {StyleSheet, ModalProps, Modal} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import CustomButtom from '../Common/CustomButton';

interface CustomModalProps extends ModalProps {
  isVisible: boolean;
  hide: () => void;
}

const CreateChallengeModal = ({isVisible, hide}: CustomModalProps) => {
  return (
    <Modal visible={isVisible} animationType="slide">
      <SafeAreaView style={styles.container} onTouchEnd={hide}>
        <CustomButtom label="생성하기" />
      </SafeAreaView>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 0.5,
    borderWidth: 1,
    borderTopLeftRadius: 10,
    borderTopRightRadius: 10,
    justifyContent: 'center',
  },
});

export default CreateChallengeModal;
