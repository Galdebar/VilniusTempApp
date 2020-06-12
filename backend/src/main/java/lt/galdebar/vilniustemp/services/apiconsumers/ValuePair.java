package lt.galdebar.vilniustemp.services.apiconsumers;

import lombok.Data;

@Data
public class ValuePair<A,B> {
    private final A first;
    private final B second;
}
