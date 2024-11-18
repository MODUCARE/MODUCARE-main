package world.moducare.domain.product.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import world.moducare.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "latest_product")
public class LatestProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @Column(name = "img_src", nullable = false, columnDefinition = "TEXT")
    private String imgSrc;

    @Column(name = "link", nullable = false, columnDefinition = "TEXT")
    private String link;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime updatedAt;

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @Builder
    public LatestProduct(String imgSrc, String link, Member member) {
        this.imgSrc = imgSrc;
        this.link = link;
        this.member = member;
    }
}
