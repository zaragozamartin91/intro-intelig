package mz.iint.bank;

import java.io.IOException;
import java.text.ParseException;

public class MainApp {

    public static void main(String[] args) throws IOException, ParseException {
        int recordsToKeep = args.length == 0 ? 5000 : Integer.parseInt(args[0]);

        String outFilePath = "out.csv";
        String inFileName = "bank-full.csv";

        final FileDeleter fileDeleter = new FileDeleter(outFilePath);
        fileDeleter.deleteFile();

        YesNoFilter yesNoFilter = new YesNoFilter(5000, 0.4, Config.CLASS_INDEX);

        final CsvNormalizer csvNormalizer = new CsvNormalizer(outFilePath, inFileName, recordsToKeep, yesNoFilter);
        csvNormalizer.parse();
    }

}
