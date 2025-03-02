package world.moducare.domain.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import world.moducare.domain.api.dto.WeatherRequestDto;
import world.moducare.domain.api.dto.WeatherResponseDto;
import world.moducare.domain.api.dto.WeatherResultResponseDto;
import world.moducare.domain.api.gpt.GptService;
import world.moducare.domain.api.service.EnvironmentalDataService;
import world.moducare.domain.api.gpt.PromptService;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "날씨 정보 API 컨트롤러", description = "환경 데이터 관련 API")
public class WeatherController {

    private final EnvironmentalDataService environmentalDataService;
    private final GptService gptService;
    private final PromptService promptService;

    @PostMapping(value = "")
    @Operation(summary = "현재 기준 날씨 예보 정보 조회")
    public ResponseEntity<WeatherResultResponseDto> getWeatherData(@RequestBody WeatherRequestDto weatherRequestDto) {
        WeatherResponseDto weatherResponseDto = environmentalDataService.getEnvironmentalData(weatherRequestDto);
        WeatherResultResponseDto result;
        if (weatherResponseDto == null) {
            WeatherResponseDto nullWeatherResponseDto = WeatherResponseDto.builder().airCondition(0).uvCondition(0).temperature(0).build();
            result = WeatherResultResponseDto.builder().weatherDto(nullWeatherResponseDto).gptResponse("오늘은 자라나라머리머리빔의 행운이 가득할거에요! ").build();
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        String gptAnswer = gptService.chat(promptService.makeEnvironmentPrompt(weatherResponseDto));
        result = WeatherResultResponseDto.builder().weatherDto(weatherResponseDto).gptResponse(gptAnswer).build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
