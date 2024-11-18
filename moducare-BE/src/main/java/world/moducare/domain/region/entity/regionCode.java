package world.moducare.domain.region.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "region_code")
public class regionCode {
    @Id
    @Column(name = "code")
    private String code;

    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String gugun;
}
