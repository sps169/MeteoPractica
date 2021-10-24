import pojos.*;

import ioutils.DataReader;
import service.Analytics;
import service.MeteoPractice;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main (String[] args) {
        Analytics analysis = MeteoPractice.generateMeteoAnalysis(args[0], args[1]);
//        analysis.
    }
}
