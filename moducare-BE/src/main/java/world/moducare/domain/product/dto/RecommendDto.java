package world.moducare.domain.product.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendDto {
    private String productImg;
    private String productName;
    private String link;
    private String price;
    private String[] productType;
}
