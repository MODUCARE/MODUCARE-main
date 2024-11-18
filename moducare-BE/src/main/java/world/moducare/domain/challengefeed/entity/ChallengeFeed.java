package world.moducare.domain.challengefeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import world.moducare.domain.challenge.entity.Challenge;
import world.moducare.domain.favorite.entity.Favorite;
import world.moducare.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "challenge_feed")
public class ChallengeFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @Column(name = "img_src", nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
    }

    // relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_idx", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Favorite> favorites = new ArrayList<>();

    @Builder
    public ChallengeFeed(String image, String content, Challenge challenge, Member member) {
        this.image = image;
        this.content = content;
        this.challenge = challenge;
        this.member = member;
    }

}
