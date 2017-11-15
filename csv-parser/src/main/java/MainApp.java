import java.io.IOException;
import java.text.ParseException;

public class MainApp {

    public static void main(String[] args) throws IOException, ParseException {
        int recordsToKeep = args.length == 0 ? 5000 : Integer.parseInt(args[0]);

        String outFilePath = "out.csv";
        String inFileName = "Traffic_Violations.csv";
        CsvNormalizer csvNormalizer = new CsvNormalizer(recordsToKeep, outFilePath, inFileName);

        csvNormalizer.run();

        System.out.println();
        System.out.println("Dataset original headers:");
        new HeaderPrinter(inFileName).printHeaders();

        System.out.println();
        System.out.println("Out file headers:");
        new HeaderPrinter(outFilePath).printHeaders();

        System.out.println();
        int recordCount = new RecordCounter(outFilePath).count(record ->
                record.get(6).trim().equalsIgnoreCase("yes")
        );
        System.out.printf("Record count by criteria: %d%n", recordCount);


        System.out.println();
        final RecordOccurrenceFilter occurrenceFilter = new RecordOccurrenceFilter(outFilePath, "description", 0.07, "out_2.csv");
        occurrenceFilter.filter();
    }

}
