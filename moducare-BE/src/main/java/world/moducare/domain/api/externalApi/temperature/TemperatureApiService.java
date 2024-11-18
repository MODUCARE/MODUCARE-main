package world.moducare.domain.api.externalApi.temperature;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class TemperatureApiService {

    @Value("${API_KEY}")
    private String TEMPERATURE_KEY;
    private static final String API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    public static final Map<String, Integer> REGION_MAP = Map.ofEntries(
            Map.entry("서울", 0),
            Map.entry("부산", 1),
            Map.entry("대구", 2),
            Map.entry("인천", 3),
            Map.entry("광주", 4),
            Map.entry("대전", 5),
            Map.entry("울산", 6),
            Map.entry("세종", 7),
            Map.entry("경기도", 8),
            Map.entry("충청북도", 9),
            Map.entry("충청남도", 10),
            Map.entry("전라북도", 11),
            Map.entry("전라남도", 12),
            Map.entry("경상북도", 13),
            Map.entry("경상남도", 14),
            Map.entry("제주", 15),
            Map.entry("강원도", 16)
    );

    String[][] regions = {
            {"60", "127"}, {"98", "76"}, {"89", "90"},
            {"55", "124"}, {"58", "74"}, {"67", "100"},
            {"102", "84"}, {"66", "103"}, {"60", "120"},
            {"69", "107"}, {"68", "100"}, {"63", "89"},
            {"51", "67"}, {"87", "106"}, {"91", "77"},
            {"52", "38"}, {"73", "134"}
    };

    @Retryable(value = {DataNotFoundException.class, Exception.class}, maxAttempts = 10, backoff = @Backoff(delay = 100))
    public CompletableFuture<Integer> callTemperatureApi(WeatherRequestDto weatherRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int index = REGION_MAP.get(weatherRequestDto.getSido());
                String nx = regions[index][0];
                String ny = regions[index][1];

                // 현재 시각에서 10분을 뺀 adjustedNow 계산
                LocalDateTime adjustedNow = LocalDateTime.now().minusMinutes(10);

                // API 호출을 위한 파라미터 설정
                String baseDate = getBaseDate(adjustedNow);
                String baseTime = getBaseTime(adjustedNow);

                // 요청 파라미터 설정 (이하 동일)
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                        .queryParam("serviceKey", TEMPERATURE_KEY) // 인코딩하지 않음
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", 1000)
                        .queryParam("dataType", "JSON")
                        .queryParam("base_date", baseDate)
                        .queryParam("base_time", baseTime)
                        .queryParam("nx", nx)
                        .queryParam("ny", ny);

                UriComponents uriComponents = uriBuilder.build()
                        .expand()
                        .encode(Charset.forName("UTF-8"));

                // URI 생성 시 인코딩되지 않은 serviceKey를 포함
                URI uri = URI.create(uriComponents.toUriString().replace("serviceKey=" + URLEncoder.encode(TEMPERATURE_KEY, "UTF-8"), "serviceKey=" + TEMPERATURE_KEY));

                // API 호출
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

                // JSON 응답 확인 및 데이터 파싱
                if (response.getBody() == null || response.getBody().isEmpty()) {
                    throw new DataNotFoundException("Empty JSON response");
                }

                // 응답 데이터 파싱하여 기온 추출
                return parseTemperature(response.getBody(), adjustedNow);
            } catch (Exception e) {
                throw new DataNotFoundException("Data not found for the requested station");
            }
        });
    }

    // 기준 날짜를 가져오는 메서드
    private String getBaseDate(LocalDateTime adjustedNow) {
        String baseTime = getBaseTime(adjustedNow); // 기준 시간 가져오기

        // 기준 시간이 "2300"이고 조정된 시간이 2시 이전일 경우, 전날로 설정
        if (baseTime.equals("2300") && adjustedNow.getHour() < 2) {
            LocalDate date = adjustedNow.toLocalDate().minusDays(1); // 전날로 설정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); // 날짜 포맷터
            return date.format(formatter); // 포맷팅된 날짜 반환
        } else {
            LocalDate date = adjustedNow.toLocalDate(); // 오늘 날짜
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return date.format(formatter); // 포맷팅된 날짜 반환
        }
    }

    // 기준 시간을 가져오는 메서드
    private String getBaseTime(LocalDateTime adjustedNow) {
        // 제공되는 base time 목록
        int[] baseHours = {2, 5, 8, 11, 14, 17, 20, 23};

        int hour = adjustedNow.getHour(); // 조정된 시간의 시
        int minute = adjustedNow.getMinute(); // 조정된 시간의 분

        String baseTime = null; // 기본 시간 초기화

        // 역순으로 순회하여 조정된 시간보다 작거나 같은 가장 큰 base time을 찾음
        for (int i = baseHours.length - 1; i >= 0; i--) {
            int baseHour = baseHours[i];
            if (hour > baseHour || (hour == baseHour && minute >= 0)) {
                baseTime = String.format("%02d00", baseHour); // 포맷팅하여 기본 시간 설정
                break;
            }
        }

        // 기본 시간을 찾지 못했다면 "2300"을 반환
        if (baseTime == null) {
            baseTime = "2300";
        }

        return baseTime; // 기본 시간 반환
    }

    // JSON 응답에서 기온을 파싱하는 메서드
    private int parseTemperature(String responseBody, LocalDateTime adjustedNow) {
        try {
            // 현재 시간에 가장 가까운 fcstTime 계산
            String targetFcstDate = getNearestFcstDate(adjustedNow); // 목표 날짜
            String targetFcstTime = getNearestFcstTime(adjustedNow); // 목표 시간
            System.out.println("Adjusted now: " + adjustedNow); // 조정된 시간 로그
            System.out.println("Calculated target forecast time (fcstTime): " + targetFcstTime); // 목표 예보 시간 로그

            ObjectMapper objectMapper = new ObjectMapper(); // JSON 파서를 위한 객체 생성
            JsonNode rootNode = objectMapper.readTree(responseBody); // JSON 문자열을 노드로 변환

            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item"); // 아이템 노드 가져오기

            if (itemsNode.isArray()) { // 아이템 노드가 배열인지 확인
                for (JsonNode itemNode : itemsNode) {
                    String category = itemNode.path("category").asText(); // 카테고리
                    String fcstDate = itemNode.path("fcstDate").asText(); // 예보 날짜
                    String fcstTime = itemNode.path("fcstTime").asText(); // 예보 시간

                    // 기온 정보가 있는지 확인
                    if ("TMP".equals(category) && fcstDate.equals(targetFcstDate) && fcstTime.equals(targetFcstTime)) {
                        String fcstValue = itemNode.path("fcstValue").asText(); // 기온 값
                        System.out.println("Temperature forecast value (fcstValue) found: " + fcstValue); // 기온 값 로그
                        return Integer.parseInt(fcstValue); // 기온을 정수로 변환하여 반환
                    }
                }
            } else {
                System.out.println("Items node is not an array or does not contain any items."); // 배열이 아니거나 아이템이 없음
            }

            // 데이터를 찾지 못한 경우
            System.out.println("No matching data found in JSON response for target date and time."); // 일치하는 데이터 없음
            throw new DataNotFoundException("Temperature data not found in the response"); // 예외 발생

        } catch (Exception e) {
            System.out.println("Error encountered while parsing JSON response: " + e.getMessage()); // 파싱 중 오류 로그
            throw new DataNotFoundException("Error parsing JSON response"); // 예외 발생
        }
    }

    // 조정된 시간을 기반으로 가장 가까운 fcstDate를 반환하는 메서드
    private String getNearestFcstDate(LocalDateTime adjustedNow) {
        String baseTime = getBaseTime(adjustedNow); // 기준 시간 가져오기

        // 기준 시간이 "2300"이고 조정된 시간이 2시 이전일 경우, 전날로 설정
        if (baseTime.equals("2300") && adjustedNow.getHour() > 22) {
            LocalDate date = adjustedNow.toLocalDate().plusDays(1); // 전날로 설정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); // 날짜 포맷터
            return date.format(formatter); // 포맷팅된 날짜 반환
        } else {
            LocalDate date = adjustedNow.toLocalDate(); // 오늘 날짜
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return date.format(formatter); // 포맷팅된 날짜 반환
        }
    }

    // 조정된 시간을 기반으로 가장 가까운 fcstTime을 반환하는 메서드
    private String getNearestFcstTime(LocalDateTime adjustedNow) {
        // 예보 시간대는 매 시간마다 존재 (예: 0000, 0100, ..., 2300)
        int fcstHour = adjustedNow.getHour(); // 조정된 시간의 시
        int fcstMinute = adjustedNow.getMinute(); // 조정된 시간의 분

        // fcstTime은 3시간 간격으로 제공될 수 있으므로, 필요한 경우 로직을 수정해야 함
        // 여기서는 가장 가까운 시간의 fcstTime을 반환하도록 함
        if (fcstMinute >= 30) {
            fcstHour += 1; // 분이 30분 이상일 경우 1시간 증가
        }

        if (fcstHour >= 24) {
            fcstHour = 0; // 24시 이상이면 0시로 설정
        }

        return String.format("%02d00", fcstHour); // 포맷팅된 fcstTime 반환
    }

    public int callTemperatureApiSync(WeatherRequestDto weatherRequestDto) {
        try {
            int index = REGION_MAP.get(weatherRequestDto.getSido());
            String nx = regions[index][0];
            String ny = regions[index][1];

            // 현재 시각에서 10분을 뺀 adjustedNow 계산
            LocalDateTime adjustedNow = LocalDateTime.now().minusMinutes(10);

            // API 호출을 위한 파라미터 설정
            String baseDate = getBaseDate(adjustedNow);
            String baseTime = getBaseTime(adjustedNow);

            // 요청 파라미터 설정
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam("serviceKey", TEMPERATURE_KEY)
                    .queryParam("pageNo", "1")
                    .queryParam("numOfRows", 1000)
                    .queryParam("dataType", "JSON")
                    .queryParam("base_date", baseDate)
                    .queryParam("base_time", baseTime)
                    .queryParam("nx", nx)
                    .queryParam("ny", ny);

            UriComponents uriComponents = uriBuilder.build()
                    .expand()
                    .encode(Charset.forName("UTF-8"));

            // URI 생성 시 인코딩되지 않은 serviceKey를 포함
            URI uri = URI.create(uriComponents.toUriString().replace(
                    "serviceKey=" + URLEncoder.encode(TEMPERATURE_KEY, "UTF-8"),
                    "serviceKey=" + TEMPERATURE_KEY));


            // API 호출
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            // JSON 응답 확인 및 데이터 파싱
            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new DataNotFoundException("Empty JSON response");
            }

            // 응답 데이터 파싱하여 기온 추출
            return parseTemperature(response.getBody(), adjustedNow);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataNotFoundException("Data not found for the requested station");
        }
    }
}
