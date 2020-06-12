package lt.galdebar.vilniustemp.context;

import lombok.Getter;
import lombok.Setter;
import lt.galdebar.vilniustemp.services.apiconsumers.ValuePair;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "remoteapi.climacell")
@Setter
public class ClimaCellRemoteAPIConfig {

    @Getter private String scheme;
    @Getter private String url;

    private String authorizationKey;
    private String authorizationValue;

    private String latitudeKey;
    private Float latitudeValue;

    private String longitudeKey;
    private Float longitudeValue;

    private String defaultFieldsKey;
    private String defaultFieldsValue;

    @Getter private String realtimePath;
    @Getter private String historyPath;

    @Getter private String startTimeKey;
    @Getter private String endTimeKey;

    @Getter private int maxHistoryDays;
    @Getter private int requestRangeDays;

    public ValuePair<String,String> getAuthorization(){
        return new ValuePair<>(authorizationKey, authorizationValue);
    }

    public ValuePair<String,Float> getLatitude(){
        return new ValuePair<>(latitudeKey, latitudeValue);
    }

    public ValuePair<String,Float> getLongitude(){
        return new ValuePair<>(longitudeKey, longitudeValue);
    }

    public ValuePair<String,String> getDefaultFields(){
        return new ValuePair<>(defaultFieldsKey, defaultFieldsValue);
    }


}
