package world.moducare.domain.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import world.moducare.domain.api.dto.WeatherRequestDto;
import world.moducare.domain.api.dto.WeatherResponseDto;
import world.moducare.domain.api.externalApi.dust.DustApiService;
import world.moducare.domain.api.externalApi.infrared.UvApiService;
import world.moducare.domain.api.externalApi.temperature.TemperatureApiService;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class EnvironmentalDataService {

    private final DustApiService dustApiService;
    private final TemperatureApiService temperatureApiService;
    private final UvApiService uvApiService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "weatherData", key = "#weatherRequestDto.sido + ':' + #weatherRequestDto.gugun",
            cacheManager = "redisCacheManager")
    public WeatherResponseDto getEnvironmentalData(WeatherRequestDto weatherRequestDto) {
        String locationKey = buildLocationKey(weatherRequestDto);

        WeatherResponseDto cachedData = (WeatherResponseDto) redisTemplate.opsForValue().get(locationKey);
        if (cachedData != null) {
            return cachedData;
        }

        CompletableFuture<Integer> dustFuture = dustApiService.callDustApi(weatherRequestDto);
        // 매시 15분 내외
        CompletableFuture<Integer> temperatureFuture = temperatureApiService.callTemperatureApi(weatherRequestDto);
        // - API 제공 시간(~이후) : 02:10, 05:10, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
        CompletableFuture<Integer> uvFuture = uvApiService.callUvApi(weatherRequestDto);
        // 00시, 03시, 06시, 09시, 12시, 15시, 18시, 21시, 24시

        try {
            CompletableFuture.allOf(dustFuture, temperatureFuture, uvFuture).join();

            int dustData = dustFuture.get();
            int temperatureData = temperatureFuture.get();
            int uvData = uvFuture.get();

            WeatherResponseDto newData = WeatherResponseDto.builder()
                    .airCondition(dustData)
                    .temperature(temperatureData)
                    .uvCondition(uvData)
                    .build();

            if (dustData == -1 && temperatureData == -1 && uvData == -1) {
                System.out.println("Dust data is null");
                return null;
            } else redisTemplate.opsForValue().set(locationKey, newData, Duration.ofHours(1));

            return newData;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildLocationKey(WeatherRequestDto weatherRequestDto) {
        return weatherRequestDto.getSido() + ":" + weatherRequestDto.getGugun();
    }

    // 매시 20분에 캐시 갱신
    @Scheduled(cron = "0 20 * * * *") // 매시 20분에 캐시 갱신
    @CacheEvict(value = "weatherData", allEntries = true) // 모든 캐시 항목 삭제
    public void refreshCache() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            // 키를 기반으로 WeatherRequestDto 생성 (키에 저장된 정보 사용)
            String[] location = key.split(":");
            if (location.length < 2) continue;
            WeatherRequestDto requestDto = new WeatherRequestDto(location[0], location[1]);

            // Redis에서 해당 키 삭제
            redisTemplate.delete(key);

            // API 호출 후 캐시에 업데이트
            WeatherResponseDto updatedData = getEnvironmentalData(requestDto);
            if (updatedData != null) {
                redisTemplate.opsForValue().set(key, updatedData);
            }
        }
    }
}
