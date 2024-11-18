package world.moducare.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SocialLoginRequest {
    private String accessToken;
    private String fcmToken;
}