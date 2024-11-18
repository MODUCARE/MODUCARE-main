package world.moducare.domain.challengefeed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import world.moducare.domain.challengefeed.dto.FeedResponseDto;
import world.moducare.domain.challengefeed.service.ChallengeFeedService;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.global.config.oauth.CustomOAuth2User;
import world.moducare.global.s3.S3Service;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "챌린지 인증 컨트롤러", description = "챌린지 인증 관련 API")
@RequestMapping("/api/challenge-feeds")
public class ChallengeFeedController {

    private final MemberService memberService;
    private final ChallengeFeedService feedService;
    private final S3Service s3Service;

    @PostMapping(value = "/{challengeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "챌린지 인증하기(하루 단위)")
    public ResponseEntity<Void> postFeed(@AuthenticationPrincipal CustomOAuth2User principal, @PathVariable Long challengeId,
                                         @RequestParam("content") String content,
                                         @RequestParam(value = "file") MultipartFile file) {

        Member member = memberService.findById(principal.getId());

        // 파일이 있는 경우 업로드, 없으면 null
        String fileUrl = (file != null && !file.isEmpty()) ? s3Service.uploadImage(file) : null;

        feedService.saveFeed(member, challengeId, content, fileUrl);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    @Operation(summary = "챌린지 피드 조회")
    public ResponseEntity<List<FeedResponseDto>> getFeed(
            @AuthenticationPrincipal CustomOAuth2User principal,
            @RequestParam("cid") Long challengeId,
            @RequestParam("page") int page) {

        Member member = memberService.findById(principal.getId());
        List<FeedResponseDto> feedResponseDtoList = feedService.getFeed(member, challengeId, page);
        return ResponseEntity.status(HttpStatus.OK).body(feedResponseDtoList);
    }

}
