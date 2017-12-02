package mz.iint.bank;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class MainApp {

    public static void main(String[] args) throws IOException, ParseException {
        run(args);
//        normalize();
    }

    /**
     * Normaliza un csv con ; y lo pasa a ,
     *
     * @throws IOException
     */
    private static void normalize() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("bank-additional-full.csv")));
        String line;

        PrintWriter writer = new PrintWriter(new FileOutputStream("out.csv"));
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            line = line.replaceAll(Pattern.quote(","), "");
            line = line.replaceAll(Pattern.quote(";"), ",");
            writer.println(line);
        }

        writer.close();
        reader.close();
    }

    private static void run(String[] args) throws IOException, ParseException {
        final InputStream configStream = MainApp.class.getClassLoader().getResourceAsStream("app.properties");
        Properties configProperties = new Properties();
        configProperties.load(configStream);

        Configuration.load(configProperties);

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

        if (Configuration.get().zeroDurationFilterActive()) {
            final ZeroDurationFilter zeroDurationFilter = new ZeroDurationFilter(Configuration.get().durationIndex());
            filters.add(zeroDurationFilter);
        }

        filters.add(new ValueFilter(1, "unknown"));
        filters.add(new ValueFilter(2, "unknown"));
        filters.add(new ValueFilter(3, "unknown"));
        filters.add(new ValueFilter(5, "unknown"));
        filters.add(new ValueFilter(6, "unknown"));
        filters.add(new ValueFilter(3, "illiterate"));

        List<RecordTransformer> transformers = new ArrayList<>();

        if (Configuration.get().ageTransformerActive()) {
            final AgeTransformer ageTransformer = new AgeTransformer(Configuration.get().ageIndex());
            transformers.add(ageTransformer);
        }



        final CsvNormalizer csvNormalizer = new CsvNormalizer(outFilePath, inFileName, recordsToKeep, filters, transformers);
        csvNormalizer.parse();
    }

}
