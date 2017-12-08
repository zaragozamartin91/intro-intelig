package mz.iint.bank;

import mz.iint.bank.filter.*;
import mz.iint.bank.trans.*;

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
//        numerize();
    }

    private static void numerize() throws IOException, ParseException {
        configure();
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

        if (Configuration.get().unknownDurationFilterActive()) {
            filters.add(new ValueFilter(1, "unknown"));
            filters.add(new ValueFilter(2, "unknown"));     //marital
            filters.add(new ValueFilter(3, "illiterate"));  // education
            filters.add(new ValueFilter(3, "unknown"));     // education
            filters.add(new ValueFilter(5, "unknown"));     // housing
//            filters.add(new ValueFilter(6, "unknown"));     // loan
        }

        if (Configuration.get().previousFilterActive()) {
            filters.add(new PreviousFilter(Configuration.get().recordsToKeep(), Configuration.get().previousYesnoRatio(), Configuration.get().previousIndex()));
        }

        if (Configuration.get().campaignFilterActive()) {
            filters.add(new CampaignFilter(Configuration.get().recordsToKeep(), Configuration.get().campaignOneRatio(), Configuration.get().campaignIndex()));
        }

        List<RecordTransformer> transformers = new ArrayList<>();

        if (Configuration.get().ageTransformerActive()) {
            final AgeTransformer ageTransformer = new AgeTransformer(Configuration.get().ageIndex());
            transformers.add(ageTransformer);
        }

        if (Configuration.get().pdaysTransformerActive()) {
            final PdaysTransformer pdaysTransformer = new PdaysTransformer(Configuration.get().pdaysIndex());
            transformers.add(pdaysTransformer);
        }

        if (Configuration.get().monthTransformerActive()) {
            final MonthTransformer monthTransformer = new MonthTransformer(8);
            transformers.add(monthTransformer);
        }

        if (Configuration.get().durationTransformerActive()) {
            final DurationTransformer durationTransformer = new DurationTransformer(10);
            transformers.add(durationTransformer);
        }

        if (Configuration.get().educationTransformerActive()) {
            EducationTransformer educationTransformer = new EducationTransformer(3);
            transformers.add(educationTransformer);
        }

        if (Configuration.get().previousTransformerActive()) {
            PreviousTransformer previousTransformer = new PreviousTransformer(13);
            transformers.add(previousTransformer);
        }

        if (Configuration.get().campaignTransformerActive()) {
            CampaignTransformer campaignTransformer = new CampaignTransformer(11);
            transformers.add(campaignTransformer);
        }

        if (Configuration.get().maritalTransformerActive()) {
            MaritalTransformer maritalTransformer = new MaritalTransformer(2);
            transformers.add(maritalTransformer);
        }

        final CsvNormalizer csvNormalizer = new CsvNormalizer(inFileName, outFilePath, recordsToKeep, filters, transformers);
        csvNormalizer.parse();
    }

    private static void configure() throws IOException {
        Properties configProperties = new Properties();
//        configProperties.load(new FileInputStream("app.properties"));
        configProperties.load(MainApp.class.getClassLoader().getResourceAsStream("app.properties"));

        Configuration.load(configProperties);
    }

}
