package lt.galdebar.vilniustemp.services.apiconsumers.climacell;


import lombok.RequiredArgsConstructor;
import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;
import lt.galdebar.vilniustemp.services.apiconsumers.ValuePair;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lt.galdebar.vilniustemp.services.apiconsumers.climacell.ResponseToDTOAdapter.*;

@Service
@RequiredArgsConstructor
public class RemoteAPIConsumer {

    private final RestTemplate restTemplate;
    private final QueryBuilder queryBuilder;


    public TemperatureDTO getCurrentTemperature() {

        UriComponents uri = queryBuilder.queryRealtimeWithDefaultParams().build();
        HttpEntity httpEntity = queryBuilder.buildEntityWithHeaders();

        ResponseEntity<RemoteResponse> response = restTemplate.exchange(
                uri.toString(),
                HttpMethod.GET,
                httpEntity,
                RemoteResponse.class
        );

        RemoteResponse responseObj = response.getBody();
        responseObj.setTime(LocalDateTime.now());

        return responseToDTO(responseObj);
    }

    public List<TemperatureDTO> getHistory(int numOfDays) {
        List<ValuePair<LocalDateTime, LocalDateTime>> dates = queryBuilder.generateHistoryRanges(numOfDays);
        List<TemperatureDTO> dtos = new ArrayList<>();

        for (ValuePair<LocalDateTime, LocalDateTime> range : dates) {

            UriComponents uri = queryBuilder
                    .queryHistoryWithDefaultParams()
                    .addTimeRange(range.getFirst(), range.getSecond())
                    .build();

            HttpEntity httpEntity = queryBuilder.buildEntityWithHeaders();

            ResponseEntity<RemoteResponse[]> response = restTemplate.exchange(
                    uri.toString(),
                    HttpMethod.GET,
                    httpEntity,
                    RemoteResponse[].class
            );

            List<RemoteResponse> filteredList = filterList(Arrays.asList(response.getBody()));

            dtos.addAll(
                    responseToDTO(filteredList)
            );
        }


        return dtos;
    }

    private List<RemoteResponse> filterList(List<RemoteResponse> unfilteredList) {
        ResponseTimeStandardizer standardizer = new ResponseTimeStandardizer();

        List<RemoteResponse> filteredList = unfilteredList.stream()
                .filter(standardizer::isCloserToHourMark)
                .collect(Collectors.toList());
        filteredList.forEach(standardizer::setHourToZero);

        return filteredList;
    }

}
