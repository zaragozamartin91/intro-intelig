import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RecordOccurrenceFilter {
    private String fileName;
    private String colName;
    private double occPercentage;
    private String outFileName;

    public RecordOccurrenceFilter(String fileName, String colName, double occPercentage, String outFileName) {
        this.fileName = fileName;
        this.colName = colName;
        this.occPercentage = occPercentage;
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

            final int colIdx = getColIdx(header);

            final Map<String, Counter> occurrences = new HashMap<>();
            final Counter recordCounter = new Counter();

            recordIterator.forEachRemaining(record -> {
                String item = record.get(colIdx);
                final Counter itemCounter = occurrences.getOrDefault(item, new Counter()).augment();
                occurrences.put(item, itemCounter);
                recordCounter.augment();
            });

            return new DatasetInfo(occurrences, colIdx, recordCounter);
        } finally {

            csvReader.close();
        }
    }

    private int getColIdx(CSVRecord header) {
        int idx = 0;
        for (; ; idx++) if (colName.equalsIgnoreCase(header.get(idx))) break;
        return idx;
    }

    public void filter() throws IOException {
        final DatasetInfo datasetInfo = parseDataset();
        final Map<String, Counter> occurrences = datasetInfo.occurrences;
        final int colIdx = datasetInfo.colIdx;
        final int recordCount = datasetInfo.recordCounter.count;

        PrintWriter writer = new PrintWriter(new FileOutputStream(outFileName));
        InputStream filestream = new FileInputStream(fileName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(filestream));
        CSVParser parser = CSVParser.parse(csvReader, CSVFormat.EXCEL);

        try {
            final Iterator<CSVRecord> recordIterator = parser.iterator();
            final CSVRecord header = recordIterator.next();

            recordIterator.forEachRemaining(record -> {
                final String value = record.get(colIdx);
                double count = occurrences.get(value).count;
                double average = count / recordCount;

            });
        } finally {
            writer.close();
            csvReader.close();
        }
    }
}
