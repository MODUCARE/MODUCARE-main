package world.moducare.domain.favorite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.moducare.domain.challengefeed.entity.ChallengeFeed;
import world.moducare.domain.challengefeed.repository.ChallengeFeedRepository;
import world.moducare.domain.favorite.dto.LikeRequestDto;
import world.moducare.domain.favorite.entity.Favorite;
import world.moducare.domain.favorite.repository.FavoriteRepository;
import world.moducare.domain.member.entity.Member;
import world.moducare.global.exception.ErrorCode;
import world.moducare.global.exception.RestApiException;

@RequiredArgsConstructor
@Service
public class FavoriteService {
    private final ChallengeFeedRepository challengeFeedRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void likeFeed(Member member, Long feedId, LikeRequestDto requestDto) {
        ChallengeFeed challengeFeed = challengeFeedRepository.findById(feedId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));
        switch (requestDto.getStatus()) {
            case 0:
                addLike(member, challengeFeed);
                break;
            case 1:
                deleteLike(member, challengeFeed);
                break;
            default:
                throw new RestApiException(ErrorCode.CONFLICT);
        }
    }


    private void addLike(Member member, ChallengeFeed challengeFeed) {
        Favorite favorite = Favorite.builder()
                .member(member)
                .feed(challengeFeed)
                .build();
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void deleteLike(Member member, ChallengeFeed challengeFeed) {
        favoriteRepository.deleteByMemberIdAndFeedId(member.getId(), challengeFeed.getId());
    }
}
