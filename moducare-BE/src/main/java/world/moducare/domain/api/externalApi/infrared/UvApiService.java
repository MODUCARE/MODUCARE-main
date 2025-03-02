package world.moducare.domain.api.externalApi.infrared;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import world.moducare.domain.api.dto.WeatherRequestDto;
import world.moducare.domain.region.entity.regionCode;
import world.moducare.domain.region.repository.RegionCodeRepository;
import world.moducare.global.exception.DataNotFoundException;
import world.moducare.global.exception.ErrorCode;
import world.moducare.global.exception.RestApiException;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UvApiService {

    @Value("${API_KEY}")
    private String UV_KEY;
    private static final String API_URL = "http://apis.data.go.kr/1360000/LivingWthrIdxServiceV4/getUVIdxV4";

    private final RegionCodeRepository regionCodeRepository;

    @Retryable(value = {DataNotFoundException.class, Exception.class}, maxAttempts = 10, backoff = @Backoff(delay = 100))
    public CompletableFuture<Integer> callUvApi(WeatherRequestDto weatherRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String sidoName = weatherRequestDto.getSido();
                String gugunName = weatherRequestDto.getGugun();

                String areaNo = getAreaNo(sidoName, gugunName);

                // 요청 파라미터 설정
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                        .queryParam("serviceKey", UV_KEY) // 인코딩하지 않음
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", "10")
                        .queryParam("dataType", "JSON")
                        .queryParam("areaNo", areaNo)
                        .queryParam("time", getTime());

                // 특정 파라미터만 인코딩 설정
                UriComponents uriComponents = uriBuilder.build()
                        .expand()
                        .encode(Charset.forName("UTF-8"));

                // URI 생성 시 인코딩되지 않은 serviceKey를 포함
                URI uri = URI.create(uriComponents.toUriString().replace("serviceKey=" + URLEncoder.encode(UV_KEY, "UTF-8"), "serviceKey=" + UV_KEY));

                // API 호출
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

                // JSON 응답 확인 및 데이터 파싱
                if (response.getBody() == null || response.getBody().isEmpty()) {
                    throw new DataNotFoundException("Empty JSON response");
                }

                // 응답 데이터 파싱하여 자외선 지수 추출
                return parseUvIndex(response.getBody());
            } catch (Exception e) {
                throw new DataNotFoundException("Data not found for the requested station");
            }
        });
    }

    private String getAreaNo(String sidoName, String gugunName) {
        Optional<regionCode> regionCodeOptional = regionCodeRepository.findBySidoAndGugun(sidoName, gugunName);
        if (regionCodeOptional.isPresent()) {
            return regionCodeOptional.get().getCode();
        } else {
            throw new RestApiException(ErrorCode.NOT_FOUND);
        }
    }

    private String getTime() {
        // 현재 시간을 YYYYMMDDHH 형식으로 반환
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        return now.format(formatter);
    }

    private int parseUvIndex(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    // 원하는 데이터 추출
                    String h0Value = itemNode.path("h0").asText();
                    if (h0Value == null || h0Value.isEmpty()) {
                        throw new DataNotFoundException("Data not available for the requested station");
                    } else {
                        try {
                            return Integer.parseInt(h0Value);
                        } catch (NumberFormatException e) {
                            throw new DataNotFoundException("Data not available for the requested station");
                        }
                    }
                }
            }
            // 데이터를 찾지 못한 경우
            throw new DataNotFoundException("UV Data not found in the response");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException("Error parsing JSON response");
        }
    }

    @Recover
    public CompletableFuture<Integer> recoverUvApi(DataNotFoundException e, WeatherRequestDto weatherRequestDto) {
        // UV API 실패 시 기본값을 반환
        return CompletableFuture.completedFuture(-1); // -1을 UV 데이터 기본값으로 사용
    }


    public int callUvApiSync(WeatherRequestDto weatherRequestDto) {
        try {
            String sidoName = weatherRequestDto.getSido();
            String gugunName = weatherRequestDto.getGugun();

            String areaNo = getAreaNo(sidoName, gugunName);

            // 요청 파라미터 설정
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam("serviceKey", UV_KEY)
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", "10")
                    .queryParam("dataType", "JSON")
                    .queryParam("areaNo", areaNo)
                    .queryParam("time", getTime());

            // 특정 파라미터만 인코딩 설정
            UriComponents uriComponents = uriBuilder.build()
                    .expand()
                    .encode(Charset.forName("UTF-8"));

            // URI 생성 시 인코딩되지 않은 serviceKey를 포함
            URI uri = URI.create(uriComponents.toUriString().replace(
                    "serviceKey=" + URLEncoder.encode(UV_KEY, "UTF-8"),
                    "serviceKey=" + UV_KEY));

            // API 호출
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            // JSON 응답 확인 및 데이터 파싱
            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new DataNotFoundException("Empty JSON response");
            }

            // 응답 데이터 파싱하여 자외선 지수 추출
            return parseUvIndex(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException("Data not found for the requested station");
        }
    }
}
