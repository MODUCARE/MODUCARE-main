package world.moducare.domain.api.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import world.moducare.domain.api.dto.WeatherRequestDto;
import world.moducare.domain.api.dto.WeatherResponseDto;
import world.moducare.domain.api.externalApi.dust.DustApiService;
import world.moducare.domain.api.externalApi.infrared.UvApiService;
import world.moducare.domain.api.externalApi.temperature.TemperatureApiService;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvironmentalDataServiceTest {

    @Mock
    private DustApiService dustApiService;
    @Mock
    private TemperatureApiService temperatureApiService;
    @Mock
    private UvApiService uvApiService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private ValueOperations<String, Object> valueOperations; // Mock ValueOperations

    @InjectMocks
    private EnvironmentalDataService environmentalDataService;

    @BeforeEach
    public void setup() {
        // Mock CacheManager behavior
        Cache mockCache = Mockito.mock(Cache.class);
        lenient().when(cacheManager.getCache("weatherData")).thenReturn(mockCache); // lenient 사용

        // Mock RedisTemplate behavior
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations); // lenient 사용
    }


    @Test
    public void testGetEnvironmentalData_Cacheable() {
        // Given
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto("서울", "강남");
        WeatherResponseDto mockResponse = WeatherResponseDto.builder()
                .airCondition(10)
                .temperature(25)
                .uvCondition(5)
                .build();

        // Mocking API service calls
        when(dustApiService.callDustApi(any(WeatherRequestDto.class))).thenReturn(CompletableFuture.completedFuture(10));
        when(temperatureApiService.callTemperatureApi(any(WeatherRequestDto.class))).thenReturn(CompletableFuture.completedFuture(25));
        when(uvApiService.callUvApi(any(WeatherRequestDto.class))).thenReturn(CompletableFuture.completedFuture(5));

        // Mock the redisTemplate's opsForValue() to mock the cache
        when(valueOperations.get(any(String.class))).thenReturn(null); // Cache miss

        // First call, should trigger API calls
        WeatherResponseDto result = environmentalDataService.getEnvironmentalData(weatherRequestDto);

        // Verify that the API methods were called
        verify(dustApiService).callDustApi(weatherRequestDto);
        verify(temperatureApiService).callTemperatureApi(weatherRequestDto);
        verify(uvApiService).callUvApi(weatherRequestDto);

        // Verify that the result is as expected
        assert result != null;
        assert result.getAirCondition() == 10;
        assert result.getTemperature() == 25;
        assert result.getUvCondition() == 5;

        // Simulate Cache hit for next call
        when(valueOperations.get(any(String.class))).thenReturn(mockResponse); // Cache hit

        // Second call, should not trigger API calls and return cached data
        WeatherResponseDto cachedResult = environmentalDataService.getEnvironmentalData(weatherRequestDto);

        // Verify the result is from the cache, not the API
        assert cachedResult != null;
        assert cachedResult.getAirCondition() == 10;
        assert cachedResult.getTemperature() == 25;
        assert cachedResult.getUvCondition() == 5;

        // Verify that no API calls were made this time (cache hit)
        verify(dustApiService, Mockito.times(1)).callDustApi(weatherRequestDto);
        verify(temperatureApiService, Mockito.times(1)).callTemperatureApi(weatherRequestDto);
        verify(uvApiService, Mockito.times(1)).callUvApi(weatherRequestDto);
    }
}
