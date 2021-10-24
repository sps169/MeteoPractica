package service;

import ioutils.DataReader;
import pojos.Magnitude;
import pojos.Measure;
import pojos.MonthData;
import pojos.Station;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MeteoPractice {
    private static final String STATIONS_FILE = "calidad_aire_estaciones.csv";
    private static final String METEOROLOGY_FILE = "calidad_aire_datos_meteo_mes.csv";
    private static final String CONTAMINATION_FILE = "calidad_aire_datos_mes.csv";
    private static final String MAGNITUDES_FILE = "magnitudes_aire.csv";
    private static final String MAGNITUDES_METEO_FILE = "magnitudes_aire_meteo.csv";
    private static final String CSV_SEPARATOR = ";";
    public static Analytics generateMeteoAnalysis (String city, String directoryURI) {
        Path directory = DataReader.createDirectory(directoryURI);
        Station station = DataReader.getStation(city, STATIONS_FILE).orElse(null);
        Stream<String> contaminationStream = null;
        Stream<String> meteorologyStream = null;
        if (station != null) {
            contaminationStream = DataReader.getStationDataStream(station, CONTAMINATION_FILE);
            meteorologyStream = DataReader.getStationDataStream(station, METEOROLOGY_FILE);
            List<Measure> contaminationMeasuresList = DataReader.getMeasures(contaminationStream);
            List<Measure> meteorologyMeasuresList = DataReader.getMeasures(meteorologyStream);
            List<Magnitude> magnitudes = DataReader.getFile(MAGNITUDES_FILE, Charset.forName("windows-1252")).map(s -> Arrays.asList(s.split(CSV_SEPARATOR))).map(t -> new Magnitude(t.get(0),t.get(1),t.get(4))).collect(Collectors.toList());
            List<Magnitude> magnitudesMeteo = DataReader.getFile(MAGNITUDES_METEO_FILE, Charset.forName("windows-1252")).map(s -> Arrays.asList(s.split(CSV_SEPARATOR))).map(t -> new Magnitude(t.get(0),t.get(1),t.get(4))).collect(Collectors.toList());
            List<MonthData> contaminationReport = new ArrayList<>();
            List<MonthData> meteorologyReport = new ArrayList<>();
            for (Magnitude type: magnitudes) {

                contaminationReport.add(new MonthData(contaminationMeasuresList.stream().filter(s -> s.getMagnitude().equals(type.getCodMagnitude())).collect(Collectors.toList()), type));
            }
            for(Magnitude type:magnitudesMeteo){
                meteorologyReport.add(new MonthData(meteorologyMeasuresList.stream().filter(s -> s.getMagnitude().equals(type.getCodMagnitude())).collect(Collectors.toList()), type));
            }
            Analytics generalReport = null;
            try {
                generalReport = new Analytics(contaminationReport,meteorologyReport, station, directory);
            } catch (IOException e) {
                System.err.println("There is a problem with save directory");
            }
            return generalReport;
        }
        else {
            System.err.println("That city doesn't exists");
            return null;
        }
    }
}
