package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

/**
 * Class that models a measurement.
 * @author sps169, FedericoTB
 */
public class HourMeasurement {

    /**
     * int value that stores the hour of measurement
     */
    private int hour;

    /**
     * value of the measurement
     */
    private float value;

    /**
     * validation of the measurement. It can be {"V", "N", "T"}.
     */
    private char validation;
}
