package lt.galdebar.vilniustemp.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
public class TemperatureDTO {
    private final Float temperature;
    private final ZonedDateTime observationTime;
}
