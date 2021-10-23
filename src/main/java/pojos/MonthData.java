package pojos;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MonthData {
    private List<Measure> measures;
    private String type;
    private Moment maxMeasure;
    private Moment minMeasure;
    private double meanValueOfMeasures;
    /* todo graph */

    public MonthData(List<Measure> measures, String type) {
        this.measures = measures;
        if (measures.size() != 0) {
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


