import {setEncryptStorage} from '../util';
import axiosInstance from '../util/axios';

const postCreateChallenge = async (file?: FormData): Promise<void> => {
  try {
    const {data} = await axiosInstance.post('challenges', file, {
      headers: {'Content-Type': 'multipart/form-data'},
    });

    return data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

type getListType = {
  challengeId: number;
  challengeImg: string;
  challengeUser: number;
  challengeName: string;
};

const getChallengeList = async (): Promise<getListType[]> => {
  const {data} = await axiosInstance.get('challenges');

  return data;
};

type getMyListType = {
  challengeId: number;
  challengeImg: string;
  challengeName: string;
  isDone: number;
};

const getMyChallengeList = async (): Promise<getMyListType[]> => {
  try {
    const {data} = await axiosInstance.get('my-challenges');

    return data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const postJoinChallenge = async (challengeId: number): Promise<void> => {
  try {
    const {data} = await axiosInstance.post(`my-challenges/${challengeId}/in`);

    return data;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

const postOutChallenge = async (challengeId: number): Promise<void> => {
  const {data} = await axiosInstance.post(`my-challenges/${challengeId}/out`);

  return data;
};

const postFeedChallenge = async (
  challengeId: number,
  file: FormData,
): Promise<void> => {
  try {
    const {data} = await axiosInstance.post(
      `challenge-feeds/${challengeId}`,
      file,
      {
        headers: {'Content-Type': 'multipart/form-data'},
      },
    );
    setEncryptStorage('isDone', 1);
    return data;
  } catch (error) {
    console.log('에러', error);
    throw error;
  }
};

type FeedType = {
  feedId: number;
  feedImg: string;
  feedUserName: string;
  content: string;
  feedRegDate: string;
  like: number;
  isLiked: number;
};

const getFeed = async (
  challengeId: number,
  page: number,
): Promise<FeedType[]> => {
  try {
    const {data} = await axiosInstance.get(
      `/challenge-feeds?cid=${challengeId}&page=${page}`,
    );
    return data;
  } catch (error) {
    console.log('피드가져오기 에러', error);
    throw error;
  }
};

const postLike = async (feedId: number, status: number): Promise<number> => {
  try {
    const {data} = await axiosInstance.post(`favorites/${feedId}`, {status});

    return data;
  } catch (error) {
    console.log('좋아요 에러', error);
    throw error;
  }
};

export {
  postCreateChallenge,
  postFeedChallenge,
  postJoinChallenge,
  postLike,
  postOutChallenge,
  getChallengeList,
  getFeed,
  getMyChallengeList,
};

export type {FeedType, getListType, getMyListType};
