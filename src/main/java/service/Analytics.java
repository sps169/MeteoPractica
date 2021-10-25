package service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import pojos.HourMeasurement;
import pojos.Measure;
import pojos.MonthData;
import pojos.Station;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that models all the data obtained in order to generate the output
 * @author sps169, FedericoTB
 */
public class Analytics {
    private final List<MonthData> contaminationData;
    private final List<MonthData> meteorologyData;
    private List<String> contentHtml;
    private final Station station;
    private final Path uri;
    private final long initialTime;

    /**
     * Constructor Method that initialize the variables and call the method generatedChart for each MonthData to generate
     * the body of the html output.
     * @param contaminationData that is a List of {@link MonthData} of contamination data.
     * @param meteorologyData that is a List of {@link MonthData} of meteorology data.
     * @param station that is a instance of the {@link Station} passed by argument to the program
     * @param uri that is a {@link Path} of directory passed by argument where is will be created the inform.
     * @param initialTime long used to measure time of execution
     * @throws IOException when the method write of {@link File} fails to write
     */
    public Analytics (List<MonthData> contaminationData, List<MonthData> meteorologyData, Station station, Path uri, long initialTime) throws IOException {
        this.contaminationData =contaminationData;
        this.meteorologyData = meteorologyData;
        this.contentHtml = new ArrayList<>();
        this.station = station;
        this.uri = uri;
        this.initialTime = initialTime;
        generateChart(this.meteorologyData);
        generateChart(this.contaminationData);
    }

    /**
     * Method that create the output html with each line of the List of String contentHtml and open the default system web browser with him.
     * If the file existed before is deleted. If the sepator char is of the path is \ is replaced for /.
     * @throws IOException when the method write of {@link File} fails to write
     * @throws URISyntaxException when the method browse of {@link Desktop} fails to resolve the syntax of the uri
     */
    public void generateHtml() throws IOException, URISyntaxException {
        String filePath = this.uri + File.separator + station.getStationCity()+"-"+LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-uuuu"))+".html";
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

    /**
     * Method that create the body of the output html adding line to line with html tags and using the method generatorHTMLBody
     * for the content of the Data.
     */
    public void htmlBuilder() {
        this.contentHtml.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<style>table,th,td{border: 1px solid black}</style>\n<title>\n"+station.getStationCity()+" Measures"+"</title>\n</head>\n<body>\n");
        this.contentHtml.add("<h1>"+this.station.getStationCity()+"</h1>\n"+"<h2>Start Date of Measures: "+getStartingDate()+"</h2>\n");
        this.contentHtml.add("<h2>End Date of Measures: "+getEndingDate()+"</h2>\n"+"<h2>Name of Stations: "+this.station.getStationCity()
                + " " + this.station.getStationZone() + " (" + this.station.getStationCode() + ") " +"</h2>\n<h2>Measures Information:</h2>\n");
        this.contentHtml.add("<h2>Meteorologic Measures:</h2>");
        generatorHTMLBody(this.meteorologyData);
        this.contentHtml.add("<h2>Pollution Measures:</h2>");
        generatorHTMLBody(this.contaminationData);
        double endTime = ((double)(System.currentTimeMillis()-initialTime))/1000;
        this.contentHtml.add("<h2>This inform is generated with date "+LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))+" at time "+ LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))+" in "+endTime+" seconds</h2>\n");
        this.contentHtml.add("<h2>By Sergio P&eacute;rez Sanz & Federico Toledo Baeza</h2>\n");
        this.contentHtml.add("</body>\n</html>");
    }
    /**
     * Method that generate the content of the body of the output html adding line to line with html tags with the values of the
     * MonthData passed in html tables with the data and the png charts generated by the method generateChart
     * for the content of the Data.
     * @param monthDataList that is a {@link List} of {@link MonthData} with the data of the measures readied
     */
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
    /**
     * Method that generate the charts using JFreeChart each measure
     * type(Histogram for Precipitacion type, BarChart for the rest of types).
     * @param monthDataList that is a {@link List} of {@link MonthData} with the data of the measures readied
     * @throws IOException when the method createFile of {@link Files} fails to find the parent
     * directory of the file to create
     */
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

                    hDataSet.addSeries(measure.getDay().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")),measure.getDayMeasurements().stream().mapToDouble(HourMeasurement::getValue).toArray(),measure.getDayMeasurements().size());
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
                    egDataSet.addValue(measure.getDayMean(),new String(monthData.getType().getDescription().getBytes("windows-1252")),""+measure.getDay().getDayOfMonth());
                }
                JFreeChart egChart = ChartFactory.createBarChart(new String(monthData.getType().getDescription().getBytes("windows-1252")),
                        "Days", new String(monthData.getType().getUnit().getBytes("windows-1252")), egDataSet);

                if(Files.exists(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png"))){
                    Files.delete(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png"));
                }
                chart = Files.createFile(Path.of(uri+File.separator+"images"+File.separator+"gechart"+monthData.getType().getCodMagnitude()+".png")).toFile();
                ChartUtils.saveChartAsPNG(chart, egChart, 600, 400);
            }
        }
    }
    /**
     * Method that return the date of minimum date of the measures.
     * @return a {@link String} with a date in format DD/MM/YYYY
     */
    private String getStartingDate(){
       return this.meteorologyData.stream().filter(Objects::nonNull).map(MonthData::getStartDayMeasure).filter(Objects::nonNull)
               .min(LocalDate::compareTo).get().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
    }
    /**
     * Method that return the date of maximum date of the measures.
     * @return a {@link String} with a date in format DD/MM/YYYY
     */
    private String getEndingDate(){
       return this.meteorologyData.stream().filter(Objects::nonNull).map(MonthData::getEndDayMeasure).filter(Objects::nonNull)
               .max(LocalDate::compareTo).get().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
    }
}
