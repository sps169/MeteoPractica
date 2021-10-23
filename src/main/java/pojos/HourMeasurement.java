package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HourMeasurement {
    private int hour;
    private float value;
    private char validation;
}
