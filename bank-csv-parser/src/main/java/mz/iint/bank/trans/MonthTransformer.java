package mz.iint.bank.trans;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;

public class MonthTransformer implements RecordTransformer {
    private static Field valuesField;

    static {
        try {
            valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int monthIndex;

    public MonthTransformer(int monthIndex) {
        this.monthIndex = monthIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        String month = record.get(monthIndex);
        String fmonth = month;

        switch (month) {
            case "mar":
            case "apr":
            case "may":
                fmonth = "spring";
                break;
            case "jun":
            case "jul":
            case "aug":
                fmonth = "summer";
                break;
            case "sep":
            case "oct":
            case "nov":
                fmonth = "autum";
                break;
            case "dec":
            case "jan":
            case "feb":
                fmonth = "winter";
                break;
        }

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[monthIndex] = fmonth;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
