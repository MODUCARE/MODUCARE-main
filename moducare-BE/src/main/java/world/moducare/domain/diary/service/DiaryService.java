package world.moducare.domain.diary.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import world.moducare.domain.diary.dto.DiaryRequestDto;
import world.moducare.domain.diary.dto.DiaryResponseDto;
import world.moducare.domain.diary.entity.HeadDiary;
import world.moducare.domain.diary.entity.HeadType;
import world.moducare.domain.diary.repository.DiaryRepository;
import world.moducare.domain.member.entity.Member;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public void saveDiary(Member member, DiaryRequestDto diaryRequestDto) {
        HeadDiary headDiary = HeadDiary.builder()
                .member(member)
                .image(diaryRequestDto.getImg())
                .type(HeadType.getHeadType(diaryRequestDto.getType()))
                .build();
        diaryRepository.save(headDiary);
    }

    public List<DiaryResponseDto> findDiary(Member member, String type) {
        List<HeadDiary> list = diaryRepository.findAllByMemberAndTypeOrderByCreatedAtDesc(member, HeadType.getHeadType(type)).orElse(null);
        if (list == null) {
            return new ArrayList<>();
        }

        List<DiaryResponseDto> diaryResponseDtoList = new ArrayList<>();
        for (HeadDiary headDiary : list) {
            DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
                    .img(headDiary.getImage())
                    .regDate(formatZonedDateTime(headDiary.getCreatedAt()))
                    .build();

            diaryResponseDtoList.add(diaryResponseDto);
        }
        return diaryResponseDtoList;
    }

    // YYYY-MM-DD 형식으로 변환
    public static String formatZonedDateTime(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return zonedDateTime.format(formatter);
    }
}