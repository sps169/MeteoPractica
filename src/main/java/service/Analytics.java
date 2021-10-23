package service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import pojos.HourMeasurement;
import pojos.Measure;
import pojos.MonthData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Analytics {
    private List<MonthData> data;
    private String uri;

    public Analytics (List<MonthData> data,String uri) throws IOException {
        this.data=data;
        this.uri = uri;
        generateChart();
    }

    public void generateChart() throws IOException {
        if(!Files.exists(Path.of(this.uri))){
            Files.createDirectory(Paths.get(this.uri));
        }
        File chart;
        for (MonthData monthData:
                this.data) {

            if (false) {//monthData.getType().getCodMagnitude().equals("89")){
                HistogramDataset hDataSet = new HistogramDataset();
                for (Measure measure:monthData.getMeasures()
                     ) {

                    hDataSet.addSeries(measure.getDay(),measure.getDayMeasurements().stream().mapToDouble(s->s.getValue()).toArray(),measure.getDayMeasurements().size());
                }

                JFreeChart histoChart = ChartFactory.createHistogram("Precipitaciones",
                        "Data", "Frequency", hDataSet);
               chart = Files.createFile(Path.of(uri+File.separator+"images"+File.separator+"hchart89.png")).toFile();
                ChartUtils.saveChartAsPNG(chart, histoChart, 600, 400);
            }else{
                DefaultCategoryDataset egDataSet = new DefaultCategoryDataset();

                for (Measure measure:monthData.getMeasures()) {
                    egDataSet.addValue(measure.getDayMean(),"pepino",""+measure.getDay().getDayOfMonth());
                }
                JFreeChart egChart = ChartFactory.createBarChart("JFreeChart Histogram",
                        "Days", "Quantity", egDataSet);
                chart = Files.createFile(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png")).toFile();
                ChartUtils.saveChartAsPNG(chart, egChart, 600, 400);
            }
        }
    }
}
