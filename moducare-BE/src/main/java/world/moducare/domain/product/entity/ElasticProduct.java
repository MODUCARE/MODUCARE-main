package world.moducare.domain.product.entity;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@Getter
@Setter
@Document(indexName = "elastic_products", createIndex = true, writeTypeHint = WriteTypeHint.FALSE)
public class ElasticProduct {

    @Id
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Field(type = FieldType.Keyword)
    private String 제품명;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Field(type = FieldType.Keyword)
    private String 가격;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Field(type = FieldType.Keyword)
    private String 제품_이미지;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Field(type = FieldType.Keyword)
    private String 제품_링크;

    @Column(columnDefinition = "TEXT")
    @Field(type = FieldType.Text)
    private String 세부제품특징;

    @Column(columnDefinition = "TEXT")
    @Field(type = FieldType.Text)
    private String 주요제품특징;

    @Column(columnDefinition = "TEXT")
    @Field(type = FieldType.Text)
    private String 헤어타입;

    @Column(columnDefinition = "TEXT")
    @Field(type = FieldType.Text)
    private String 두피타입;

    @Column(columnDefinition = "TEXT")
    @Field(type = FieldType.Text)
    private String 모발타입;

    @Field(type = FieldType.Dense_Vector, dims = 384)
    private float[] descriptionVector;

    @Builder
    public ElasticProduct(String id, String 제품명, String 가격, String 제품_이미지, String 제품_링크, String 세부제품특징, String 주요제품특징, String 헤어타입, String 두피타입, String 모발타입) {
        this.id = id;
        this.제품명 = 제품명;
        this.가격 = 가격;
        this.제품_이미지 = 제품_이미지;
        this.제품_링크 = 제품_링크;
        this.세부제품특징 = 세부제품특징;
        this.주요제품특징 = 주요제품특징;
        this.헤어타입 = 헤어타입;
        this.두피타입 = 두피타입;
        this.모발타입 = 모발타입;
    }
}
