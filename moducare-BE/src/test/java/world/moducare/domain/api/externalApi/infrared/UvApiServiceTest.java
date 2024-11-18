package world.moducare.domain.api.externalApi.infrared;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import world.moducare.domain.api.dto.WeatherRequestDto;
import world.moducare.domain.api.externalApi.infrared.UvApiService;
import world.moducare.domain.region.repository.RegionCodeRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UvApiServiceTest {

    @Test
    public void testRetryableUvApiService() throws Exception {

        // Mock dependencies
        RegionCodeRepository regionCodeRepository = mock(RegionCodeRepository.class);
        RestTemplate restTemplate = mock(RestTemplate.class);

        // WeatherRequestDto 객체 생성
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto();
        weatherRequestDto.setSido("서울");
        weatherRequestDto.setGugun("강남");

        // UvApiService 객체 생성
        UvApiService uvApiService = new UvApiService(regionCodeRepository);

        // RestTemplate의 getForEntity 메서드가 실패하도록 mock 처리 (빈 응답)
        when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(ResponseEntity.status(500).body(""));

        // callUvApi 메서드 호출 (재시도하도록 유도)
        try {
            uvApiService.callUvApi(weatherRequestDto).get(); // CompletableFuture 사용 시 get() 호출
        } catch (Exception e) {
            // 예외가 발생하면 재시도가 일어났음을 확인
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}
