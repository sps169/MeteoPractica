package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor

/**
 * Class that contains the hourly measurements of a day.
 * @serialField magnitude String code that represents a magnitude.
 * @serialField day {@LocalDate} date in which the measures where taken.
 * @serialField dayMeasurements List of {@HourMeasurement} of a day.
 */
public class Measure {
    private String magnitude;
    private LocalDate day;
    private List<HourMeasurement> dayMeasurements;

    public Moment getMaxValue() {
        HourMeasurement maxValue = dayMeasurements.stream().filter(s-> s.getValidation() == 'V').max(Comparator.comparing(HourMeasurement::getValue)).get();
        LocalTime hour = LocalTime.of(maxValue.getHour() - 1, 0);
        return new Moment(LocalDateTime.of(day, hour), maxValue.getValue());
    }

    public Moment getMinValue() {
        HourMeasurement minValue = dayMeasurements.stream().filter(s-> s.getValidation() == 'V').min(Comparator.comparing(HourMeasurement::getValue)).get();
        LocalTime hour = LocalTime.of(minValue.getHour() - 1, 0);
        return new Moment(LocalDateTime.of(day, hour), minValue.getValue());
    }

    public Float getDayMean () {
        List<Float> validData = dayMeasurements.stream().filter(v -> v.getValidation() == 'V').map(s -> s.getValue()).collect(Collectors.toList());
        return validData.stream().reduce(Float::sum).get().floatValue()/validData.size();
    }
}
