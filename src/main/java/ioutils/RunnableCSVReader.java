package ioutils;

import pojos.Magnitude;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Runnable class that reads and parses a magnitude document with a given charset and stores it in a list of Magnitudes
 * @author sps169, FedericoTB
 */
public class RunnableCSVReader implements Runnable {
    private List<Magnitude> list;
    private String file;
    private Charset charset;

    /**
     * Creates a RunnableCSVReader
     * @param file {@link String} file URI of readable data
     * @param charset {@link Charset} of the file of data
     */
    public RunnableCSVReader (String file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }
    @Override
    public void run() {
        list = DataReading.getFile(file, charset).map(s -> Arrays.asList(s.split(";"))).map(t -> new Magnitude(t.get(0), t.get(1), t.get(4))).collect(Collectors.toList());
    }

    public List<Magnitude> getList() {
        return this.list;
    }
}
