import axios from 'axios';
import axiosInstance from '../util/axios';
import Config from 'react-native-config';

type Location = {
  address_name: string;
  code: string;
  region_1depth_name: string;
  region_2depth_name: string;
  region_3depth_name: string;
  region_4depth_name: string;
  region_type: string;
  x: number;
  y: number;
};

const getLocation = async (
  latitude: number,
  longitude: number,
): Promise<Location> => {
  try {
    const res = await axios.get(
      `https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${longitude}&y=${latitude}`,
      {
        headers: {
          Authorization: `KakaoAK ${Config.KAKAO_LOCATION_API_KEY}`,
        },
      },
    );
    return res.data.documents[0];
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const postWeather = async (sido: string, gugun: string) => {
  try {
    const response = await axiosInstance.post(`${Config.API_URL}weather`, {
      sido,
      gugun,
    });

    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export {getLocation, postWeather};
