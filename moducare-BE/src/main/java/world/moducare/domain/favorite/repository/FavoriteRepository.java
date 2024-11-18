package world.moducare.domain.favorite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import world.moducare.domain.challengefeed.entity.ChallengeFeed;
import world.moducare.domain.favorite.entity.Favorite;
import world.moducare.domain.member.entity.Member;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    int countByFeed(ChallengeFeed feed);

    boolean existsByFeedAndMember(ChallengeFeed feed, Member member);

    void deleteByMemberIdAndFeedId(Long memberId, Long feedId);

}

