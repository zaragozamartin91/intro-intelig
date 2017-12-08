package mz.iint.bank.trans;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;

public class PreviousTransformer implements RecordTransformer {
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

    public PreviousTransformer(int previousIndex) {
        this.previousIndex = previousIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        final Integer previous = Integer.valueOf(record.get(previousIndex));
        String sprevious;

//        if (previous == 0) sprevious = "0";
//        else if (previous == 1) sprevious = "1";
//        else sprevious = "gt1";

        if (previous == 0) sprevious = "no";
        else sprevious = "yes";

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[previousIndex] = sprevious;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }

}
