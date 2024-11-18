package world.moducare.domain.challengefeed.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import world.moducare.domain.challenge.entity.Challenge;
import world.moducare.domain.challengefeed.entity.ChallengeFeed;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeFeedRepository extends JpaRepository<ChallengeFeed, Long> {

    @Query("SELECT COUNT(cf) > 0 FROM ChallengeFeed  cf " +
            "WHERE cf.member.id = :memberId " +
            "And cf.challenge.id = :challengeId " +
            "AND DATE(cf.createdAt) = :today")
    boolean existsByChallengeAndMemberAndToday(@Param("memberId") Long memberId,
                                               @Param("challengeId") Long challengeId,
                                               @Param("today") LocalDate today);

    Optional<List<ChallengeFeed>> findAllByChallengeOrderByCreatedAtDesc(Challenge challenge, Pageable pageable);
}
