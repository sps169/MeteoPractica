import pojos.*;

import ioutils.DataReader;
import service.Analytics;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main (String[] args) {
        Optional<Station> optionalStation = DataReader.getStation("Villa del Prado");
        Station station = optionalStation.orElse(null);
        Stream<String> data = null;
        if (station != null)
            data = DataReader.getStationDataStream((station));
        else
            System.err.println("No esiste siudad equisde");
        List<Measure> measuresList = DataReader.getMeasures(data);
        List<Magnitude> magnitudes = DataReader.getFile("magnitudes_aire.csv", Charset.defaultCharset()).map(s -> Arrays.asList(s.split(";"))).map(t -> new Magnitude(t.get(0),t.get(1),t.get(4))).collect(Collectors.toList());
        List<MonthData> informe = new ArrayList<>();
        for (Magnitude type: magnitudes) {
            informe.add(new MonthData(measuresList.stream().filter(s -> s.getMagnitude().equals(type.getCodMagnitude())).collect(Collectors.toList()), type));
        }
        informe.stream().forEach(System.out::println);
        try {
            Analytics anal = new Analytics(informe,System.getProperty("user.dir")+ File.separator+"data"+File.separator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
