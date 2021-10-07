package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Station {
    private String stationCode;
    private String stationZone;
    private String stationCity;
}
