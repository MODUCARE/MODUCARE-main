//package world.moducare.domain.product.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import world.moducare.domain.product.dto.RecommendDto;
//import world.moducare.domain.product.entity.CrawledProduct;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//@ActiveProfiles("test")
//public class RecommendationServiceTest {
//
//    @Autowired
//    private RecommendationService recommendationService;
//
//    @Autowired
//    private ElasticProductService elasticProductService;
//
//    @Autowired
//    private CrawledProductService crawledProductService;
//
//    @Autowired
//    private EmbeddingService embeddingService;
//
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        // 새로운 인덱스에 데이터 인덱싱
//        elasticProductService.indexProductsFromMySQL();
//    }
//
//    @Test
//    public void testDocumentCount2() {
//        String indexName = "elastic_products";  // 인덱스 이름을 설정
//        long documentCount = recommendationService.getDocumentCount(indexName);
//        System.out.println("Document count in index '" + indexName + "': " + documentCount);
//    }
//
//    /**
//     * 일반 SQL 쿼리 방식으로 제품 추천을 가져오는 시간 측정
//     */
//    @Test
//    @Transactional
//    public void testSqlQueryPerformance() {
//        // SQL 쿼리 방식으로 추천 결과 가져오기
//        long startTime = System.currentTimeMillis();
//
//        List<CrawledProduct> sqlRecommendations = crawledProductService.getRandomProductsByKeyword(new String[]{"저자극", "민감성", "지성", "상쾌함", "촉촉함(수분공급)", "각질케어", "비듬케어", "약산성"},100);
//        List<RecommendDto> recommendDtoList = new ArrayList<>();
//        for (CrawledProduct sqlRecommendation : sqlRecommendations) {
//            String[] typeText =
//                    ((sqlRecommendation.get세부제품특징() == null ? "" : sqlRecommendation.get세부제품특징().trim()+ ", ") +
//                            (sqlRecommendation.get주요제품특징() == null ? "" : sqlRecommendation.get주요제품특징().trim()+ ", ") +
//                            (sqlRecommendation.get모발타입() == null ? "" : sqlRecommendation.get모발타입().trim()+ ", ") +
//                            (sqlRecommendation.get두피타입() == null ? "" : sqlRecommendation.get두피타입().trim()+ ", ") +
//                            (sqlRecommendation.get헤어타입() == null ? "" : sqlRecommendation.get헤어타입().trim())).split(", ");
//
//            RecommendDto recommendDto = RecommendDto.builder()
//                    .productImg(sqlRecommendation.get제품_이미지())
//                    .productName(sqlRecommendation.get제품명())
//                    .link(sqlRecommendation.get제품_링크())
//                    .price(sqlRecommendation.get가격())
//                    .productType(typeText)
//                    .build();
//
//            recommendDtoList.add(recommendDto);
//        }
//        long endTime = System.currentTimeMillis();
//
//        long sqlQueryTime = endTime - startTime;
//        System.out.println("SQL 쿼리 방식 소요 시간: " + sqlQueryTime + "ms");
//
//        // 최소 1개의 추천이 있어야 합니다.
//        System.out.println("검색결과 수: "+recommendDtoList.size());
//        System.out.println("<SQL 검색 결과>");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(i+"번째 타입"+ Arrays.toString(recommendDtoList.get(i).getProductType()));
//        }
//        assertTrue(recommendDtoList.size()==100);
//    }
//
//    /**
//     * Elasticsearch 방식으로 제품 추천을 가져오는 시간 측정
//     */
//    @Test
//    public void testElasticsearchQueryPerformance() throws JsonProcessingException {
//        // Elasticsearch 방식으로 추천 결과 가져오기
//
//        float[] userEmbedding = embeddingService.getEmbedding("저자극 민감성 지성 상쾌함 촉촉함(수분공급) 각질케어 비듬케어 약산성"); // 예시 임베딩 벡터
//        long startTime = System.currentTimeMillis();
//
//        List<RecommendDto> elasticsearchRecommendations = recommendationService.recommendProducts(userEmbedding);
//        long endTime = System.currentTimeMillis();
//
//        long elasticsearchQueryTime = endTime - startTime;
//        System.out.println("Elasticsearch 쿼리 방식 소요 시간: " + elasticsearchQueryTime + "ms");
//
//        // 최소 1개의 추천이 있어야 합니다.
//        System.out.println("검색결과 수: "+elasticsearchRecommendations.size());
//        System.out.println("<Elasticsearch 검색 결과>");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(i+"번째 타입"+ Arrays.toString(elasticsearchRecommendations.get(i).getProductType()));
//        }
//        assertTrue(elasticsearchRecommendations.size()==100);
//    }
//}
