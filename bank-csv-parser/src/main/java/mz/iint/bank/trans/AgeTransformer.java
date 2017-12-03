package mz.iint.bank.trans;

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

        if (age < 30) sage = "lt30";
        else if (age < 40) sage = "30to40";
        else if (age < 50) sage = "40to50";
        else if (age < 60) sage = "50to60";
        else sage = "gt60";

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[ageIndex] = sage;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
