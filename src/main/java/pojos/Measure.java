package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Measure {
    private String type;
    private LocalDate day;
    private List<HourMeasurement> dayMeasurements;
}
