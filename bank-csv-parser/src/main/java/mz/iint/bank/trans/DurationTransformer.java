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

        if (duration < 132) sduration = "lt131";
        else if (duration < 262) sduration = "132to261";
        else if (duration < 549) sduration = "262to548";
        else sduration = "gt549";

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[durationIndex] = sduration;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
