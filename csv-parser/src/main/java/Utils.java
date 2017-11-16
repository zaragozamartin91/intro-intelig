import org.apache.commons.csv.CSVRecord;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
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
        for (; ; idx++) if (colName.equalsIgnoreCase(header.get(idx))) break;
        return idx;
    }
}
