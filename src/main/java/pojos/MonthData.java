package pojos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

@Data
public class MonthData {
    private List<Measure> measures;
    private String type;
    private Moment maxTemperature;
    private Moment minTemperature;
    private double meanTemperature;
    /* todo graph */

    public MonthData(List<Measure> measures, String type) {
        this.measures = measures;
        if (measures.size() != 0) {
            this.maxTemperature = measures.stream().max((s, t) -> Float.compare(s.getMaxValue().getValue(), t.getMaxValue().getValue())).get().getMaxValue();
            this.minTemperature = measures.stream().min((s, t) -> Float.compare(s.getMinValue().getValue(), t.getMinValue().getValue())).get().getMinValue();
            this.meanTemperature = measures.stream().collect(Collectors.averagingDouble(s -> s.getDayMean()));
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return "MonthData{" +
                "type='" + type + '\'' +
                ", maxTemperature=" + maxTemperature +
                ", minTemperature=" + minTemperature +
                ", meanTemperature=" + meanTemperature +
                '}';
    }
}


