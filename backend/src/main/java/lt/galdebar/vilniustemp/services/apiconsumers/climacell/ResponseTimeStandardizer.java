package lt.galdebar.vilniustemp.services.apiconsumers.climacell;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

class ResponseTimeStandardizer {
    private static final int MAX_MINUTES_BEFORE_CUTOFF = 15;
    private static final int MIN_MINUTES_BEFORE_CUTOFF = 45;

    boolean isCloserToHourMark(RemoteResponse response) {
        int actualMinutes = response.getTime().getMinute();
        return actualMinutes <= MAX_MINUTES_BEFORE_CUTOFF || actualMinutes >= MIN_MINUTES_BEFORE_CUTOFF;
    }

    void setHourToZero(RemoteResponse response) {
        ZonedDateTime actualTime = response.getObservationTime().getValue();
        int actualMinutes = response.getTime().getMinute();
        ZonedDateTime newTime;

        if (actualMinutes < MAX_MINUTES_BEFORE_CUTOFF){
            newTime = actualTime.minusMinutes(actualMinutes);
        } else if(actualMinutes > MIN_MINUTES_BEFORE_CUTOFF){
            long deltaMinutes = TimeUnit.HOURS.toMinutes(1) - actualMinutes;
            newTime = response.getObservationTime().getValue().plusMinutes(
                    deltaMinutes
            );
        } else {
            newTime = actualTime;
        }

        response.getObservationTime().setValue(newTime);
    }
}
