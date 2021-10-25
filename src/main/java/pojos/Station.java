package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

/**
 * Class that models a Station.
 * @author sps169, FedericoTB
 */
public class Station {
    /**
     * Stores the stationCode provided by Comunidad de Madrid.
     */
    private String stationCode;
    private String stationZone;
    private String stationCity;
}
