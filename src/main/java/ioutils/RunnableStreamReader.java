package ioutils;

import pojos.Measure;
import pojos.Station;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Runnable class that reads and parses a data document with a given charset and stores it in a list of Measures
 * @author sps169, FedericoTB
 */
public class RunnableStreamReader implements Runnable {
    private List<Measure> list;
    private Station station;
    private String file;
    private Charset charset;

    /**
     * Creates a RunnableStreamReader with the required params
     * @param station {@link Station} to filter data
     * @param file {@link String} file URI of readable data
     * @param charset {@link Charset} of the file of data
     */
    public RunnableStreamReader (Station station, String file, Charset charset) {
        this.station = station;
        this.file = file;
        this.charset = charset;
    }
    @Override
    public void run() {
       list = DataReading.getMeasures(DataReading.getStationDataStream(station, file, charset));
    }

    public List<Measure> getList() {
        return this.list;
    }
}
