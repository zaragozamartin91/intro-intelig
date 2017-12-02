package mz.iint.bank;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvNormalizer {
    private final List<RecordFilter> filters;
    private final CsvStats csvStats = new CsvStats();

    private String outFilePath;
    private String inFileName;
    private final int recordsToKeep;
    private int writtenRecords = 0;

    public CsvNormalizer(String outFilePath, String inFileName, int recordsToKeep, Collection filters) {
        this.outFilePath = outFilePath;
        this.inFileName = inFileName;
        this.recordsToKeep = recordsToKeep;
        this.filters = new ArrayList<>(filters);
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
        final String value = Optional.ofNullable(record.get(Configuration.get().classIndex())).orElse("");
        return "yes".equalsIgnoreCase(value);
    }

    public boolean isNo(CSVRecord record) {
        final String value = Optional.ofNullable(record.get(Configuration.get().classIndex())).orElse("");
        return "no".equalsIgnoreCase(value);
    }

    private void writeHeader(CSVRecord record, PrintWriter writer) {
        final Integer[] headerIndexes = Configuration.get().headerIndexes();
        StringBuilder headerLine = new StringBuilder(removeSpaces(record.get(headerIndexes[0])));
        for (int i = 1; i < headerIndexes.length; i++) {
            headerLine.append("," + removeSpaces(record.get(headerIndexes[i])));
        }
        writer.println(headerLine.toString());
    }

    private String removeSpaces(String s) {
        return s.replaceAll(" +", "");
    }

    private void writeRecord(CSVRecord record, PrintWriter writer) throws ParseException {
        StringBuilder recordLine = new StringBuilder();

        final Integer[] headerIndexes = Configuration.get().headerIndexes();
        for (int i = 0; i < headerIndexes.length; i++) {
            int headerIndex = headerIndexes[i];

            String value = record.get(headerIndex).trim();
            value = value.isEmpty() ? "__EMPTY__" : value;

            if (i == Configuration.get().lastIndex()) recordLine.append(value);
            else recordLine.append(value + ",");
        }

        writer.println(recordLine.toString());
    }

}
