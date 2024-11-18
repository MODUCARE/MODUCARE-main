package world.moducare.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import world.moducare.domain.product.entity.CrawledProduct;

@Repository
public interface CrawledProductRepository extends JpaRepository<CrawledProduct, Long>, JpaSpecificationExecutor<CrawledProduct> {

}