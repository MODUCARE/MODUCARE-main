package world.moducare.domain.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.moducare.domain.diary.entity.HeadDiary;
import world.moducare.domain.diary.entity.HeadType;
import world.moducare.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<HeadDiary, Long> {
    Optional<List<HeadDiary>> findAllByMemberAndTypeOrderByCreatedAtDesc(Member member, HeadType headType);
}
