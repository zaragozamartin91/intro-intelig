package mz.iint.bank;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CsvNormalizer {
    static final int[] HEADER_INDEXES = Config.HEADER_INDEXES;
    static final int LAST_HEADER_INDEX = Config.LAST_HEADER_INDEX;
    static final SimpleDateFormat DATE_FORMAT = Config.DATE_FORMAT;
    private final List<RecordFilter> filters;

    private final CsvStats csvStats = new CsvStats();

    String outFilePath = "out.csv";
    String inFileName = "bank-full.csv";
    final int recordsToKeep;
    int writtenRecords = 0;

    public CsvNormalizer(String outFilePath, String inFileName, int recordsToKeep, RecordFilter... filters) {
        this.outFilePath = outFilePath;
        this.inFileName = inFileName;
        this.recordsToKeep = recordsToKeep;
        this.filters = Arrays.asList(filters);
    }

    public void parse() throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFileName)));
        final CSVParser parser = CSVParser.parse(reader, CSVFormat.EXCEL);

        PrintWriter writer = new PrintWriter(new FileOutputStream(outFilePath), true);


        for (CSVRecord record : parser) {
            if (record.getRecordNumber() == 1) {
                writeHeader(record, writer);
            } else {
                if (recordOk(record)) {
                    writeRecord(record, writer);
                    if (isYes(record)) csvStats.augmentYes();
                    else if (isNo(record)) csvStats.augmentNo();
                    if (writtenRecords++ >= recordsToKeep) break;
                }
            }
            System.out.println(record);
        }

        parser.close();
    }


    public boolean recordOk(CSVRecord record) {
        for (RecordFilter filter : filters) if (filter.filter(record, csvStats)) return true;
        return false;
    }

    public boolean isYes(CSVRecord record) {
        final String value = Optional.ofNullable(record.get(Config.CLASS_INDEX)).orElse("");
        return "yes".equalsIgnoreCase(value);
    }

    public boolean isNo(CSVRecord record) {
        final String value = Optional.ofNullable(record.get(Config.CLASS_INDEX)).orElse("");
        return "no".equalsIgnoreCase(value);
    }

    private void writeHeader(CSVRecord record, PrintWriter writer) {
        StringBuilder headerLine = new StringBuilder(removeSpaces(record.get(HEADER_INDEXES[0])));
        for (int i = 1; i < HEADER_INDEXES.length; i++) {
            headerLine.append("," + removeSpaces(record.get(HEADER_INDEXES[i])));
        }
        writer.println(headerLine.toString());
    }

    private String removeSpaces(String s) {
        return s.replaceAll(" +", "");
    }

    private void writeRecord(CSVRecord record, PrintWriter writer) throws ParseException {
        StringBuilder recordLine = new StringBuilder();

        for (int i = 0; i < HEADER_INDEXES.length; i++) {
            int headerIndex = HEADER_INDEXES[i];

            String value = record.get(headerIndex).trim();
            value = value.isEmpty() ? "__EMPTY__" : value;

            if (i == LAST_HEADER_INDEX) recordLine.append(value);
            else recordLine.append(value + ",");
        }

        writer.println(recordLine.toString());
    }

}
