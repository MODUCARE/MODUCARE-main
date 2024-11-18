package world.moducare.domain.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import world.moducare.domain.challenge.entity.Challenge;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("select c from Challenge c where c.headCount < 10" +
            "and c.id not in (select m.challenge.id from MyChallenge m where m.member.id = :memberId and m.status = 'IN')" +
            "order by c.id desc ")
    List<Challenge> findAvailableChallenges(@Param("memberId") Long memberId);
}
