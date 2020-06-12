package lt.galdebar.vilniustemp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lt.galdebar.vilniustemp.services.exceptions.BadRequest;
import lt.galdebar.vilniustemp.context.ClimaCellRemoteAPIConfig;
import lt.galdebar.vilniustemp.persistence.TemperatureAdapter;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureEntity;
import lt.galdebar.vilniustemp.persistence.repositories.TemperatureRepo;
import lt.galdebar.vilniustemp.services.apiconsumers.climacell.RemoteAPIConsumer;
import lt.galdebar.vilniustemp.services.exceptions.InternalError;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;
import static lt.galdebar.vilniustemp.persistence.TemperatureAdapter.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class TemperatureService {


    private final RemoteAPIConsumer remoteAPIConsumer;
    private final TemperatureRepo repo;
    private final ClimaCellRemoteAPIConfig config;

    public TemperatureDTO updateTemperature() {
        TemperatureDTO latestTemp = remoteAPIConsumer.getCurrentTemperature();
        TemperatureEntity entityToSave = TemperatureAdapter.dtoToEntity(latestTemp);
        TemperatureEntity savedEntity = repo.save(entityToSave);

        TemperatureEntity lastEntity = repo.findFirstByOrderByObservationTimeAsc();
        repo.deleteById(lastEntity.getId());
        repo.delete(lastEntity);
        return entityToDTO(savedEntity);
    }

    public TemperatureDTO getLatestTemperature() {
        TemperatureEntity entity = repo.findFirstByOrderByObservationTimeDesc();
        return entityToDTO(entity);
    }

    public void refreshHistory() {
        log.info("Updating temperature history");
        List<TemperatureDTO> history = remoteAPIConsumer.getHistory(config.getMaxHistoryDays());
        List<TemperatureEntity> entities = dtoToEntity(history);
        repo.saveAll(entities);
    }

    public List<TemperatureDTO> getHistory(int numOfDays) {
        isDaysCountValid(numOfDays);
        List<TemperatureEntity> entities= repo.findAllByOrderByObservationTimeDesc(PageRequest.of(0, numOfDays * 24).first());
        return entityToDTO(entities);
    }

    public List<TemperatureDTO> getHistory() {
        return getHistory(config.getMaxHistoryDays());
    }

    private void isDaysCountValid(Integer days){
        if(days<1 || days > config.getMaxHistoryDays()){
            throw new BadRequest("Incorrect number of days specified. Must be between 1 and " + config.getMaxHistoryDays());
        }
    }

}
