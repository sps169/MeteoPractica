package pojos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class MonthTemperatureData {
    private ArrayList<TemperatureMeasure> measures;
    private Moment maxTemperature;
    private Moment minTemperature;
    private float meanTemperature;
    /* todo graph */

    public MonthTemperatureData(ArrayList<TemperatureMeasure> measures) {
         this.measures = measures;
    }
//
//    public static void main (String[] args)
//    {
//        monthTemperatureData
//    }
}


