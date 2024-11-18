package world.moducare.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import world.moducare.domain.challengefeed.entity.ChallengeFeed;
import world.moducare.domain.diagnosis.entity.DiagnosticResult;
import world.moducare.domain.diary.entity.HeadDiary;
import world.moducare.domain.favorite.entity.Favorite;
import world.moducare.domain.mychallenge.entity.MyChallenge;
import world.moducare.domain.notification.entity.Notification;
import world.moducare.domain.product.entity.LatestProduct;
import world.moducare.domain.stress.entity.StressResult;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(length = 30)
    private String name;

    @Column(length = 20)
    private String birth;

    @Column(name = "fcm_token", columnDefinition = "TEXT")
    private String fcmToken;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime updatedAt;

    @Builder
    public Member(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
        this.updatedAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
    }

    // relationships
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private LatestProduct latestProduct = new LatestProduct();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StressResult> stressResults = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<HeadDiary> headDiaries = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<DiagnosticResult> diagnosticResults = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ChallengeFeed> challengeFeeds = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MyChallenge> myChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Favorite> favorites = new ArrayList<>();

    public void updateRefreshAndFcm(String newRefreshToken, String newFcmToken) {
        this.refreshToken = newRefreshToken;
        this.fcmToken = newFcmToken;
    }

    public void updateRefresh(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }

    public void logout() {
        this.refreshToken = null;
        this.fcmToken = null;
    }

    public void modify(String name, String birth) {
        this.name = name;
        this.birth = birth;
    }

    public Member findMember() {
        return this;
    }
}
