import axiosInstance from '../util/axios';

interface LatestProduct {
  link: string;
  imgSrc: string;
}

const getLastestProduct = async (): Promise<LatestProduct> => {
  try {
    const response = await axiosInstance.get('product/latest');
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const postLastestProduct = async (
  imgSrc: string,
  link: string,
): Promise<LatestProduct> => {
  try {
    const response = await axiosInstance.post('product/latest', {
      imgSrc,
      link,
    });
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export {getLastestProduct, postLastestProduct};
export type {LatestProduct};
