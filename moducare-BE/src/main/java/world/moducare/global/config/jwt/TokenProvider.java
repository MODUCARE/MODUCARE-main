package world.moducare.global.config.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import world.moducare.domain.member.entity.Member;
import world.moducare.domain.member.service.MemberService;
import world.moducare.global.config.oauth.CustomOAuth2User;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    // 토큰을 생성하고 올바른 토큰인지 유효성을 검사하고, 토큰에서 필요한 정보를 가져오는 클래스
    private final JwtProperties jwtProperties;
    private final MemberService memberService;


    public String generateMemberToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member.getEmail(), member.getId());
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, String email, Long id) { // 인자는 만료 시간, 유저 정보 받음
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ 타입 : JWT
                // 내용 iss 발급자 : minipeach0923@gmail.com (propertise 파일에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now) // 내용 iat 발급일시 : 현재 시간
                .setExpiration(expiry) // 내용 exp 만료 일시 : expiry 멤버 변숫값
                .setSubject(email) // 내용 sub 토큰 제목 : 유저의 이메일
                .claim("id", id) // 클레임 id : 유저 ID
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Long userId = claims.get("id", Long.class);

        Set<SimpleGrantedAuthority> authorities;

        Member member = memberService.findById(userId);
        authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        // CustomOAuth2User를 Principal로 사용
        return new UsernamePasswordAuthenticationToken(
                new CustomOAuth2User(member),
                token,
                authorities
        );
    }

    // 비밀값으로 토큰을 복호화한 다음 클레임을 가져오는 메서드
    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
