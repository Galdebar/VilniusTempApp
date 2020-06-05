package lt.galdebar.vilniustemp.services.apiconsumers.climacell;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClimaCellConsumer {
    @Value("${climacell.default_url:https://api.climacell.co/v3/weather/}")
    public static final String DEFAULT_URL = "";

    private static final String NOWCAST_LAYER = "/nowcast";
    private static final Pair<String,Float> LATITUDE = new Pair<>("lat", 54.6872f);
    private static final Pair<String,Float> LONGITUDE = new Pair<>("lon", 25.2797f);


}
