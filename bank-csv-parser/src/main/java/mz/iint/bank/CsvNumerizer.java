package mz.iint.bank;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvNumerizer {
    private final Category[] categories;
    private final Integer[] headerIndexes;
    private List<String> headerNames = new ArrayList<>();
    private String outFilePath;
    private String inFileName;


    public CsvNumerizer(String inFileName, String outFilePath) {
        this.outFilePath = outFilePath;
        this.inFileName = inFileName;

        this.headerIndexes = Configuration.get().headerIndexes();
        this.categories = Category.buildCategories(headerIndexes.length, Configuration.get().mixedCategories());
    }

    public void run() throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFileName)));
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.EXCEL);

        PrintWriter writer = new PrintWriter(new FileOutputStream(outFilePath), true);

        for (CSVRecord record : parser) {
            System.out.println("Record: " + record);

            if (record.getRecordNumber() == 1) {
                writeHeader(record, writer);
                continue;
            }

            writeRecord(record, writer);
        }

        parser.close();
        writer.close();

        for (int i = 0; i < categories.length; i++) {
            System.out.println(headerNames.get(i) + ": " + categories[i].getCategories());
        }
    }

    private void writeHeader(CSVRecord record, PrintWriter writer) {
        StringBuilder headerLine = new StringBuilder(record.get(0));
        headerNames.add(record.get(0));
        for (int i = 1; i < headerIndexes.length; i++) {
            headerNames.add(record.get(i));
            headerLine.append("," + record.get(i));
        }
        writer.println(headerLine.toString());
    }

    private void writeRecord(CSVRecord record, PrintWriter writer) throws ParseException {
        StringBuilder recordLine = new StringBuilder();

        for (int i = 0; i < headerIndexes.length; i++) {
            String value = record.get(i).trim();
            final Category category = categories[i];
            final Integer cat = category.get(value);

            if (i == Configuration.get().lastIndex()) recordLine.append(cat);
            else recordLine.append(cat + ",");
        }

        writer.println(recordLine.toString());
    }
}
