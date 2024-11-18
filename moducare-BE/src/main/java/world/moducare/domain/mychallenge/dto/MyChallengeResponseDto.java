package world.moducare.domain.mychallenge.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyChallengeResponseDto {

    private Long challengeId;

    private String challengeImg;

    private String challengeName;

    private int isDone;

}
