package service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import pojos.Measure;
import pojos.MonthData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class Analytics {
    private List<MonthData> contaminationData;
    private List<MonthData> meteorologyData;
    private String uri;

    public Analytics (List<MonthData> contaminationData, List<MonthData> meteorologyData,String uri) throws IOException {
        this.contaminationData =contaminationData;
        this.meteorologyData = meteorologyData;
        this.uri = uri;
        generateChart();
    }
    public List<String> generatorHTMLBody(String city,String stations){
        List<String> body= new ArrayList<>();
        body.add("<h1>"+city+"</h1>\n"+"<h2>Start Date of Measures: </h2>\n"+"<h2>End Date of Measures: </h2>\n"+"<h2>Name of Stations: "+stations+"</h2>\n<h2>Measures Information:</h2>\n");
        for (MonthData monthData:
                this.contaminationData) {
            if(monthData.getMaxMeasure()!=null){
                if(!monthData.getType().getCodMagnitude().equals("89")){
                    body.add("<table>\n<tr>\n<th>"+monthData.getType().getDescription()+"</th>\n</tr>\n");
                    body.add("<tr>\n<td>Average "+monthData.getType().getDescription()+" Monthly: "+monthData.getMeanValueOfMeasures()+" "+monthData.getType().getUnit()+"</td>\n</tr>\n+");
                    body.add("<tr>\n<td>Maximum "+monthData.getType().getDescription()+" Monthly: Date "+monthData.getMaxMeasure().getDate()+" "+monthData.getMaxMeasure()+" "+monthData.getType().getUnit()+"</td>\n</tr>\n+");
                    body.add("<tr>\n<td>Minimum "+monthData.getType().getDescription()+" Monthly: Date "+monthData.getMinMeasure().getDate()+" "+monthData.getMinMeasure()+" "+monthData.getType().getUnit()+"</td>\n</tr>\n+");
                    body.add("<tr>\n<td><img src='"+uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png"+"'/></td></tr>");
                    body.add("</table>\n");}
                else{
                    body.add("<table>\n<tr>\n<th>"+monthData.getType().getDescription()+"</th>\n</tr>\n");
                    body.add("<tr>\n<td>Average "+monthData.getType().getDescription()+" Monthly: "+monthData.getMeanValueOfMeasures()+" "+monthData.getType().getUnit()+"</td>\n</tr>\n+");
                    body.add("<tr>\n<td>List of "+monthData.getType().getDescription()+" monthly for Dates:\n <ul>");
                    for(Measure measure : monthData.getMeasures()){
                        if(measure.getDayMean()>0)
                            body.add("<li>"+measure.getDay()+" "+measure.getDayMean()+" "+monthData.getType().getUnit()+"</li>\n");
                    }
                    body.add("</ul>\n</tr>\n<tr>\n<td><img src='"+uri+File.separator+"images"+File.separator+"hchart89.png"+".png"+"'/></td></tr>");
                    body.add("</table>\n");
                }
            }else{
                body.add("<table>\n<tr>\n<th>"+monthData.getType().getDescription()+"</th>\n</tr>\n");
                body.add("<tr>\n<td>No disponible Data</td>\n</tr>\n+");
            }
        }
        return body;
    }
    public void generateChart() throws IOException {
        if(!Files.exists(Path.of(this.uri))){
            Files.createDirectory(Paths.get(this.uri));
        }
        File chart;
        for (MonthData monthData:
                this.contaminationData) {

            if (false) {//monthData.getType().getCodMagnitude().equals("89")){
                HistogramDataset hDataSet = new HistogramDataset();
                for (Measure measure:monthData.getMeasures()
                ) {

                    hDataSet.addSeries(measure.getDay(),measure.getDayMeasurements().stream().mapToDouble(s->s.getValue()).toArray(),measure.getDayMeasurements().size());
                }

                JFreeChart histoChart = ChartFactory.createHistogram("Precipitaciones",
                        "Data", "Frequency", hDataSet);
                if(Files.exists(Path.of(uri+File.separator+"images"+File.separator+"hchart89.png"))){
                    Files.delete(Path.of(uri+File.separator+"images"+File.separator+"hchart89.png"));
                }
                chart = Files.createFile(Path.of(uri+File.separator+"images"+File.separator+"hchart89.png")).toFile();
                ChartUtils.saveChartAsPNG(chart, histoChart, 600, 400);
            }else{
                DefaultCategoryDataset egDataSet = new DefaultCategoryDataset();

                for (Measure measure:monthData.getMeasures()) {
                    egDataSet.addValue(measure.getDayMean(),"pepino",""+measure.getDay().getDayOfMonth());
                }
                JFreeChart egChart = ChartFactory.createBarChart("JFreeChart Histogram",
                        "Days", "Quantity", egDataSet);

                if(Files.exists(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png"))){
                    Files.delete(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png"));
                }
                chart = Files.createFile(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png")).toFile();
                ChartUtils.saveChartAsPNG(chart, egChart, 600, 400);
            }
        }
    }
}
