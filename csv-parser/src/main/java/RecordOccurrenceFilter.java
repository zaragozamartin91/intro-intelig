import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.*;

/**
 * Filtro de registros con bajas ocurrencias dado una columna
 */
public class RecordOccurrenceFilter {
    private String fileName;
    private String colName;
    private int minRecordCount;
    private String outFileName;

    public RecordOccurrenceFilter(String fileName, String colName, int minRecordCount, String outFileName) {
        this.fileName = fileName;
        this.colName = colName;
        this.minRecordCount = minRecordCount;
        this.outFileName = outFileName;
    }

    class Counter {
        int count = 0;

        Counter augment() {
            ++count;
            return this;
        }
    }

    class DatasetInfo {
        Map<String, Counter> occurrences;
        int colIdx;
        Counter recordCounter;

        public DatasetInfo(Map<String, Counter> occurrences, int colIdx, Counter recordCounter) {
            this.occurrences = occurrences;
            this.colIdx = colIdx;
            this.recordCounter = recordCounter;
        }
    }

    DatasetInfo parseDataset() throws IOException {
        InputStream filestream = new FileInputStream(fileName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(filestream));

        try {
            CSVParser parser = CSVParser.parse(csvReader, CSVFormat.EXCEL);
            Iterator<CSVRecord> recordIterator = parser.iterator();
            CSVRecord header = recordIterator.next();

            final int colIdx = Utils.getColIdx(header, colName);

            final Map<String, Counter> occurrences = new HashMap<>();
            final Counter recordCounter = new Counter();

            recordIterator.forEachRemaining(record -> {
                String item = record.get(colIdx);
                final Counter itemCounter = occurrences.getOrDefault(item, new Counter()).augment();
                occurrences.put(item, itemCounter);
                recordCounter.augment();
            });

            System.out.println();
            System.out.println("Occurrences:");
            Set<Map.Entry<String, Counter>> entries = occurrences.entrySet();
            entries.forEach(entry -> System.out.printf("%s -> %d%n", entry.getKey(), entry.getValue().count));

            return new DatasetInfo(occurrences, colIdx, recordCounter);
        } finally {
            csvReader.close();
        }
    }


    public void filter() throws IOException, ParseException {
        try {
            Files.delete(new File(outFileName).toPath());
            System.out.println(outFileName + " eliminado");
        } catch (IOException e) {}

        final DatasetInfo datasetInfo = parseDataset();
        final Map<String, Counter> occurrences = datasetInfo.occurrences;
        final int colIdx = datasetInfo.colIdx;
        final int recordCount = datasetInfo.recordCounter.count;

        PrintWriter writer = new PrintWriter(new FileOutputStream(outFileName));
        InputStream filestream = new FileInputStream(fileName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(filestream));
        CSVParser parser = CSVParser.parse(csvReader, CSVFormat.EXCEL);

        Set<String> remainingCategories = new HashSet<>();

        try {
            final Iterator<CSVRecord> recordIterator = parser.iterator();
            final CSVRecord header = recordIterator.next();

            Utils.writeRecord(header, writer);

            recordIterator.forEachRemaining(record -> {
                final String value = record.get(colIdx);
                int count = occurrences.get(value).count;

                if (count >= minRecordCount) try {
                    Utils.writeRecord(record, writer);
                    remainingCategories.add(value);
                } catch (ParseException e) {
                    throw new RuntimeException("Error al escribir los registros");
                }
            });

            System.out.println();
            int delCatCount = occurrences.size() - remainingCategories.size();
            System.out.println("# Categorias eliminadas: " + delCatCount);
            System.out.println("Categorias pendientes:");
            remainingCategories.forEach(cat -> System.out.println(cat));

        } finally {
            writer.close();
            csvReader.close();
        }
    }
}
