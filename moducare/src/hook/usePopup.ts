import {useState} from 'react';

type PopupOption = 'Alert' | 'confirmMark' | 'Loading';

interface PopupConfig {
  option: PopupOption;
  content: string;
  confirm?: () => void | 'none';
  cancel?: () => void | 'none';
}

export const usePopup = () => {
  const [visible, setVisible] = useState(false);
  const [option, setOption] = useState<PopupOption>('Alert');
  const [content, setContent] = useState('');
  const [popupConfirm, setPopupConfirm] = useState<(() => void) | 'none'>();
  const [popupCancel, setPopupCancel] = useState<(() => void) | 'none'>();

  const showPopup = ({option, content, confirm, cancel}: PopupConfig) => {
    setOption(option);
    setContent(content);
    // 함수일 경우 함수 자체를 저장할 경우 이를 실행하고 리턴된 결과를 저장
    // () => confirm을 사용하는 경우 버튼이 클릭되었을 때 '함수가 실행되는 것'을 저장
    setPopupConfirm(() => confirm ?? 'none');
    setPopupCancel(() => cancel ?? 'none');
    setVisible(true);
  };

  const hidePopup = () => {
    setVisible(false);
  };

  return {
    visible,
    option,
    content,
    showPopup,
    hidePopup,
    popupConfirm,
    popupCancel,
  };
};
