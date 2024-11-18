package world.moducare.domain.product.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import world.moducare.domain.product.entity.ElasticProduct;

import java.util.List;

@Repository
public interface ElasticProductRepository extends ElasticsearchRepository<ElasticProduct, String> {
    // 추가적인 커스텀 쿼리 메서드를 정의할 수 있습니다.
    List<ElasticProduct> findBy두피타입(String 두피타입);
}
