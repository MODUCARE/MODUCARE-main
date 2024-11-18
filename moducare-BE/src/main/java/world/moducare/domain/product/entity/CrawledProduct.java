package world.moducare.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "products") // MySQL의 테이블명과 일치시켜야 합니다.
public class CrawledProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String 제품명;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String 가격;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String 제품_이미지;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String 제품_링크;

    @Column(columnDefinition = "TEXT")
    private String 세부제품특징;

    @Column(columnDefinition = "TEXT")
    private String 주요제품특징;

    @Column(columnDefinition = "TEXT")
    private String 헤어타입;

    @Column(columnDefinition = "TEXT")
    private String 두피타입;

    @Column(columnDefinition = "TEXT")
    private String 모발타입;
}
