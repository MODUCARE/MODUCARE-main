package world.moducare.domain.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class EmbeddingService {

    private final RestTemplate restTemplate;
    private final String embeddingServiceUrl;

    public EmbeddingService(@Value("${embedding.service.url}") String embeddingServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.embeddingServiceUrl = embeddingServiceUrl;
    }

    public float[] getEmbedding(String text) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("text", text);

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(payload);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);


        // Response를 String 형태로 먼저 받아 JSON 파싱
        ResponseEntity<String> response = restTemplate.exchange(
                embeddingServiceUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                // JSON 파서로 응답 바디에서 바로 배열 추출
                ObjectMapper mapper2 = new ObjectMapper();
                JsonNode root = mapper2.readTree(response.getBody());

                // 루트가 배열로 반환되므로 바로 순회하여 float 배열 생성
                float[] embedding = new float[root.size()];
                for (int i = 0; i < root.size(); i++) {
                    embedding[i] = root.get(i).floatValue();
                }

                return embedding;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse embedding response", e);
            }
        } else {
            throw new RuntimeException("서버에서 임베딩을 가져오는 데 실패했습니다.");
        }
    }
}

