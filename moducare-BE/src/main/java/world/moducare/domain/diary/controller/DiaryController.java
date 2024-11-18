package world.moducare.domain.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import world.moducare.domain.diary.dto.DiaryRequestDto;
import world.moducare.domain.diary.dto.DiaryResponseDto;
import world.moducare.domain.diary.service.DiaryService;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.global.config.oauth.CustomOAuth2User;
import world.moducare.global.s3.S3Service;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diaries")
@Tag(name = "머리 다이어리 컨트롤러", description = "머리 다이어리 관련 API")
public class DiaryController {

    private final MemberService memberService;
    private final DiaryService diaryService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "머리 다이어리 저장", description = "이마 라인이나 정수리 사진을 저장")
    public ResponseEntity<?> saveLatestProduct(@AuthenticationPrincipal CustomOAuth2User principal,
                                               @RequestParam("file") MultipartFile file,
                                               @RequestParam("type") String type) {

        Member member = memberService.findById(principal.getId());

        String fileUrl = s3Service.uploadImage(file);
        DiaryRequestDto diaryRequestDto = new DiaryRequestDto(fileUrl, type);

        diaryService.saveDiary(member, diaryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("머리 다이어리 저장 완료");
    }

    @GetMapping("/top")
    @Operation(summary = "정수리 다이어리 조회", description = "사용자 머리 다이어리 중 정수리 다이어리를 조회")
    public ResponseEntity<List<DiaryResponseDto>> getTopDiary(@AuthenticationPrincipal CustomOAuth2User principal) {
        Member member = memberService.findById(principal.getId());
        List<DiaryResponseDto> diaryResponseDtoList = diaryService.findDiary(member, "top");
        return ResponseEntity.status(HttpStatus.OK).body(diaryResponseDtoList);
    }

    @GetMapping("/line")
    @Operation(summary = "이마라인 다이어리 조회", description = "사용자 머리 다이어리 중 이마라인 다이어리를 조회")
    public ResponseEntity<List<DiaryResponseDto>> getLineDiary(@AuthenticationPrincipal CustomOAuth2User principal) {
        Member member = memberService.findById(principal.getId());
        List<DiaryResponseDto> diaryResponseDtoList = diaryService.findDiary(member, "line");
        return ResponseEntity.status(HttpStatus.OK).body(diaryResponseDtoList);
    }
}
