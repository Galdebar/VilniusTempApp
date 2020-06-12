package lt.galdebar.vilniustemp.scheduledtasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lt.galdebar.vilniustemp.services.TemperatureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Log4j2
public class UpdateTemperatureScheduler implements ApplicationListener<ContextRefreshedEvent> {

    private static String refreshPeriod;

    private final TemperatureService temperatureService;

    @Scheduled(cron = "${climacell.refresh_period}")
    void updateTemperature(){
        log.info("Running scheduled temperature update");
        temperatureService.updateTemperature();
    }

    @Value("${climacell.refresh_period}")
    public void setRefreshPeriod(String refreshPeriod){
        UpdateTemperatureScheduler.refreshPeriod = refreshPeriod;
    }

    public static LocalDateTime getNextUpdateTime(){
        final CronSequenceGenerator generator = new CronSequenceGenerator(refreshPeriod);
        Date date = generator.next(new Date());
        return date
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Running Context Refresh event");
        temperatureService.refreshHistory();
    }
}
