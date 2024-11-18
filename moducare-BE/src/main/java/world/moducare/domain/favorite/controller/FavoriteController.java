package world.moducare.domain.favorite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import world.moducare.domain.favorite.dto.LikeRequestDto;
import world.moducare.domain.favorite.service.FavoriteService;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.global.config.oauth.CustomOAuth2User;

@RestController
@RequiredArgsConstructor
@Tag(name = "챌린지 인증 컨트롤러", description = "챌린지 인증 관련 API")
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final MemberService memberService;
    private final FavoriteService favoriteService;

    @PostMapping("/{feedId}")
    @Operation(summary = "챌린지 인증피드 좋아요")
    public ResponseEntity<Void> likeFeed(@AuthenticationPrincipal CustomOAuth2User principal, @PathVariable Long feedId, @RequestBody LikeRequestDto requestDto) {
        Member member = memberService.findById(principal.getId());
        favoriteService.likeFeed(member, feedId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
