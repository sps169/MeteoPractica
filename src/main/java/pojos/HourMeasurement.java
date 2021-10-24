package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor


public class HourMeasurement {
    /**
     * Class that stores a value, its validation (should be 'V', 'N' or 'T') and the hour the value was taken at.
     * @serialField hour int time of the measurement
     * @serialField value int value of the measurement
     * @serialField validation char validation of the measurement
     */

    private int hour;
    private float value;
    private char validation;
}
