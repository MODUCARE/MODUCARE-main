package world.moducare.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class SocialLoginResponse {
    private String jwtAccessToken;
    private String refreshToken;
    private String name;
    private String email;
    private String birth;
}