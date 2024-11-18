package world.moducare.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import world.moducare.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // email로 사용자 정보를 가져옴
    Optional<Member> findByEmail(String email);

    // refresh token으로 사용자 정보를 가져옴
    Optional<Member> findByRefreshToken(String refreshToken);
}
