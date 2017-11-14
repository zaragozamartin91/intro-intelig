import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.function.Function;

public class RecordCounter {
    String fileName;

    public RecordCounter(String fileName) {
        this.fileName = fileName;
    }

    public int count(Function<CSVRecord, Boolean> criteria) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        try {
            CSVParser parser = CSVParser.parse(reader, CSVFormat.EXCEL);
            int count = 0;

            for (CSVRecord record : parser) {
                if (criteria.apply(record)) count++;
            }

            return count;
        } finally {
            if (reader != null) reader.close();
        }
    }
}
