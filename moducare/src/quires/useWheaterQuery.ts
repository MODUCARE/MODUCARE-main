import {useQuery} from '@tanstack/react-query';
import {postWeather} from '../api/weather-api';
import {UseQueryCustomOptions} from '../types/common';

export const WEATHER_QUERY_KEY = 'weather';

interface WeatherQuery {
  sido: string;
  gugun: string;
}

export const useWeatherQuery = (
  params: WeatherQuery,
  options?: UseQueryCustomOptions,
) => {
  const {data, isLoading, error} = useQuery({
    queryKey: [WEATHER_QUERY_KEY, params.sido, params.gugun],
    queryFn: () => postWeather(params.sido, params.gugun),
    enabled: Boolean(params.sido && params.gugun),
    staleTime: 1000 * 60 * 5,
    refetchOnMount: true, // 컴포넌트 마운트시 항상 새로운 데이터 fetch
    ...options,
  });

  return {data, isLoading, error};
};
