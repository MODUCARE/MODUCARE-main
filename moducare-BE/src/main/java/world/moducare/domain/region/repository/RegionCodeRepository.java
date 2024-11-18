package world.moducare.domain.region.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import world.moducare.domain.region.entity.regionCode;

import java.util.Optional;

@Repository
public interface RegionCodeRepository extends JpaRepository<regionCode, String> {
    Optional<regionCode> findBySidoAndGugun(String sido, String gugun);
}
