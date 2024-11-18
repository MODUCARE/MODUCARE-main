package world.moducare.domain.stress.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.stress.dto.StressResultRequestDto;
import world.moducare.domain.stress.dto.StressResultResponseDto;
import world.moducare.domain.stress.entity.StressResult;
import world.moducare.domain.stress.repository.StressResultRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StressResultService {

    private final StressResultRepository stressResultRepository;

    public void saveStressResult(Member member, StressResultRequestDto dto) {

        StressResult stressResult = StressResult.builder()
                .score(dto.getScore())
                .member(member)
                .build();

        stressResultRepository.save(stressResult);
    }

    public List<StressResultResponseDto> getRecentResultList(Member member) {

        List<StressResult> recentResults = stressResultRepository.findRecentResults(member.getId());

        // ResponseDto로 변환
        return recentResults.stream()
                .map(stressResult -> StressResultResponseDto.builder()
                        .value(stressResult.getScore())
                        .label(stressResult.getCreatedAt().toString())
                        .dataPointText(String.valueOf(stressResult.getScore()))
                        .build())
                .collect(Collectors.toList());
    }
}
