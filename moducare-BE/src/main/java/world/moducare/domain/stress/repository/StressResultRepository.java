package world.moducare.domain.stress.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import world.moducare.domain.stress.entity.StressResult;

import java.util.List;

public interface StressResultRepository extends JpaRepository<StressResult, Long> {

    @Query(value = "SELECT * FROM (SELECT * FROM stress_result where member_idx = :memberId ORDER BY created_at DESC LIMIT 7) as recent ORDER BY created_at ASC", nativeQuery = true)
    List<StressResult> findRecentResults(@Param("memberId") Long memberId);
}
