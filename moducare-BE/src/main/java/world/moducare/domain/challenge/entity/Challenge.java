package world.moducare.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import world.moducare.domain.challengefeed.entity.ChallengeFeed;
import world.moducare.domain.mychallenge.entity.MyChallenge;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "challenge")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "head_count", nullable = false, length = 1)
    @ColumnDefault(value = "1")
    private int headCount = 1;

    @Column(name = "img_src", columnDefinition = "TEXT")
    private String image;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
    }

    // relationships
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeFeed> challengeFeeds = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<MyChallenge> myChallenges = new ArrayList<>();

    @Builder
    public Challenge(String title, String image) {
        this.title = title;
        this.image = image;
    }

    // 인원수 증가
    public void increaseHeadCount() {
        this.headCount++;
    }

    // 인원수 감소
    public void decreaseHeadCount() {
        this.headCount--;
    }
}
