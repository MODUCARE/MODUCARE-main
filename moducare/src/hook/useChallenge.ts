import {useQuery} from '@tanstack/react-query';
import {getChallengeList, getMyChallengeList} from '../api/challenge-api';

const useGetAllList = () => {
  const {data, isSuccess, isPending, refetch} = useQuery({
    queryKey: ['challenge', 'getAll'],
    queryFn: getChallengeList,
  });

  return {data, isPending, isSuccess, refetch};
};

const useGetMyList = () => {
  const {data, isSuccess, isPending, refetch} = useQuery({
    queryKey: ['challenge', 'getMy'],
    queryFn: getMyChallengeList,
  });
  return {data, isPending, isSuccess, refetch};
};

const useChallenge = () => {
  const getAllList = useGetAllList();
  const getMyList = useGetMyList();
  const AllLoading = getAllList.isPending;
  const MyLoading = getMyList.isPending;

  return {
    getAllList,
    getMyList,
    AllLoading,
    MyLoading,
  };
};

export default useChallenge;
