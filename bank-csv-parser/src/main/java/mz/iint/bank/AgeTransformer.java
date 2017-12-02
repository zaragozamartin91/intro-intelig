package mz.iint.bank;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;

public class AgeTransformer implements RecordTransformer {
    private static Field valuesField;

    static {
        try {
            valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int ageIndex;

    public AgeTransformer(int ageIndex) {
        this.ageIndex = ageIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        final Integer age = Integer.valueOf(record.get(ageIndex));
        String sage;

        if (age < 30) sage = "<30";
        else if (age < 40) sage = "30-40";
        else if (age < 50) sage = "40-50";
        else if (age < 60) sage = "50-60";
        else sage = ">60";

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[ageIndex] = sage;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
