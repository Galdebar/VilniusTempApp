package lt.galdebar.vilniustemp.services;

import lt.galdebar.vilniustemp.context.ClimaCellRemoteAPIConfig;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureEntity;
import lt.galdebar.vilniustemp.persistence.repositories.TemperatureRepo;
import lt.galdebar.vilniustemp.testhelpers.TestContainersConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static lt.galdebar.vilniustemp.testhelpers.EntityCreatorHelper.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(initializers = {TestContainersConfig.Initializer.class})
@TestPropertySource(properties = "app.scheduling.enable=false")
class TemperatureServiceTest {
    private static final int DEFAULT_NUM_OF_READINGS_PER_DAY = 24;
    private static final int DEFAULT_HISTORY_DAYS = 2;


    @Autowired
    private TemperatureService service;

    @Autowired
    private TemperatureRepo repo;

    @Autowired
    private ClimaCellRemoteAPIConfig config;

    @BeforeTestExecution
    void beforeExecution() {
        config.setMaxHistoryDays(DEFAULT_HISTORY_DAYS); //reduce number of history days to limit API calls.
    }

    @BeforeEach
    void beforeEach() {
        repo.deleteAll();
    }


    @Test
    void whenUpdateTemperature_thenDBEntryAdded() {
        LocalDateTime now = LocalDateTime.now();
        TemperatureEntity tempEntity1 = createEntity(22f, now.minusHours(2));
        TemperatureEntity tempEntity2 = createEntity(12f, now.minusHours(1));

        repo.save(tempEntity1);
        repo.save(tempEntity2);


        int expectedSize = 2;
        TemperatureEntity oldestEntityBeforeUpdate = tempEntity1;
        TemperatureEntity newestEntityBeforeUpdate = tempEntity2;

        TemperatureDTO actualTemp = service.updateTemperature();
        long actualSize = repo.count();

        TemperatureEntity oldestEntityAfterUpdate = repo.findFirstByOrderByObservationTimeAsc();
        TemperatureEntity newestEntityAfterUpdate = repo.findFirstByOrderByObservationTimeDesc();

        assertNotNull(actualTemp);
        assertEquals(expectedSize, actualSize);
        assertFalse(repo.existsById(oldestEntityBeforeUpdate.getId()));
        assertEquals(newestEntityBeforeUpdate.getId(), oldestEntityAfterUpdate.getId());
        assertEquals(newestEntityAfterUpdate.getObservationTime().truncatedTo(ChronoUnit.MINUTES),
                actualTemp.getObservationTime().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void whenGetLatestTemperature_thenReturnNewestTemperatureDTO() {
        LocalDateTime now = LocalDateTime.now();
        TemperatureEntity tempEntity1 = createEntity(22f, now.minusHours(2));
        TemperatureEntity tempEntity2 = createEntity(12f, now.minusHours(1));

        repo.save(tempEntity1);
        repo.save(tempEntity2);

        TemperatureDTO actualTemperature = service.getLatestTemperature();

        assertNotNull(actualTemperature);
        assertEquals(tempEntity2.getTemperature(), actualTemperature.getTemperature());
        assertEquals(tempEntity2.getObservationTime().truncatedTo(ChronoUnit.MINUTES),
                actualTemperature.getObservationTime().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void whenUpdateHistory_thenDBEntriesAdded() {
        service.refreshHistory();
        long actualCount = repo.count();
        assertTrue(actualCount > 0);
    }

    @Test
    void givenNoParams_whenGetHistory_thenReturnFullDBContents() {
        int expectedCount = DEFAULT_HISTORY_DAYS * 24; // because readings are fetched per hour
        service.refreshHistory();
        List<TemperatureDTO> actualList = service.getHistory();

        assertNotNull(actualList);
        assertEquals(expectedCount, actualList.size());
    }

    @Test
    void givenNumOfDays_whenGetHistory_thenGetRequiredNumOfObjects() {
        service.refreshHistory();
        int numOfDays = config.getMaxHistoryDays() - 1;
        int expectedCount = DEFAULT_NUM_OF_READINGS_PER_DAY * numOfDays;

        List<TemperatureDTO> actualList = service.getHistory(numOfDays);

        assertNotNull(actualList);
        assertEquals(expectedCount, actualList.size());
    }

}