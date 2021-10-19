package pojos;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MonthData {
    private ArrayList<Measure> measures;
    private Moment maxTemperature;
    private Moment minTemperature;
    private float meanTemperature;
    /* todo graph */

    public MonthData(ArrayList<Measure> measures) {
         this.measures = measures;
    }
//
//    public static void main (String[] args)
//    {
//        monthTemperatureData
//    }
}


