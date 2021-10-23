package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HTMLInformGenerator {
    private final static String uri = "";
    private String headPart;
    private List<String> bodyPart;

    public void generate(String uri) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(uri)));
        bw.write(this.headPart +this.bodyPart);
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
