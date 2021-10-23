import pojos.HourMeasurement;
import pojos.Measure;
import pojos.MonthData;
import pojos.Station;

import ioutils.DataReader;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main (String[] args) {
        Optional<Station> optionalStation = DataReader.getStation("Leganés");
        Station station = optionalStation.orElse(null);
        Stream<String> data = null;
        if (station != null)
            data = DataReader.getStationDataStream((station));
        else
            System.err.println("No esiste siudad equisde");
        List<Measure> measuresList = DataReader.getMeasures(data);
        List<String> magnitudes = DataReader.getFile("magnitudes_aire.csv", Charset.defaultCharset()).map(s -> Arrays.asList(s.split(";"))).map(t -> t.get(0)).collect(Collectors.toList());
        List<MonthData> informe = new ArrayList<>();
        for (String type: magnitudes) {
            informe.add(new MonthData(measuresList.stream().filter(s -> s.getType().equals(type)).collect(Collectors.toList()), type));
        }
        informe.stream().forEach(System.out::println);
    }
}
