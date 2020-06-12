package lt.galdebar.vilniustemp.services.apiconsumers.climacell;

import lt.galdebar.vilniustemp.persistence.domain.TemperatureDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ResponseToDTOAdapter {
    static TemperatureDTO responseToDTO(RemoteResponse response) {
        if(response==null){
            return null;
        }
        return new TemperatureDTO(
                response.getTemperature(),
                response.getTime()
        );
    }

    static List<TemperatureDTO> responseToDTO(List<RemoteResponse> responses) {
        if (responses == null) {
            return new ArrayList<>();
        }
        return responses.stream().map(ResponseToDTOAdapter::responseToDTO).collect(Collectors.toList());
    }
}
