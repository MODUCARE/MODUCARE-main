import axiosInstance from '../util/axios';

interface LatestProduct {
  link: string;
  imgSrc: string;
}

const getLastestProduct = async (): Promise<LatestProduct> => {
  try {
    console.log('getLastestProduct');
    const response = await axiosInstance.get('product/latest');
    console.log(response.data, 'response');
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
    console.log(imgSrc, link, 'postLastestProduct');
    const response = await axiosInstance.post('product/latest', {
      imgSrc,
      link,
    });
    console.log(response.data, 'response');
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export {getLastestProduct, postLastestProduct};
export type {LatestProduct};
