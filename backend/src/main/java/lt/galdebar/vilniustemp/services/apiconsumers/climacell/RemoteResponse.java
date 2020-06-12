package lt.galdebar.vilniustemp.services.apiconsumers.climacell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
public class RemoteResponse {
    private Temp temp;
    @JsonProperty("observation_time")
    private ObservationTime observationTime;

    Float getTemperature(){
        return temp.getValue();
    }

    public LocalDateTime getTime(){
        return observationTime.value.toLocalDateTime();
    }

    public void setTime(LocalDateTime time){
        observationTime.setValue(
                time.atZone(ZoneId.systemDefault())
        );
    }

    @Data
    static class Temp{
        private float value;
        private String units;
    }

    @Data
    static class ObservationTime{
        private ZonedDateTime value;
    }

}
