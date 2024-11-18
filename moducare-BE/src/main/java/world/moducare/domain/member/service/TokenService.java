package world.moducare.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.repository.MemberRepository;
import world.moducare.global.config.jwt.TokenProvider;
import world.moducare.global.exception.ErrorCode;
import world.moducare.global.exception.RestApiException;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생 -> 401
        if (!tokenProvider.validToken(refreshToken)) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        // 유효한 토큰일 때 리프레시 토큰으로 사용자 ID 찾음
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RestApiException(ErrorCode.FORBIDDEN_ACCESS));
        return tokenProvider.generateMemberToken(member, Duration.ofHours(2));
    }

}
