package lt.galdebar.vilniustemp.scheduledtasks;

import lt.galdebar.vilniustemp.context.ClimaCellRemoteAPIConfig;
import lt.galdebar.vilniustemp.persistence.repositories.TemperatureRepo;
import lt.galdebar.vilniustemp.testhelpers.TestContainersConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(initializers = {TestContainersConfig.Initializer.class})
@SpringBootTest(properties = {"climacell.refresh_period=1 * * * * *"})
class UpdateTemperatureSchedulerTest {

    private static final int DEFAULT_HISTORY_DAYS = 1;
    @Value("${climacell.refresh_period}")
    private String refreshPeriod;

    @Autowired
    private TemperatureRepo repo;

    @Autowired
    private ClimaCellRemoteAPIConfig config;

    @BeforeTestExecution
    void beforeExecution() {
        config.setMaxHistoryDays(DEFAULT_HISTORY_DAYS); //reduce number of history days to limit API calls.
    }

    @Autowired
    private UpdateTemperatureScheduler scheduler;

    @AfterEach
    void afterEach() {
        repo.deleteAll();
    }

    @Test
    void givenRefreshPeriod_whenGetNextUpdateTime_thenReturnLocalDateTime() {
        CronSequenceGenerator cronGenerator = new CronSequenceGenerator(refreshPeriod);
        LocalDateTime expectedTime = cronGenerator.next(new Date())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime actualTime = UpdateTemperatureScheduler.getNextUpdateTime();
        assertEquals(expectedTime, actualTime);
    }
}