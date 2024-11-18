package world.moducare.domain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class WeatherResultResponseDto {
    private WeatherResponseDto weatherDto;
    private String gptResponse;
}
