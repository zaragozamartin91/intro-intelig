import java.io.*;
import java.util.regex.Pattern;

public class HeaderPrinter {
    String fileName;

    public HeaderPrinter(String fileName) {
        this.fileName = fileName;
    }

    public void printHeaders() throws IOException {
        InputStream fileStream = new FileInputStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));

        try {
            String headerLine = reader.readLine();
            int idx = 0;
            for (String header : headerLine.split(Pattern.quote(","))) {
                System.out.printf("%d. %s%n", idx++, header);
            }
        } finally {
            if (reader != null) reader.close();
        }
    }
}
