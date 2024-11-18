package world.moducare.domain.api.gpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import world.moducare.domain.api.dto.ChatGPTRequestDTO;
import world.moducare.domain.api.dto.ChatGPTResponseDTO;

@Service
public class GptService {

    private final RestTemplate restTemplate;

    @Autowired
    public GptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String chat(String prompt) {
        String apiURL = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-4o-mini"; // 또는 "gpt-4"

        ChatGPTRequestDTO request = new ChatGPTRequestDTO(model, prompt);

        ResponseEntity<ChatGPTResponseDTO> response = restTemplate.postForEntity(apiURL, request, ChatGPTResponseDTO.class);

        // 응답 처리
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getChoices().get(0).getMessage().getContent();
        } else {
            // 에러 처리 로직 추가
            throw new RuntimeException("Failed to get response from GPT API");
        }
    }
}
