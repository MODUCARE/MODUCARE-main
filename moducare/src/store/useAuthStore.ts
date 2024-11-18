import {create} from 'zustand';
import {createJSONStorage, persist} from 'zustand/middleware';
import {NaverLoginResponse} from '../types/socialLogin';
import AsyncStorage from '@react-native-async-storage/async-storage';

type AuthStore = {
  user: string;
  isLoggedIn: boolean;
  naverLoginSuccess: NaverLoginResponse['successResponse'];
  naverLoginFailure: NaverLoginResponse['failureResponse'];

  setIsLoggedIn: (value: boolean) => void;
  setNaverLoginSuccess: (value: NaverLoginResponse['successResponse']) => void;
  setNaverLoginFailure: (value: NaverLoginResponse['failureResponse']) => void;
  setUser: (value: string) => void;
};

const useAuthStore = create(
  persist<AuthStore>(
    set => ({
      user: '사용자',
      isLoggedIn: false,
      naverLoginSuccess: undefined,
      naverLoginFailure: undefined,

      setUser: value => set({user: value}),
      setIsLoggedIn: value => set({isLoggedIn: value}),
      setNaverLoginSuccess: value => set({naverLoginSuccess: value}),
      setNaverLoginFailure: value => set({naverLoginFailure: value}),
    }),
    {
      name: 'login state',
      storage: createJSONStorage(() => AsyncStorage),
    },
  ),
);

export default useAuthStore;
