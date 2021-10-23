package pojos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonthData {
    private List<Measure> measures;
    private Moment maxTemperature;
    private Moment minTemperature;
    private float meanTemperature;
    /* todo graph */

    public MonthData(List<Measure> measures) {
         this.measures = measures;
    }

}


