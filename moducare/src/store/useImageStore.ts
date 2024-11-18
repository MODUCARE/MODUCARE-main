import {ImagePickerResponse} from 'react-native-image-picker';
import {create} from 'zustand';

interface ImageStore {
  img: ImagePickerResponse | undefined;
  regDate: string;
  imgType: string;

  setImg: (img: ImagePickerResponse | undefined) => void;
  setRegDate: (date: string) => void;
  setImgType: (type: string) => void;
}

const useImageStore = create<ImageStore>(set => ({
  img: undefined,
  regDate: '',
  imgType: '',
  setImg: img => set({img}),
  setRegDate: date => set({regDate: date}),
  setImgType: type => set({imgType: type}),
}));

export default useImageStore;
