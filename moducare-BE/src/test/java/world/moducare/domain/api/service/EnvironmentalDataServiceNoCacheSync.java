//package world.moducare.domain.api.service;
//
//import world.moducare.domain.api.dto.WeatherRequestDto;
//import world.moducare.domain.api.dto.WeatherResponseDto;
//import world.moducare.domain.api.externalApi.dust.DustApiService;
//import world.moducare.domain.api.externalApi.infrared.UvApiService;
//import world.moducare.domain.api.externalApi.temperature.TemperatureApiService;
//
////public class EnvironmentalDataServiceNoCacheSync {
////
////
////    private final DustApiService dustApiService;
////    private final TemperatureApiService temperatureApiService;
////    private final UvApiService uvApiService;
////
////    public EnvironmentalDataServiceNoCacheSync(DustApiService dustApiService, TemperatureApiService temperatureApiService, UvApiService uvApiService) {
////        this.dustApiService = dustApiService;
////        this.temperatureApiService = temperatureApiService;
////        this.uvApiService = uvApiService;
////    }
////
////    public WeatherResponseDto getEnvironmentalData(WeatherRequestDto weatherRequestDto) {
////
////        try {
////            // 각 API를 동기적으로 호출
////            int dustData = dustApiService.callDustApiSync(weatherRequestDto);
////            int temperatureData = temperatureApiService.callTemperatureApiSync(weatherRequestDto);
////            int uvData = uvApiService.callUvApiSync(weatherRequestDto);
////
////            WeatherResponseDto newData = WeatherResponseDto.builder()
////                    .airCondition(dustData)
////                    .temperature(temperatureData)
////                    .uvCondition(uvData)
////                    .build();
////
////            return newData;
////        } catch (Exception e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
////}
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EnvironmentalDataServiceNoCacheSync {
//
//    @Autowired
//    private DustApiService dustApiService;
//
//    @Autowired
//    private TemperatureApiService temperatureApiService;
//
//    @Autowired
//    private UvApiService uvApiService;
//
//    public WeatherResponseDto getEnvironmentalData(WeatherRequestDto weatherRequestDto) {
//        try {
//            // 각 API를 동기적으로 호출
//            int dustData = dustApiService.callDustApiSync(weatherRequestDto);
//            int temperatureData = temperatureApiService.callTemperatureApiSync(weatherRequestDto);
//            int uvData = uvApiService.callUvApiSync(weatherRequestDto);
//
//            WeatherResponseDto newData = WeatherResponseDto.builder()
//                    .airCondition(dustData)
//                    .temperature(temperatureData)
//                    .uvCondition(uvData)
//                    .build();
//
//            return newData;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
//
