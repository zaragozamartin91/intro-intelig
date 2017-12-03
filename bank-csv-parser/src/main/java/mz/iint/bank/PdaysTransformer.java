package mz.iint.bank;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;

public class PdaysTransformer implements RecordTransformer {
    private static Field valuesField;

    static {
        try {
            valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int previousIndex;

    public PdaysTransformer(int previousIndex) {
        this.previousIndex = previousIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        final Integer previous = Integer.valueOf(record.get(previousIndex));
        String sprevious = previous.toString();

        if (previous == 999) sprevious = "NO_CONTACT";
        else if (previous < 5) sprevious = "lt5";
        else if (previous < 10) sprevious = "5to10";
        else sprevious = "gt10";


        try {
            final String[] values = (String[]) valuesField.get(record);
            values[previousIndex] = sprevious;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
