package lt.galdebar.vilniustemp.persistence.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class TemperatureDTO {
    private final Float temperature;
    private final LocalDateTime observationTime;
}
