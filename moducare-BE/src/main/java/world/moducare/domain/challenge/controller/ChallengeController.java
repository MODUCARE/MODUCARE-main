package world.moducare.domain.challenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import world.moducare.domain.challenge.dto.ChallengeResponseDto;
import world.moducare.domain.challenge.service.ChallengeService;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.global.config.oauth.CustomOAuth2User;
import world.moducare.global.s3.S3Service;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "챌린지 컨트롤러", description = "챌린지 관련 API")
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "챌린지 생성 API")
    public ResponseEntity<Void> makeChallenge(@AuthenticationPrincipal CustomOAuth2User principal,
                                              @RequestParam("title") String title,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {

        Member member = memberService.findById(principal.getId());

        // 파일이 있는 경우 업로드, 없으면 null
        String fileUrl = (file != null && !file.isEmpty()) ? s3Service.uploadImage(file) : null;

        challengeService.saveChallenge(member, fileUrl, title);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    @Operation(summary = "전체 챌린지 목록 조회 API")
    public ResponseEntity<List<ChallengeResponseDto>> getChallengeList(@AuthenticationPrincipal CustomOAuth2User principal) {
        Member member = memberService.findById(principal.getId());
        List<ChallengeResponseDto> list = challengeService.getList(member);
        return ResponseEntity.ok(list);
    }
}
