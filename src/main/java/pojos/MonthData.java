package pojos;

import lombok.Data;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MonthData {
    private List<Measure> measures;
    private Magnitude type;
    private Moment maxMeasure;
    private Moment minMeasure;
    private LocalDate startDayMeasure;
    private LocalDate endDayMeasure;
    private double meanValueOfMeasures;
    /* todo graph */

    public MonthData(List<Measure> measures, Magnitude type) {
        this.measures = measures;
        if (measures.size() != 0) {
            this.startDayMeasure = measures.stream().map(s->s.getDay()).min(LocalDate::compareTo).get();
            this.endDayMeasure = measures.stream().map(s->s.getDay()).max(LocalDate::compareTo).get();
            this.maxMeasure = measures.stream().max((s, t) -> Float.compare(s.getMaxValue().getValue(), t.getMaxValue().getValue())).get().getMaxValue();
            this.minMeasure = measures.stream().min((s, t) -> Float.compare(s.getMinValue().getValue(), t.getMinValue().getValue())).get().getMinValue();
            this.meanValueOfMeasures = measures.stream().collect(Collectors.averagingDouble(s -> s.getDayMean()));
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return "MonthData{" +
                "type='" + type + '\'' +
                ", maxMeasure=" + maxMeasure +
                ", minMeasure=" + minMeasure +
                ", meanValueOfMeasures=" + meanValueOfMeasures +
                '}';
    }
}


