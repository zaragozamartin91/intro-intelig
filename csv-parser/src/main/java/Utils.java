import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public class Utils {
    public static void writeRecord(CSVRecord record, PrintWriter writer) throws ParseException {
        StringBuilder recordLine = new StringBuilder();

        for (int i = 0; i < Config.HEADER_INDEXES.length; i++) {
            String value = removeCommas(record.get(i));
            if (i == Config.LAST_HEADER_INDEX) recordLine.append(value);
            else recordLine.append(value + ", ");
        }

        writer.println(recordLine.toString());
    }


    public static String removeCommas(String value) {
        return value.replaceAll(Pattern.quote(","), "");
    }

    public static int getColIdx(CSVRecord header, String colName) {
        int idx = 0;
        for (; ; idx++) {
            final String headerName = header.get(idx).trim();
            if (colName.equalsIgnoreCase(headerName)) break;
        }
        return idx;
    }

    public static DatasetInfo parseDataset(String fileName, String colName) throws IOException {
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
}
