package service;

import ioutils.DataReading;
import ioutils.RunnableCSVReader;
import ioutils.RunnableStreamReader;
import pojos.Magnitude;
import pojos.Measure;
import pojos.MonthData;
import pojos.Station;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * Facade implementation of the Analytics object creation
 * @author sps169, FedericoTB
 */
public class MeteoPractice {
    private static final String STATIONS_FILE = "calidad_aire_estaciones.csv";
    private static final String METEOROLOGY_FILE = "calidad_aire_datos_meteo_mes.csv";
    private static final String CONTAMINATION_FILE = "calidad_aire_datos_mes.csv";
    private static final String MAGNITUDES_FILE = "magnitudes_aire.csv";
    private static final String MAGNITUDES_METEO_FILE = "magnitudes_aire_meteo.csv";

    /**
     * Generates an Analytics object given a {@link String} city and directory URI.
     * @param city {@link String} name of city
     * @param directoryURI {@link String} URI of the directory where to output images and html
     * @return {@link Analytics} object containing list of temperature and contamination, the station and the HTML
     */
    public static Analytics generateMeteoAnalysis (String city, String directoryURI) {
        Path directory = DataReading.createDirectory(directoryURI);
        long initialTime = System.currentTimeMillis();
        if (directory != null) {
            Station station = DataReading.getStation(city, STATIONS_FILE, Charset.forName("windows-1252")).orElse(null);
            if (station != null) {
                //thread approach to reading csv and filtering streams
                ThreadPoolExecutor threads = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
                RunnableStreamReader contaminationRunnable = new RunnableStreamReader(station,CONTAMINATION_FILE, Charset.forName("windows-1252"));
                RunnableStreamReader meteorologyRunnable = new RunnableStreamReader(station,METEOROLOGY_FILE, Charset.forName("windows-1252"));
                RunnableCSVReader magnitudeRunnable = new RunnableCSVReader(MAGNITUDES_FILE, Charset.forName("windows-1252"));
                RunnableCSVReader magnitudeMeteoRunnable = new RunnableCSVReader(MAGNITUDES_METEO_FILE, Charset.forName("windows-1252"));
                threads.execute(contaminationRunnable);
                threads.execute(meteorologyRunnable);
                threads.execute(magnitudeRunnable);
                threads.execute(magnitudeMeteoRunnable);
                threads.shutdown();
                while (threads.getActiveCount()!= 0) {
                    try {
                        Thread.sleep(1);
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return buildAnalytics(directory, initialTime, station,
                        contaminationRunnable.getList(), meteorologyRunnable.getList(),
                        magnitudeRunnable.getList(),  magnitudeMeteoRunnable.getList());
            } else {
                System.err.println("That city doesn't exists");
                return null;
            }
        }
        return null;
    }

    /**
     * Receives the lists with Measures and Magnitudes and creates the final lists used by {@link Analytics}
     * @param directory {@link Path} used to store the output data
     * @param initialTime long used to measure time of execution
     * @param station {@link Station} used to filter data
     * @param contaminationMeasuresList {@link List} of {@link Measure} of contamination data
     * @param meteorologyMeasuresList {@link List} of {@link Measure} of temperature data
     * @param magnitudes {@link List} of {@link Magnitude} of contamination magnitudes
     * @param magnitudesMeteo {@link List} of {@link Magnitude} of temperature magnitudes
     * @return {@link Analytics} Analytics object built
     */
    private static Analytics buildAnalytics(Path directory, long initialTime, Station station,
                                            List<Measure> contaminationMeasuresList, List<Measure> meteorologyMeasuresList,
                                            List<Magnitude> magnitudes, List<Magnitude> magnitudesMeteo) {
        List<MonthData> contaminationReport = new ArrayList<>();
        List<MonthData> meteorologyReport = new ArrayList<>();
        //fill the Monthly Data lists with filtered and parsed data of the params lists.
        for (Magnitude type : magnitudes) {
            contaminationReport.add(
                    new MonthData(
                            contaminationMeasuresList.stream()
                                .filter(s -> s.getMagnitude().equals(type.getCodMagnitude()))
                                .collect(Collectors.toList()),
                            type));
        }
        for (Magnitude type : magnitudesMeteo) {
            meteorologyReport.add(
                    new MonthData(
                            meteorologyMeasuresList.stream()
                                    .filter(s -> s.getMagnitude().equals(type.getCodMagnitude()))
                                    .collect(Collectors.toList()),
                            type));
        }

        Analytics generalReport = null;
        try {
            generalReport = new Analytics(contaminationReport, meteorologyReport, station, directory, initialTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generalReport;
    }
}
