import {useState} from 'react';

type PopupOption = 'Alert' | 'confirmMark' | 'Loading';

interface PopupConfig {
  option: PopupOption;
  content: string;
}

export const usePopup = () => {
  const [visible, setVisible] = useState(false);
  const [popupOption, setPopupOption] = useState<PopupOption>('Alert');
  const [popupContent, setPopupContent] = useState('');

  const showPopup = ({option, content}: PopupConfig) => {
    setPopupOption(option);
    setPopupContent(content);
    setVisible(true);
  };

  const hidePopup = () => {
    setVisible(false);
  };

  return {
    visible,
    popupOption,
    popupContent,
    showPopup,
    hidePopup,
  };
};
