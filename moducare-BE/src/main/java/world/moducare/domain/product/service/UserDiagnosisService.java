package world.moducare.domain.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import world.moducare.domain.api.gpt.GptService;
import world.moducare.domain.api.gpt.PromptService;
import world.moducare.domain.diagnosis.dto.AiResultDto;

@RequiredArgsConstructor
@Service
public class UserDiagnosisService {

    private final EmbeddingService embeddingService;
    private final PromptService promptService;
    private final GptService gptService;

    public float[] getUserEmbedding(AiResultDto aiResultDto) throws JsonProcessingException {
        // 진단 결과를 텍스트로 결합
        String diagnosisText = aiResultDto.headTypeToString() + "\n";
        for (int i = 0; i < 6; i++) {
            if (aiResultDto.getResult()[i] >= 1) {
                diagnosisText = diagnosisText + aiResultDto.valueOfResult(i) + ":" + aiResultDto.resultToString(i) + "\n";
            }
        }

        String prompt = promptService.makeRecommendPrompt(diagnosisText);
        String answer = gptService.chat(prompt);

        // 벡터화
        return embeddingService.getEmbedding(answer);
    }
}
