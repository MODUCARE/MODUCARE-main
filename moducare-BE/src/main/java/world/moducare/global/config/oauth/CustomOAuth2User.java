package world.moducare.global.config.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import world.moducare.domain.member.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Member member;

    @Override
    public Map<String, Object> getAttributes() {

        return Map.of(
                "email", member.getEmail(),
                "memberId", member.getId() // 변경된 부분
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return member.getEmail();
    }

    public Long getId() {
        return member.getId();
    }

    public String getEmail() {
        return member.getEmail();
    }
}