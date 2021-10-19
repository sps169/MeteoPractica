package ioutils;

import pojos.HourMeasurement;
import pojos.Station;
import pojos.Measure;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataReader {
    public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    public static final String DATA_DIR = System.getProperty("user.dir") + SEPARATOR + "data";
    public static Stream<String> getFile(String path, Charset charset){
        Stream<String> result = null;
        try {
            result = Files.lines(Paths.get(path), charset);
        } catch (IOException ex) {
            System.err.println("Error de lectura de datos");
        }
        return result;
    }

    public static Optional<Station> getStation(String city) {
        Stream<String> fileData = getFile(DATA_DIR + SEPARATOR + "calidad_aire_estaciones.csv", Charset.forName("windows-1252"));
        return fileData.filter(s -> Arrays.asList(s.split(";")).get(2).equalsIgnoreCase(city)).map(s -> s.split(";")).map(v -> new Station(v[0], v[1], v[2])).findFirst();
    }

    public static Stream<String> getStationDataStream (Station station)
    {
        Stream<String> data = getFile(DATA_DIR + SEPARATOR + "calidad_aire_datos_meteo_mes.csv", Charset.forName("windows-1252"));
        return data.filter(s -> Arrays.asList(s.split(";")).get(4).contains(station.getStationCode()));
    }

    public static List<Measure> getTemperatureMeasures(Stream<String> data) {
        List<Measure> measuresList = data.map(s -> Arrays.asList(s.split(";"))).map(list -> {
            HourMeasurement[] measures = new HourMeasurement[24];
            for (int i = 0; i < measures.length; i++) {
                List<String> measurement= list.subList(8 + i * 2, 8 + i * 2 + 2);
                float value = 0;
                if (!measurement.get(0).equals("")) {
                    value = Float.parseFloat(measurement.get(0).replace(',', '.'));
                }
                measures[i] = new HourMeasurement(i + 1 , value, measurement.get(1).charAt(0));
            }
            LocalDate date = LocalDate.of(Integer.parseInt(list.get(5)), Integer.parseInt(list.get(6)), Integer.parseInt(list.get(7)));
            return new Measure("typePlaceHolder", date, measures);
        }).collect(Collectors.toList());
        return measuresList;
    }

    public static void main (String[] args) {
        Optional<Station> optionalStation = getStation("Leganés");
        Station station = optionalStation.orElse(null);
        Stream<String> data = null;
        if (station != null)
            data = getStationDataStream((station));
        else
            System.err.println("No esiste siudad equisde");
        List<Measure> measuresList = getTemperatureMeasures(data);
        measuresList.stream().forEach(System.out::println);
    }
}
