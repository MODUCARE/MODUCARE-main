import axiosInstance from '../util/axios';

const getLineDiaryData = async () => {
  try {
    const response = await axiosInstance.get('diaries/line');
    const mappedData = response.data.map((item: any) => ({
      ...item,
      img: {uri: item.img},
    }));
    return mappedData;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const getTopDiaryData = async () => {
  try {
    const response = await axiosInstance.get('diaries/top');
    const mappedData = response.data.map((item: any) => ({
      ...item,
      img: {uri: item.img},
    }));
    return mappedData;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const getReportData = async () => {
  try {
    const response = await axiosInstance.get('diagnosis');
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const getReportDetailData = async (id: number) => {
  try {
    const response = await axiosInstance.get(`/diagnosis/${id}`);
    return response.data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const postHairImg = async (formData: FormData, imgType: string | undefined) => {
  try {
    const data = await axiosInstance.post('diaries', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      params: {
        type: imgType,
      },
    });

    return data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export {
  getLineDiaryData,
  getTopDiaryData,
  getReportData,
  getReportDetailData,
  postHairImg,
};
