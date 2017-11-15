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
            int headerIndex = Config.HEADER_INDEXES[i];

            String value = removeCommas(record.get(headerIndex));
            if (headerIndex == Config.DESC_HEADER_INDEX) value = standarizeDescription(value);
            else if (headerIndex == Config.TIME_HEADER_INDEX) value = standarizeTime(value);

            if (i == Config.LAST_HEADER_INDEX) recordLine.append(value);
            else recordLine.append(value + ",");
        }

        writer.println(recordLine.toString());
    }

    public static String standarizeTime(String value) throws ParseException {
        if (value.startsWith("0")) value = "0" + value;
        Date date = Config.DATE_FORMAT.parse(value);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        return hour + ":00:00";
    }

    public static String removeCommas(String value) {
        return value.replaceAll(Pattern.quote(","), "");
    }
}
