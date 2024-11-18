import {useEffect} from 'react';
import useThemeStore from '../store/useThemeStore';
import {ThemeMode} from '../types/common';
import {getEncryptStorage, setEncryptStorage} from '../util';
import {useColorScheme} from 'react-native';

const useThemeStorage = () => {
  const systemTheme = useColorScheme();
  const {theme, isSystem, setTheme, setSystemTheme} = useThemeStore();

  const setMode = async (mode: ThemeMode) => {
    await setEncryptStorage('themeMode', mode);
    setTheme(mode);
  };

  const setSystem = async (flag: boolean) => {
    await setEncryptStorage('themeSystem', flag);
    setSystemTheme(flag);
  };

  useEffect(() => {
    async () => {
      const mode = (await getEncryptStorage('themeMode')) ?? 'light';
      const systemMode = (await getEncryptStorage('themeSystem')) ?? 'false';
      const newMode = systemMode ? systemTheme : mode;
      setTheme(newMode);
      setSystemTheme(systemMode);
    };
  }, [setTheme, setSystemTheme, systemTheme]);

  return {theme, isSystem, setMode, setSystem};
};

export default useThemeStorage;
