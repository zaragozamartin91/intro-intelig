import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class HeaderPrinter {
    String fileName;

    public HeaderPrinter(String fileName) {
        this.fileName = fileName;
    }

    public void printHeaders() throws IOException {
        InputStream fileStream = HeaderPrinter.class.getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));

        String headerLine = reader.readLine();
        for (String header : headerLine.split(Pattern.quote(","))) {
            System.out.println(header);
        }


        reader.close();
    }
}
