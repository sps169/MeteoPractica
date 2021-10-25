package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor

/**
 * Class that models a day worth of data.
 * @author sps169, FedericoTB
 */
public class Measure {

    /**
     * Stores only the String value of a {@link Magnitude}
     * which means magnitude ==  Magnitude.getCodMagnitude().
     */
    private String magnitude;
    private LocalDate day;

    /**
     * List of {@link HourMeasurement} of a day. Should never have more than 24 elements.
     */
    private List<HourMeasurement> dayMeasurements;

    /**
     * Method that looks for the maximum valid value of the list.
     * @return {@link Moment} modeling a value and the DateTime of its measurement.
     */
    public Moment getMaxValue() {
        HourMeasurement maxValue = dayMeasurements.stream().filter(s-> s.getValidation() == 'V').max(Comparator.comparing(HourMeasurement::getValue)).get();
        //Hour is decremented by 1 to parse between 1 - 24 format and 0 - 23 format
        LocalTime hour = LocalTime.of(maxValue.getHour() - 1, 0);
        return new Moment(LocalDateTime.of(day, hour), maxValue.getValue());
    }
    /**
     * Method that looks for the minium valid value of the list.
     * @return {@link Moment} modeling a value and the DateTime of its measurement.
     */
    public Moment getMinValue() {
        HourMeasurement minValue = dayMeasurements.stream().filter(s-> s.getValidation() == 'V').min(Comparator.comparing(HourMeasurement::getValue)).get();
        //Hour is decremented by 1 to parse between 1 - 24 format and 0 - 23 format
        LocalTime hour = LocalTime.of(minValue.getHour() - 1, 0);
        return new Moment(LocalDateTime.of(day, hour), minValue.getValue());
    }

    /**
     * Method that computes the meaan of the valid values of the list.
     * @return {@link Float} mean value.
     */
    public Float getDayMean () {
        List<Float> validData = dayMeasurements.stream().filter(v -> v.getValidation() == 'V').map(s -> s.getValue()).collect(Collectors.toList());
        return validData.stream().reduce(Float::sum).get().floatValue()/validData.size();
    }
}
