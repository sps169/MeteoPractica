package ioutils;

import pojos.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that provides file reading utility.
 * @author sps169, FedericoTB
 */
public class DataReading {
    public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    /**
     * Default input data directory.
     */
    public static final String DATA_DIR = System.getProperty("user.dir") + SEPARATOR + "data";

    /**
     * Method that receives the String path where the file is located
     * and the {@link Charset} it is encoded with and returns a {@link Stream} of {@link String}
     * containing its lines.
     * @param path {@link String} that contains the URI of the file.
     * @param charset {@link Charset} of the file.
     * @return {@link Stream} of {@link String} if the file is readable, null Stream otherwise.
     */
    public static Stream<String> getFile(String path, Charset charset){
        Stream<String> result = null;
        try {
            result = Files.lines(Paths.get(DATA_DIR + SEPARATOR + path), charset);
        } catch (IOException ex) {
            System.err.println("Data reading fatal error.");
        }
        return result;
    }

    /**
     * Method that deletes a directory and EVERYTHING INSIDE OF IT.
     * @param path {@link Path} to be erased.
     * @throws IOException if path can't be accessed.
     */
    private static void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> pathStream = Files.newDirectoryStream(path)) {
                for (Path killable : pathStream) {
                    deleteDirectory(killable);
                }
            }
        }
        Files.delete(path);
    }

    /**
     * Creates a directory given a {@link String} URI.
     * If the directory exists, it asks the user if the directory should be erased.
     * The method returns null if the user doesn't want to erase the directory.
     * @param uri {@link String} containing the URI.
     * @return {@link Path} of the new directory if operation was successful, null otherwise.
     */
    public static Path createDirectory(String uri) {
        Path directoryPath = null;
        if (Files.exists(Path.of(uri))) {
            Scanner scanner = new Scanner(System.in);
            boolean correctAnswer = false;
            while (!correctAnswer){
                System.out.println("Would you like to erase the existing output directory? (yes/no)");
                String answer = scanner.next();
                if (answer.equalsIgnoreCase("yes")) {
                    correctAnswer = true;
                    try {
                        if (Files.exists(Path.of(uri))){
                            deleteDirectory(Path.of(uri));
                        }
                    }catch (IOException e) {
                        System.err.println("Couldn't delete directory");
                    }
                    try {
                        directoryPath = Files.createDirectory(Path.of(uri));
                    }catch (IOException e) {
                        System.err.println("Couldn't create output directory");
                    }
                } else if (answer.equalsIgnoreCase("no")) {
                    correctAnswer = true;
                    System.out.println("Nothing was generated");
                }
                scanner.nextLine();
            }
        }else{
            try {
                directoryPath = Files.createDirectory(Path.of(uri));
            }catch (IOException e) {
                System.err.println("Couldn't create output directory");
            }
        }
        return directoryPath;
    }

    /**
     * Gets a {@link Station} from a csv given a {@link String} containing its name.
     * File is read using the "windows-1252" charset.
     * @param city {@link String} containing the station name.
     * @param stationFile {@link String} URI of the csv file where station data is stored.
     * @param charset {@link Charset} defining the charset of the file to be read.
     * @return {@link Optional} of {@link Station}.
     */
    public static Optional<Station> getStation(String city, String stationFile, Charset charset) {
        Stream<String> fileData = getFile(stationFile, charset);
        return fileData.filter(s -> Arrays.asList(s.split(";")).get(2).equalsIgnoreCase(city)).map(s -> s.split(";")).map(v -> new Station(v[0], v[1], v[2])).findFirst();
    }

    /**
     * Method that reads from a data csv file filtering its lines by station code.
     * @param station {@link Station} whose data we want to obtain.
     * @param file {@link String} containing the URI of the csv data file.
     * @param charset {@link Charset} defining the charset of the file to be read.
     * @return {@link Stream} of {@link String} containing the lines filtered by station.
     */
    public static Stream<String> getStationDataStream (Station station, String file, Charset charset)
    {
        Stream<String> data = getFile(file, charset);
        return data.filter(s -> Arrays.asList(s.split(";")).get(4).contains(station.getStationCode()));
    }

    /**
     * Method that parses a {@link Stream} of {@link String} containing data lines into
     * a {@link List} of {@link Measure}. If the stream is empty, the list is returned empty.
     * @param data {@link Stream} of station data.
     * @return {@link List} of {@link Measure}
     */
    public static List<Measure> getMeasures(Stream<String> data) {
        List<Measure> measuresList = data.map(s -> Arrays.asList(s.split(";"))).map(list -> {
            List<HourMeasurement> measures = new ArrayList<>();
            //for that accesses each couple of values after the starting data pos: 8 and parses it as HourMeasurement
            for (int i = 0; i < 24; i++) {
                List<String> measurement= list.subList(8 + i * 2, 8 + i * 2 + 2);
                float value = 0;
                if (!measurement.get(0).equals("")) {
                    //Spanish data has decimals in format n,d , whereas floats require format n.d .
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
