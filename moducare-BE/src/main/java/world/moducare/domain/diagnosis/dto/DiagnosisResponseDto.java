package world.moducare.domain.diagnosis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DiagnosisResponseDto {
    private Long id;
    private String date;
    private String diagnosis;
}
