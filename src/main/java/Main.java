import service.Analytics;
import service.MeteoPractice;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main (String[] args) {
        Analytics analysis = MeteoPractice.generateMeteoAnalysis(args[0], args[1]);
        if (analysis!= null) {
            analysis.htmlBuilder();
            try {
                analysis.generateHtml();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
