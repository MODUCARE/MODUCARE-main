package world.moducare.domain.product.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 추가
@NoArgsConstructor
@Builder
public class LatestProductDto {
    private String imgSrc;
    private String link;
}
