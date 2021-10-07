package ioutils;



import pojos.Station;


import java.io.IOException;
import java.nio.charset.Charset;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;
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

    public static void main(String[] args) {
        Stream<String> fileData = getFile(DATA_DIR + SEPARATOR + "calidad_aire_estaciones.csv", Charset.forName("windows-1252"));
        Station station = fileData.filter(s -> Arrays.asList(s.split(";")).get(2).equals("Leganés")).map(s -> s.split(";")).map(v -> new Station(v[0], v[1], v[2])).findFirst().get();
        System.out.println(station);
//        StringTokenizer tokenizer = new StringTokenizer(fileData, "\n");
//        tokenizer.nextToken();
//        Map<String, Station> estaciones = new HashMap<String, Station>();
//        while(tokenizer.hasMoreTokens()){
//            StringTokenizer subToken = new StringTokenizer(tokenizer.nextToken(), ";");
//            String key = subToken.nextToken();
//            String zone = subToken.nextToken();
//            String value = subToken.nextToken();
//            estaciones.put(key, new Station(key, zone, value));
//        }
//        estaciones.forEach((c, k) -> System.out.println(c + " " + k));
//        String[] estaciones = fileData.split(";");
//        Arrays.stream(estaciones).forEach(System.out::println);
    }

}
