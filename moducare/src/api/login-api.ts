import {getEncryptStorage} from '../util';
import axiosInstance from './../util/axios';

type RequestMember = {
  name: string;
  birth: string;
};

type RequestLogin = {
  fcmToken: string;
  accessToken: string;
  registerId: 'kakao' | 'naver' | 'google';
};
interface ResponseLogin {
  jwtAccessToken: string;
  refreshToken: string;
  name: string;
  email: string;
  birth: string;
}

const postLogin = async ({
  registerId,
  fcmToken,
  accessToken,
}: RequestLogin): Promise<ResponseLogin> => {
  try {
    console.log('registerId---------------', registerId);
    console.log('fcmToken---------------', fcmToken);
    console.log('accessToken---------------', accessToken);
    const {data} = await axiosInstance.post(`members/login/${registerId}`, {
      accessToken,
      fcmToken,
    });
    console.log('data---------------', data);
    return data;
  } catch (error) {
    console.log('백엔드에서 오는 에러?', error);
    console.error(error);
    throw error;
  }
};

type ResponseAccess = {
  accessToken: string;
};

const postRefreshToken = async (): Promise<ResponseAccess> => {
  try {
    const refreshToken = await getEncryptStorage('refreshToken');

    if (!refreshToken) {
      throw new Error('리프레시 토큰이 없습니다.');
    }
    const {data} = await axiosInstance.post('tokens/refresh', {refreshToken});
    return data;
  } catch (error) {
    console.error('리프레시 토큰 에러', error);
    throw error;
  }
};

const postLogout = async (fcmToken: string): Promise<void> => {
  try {
    const {data} = await axiosInstance.post('members/logout', {fcmToken});

    return data;
  } catch (error) {
    console.error('로그아웃 실패', error);
    throw error;
  }
};

const deleteMember = async (): Promise<void> => {
  try {
    const {data} = await axiosInstance.delete('members/');

    return data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

const putMember = async (userInfo: RequestMember): Promise<string> => {
  try {
    const {data} = await axiosInstance.put('members/modify', null, {
      params: {
        name: userInfo.name,
        birth: userInfo.birth,
      },
    });

    return data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};
type socialLoginType = {
  registerId: string;
  accessToken: string;
  fcmToken: string;
};
type socialResponse = {
  jwtAccessToken: string;
  refreshToken: string;
};

const postLoginKaKao = async ({
  accessToken,
  fcmToken,
}: socialLoginType): Promise<socialResponse> => {
  const {data} = await axiosInstance.post('members/social/kakao', {
    accessToken,
    fcmToken,
  });

  return data;
};

export {
  postLoginKaKao,
  postLogin,
  postRefreshToken,
  postLogout,
  deleteMember,
  putMember,
};
export type {
  socialLoginType,
  socialResponse,
  RequestLogin,
  ResponseLogin,
  ResponseAccess,
};
