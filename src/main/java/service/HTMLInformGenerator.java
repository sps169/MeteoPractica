package service;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HTMLInformGenerator {
    private final static String uri = "";
    private String headPart;
    private List<String> bodyPart;

    public void generate(String uri) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(uri + File.separator + "ciudad-dd-mm-aaaa.html"));
        bw.write(this.headPart + this.bodyPart);
        Desktop.getDesktop().browse(URI.create(uri + File.separator + "ciudad-dd-mm-aaaa.html"));
        bw.close();
    }

    public HTMLInformGenerator(String headPart, List<String> bodyPart) {
        this.headPart = "<html>\n<head>\n<title>\n"+headPart+"</title>\n";
        this.bodyPart.add("<body>\n");
        for (String string: bodyPart) {
            this.bodyPart.add(string);
        }
        this.bodyPart.add("</body>\n</html>");
    }

    public String getHeadPart() {
        return headPart;
    }

    public void setHeadPart(String headPart) {
        this.headPart = "<html>\n<head>\n<title>\n"+headPart+"</title>\n";
    }

    public List<String> getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(List<String> bodyPart) {
        this.bodyPart=bodyPart;
    }
}
