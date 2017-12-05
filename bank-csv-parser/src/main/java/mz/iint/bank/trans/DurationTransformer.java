package mz.iint.bank.trans;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;

public class DurationTransformer implements RecordTransformer {
    private static Field valuesField;

    static {
        try {
            valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int durationIndex;

    public DurationTransformer(int durationIndex) {
        this.durationIndex = durationIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        final Integer duration = Integer.valueOf(record.get(durationIndex));
        String sduration;

        if (duration < 103) sduration = "lt103";
        else if (duration < 180) sduration = "103to180";
        else if (duration < 320) sduration = "180to320";
        else sduration = "gt320";

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[durationIndex] = sduration;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
