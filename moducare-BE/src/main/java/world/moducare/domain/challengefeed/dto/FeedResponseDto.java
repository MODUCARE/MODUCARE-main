package world.moducare.domain.challengefeed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class FeedResponseDto {
    private Long feedId;
    private String feedImg;
    private String feedUserName;
    private String content;
    private String feedRegDate;
    private int like;
    private int isLiked;
}