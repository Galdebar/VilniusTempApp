package lt.galdebar.vilniustemp.services.apiconsumers.climacell;

import lt.galdebar.vilniustemp.context.ClimaCellRemoteAPIConfig;
import lt.galdebar.vilniustemp.services.apiconsumers.ValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
class QueryBuilder {
    private static final String SCHEME = "https";
    private static final String DEFAULT_URL = "api.climacell.co/v3/weather";
    private static final String REALTIME = "realtime";
    private static final String HISTOROY = "historical/station";
    private static final String START_TIME_KEY = "start_time";
    private static final String END_TIME_KEY = "end_time";
    private static final int MAX_RANGE_DAYS = 1;
    private static final ValuePair<String, String> API_KEY = new ValuePair<>("apikey", "JonNnBYTlT6g8ZwKf77GqNB0tnAJWNOn");
    private static final ValuePair<String, Float> QUERY_PARAM_LOCATIOON_LATITUDE = new ValuePair<>("lat", 54.6872f);
    private static final ValuePair<String, Float> QUERY_PARAM_LOCATIOON_LONGITUDE = new ValuePair<>("lon", 25.2797f);
    private static final ValuePair<String, String> QUERY_PARAM_TEMPERATURE = new ValuePair<>("fields", "temp");
    private static final ValuePair<String, Integer> QUERY_PARAM_TIMESTEP = new ValuePair<>("timestep", 60);

    private UriComponentsBuilder builder;

    private final ClimaCellRemoteAPIConfig config;

    @Autowired
    QueryBuilder(ClimaCellRemoteAPIConfig config) {
        this.config = config;
        initBuilder();
    }

    private void initBuilder() {
        builder = UriComponentsBuilder.newInstance();
        builder
                .scheme(config.getScheme())
                .host(config.getUrl());
    }

    QueryBuilder queryRealtimeWithDefaultParams() {
        builder
                .path(config.getRealtimePath())
                .queryParam(config.getLatitude().getFirst(), config.getLatitude().getSecond())
                .queryParam(config.getLongitude().getFirst(), config.getLongitude().getSecond())
                .queryParam(config.getDefaultFields().getFirst(), config.getDefaultFields().getSecond());

        return this;
    }

    QueryBuilder queryHistoryWithDefaultParams() {
        builder
                .path(config.getHistoryPath())
                .queryParam(config.getLatitude().getFirst(), config.getLatitude().getSecond())
                .queryParam(config.getLongitude().getFirst(), config.getLongitude().getSecond())
                .queryParam(config.getDefaultFields().getFirst(), config.getDefaultFields().getSecond());
        return this;
    }

    QueryBuilder addTimeRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        builder
                .queryParam(config.getStartTimeKey(), rangeStart)
                .queryParam(config.getEndTimeKey(), rangeEnd);

        return this;
    }

    UriComponents build() {
        UriComponents finalUri = builder.build();
        initBuilder();
        return finalUri;
    }

    HttpEntity buildEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(config.getAuthorization().getFirst(), config.getAuthorization().getSecond());
        return new HttpEntity(headers);
    }

    private static LocalDateTime getStartOfDay() {
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime
                .minusSeconds(dateTime.getSecond())
                .minusMinutes(dateTime.getMinute())
                .minusHours(dateTime.getHour());
        return dateTime;
    }

    private static ValuePair<LocalDateTime, LocalDateTime> getTimeRangeForCurrentDay() {
        LocalDateTime startDate = LocalDateTime.now();
        startDate = startDate
                .minusSeconds(startDate.getSecond())
                .minusMinutes(startDate.getMinute());
        LocalDateTime endDate = getStartOfDay();
        return new ValuePair<>(endDate, startDate);
    }

    private static LocalDateTime getHistoryStartDate(int days) {
        return getStartOfDay().minusDays(days);
    }

    private ValuePair<LocalDateTime, LocalDateTime> getDayTimeRange(LocalDateTime start) {
        return new ValuePair<>(
                start,
                start.plusDays(config.getRequestRangeDays())
        );
    }

    List<ValuePair<LocalDateTime, LocalDateTime>> generateHistoryRanges(int numOfDays) {
        List<ValuePair<LocalDateTime, LocalDateTime>> list = new ArrayList<>();
        for (int i = 0; i < numOfDays; i++) {
            if (i == 0) {
                list.add(getTimeRangeForCurrentDay());
            }
            LocalDateTime startDate = getHistoryStartDate(numOfDays);
            list.add(
                    getDayTimeRange(startDate.plusDays(i))
            );
        }
        return list;
    }
}
