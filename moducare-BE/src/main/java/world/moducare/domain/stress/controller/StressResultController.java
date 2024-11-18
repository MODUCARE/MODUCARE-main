package world.moducare.domain.stress.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.domain.stress.dto.StressResultRequestDto;
import world.moducare.domain.stress.dto.StressResultResponseDto;
import world.moducare.domain.stress.service.StressResultService;
import world.moducare.global.config.oauth.CustomOAuth2User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "스트레스 진단 컨트롤러", description = "스트레스 진단 관련 API")
@RequestMapping("/api/stress")
public class StressResultController {

    private final StressResultService stressResultService;
    private final MemberService memberService;

    @PostMapping()
    @Operation(summary = "스트레스 진달 결과 저장 API")
    public ResponseEntity<Void> saveStressResult(@AuthenticationPrincipal CustomOAuth2User principal, @RequestBody StressResultRequestDto requestDto) {
        Member member = memberService.findById(principal.getId());
        stressResultService.saveStressResult(member, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 7건의 스트레스 결과 조회 API")
    public ResponseEntity<List<StressResultResponseDto>> getRecentResultList(@AuthenticationPrincipal CustomOAuth2User principal) {
        Member member = memberService.findById(principal.getId());
        List<StressResultResponseDto> list = stressResultService.getRecentResultList(member);
        return ResponseEntity.ok(list);
    }
}