import java.io.IOException;
import java.text.ParseException;

public class MainApp {

    public static void main(String[] args) throws IOException, ParseException {
        int recordsToKeep = args.length == 0 ? 4000 : Integer.parseInt(args[0]);

        String outFilePath = "out.csv";
        String inFileName = "Traffic_Violations.csv";
        CsvNormalizer csvNormalizer = new CsvNormalizer(recordsToKeep, outFilePath, inFileName);

        csvNormalizer.run();
        new HeaderPrinter(inFileName).printHeaders();
    }

}
