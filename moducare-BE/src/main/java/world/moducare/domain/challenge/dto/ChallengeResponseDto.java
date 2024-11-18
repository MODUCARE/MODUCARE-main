package world.moducare.domain.challenge.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChallengeResponseDto {

    private Long challengeId;

    private String challengeImg;

    private int challengeUser;

    private String challengeName;
}
