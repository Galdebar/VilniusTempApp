package lt.galdebar.vilniustemp.persistence.repositories;

import lt.galdebar.vilniustemp.persistence.domain.TemperatureEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TemperatureRepo extends MongoRepository<TemperatureEntity, String> {
    TemperatureEntity findFirstByOrderByObservationTimeDesc();
    TemperatureEntity findFirstByOrderByObservationTimeAsc();
    List<TemperatureEntity> findAllByOrderByObservationTimeDesc(Pageable pageable);
    boolean existsTemperatureEntityByObservationTime(LocalDateTime observationTime);
}
