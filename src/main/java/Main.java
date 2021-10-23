import pojos.HourMeasurement;
import pojos.Measure;
import pojos.Station;

import ioutils.DataReader;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        Comparator comparador = Comparator.comparing(HourMeasurement::getValue);
        measuresList.stream().filter(y -> y.getType().equals("83")).forEach(System.out::println);
    }
}
