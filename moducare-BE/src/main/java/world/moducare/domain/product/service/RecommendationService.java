package world.moducare.domain.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import world.moducare.domain.product.dto.RecommendDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    // ElasticsearchTemplate 객체를 통해 Elasticsearch 서버와 통신할 수 있게 설정
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public long getDocumentCount(String indexName) {
        try {
            CountRequest countRequest = CountRequest.of(r -> r.index(indexName));
            CountResponse countResponse = elasticsearchClient.count(countRequest);
            return countResponse.count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 사용자 임베딩을 기반으로 제품 추천 목록을 반환하는 메소드
     *
     * @param userEmbedding 사용자 임베딩 벡터
     * @return 추천 제품 리스트
     */
    public List<RecommendDto> recommendProducts(float[] userEmbedding) {
        try {
            // RestClient 생성
            RestClient restClient = RestClient.builder(
                            new HttpHost("k11b203.p.ssafy.io", 9200, "http"))
//                            new HttpHost("localhost", 9200, "http"))
                    .build();

            // 사용자 임베딩을 JSON 배열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String queryVector = objectMapper.writeValueAsString(userEmbedding);

            // k-NN 쿼리 JSON 작성
            String jsonQuery = "{\n" +
                    "  \"size\": 100,\n" +
                    "  \"knn\": {\n" +
                    "    \"field\": \"descriptionVector\",\n" +
                    "    \"query_vector\": " + queryVector + ",\n" +
                    "    \"k\": 100,\n" +
                    "    \"num_candidates\": 1000\n" +
                    "  }\n" +
                    "}";

            // 검색 요청 생성
            Request request = new Request("POST", "/elastic_products/_search");
            request.setJsonEntity(jsonQuery);

            // 검색 요청 실행
            Response response = restClient.performRequest(request);

            // 응답 처리
            String responseBody = EntityUtils.toString(response.getEntity());

            // 응답 JSON 파싱하여 RecommendDto 리스트 생성
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            List<RecommendDto> recommendDtoList = new ArrayList<>();

            for (JsonNode hit : jsonNode.get("hits").get("hits")) {
                JsonNode source = hit.get("_source");

                // 각 필드 추출
                String 제품명 = source.get("제품명").asText();
                String 가격 = source.get("가격").asText();
                String 제품_이미지 = source.get("제품_이미지").asText();
                String 제품_링크 = source.get("제품_링크").asText();
                String 세부제품특징 = source.has("세부제품특징") ? source.get("세부제품특징").asText("") : "";
                String 주요제품특징 = source.has("주요제품특징") ? source.get("주요제품특징").asText("") : "";
                String 헤어타입 = source.has("헤어타입") ? source.get("헤어타입").asText("") : "";
                String 두피타입 = source.has("두피타입") ? source.get("두피타입").asText("") : "";
                String 모발타입 = source.has("모발타입") ? source.get("모발타입").asText("") : "";

                String[] typeText =
                        ((세부제품특징.isEmpty() ? "" : 세부제품특징.trim() + ", ") +
                                (주요제품특징.isEmpty() ? "" : 주요제품특징.trim() + ", ") +
                                (모발타입.isEmpty() ? "" : 모발타입.trim() + ", ") +
                                (두피타입.isEmpty() ? "" : 두피타입.trim() + ", ") +
                                (헤어타입.isEmpty() ? "" : 헤어타입.trim())).split(", ");

                RecommendDto recommendDto = RecommendDto.builder()
                        .productImg(제품_이미지)
                        .productName(제품명)
                        .link(제품_링크)
                        .price(가격)
                        .productType(typeText)
                        .build();

                recommendDtoList.add(recommendDto);
            }

            restClient.close();

            // RecommendDto 리스트 반환
            return recommendDtoList;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
