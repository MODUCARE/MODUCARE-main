package world.moducare.domain.stress.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StressResultResponseDto {

    private int value; // 점수
    private String label;  // 날짜
    private String dataPointText; // 점수 (toString)

}
