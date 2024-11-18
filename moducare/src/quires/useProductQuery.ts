import {useMutation, useQuery} from '@tanstack/react-query';
import {getLastestProduct, postLastestProduct} from '../api/product-api';
import queryClient from '../util/queryClient';
import {getPick} from '../api/ai-api';

export const QueryKey = {
  latest: 'latest',
  recommend: 'recommend',
};

export const useLatestProductQuery = () => {
  return useQuery({
    queryFn: getLastestProduct,
    queryKey: [QueryKey.latest],
    staleTime: 1000 * 60 * 5,
  });
};

export const usePostLastestProductMutation = () => {
  return useMutation({
    mutationFn: ({imgSrc, link}: {imgSrc: string; link: string}) =>
      postLastestProduct(imgSrc, link),
    onSuccess: () => {
      queryClient.invalidateQueries({queryKey: [QueryKey.latest]});
    },
  });
};

// export const useProductListQuery = (headType: number, result: number[]) => {
//   return useQuery({
//     queryFn: () => getPick(headType, result),
//     queryKey: [QueryKey.recommend],
//   });
// };
export const useProductListQuery = (headType: number, result: number[]) => {
  const {data, isPending} = useQuery({
    queryFn: () => getPick(headType, result),
    queryKey: [QueryKey.recommend],
  });

  return {data, isPending};
};
