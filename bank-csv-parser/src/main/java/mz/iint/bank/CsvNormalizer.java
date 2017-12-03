package mz.iint.bank;

import mz.iint.bank.filter.RecordFilter;
import mz.iint.bank.trans.RecordTransformer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class CsvNormalizer {
    private final List<RecordFilter> filters;
    private final List<RecordTransformer> transformers;
    private final CsvStats csvStats = new CsvStats();

    private String outFilePath;
    private String inFileName;
    private final int recordsToKeep;
    private int writtenRecords = 0;

    public CsvNormalizer(String inFileName, String outFilePath, int recordsToKeep, Collection filters, Collection transformers) {
        this.outFilePath = outFilePath;
        this.inFileName = inFileName;
        this.recordsToKeep = recordsToKeep;
        this.filters = new ArrayList<>(filters);
        this.transformers = new ArrayList<>(transformers);
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

        System.out.println("Registros escritos: " + writtenRecords);
        parser.close();
        writer.close();
    }


    /**
     * Aplica los filtros sobre el registro y determina si el mismo debe ser escrito en el CSV final
     *
     * @param record Registro a analizar.
     * @return True si el registro debe escribirse, false en caso contrario.
     */
    private boolean recordOk(CSVRecord record) {
        boolean ok = true;
        for (RecordFilter filter : filters) {
            ok &= filter.filter(record, csvStats);
            if (!ok) break;
        }
        return ok;
    }

    private boolean isYes(CSVRecord record) {
        final String value = Optional.ofNullable(record.get(Configuration.get().classIndex())).orElse("");
        return "yes".equalsIgnoreCase(value);
    }

    private boolean isNo(CSVRecord record) {
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

    private CSVRecord transformRecord(CSVRecord record) {
        for (RecordTransformer transformer : transformers) {
            record = transformer.transform(record);
        }
        return record;
    }

    private void writeRecord(CSVRecord record, PrintWriter writer) throws ParseException {
        StringBuilder recordLine = new StringBuilder();

        record = transformRecord(record);

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
