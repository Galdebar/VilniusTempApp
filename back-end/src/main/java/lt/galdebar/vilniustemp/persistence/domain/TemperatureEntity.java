package lt.galdebar.vilniustemp.persistence.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection="temperature")
public class TemperatureEntity {
    @Id
    private String id;
    private Float temperature;
    @Indexed(unique = true)
    private LocalDateTime observationTime;
}
