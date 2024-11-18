package world.moducare.domain.diagnosis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import world.moducare.domain.diagnosis.entity.DiagnosticResult;
import world.moducare.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosticResultRepository extends JpaRepository<DiagnosticResult, Long> {
    Optional<List<DiagnosticResult>> findAllByMemberOrderByCreatedAtDesc(Member member);

    Optional<DiagnosticResult> findByMemberAndId(Member member, Long diagnosisId);

    @Query("SELECT d.average FROM DiagnosticResult d WHERE d.member = :member ORDER BY d.createdAt DESC")
    Optional<Integer> findLatestAverageByMember(@Param("member") Member member);
}

