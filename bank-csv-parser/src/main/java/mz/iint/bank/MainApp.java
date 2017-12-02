package mz.iint.bank;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainApp {

    public static void main(String[] args) throws IOException, ParseException {
        final InputStream configStream = MainApp.class.getClassLoader().getResourceAsStream("app.properties");
        Properties configProperties = new Properties();
        configProperties.load(configStream);

        Configuration.load(configProperties);

        run(args);
    }

    private static void run(String[] args) throws IOException, ParseException {
        int recordsToKeep = Configuration.get().recordsToKeep();

        String outFilePath = Configuration.get().outFile();
        String inFileName = Configuration.get().inFile();

        final FileDeleter fileDeleter = new FileDeleter(outFilePath);
        fileDeleter.deleteFile();

        List<RecordFilter> filters = new ArrayList<>();

        if (Configuration.get().yesnoFilterActive()) {
            YesNoFilter yesNoFilter = new YesNoFilter(recordsToKeep, Configuration.get().yesnoRatio(), Configuration.get().classIndex());
            filters.add(yesNoFilter);
        }

        final CsvNormalizer csvNormalizer = new CsvNormalizer(outFilePath, inFileName, recordsToKeep, filters);
        csvNormalizer.parse();
    }

}
