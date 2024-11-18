package world.moducare.domain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
@Getter
@Builder
public class WeatherResponseDto {
    private int airCondition;
    private int temperature;
    private int uvCondition;
}
