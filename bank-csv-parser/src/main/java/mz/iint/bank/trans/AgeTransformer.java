package mz.iint.bank.trans;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

//        if (age < 32) sage = "lt32";
//        else if (age < 38) sage = "32to37";
//        else if (age < 47) sage = "38to46";
//        else sage = "gt46";

        if (age < 30) sage = "lt30";
        else if (age < 40) sage = "30to39";
        else if (age < 50) sage = "40to49";
        else sage = "ge50";

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[ageIndex] = sage;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }

}
