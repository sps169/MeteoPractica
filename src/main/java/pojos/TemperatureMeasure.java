package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TemperatureMeasure {
    private String type;
    private LocalDate day;
    private HourMeasurement[] dayMeasurements;
}
