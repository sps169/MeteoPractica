package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLInformGenerator {
    private final static String uri = "";
    private String headPart;
    private String bodyPart;

    public void generate(String uri) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(uri)));
        bw.write(this.headPart +this.bodyPart);
        bw.close();
    }

    public HTMLInformGenerator(String headPart, String bodyPart) {
        this.headPart = "<html>\n<head>\n<title>\n"+headPart+"</title>\n";
        this.bodyPart = "<body>"+bodyPart+"</body>\n</html>";
    }

    public String getHeadPart() {
        return headPart;
    }

    public void setHeadPart(String headPart) {
        this.headPart = "<html>\n<head>\n<title>\n"+headPart+"</title>\n";
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = "<body>"+bodyPart+"</body>\n</html>";
    }
}
