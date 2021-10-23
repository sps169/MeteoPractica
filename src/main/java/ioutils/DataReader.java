package ioutils;

import pojos.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataReader {
    public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    public static final String DATA_DIR = System.getProperty("user.dir") + SEPARATOR + "data";
    public static Stream<String> getFile(String path, Charset charset){
        Stream<String> result = null;
        try {
            result = Files.lines(Paths.get(DATA_DIR + SEPARATOR + path), charset);
        } catch (IOException ex) {
            System.err.println("Error de lectura de datos");
        }
        return result;
    }

    public static Optional<Station> getStation(String city) {
        Stream<String> fileData = getFile("calidad_aire_estaciones.csv", Charset.forName("windows-1252"));
        return fileData.filter(s -> Arrays.asList(s.split(";")).get(2).equalsIgnoreCase(city)).map(s -> s.split(";")).map(v -> new Station(v[0], v[1], v[2])).findFirst();
    }

    public static Stream<String> getStationDataStream (Station station)
    {
        Stream<String> data = getFile("calidad_aire_datos_meteo_mes.csv", Charset.forName("windows-1252"));
        data = Stream.concat(data, getFile("calidad_aire_datos_mes.csv", Charset.forName("windows-1252")));
        return data.filter(s -> Arrays.asList(s.split(";")).get(4).contains(station.getStationCode()));
    }

    public static List<Measure> getMeasures(Stream<String> data) {
        List<Measure> measuresList = data.map(s -> Arrays.asList(s.split(";"))).map(list -> {
            List<HourMeasurement> measures = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                List<String> measurement= list.subList(8 + i * 2, 8 + i * 2 + 2);
                float value = 0;
                if (!measurement.get(0).equals("")) {
                    value = Float.parseFloat(measurement.get(0).replace(',', '.'));
                }
                measures.add(new HourMeasurement(i + 1 , value, measurement.get(1).charAt(0)));
            }
            LocalDate date = LocalDate.of(Integer.parseInt(list.get(5)), Integer.parseInt(list.get(6)), Integer.parseInt(list.get(7)));
            return new Measure( list.get(3), date, measures);
        }).collect(Collectors.toList());
        return measuresList;
    }
}
