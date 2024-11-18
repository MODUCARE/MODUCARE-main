package world.moducare.domain.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import world.moducare.domain.product.entity.CrawledProduct;
import world.moducare.domain.product.entity.ElasticProduct;
import world.moducare.domain.product.repository.CrawledProductRepository;
import world.moducare.domain.product.repository.ElasticProductRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ElasticProductService {

    private final ElasticProductRepository elasticProductRepository;

    private final CrawledProductRepository crawledProductRepository;

    private final EmbeddingService embeddingService;

    public void indexProductsFromMySQL() throws JsonProcessingException {
        List<CrawledProduct> products = crawledProductRepository.findAll();
        for (CrawledProduct crawledProduct : products) {
            ElasticProduct elasticProduct = ElasticProduct.builder()
                    .id(String.valueOf(crawledProduct.getId()))
                    .제품명(crawledProduct.get제품명())
                    .가격(crawledProduct.get가격())
                    .제품_이미지(crawledProduct.get제품_이미지())
                    .제품_링크(crawledProduct.get제품_링크())
                    .세부제품특징(crawledProduct.get세부제품특징())
                    .주요제품특징(crawledProduct.get주요제품특징())
                    .헤어타입(crawledProduct.get헤어타입())
                    .두피타입(crawledProduct.get두피타입())
                    .모발타입(crawledProduct.get모발타입())
                    .build();

            // 벡터화를 위한 텍스트 결합
            String combinedText =
                    (crawledProduct.get세부제품특징() == null ? "" : crawledProduct.get세부제품특징().trim()) + " " +
                            (crawledProduct.get주요제품특징() == null ? "" : crawledProduct.get주요제품특징().trim()) + " " +
                            (crawledProduct.get모발타입() == null ? "" : crawledProduct.get모발타입().trim()) + " " +
                            (crawledProduct.get두피타입() == null ? "" : crawledProduct.get두피타입().trim()) + " " +
                            (crawledProduct.get헤어타입() == null ? "" : crawledProduct.get헤어타입().trim());

            // 벡터화
            float[] embeddingVector = embeddingService.getEmbedding(combinedText);
            elasticProduct.setDescriptionVector(embeddingVector);

            // Elasticsearch에 저장
            elasticProductRepository.save(elasticProduct);
        }
    }
}
