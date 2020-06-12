package lt.galdebar.vilniustemp.controllers;

import lombok.RequiredArgsConstructor;
import lt.galdebar.vilniustemp.context.ClimaCellRemoteAPIConfig;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;
import lt.galdebar.vilniustemp.scheduledtasks.UpdateTemperatureScheduler;
import lt.galdebar.vilniustemp.services.TemperatureService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class TemperatureController {

    private final TemperatureService temperatureService;
    private final ClimaCellRemoteAPIConfig config;

    @GetMapping("/current")
    public TemperatureDTO getCurrentTemperature() {
        return temperatureService.getLatestTemperature();
    }

    @GetMapping("/nextupdate")
    public LocalDateTime getNextTempUpdate(){
        return UpdateTemperatureScheduler.getNextUpdateTime();
    }

    @GetMapping("/history")
    public List<TemperatureDTO> getHistory(@RequestParam(required = false)Integer days){
        if(days == null){
            return temperatureService.getHistory();
        }

        return temperatureService.getHistory(days);
    }

}
