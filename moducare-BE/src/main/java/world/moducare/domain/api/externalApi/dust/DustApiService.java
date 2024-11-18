package world.moducare.domain.api.externalApi.dust;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import world.moducare.domain.api.dto.WeatherRequestDto;
import world.moducare.global.exception.DataNotFoundException;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

@Service
public class DustApiService {

    @Value("${API_KEY}")
    private String DUST_KEY;
    private static final String API_URL = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";

    @Retryable(value = {DataNotFoundException.class, Exception.class}, maxAttempts = 10, backoff = @Backoff(delay = 100))
    public CompletableFuture<Integer> callDustApi(WeatherRequestDto weatherRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 요청 파라미터 설정
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                        .queryParam("serviceKey", DUST_KEY) // 인코딩하지 않음
                        .queryParam("returnType", "json")
                        .queryParam("numOfRows", "100")
                        .queryParam("pageNo", "1")
                        .queryParam("sidoName", weatherRequestDto.getSido())
                        .queryParam("ver", "1.3");

                // 특정 파라미터만 인코딩 설정
                UriComponents uriComponents = uriBuilder.build()
                        .expand()
                        .encode(Charset.forName("UTF-8"));

                // URI 생성 시 인코딩되지 않은 serviceKey를 포함
                URI uri = URI.create(uriComponents.toUriString().replace("serviceKey=" + URLEncoder.encode(DUST_KEY, "UTF-8"), "serviceKey=" + DUST_KEY));

                // API 호출
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

                // JSON 응답 확인 및 데이터 파싱
                if (response.getBody() == null || response.getBody().isEmpty()) {
                    throw new DataNotFoundException("Empty JSON response");
                }

                return parseDustValue(response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataNotFoundException("Data not found for the requested station");
            }
        });
    }

    // stationName이 동일한 item의 pm10Grade1h
    private int parseDustValue(String responseBody) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items");

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
//                    String stationName = itemNode.path("stationName").asText();
//                    if (stationName.equals(targetStationName)) {
//
//                    }
                    String pm10Grade1hStr = itemNode.path("pm10Grade1h").asText();
                    if (!pm10Grade1hStr.equals("-") && !pm10Grade1hStr.equals("null")) {
                        try {
                            return Integer.parseInt(pm10Grade1hStr);
                        } catch (NumberFormatException e) {
                            throw new DataNotFoundException("PM10 data not numbered for the requested station");
                        }
                    }
                }
            }
            // 일치하는 stationName이 없을 경우
            throw new DataNotFoundException("Station name not found in the response");
        } catch (Exception e) {
            throw new DataNotFoundException("Error parsing JSON response");
        }
    }


    public int callDustApiSync(WeatherRequestDto weatherRequestDto) {
        try {
            // 요청 파라미터 설정
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam("serviceKey", DUST_KEY)
                    .queryParam("returnType", "json")
                    .queryParam("numOfRows", "100")
                    .queryParam("pageNo", "1")
                    .queryParam("sidoName", weatherRequestDto.getSido())
                    .queryParam("ver", "1.3");

            // 특정 파라미터만 인코딩 설정
            UriComponents uriComponents = uriBuilder.build()
                    .expand()
                    .encode(Charset.forName("UTF-8"));

            // URI 생성 시 인코딩되지 않은 serviceKey를 포함
            URI uri = URI.create(uriComponents.toUriString().replace(
                    "serviceKey=" + URLEncoder.encode(DUST_KEY, "UTF-8"),
                    "serviceKey=" + DUST_KEY));

            // API 호출
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            // JSON 응답 확인 및 데이터 파싱
            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new DataNotFoundException("Empty JSON response");
            }

            return parseDustValue(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException("Data not found for the requested station");
        }
    }
}
