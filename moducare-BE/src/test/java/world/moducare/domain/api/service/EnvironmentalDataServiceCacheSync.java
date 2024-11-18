//package world.moducare.domain.api.service;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import world.moducare.domain.api.dto.WeatherRequestDto;
//import world.moducare.domain.api.dto.WeatherResponseDto;
//
////@SpringBootTest
////public class EnvironmentalDataServiceCacheSync {
////
////    @Autowired
////    private EnvironmentalDataService environmentalDataService;
////
////    @Autowired
////    private RedisTemplate<String, Object> redisTemplate;
////
////    private final EnvironmentalDataServiceNoCacheSync serviceNoCacheSync;
////
////    public EnvironmentalDataServiceCacheSync(EnvironmentalDataServiceNoCacheSync serviceNoCacheSync) {
////        this.serviceNoCacheSync = serviceNoCacheSync;
////    }
////
////    @Test
////    public void testWithCachingAndAsync() {
////        WeatherRequestDto requestDto = new WeatherRequestDto("서울", "종로구");
////
////        // 캐시를 비워 캐시 미스를 유도
////        redisTemplate.delete("weatherData");
////
////        long startTime = System.currentTimeMillis();
////
////        // 첫 번째 호출 (캐시 미스)
////        WeatherResponseDto response = environmentalDataService.getEnvironmentalData(requestDto);
////
////        long endTime = System.currentTimeMillis();
////        System.out.println("첫 번째 호출 (캐시 미스) 시간: " + (endTime - startTime) + "ms");
////
////        // 두 번째 호출 (캐시 히트)
////        startTime = System.currentTimeMillis();
////
////        response = environmentalDataService.getEnvironmentalData(requestDto);
////
////        endTime = System.currentTimeMillis();
////        System.out.println("두 번째 호출 (캐시 히트) 시간: " + (endTime - startTime) + "ms");
////    }
////
////    @Test
////    public void testWithoutCachingAndAsync() {
////        WeatherRequestDto requestDto = new WeatherRequestDto("서울", "종로구");
////
////        long startTime = System.currentTimeMillis();
////
////        // 캐시와 비동기 처리를 사용하지 않는 동기적 호출
////        WeatherResponseDto response = serviceNoCacheSync.getEnvironmentalData(requestDto);
////
////        long endTime = System.currentTimeMillis();
////        System.out.println("캐시와 비동기 처리 없이 호출 시간: " + (endTime - startTime) + "ms");
////    }
////}
////
//@SpringBootTest
//public class EnvironmentalDataServiceCacheSync {
//
//    @Autowired
//    private EnvironmentalDataService environmentalDataService;
//
//    @Autowired
//    private EnvironmentalDataServiceNoCacheSync serviceNoCacheSync;
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Test
//    public void testWithCachingAndAsync() {
//        WeatherRequestDto requestDto = new WeatherRequestDto("서울", "종로구");
//
//        // 캐시를 비워 캐시 미스를 유도
//        redisTemplate.delete("weatherData");
//
//        long startTime = System.currentTimeMillis();
//
//        // 첫 번째 호출 (캐시 미스)
//        WeatherResponseDto response = environmentalDataService.getEnvironmentalData(requestDto);
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("첫 번째 호출 (캐시 미스) 시간: " + (endTime - startTime) + "ms");
//
//        // 두 번째 호출 (캐시 히트)
//        startTime = System.currentTimeMillis();
//
//        response = environmentalDataService.getEnvironmentalData(requestDto);
//
//        endTime = System.currentTimeMillis();
//        System.out.println("두 번째 호출 (캐시 히트) 시간: " + (endTime - startTime) + "ms");
//    }
//
//    @Test
//    public void testWithoutCachingAndAsync() {
//        WeatherRequestDto requestDto = new WeatherRequestDto("서울", "종로구");
//
//        long startTime = System.currentTimeMillis();
//
//        // 캐시와 비동기 처리를 사용하지 않는 동기적 호출
//        WeatherResponseDto response = serviceNoCacheSync.getEnvironmentalData(requestDto);
//
//        long endTime = System.currentTimeMillis();
//        System.out.println("캐시와 비동기 처리 없이 호출 시간: " + (endTime - startTime) + "ms");
//    }
//}