package world.moducare.domain.diagnosis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DiagnosisRequestDto {
    private String img;
    private int[] result;
    private int headType;
    private int comparison;
    private String manageComment;
    private String date;
}
