package service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import pojos.Measure;
import pojos.MonthData;
import pojos.Station;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Analytics {
    private List<MonthData> contaminationData;
    private List<MonthData> meteorologyData;
    private List<String> contentHtml;
    private Station station;
    private Path uri;

    public Analytics (List<MonthData> contaminationData, List<MonthData> meteorologyData, Station station, Path uri) throws IOException {
        this.contaminationData =contaminationData;
        this.meteorologyData = meteorologyData;
        this.contentHtml = new ArrayList<>();
        this.station = station;
        this.uri = uri;
        generateChart(this.meteorologyData);
        generateChart(this.contaminationData);
    }

    public void generateHtml() throws IOException, URISyntaxException {
        String filePath = this.uri + File.separator + station.getStationCity()+"-"+LocalDate.now()+".html";
        if(Files.exists(Path.of(filePath))){
            Files.delete(Path.of(filePath));
        }
        Files.createFile(Path.of(filePath));
        for (String line:this.contentHtml) {
            Files.write(Path.of(filePath),line.getBytes("windows-1252"), StandardOpenOption.APPEND);
        }
        String uriPath = filePath;
        if (uriPath.contains("\\")) {
            uriPath = filePath.replace("\\","/");
        }
        Desktop.getDesktop().browse(new URI(uriPath));
    }

    public void htmlBuilder(long initialTime) {
        this.contentHtml.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<style>table,th,td{border: 1px solid black}</style>\n<title>\n"+station.getStationCity()+" Measures"+"</title>\n</head>\n<body>\n");
        this.contentHtml.add("<h1>"+this.station.getStationCity()+"</h1>\n"+"<h2>Start Date of Measures: "+getStartingDate()+"</h2>\n");
        this.contentHtml.add("<h2>End Date of Measures: "+getEndingDate()+"</h2>\n"+"<h2>Name of Stations: "+this.station.getStationCity()+"</h2>\n<h2>Measures Information:</h2>\n");
        this.contentHtml.add("<h2>Meteorologic Measures:</h2>");
        generatorHTMLBody(this.meteorologyData);
        this.contentHtml.add("<h2>Pollution Measures:</h2>");
        generatorHTMLBody(this.contaminationData);
        double endTime = ((double)(System.currentTimeMillis()-initialTime))/1000;
        this.contentHtml.add("<h2>This inform is generated with date "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))+" at time "+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))+" in "+endTime+" seconds</h2>\n");
        this.contentHtml.add("</body>\n</html>");
    }
    public void generatorHTMLBody(List<MonthData> monthDataList){
        for (MonthData monthData:monthDataList) {
            this.contentHtml.add("<table>\n<tr>\n<th>"+monthData.getType().getDescription()+"</th>\n</tr>\n");
            if(monthData.getMaxMeasure()!=null){
                if(!monthData.getType().getCodMagnitude().equals("89")){
                    this.contentHtml.add("<tr>\n<td><b>Average "+monthData.getType().getDescription()+" Monthly:</b> "+monthData.getMeanValueOfMeasures()+
                            " "+monthData.getType().getUnit()+"</td>\n</tr>\n");
                    this.contentHtml.add("<tr>\n<td><b>Maximum "+monthData.getType().getDescription()+" Monthly:</b> Date "+monthData.getMaxMeasure().getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu hh:mm:ss"))+
                            " "+monthData.getMaxMeasure().getValue()+" "+monthData.getType().getUnit()+"</td>\n</tr>\n");
                    this.contentHtml.add("<tr>\n<td><b>Minimum "+monthData.getType().getDescription()+" Monthly:</b> Date "+monthData.getMinMeasure().getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu hh:mm:ss"))+
                            " "+monthData.getMinMeasure().getValue()+" "+monthData.getType().getUnit()+"</td>\n</tr>\n");
                    this.contentHtml.add("<tr>\n<td><img src=\"images/gechart"+monthData.getType().getCodMagnitude()+
                            ".png\" alt=\"\"/></td></tr>");
                    this.contentHtml.add("</table><br/>\n");}
                else{
                    this.contentHtml.add("<tr>\n<td><b>Average "+monthData.getType().getDescription()+" Monthly:</b> "+monthData.getMeanValueOfMeasures()+
                            " "+monthData.getType().getUnit()+"</td>\n</tr>\n");
                    this.contentHtml.add("<tr>\n<td><b>List of "+monthData.getType().getDescription()+" monthly for Dates:</b>\n <ul>");
                    for(Measure measure : monthData.getMeasures()){
                        if(measure.getDayMean()>0)
                            this.contentHtml.add("<li>"+measure.getDay().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))+" "+measure.getDayMean()+" "+monthData.getType().getUnit()+"</li>\n");
                    }
                    this.contentHtml.add("</ul>\n</tr>\n<tr>\n<td><img src=\"images"+"/"+"hchart89.png\" alt=\"\"/></td></tr>");
                    this.contentHtml.add("</table><br/>\n");
                }
            }else{
                this.contentHtml.add("<tr>\n<td><b>No disponible Data</b></td>\n</tr>\n</table><br/>\n");
            }
        }
    }
    public void generateChart(List<MonthData> monthDataList) throws IOException {
        if(!Files.exists(Path.of(this.uri.toAbsolutePath() + File.separator+"images"))){
            Files.createDirectory(Path.of(this.uri.toAbsolutePath() + File.separator+"images"));
        }
        File chart;
        for (MonthData monthData:monthDataList) {

            if (monthData.getType().getCodMagnitude().equals("89")){
                HistogramDataset hDataSet = new HistogramDataset();
                for (Measure measure:monthData.getMeasures()
                ) {

                    hDataSet.addSeries(measure.getDay().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")),measure.getDayMeasurements().stream().mapToDouble(s->s.getValue()).toArray(),measure.getDayMeasurements().size());
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
                    egDataSet.addValue(measure.getDayMean(),monthData.getType().getDescription(),""+measure.getDay().getDayOfMonth());
                }
                JFreeChart egChart = ChartFactory.createBarChart(new String(monthData.getType().getDescription().getBytes("windows-1252")),
                        "Days", "Quantity", egDataSet);

                if(Files.exists(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png"))){
                    Files.delete(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png"));
                }
                chart = Files.createFile(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png")).toFile();
                ChartUtils.saveChartAsPNG(chart, egChart, 600, 400);
            }
        }
    }
    private String getStartingDate(){
       return this.meteorologyData.stream().filter(n->!n.equals(null)).map(m->m.getStartDayMeasure()).filter(l->!l.equals(null))
               .min(LocalDate::compareTo).get().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
    }

    private String getEndingDate(){
       return this.meteorologyData.stream().filter(n->!n.equals(null)).map(m->m.getEndDayMeasure()).filter(l->!l.equals(null))
               .max(LocalDate::compareTo).get().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
    }
}
