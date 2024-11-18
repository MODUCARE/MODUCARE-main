package world.moducare.domain.mychallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import world.moducare.domain.challenge.entity.Challenge;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.mychallenge.entity.MyChallenge;
import world.moducare.domain.mychallenge.entity.Status;

import java.util.List;

public interface MyChallengeRepository extends JpaRepository<MyChallenge, Long> {

    @Query("select m from MyChallenge  m where m.member.id = :memberId and m.status = 'IN' order by m.challenge.id")
    List<MyChallenge> findByMemberIdAndStatus(@Param("memberId") Long memberId);

    @Query("select m from MyChallenge  m where m.member.id = :memberId and m.challenge.id = :challengeId and m.status = :status")
    MyChallenge findByMemberIdAndChallengeIdAndStatus(@Param("memberId") Long memberId, @Param("challengeId") Long challengeId, @Param("status") Status status);

    boolean existsByChallengeAndMember(Challenge challenge, Member member);

    @Query("select distinct mc.member from MyChallenge mc where mc.status='IN'")
    List<Member> findDistinctMemberId();
}
