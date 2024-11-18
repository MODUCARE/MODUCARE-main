package world.moducare.domain.product.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import world.moducare.domain.product.entity.CrawledProduct;
import world.moducare.domain.product.repository.CrawledProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CrawledProductService {

    private final CrawledProductRepository crawledProductRepository;

    @Autowired
    public CrawledProductService(CrawledProductRepository crawledProductRepository) {
        this.crawledProductRepository = crawledProductRepository;
    }

    public List<CrawledProduct> getRandomProductsByKeyword(String[] keywords, int count) {
        // Specification을 사용하여 동적으로 조건 생성
        Specification<CrawledProduct> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (String keyword : keywords) {
                String pattern = "%" + keyword + "%";
                Predicate keywordPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("세부제품특징"), pattern),
                        criteriaBuilder.like(root.get("주요제품특징"), pattern),
                        criteriaBuilder.like(root.get("헤어타입"), pattern),
                        criteriaBuilder.like(root.get("두피타입"), pattern),
                        criteriaBuilder.like(root.get("모발타입"), pattern)
                );
                predicates.add(keywordPredicate);
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };

        // Specification을 사용하여 쿼리 실행하고 결과를 Set으로 변환하여 중복 제거
        Set<CrawledProduct> productsWithKeywordSet = new HashSet<>(crawledProductRepository.findAll(specification));

        // Set을 List로 변환, 무작위로 섞고, 상위 'count'개의 항목을 반환
        List<CrawledProduct> productsWithKeyword = new ArrayList<>(productsWithKeywordSet);
        Collections.shuffle(productsWithKeyword);
        return productsWithKeyword.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

}