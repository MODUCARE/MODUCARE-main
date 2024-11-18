import {
  InfiniteData,
  QueryKey,
  useInfiniteQuery,
  UseInfiniteQueryOptions,
} from '@tanstack/react-query';
import {FeedType, getFeed} from '../api/challenge-api';
import {ResponseError} from '../types/common';

const useGetFeeds = (
  challengeId: number,
  queryOptions?: UseInfiniteQueryOptions<
    FeedType[],
    ResponseError,
    InfiniteData<FeedType[], number>,
    FeedType[],
    QueryKey,
    number
  >,
) => {
  return useInfiniteQuery({
    queryFn: ({pageParam}) => getFeed(challengeId, pageParam),
    queryKey: ['challengeFeed', challengeId],
    initialPageParam: 0,
    getNextPageParam: (lastPage, allPages) => {
      const lastPost = lastPage[lastPage.length - 1];
      return lastPost ? allPages.length : undefined;
    },
    ...queryOptions,
  });
};

export default useGetFeeds;
