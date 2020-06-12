package lt.galdebar.vilniustemp.persistence;

import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureEntity;
import lt.galdebar.vilniustemp.services.exceptions.InternalError;

import java.util.List;
import java.util.stream.Collectors;

public class TemperatureAdapter {
    public static TemperatureEntity dtoToEntity(TemperatureDTO dto) {
        checkIfNull(dto, "Error parsing data.");
        TemperatureEntity entity = new TemperatureEntity();
        entity.setTemperature(dto.getTemperature());
        entity.setObservationTime(dto.getObservationTime());
        return entity;
    }

    public static TemperatureDTO entityToDTO(TemperatureEntity entity) {
        checkIfNull(entity, "Error retrieving data.");
        return new TemperatureDTO(
                entity.getTemperature(),
                entity.getObservationTime()
        );
    }

    public static List<TemperatureDTO> entityToDTO(List<TemperatureEntity> entities) {

        return entities.stream().map(TemperatureAdapter::entityToDTO).collect(Collectors.toList());
    }

    public static List<TemperatureEntity> dtoToEntity(List<TemperatureDTO> dtos) {
        return dtos.stream().map(TemperatureAdapter::dtoToEntity).collect(Collectors.toList());
    }

    private static void checkIfNull(Object object, String message){
        if(object == null){
            throw new InternalError(message);
        }
    }
}
