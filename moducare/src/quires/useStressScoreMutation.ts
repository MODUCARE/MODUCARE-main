import axiosInstance from '../util/axios';
import {useMutation} from '@tanstack/react-query';

export const useStressScoreMutation = () => {
  return useMutation({
    mutationFn: async (data: number) => {
      await axiosInstance.post('stress', data);
    },
  });
};
