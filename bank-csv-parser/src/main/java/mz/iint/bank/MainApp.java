package mz.iint.bank;

import mz.iint.bank.filter.RecordFilter;
import mz.iint.bank.filter.ValueFilter;
import mz.iint.bank.filter.YesNoFilter;
import mz.iint.bank.filter.ZeroDurationFilter;
import mz.iint.bank.trans.AgeTransformer;
import mz.iint.bank.trans.MonthTransformer;
import mz.iint.bank.trans.PdaysTransformer;
import mz.iint.bank.trans.RecordTransformer;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class MainApp {

    public static void main(String[] args) throws IOException, ParseException {
        run(args);
//        normalize();
        numerize();
    }

    private static void numerize() throws IOException, ParseException {
//        configure();
        final CsvNumerizer csvNumerizer = new CsvNumerizer(Configuration.get().outFile(), Configuration.get().outNumFile());
        csvNumerizer.run();
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
        configure();

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

        if (Configuration.get().previousTransformerActive()) {
            final PdaysTransformer pdaysTransformer = new PdaysTransformer(Configuration.get().previousIndex());
            transformers.add(pdaysTransformer);
        }

        if (Configuration.get().monthTransformerActive()) {
            final MonthTransformer monthTransformer = new MonthTransformer(8);
            transformers.add(monthTransformer);
        }

        final CsvNormalizer csvNormalizer = new CsvNormalizer(inFileName, outFilePath, recordsToKeep, filters, transformers);
        csvNormalizer.parse();
    }

    private static void configure() throws IOException {
        Properties configProperties = new Properties();
        configProperties.load(new FileInputStream("app.properties"));

        Configuration.load(configProperties);
    }

}
