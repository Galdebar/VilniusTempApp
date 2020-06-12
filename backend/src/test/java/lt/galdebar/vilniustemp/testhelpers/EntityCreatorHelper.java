package lt.galdebar.vilniustemp.testhelpers;

import lt.galdebar.vilniustemp.persistence.domain.TemperatureEntity;

import java.time.LocalDateTime;

public class EntityCreatorHelper {
    public static TemperatureEntity createEntity(float temperature, LocalDateTime time){
        TemperatureEntity entity = new TemperatureEntity();
        entity.setTemperature(temperature);
        entity.setObservationTime(time);
        return entity;
    }
}
