package world.moducare.domain.mychallenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.domain.mychallenge.dto.MyChallengeResponseDto;
import world.moducare.domain.mychallenge.service.MyChallengeService;
import world.moducare.global.config.oauth.CustomOAuth2User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "마이 챌린지 컨트롤러", description = "마이 챌린지 관련 API")
@RequestMapping("/api/my-challenges")
public class MyChallengeController {

    private final MyChallengeService myChallengeService;
    private final MemberService memberService;

    @PostMapping("/{challengeId}/in")
    @Operation(summary = "마이 챌린지 추가 API (챌린지 참여)")
    public ResponseEntity<Void> addMyChallenge(@AuthenticationPrincipal CustomOAuth2User principal, @PathVariable Long challengeId) {
        Member member = memberService.findById(principal.getId());
        myChallengeService.addMyChallenge(member, challengeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{challengeId}/out")
    @Operation(summary = "마이 챌린지 삭제 API (챌린지 나가기)")
    public ResponseEntity<Void> deleteMyChallenge(@AuthenticationPrincipal CustomOAuth2User principal, @PathVariable Long challengeId) {
        Member member = memberService.findById(principal.getId());
        myChallengeService.deleteMyChallenge(member, challengeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping()
    @Operation(summary = "마이 챌린지 목록 조회 API")
    public ResponseEntity<List<MyChallengeResponseDto>> getMyChallenges(@AuthenticationPrincipal CustomOAuth2User principal) {
        Member member = memberService.findById(principal.getId());
        List<MyChallengeResponseDto> myChallenges = myChallengeService.getMyChallenges(member);
        return ResponseEntity.ok(myChallenges);
    }
}
