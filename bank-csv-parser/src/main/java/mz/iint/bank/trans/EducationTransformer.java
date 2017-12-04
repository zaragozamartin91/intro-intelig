package mz.iint.bank.trans;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;

public class EducationTransformer implements RecordTransformer {
    private static Field valuesField;

    static {
        try {
            valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int educationIndex;

    public EducationTransformer(int educationIndex) {
        this.educationIndex = educationIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        String education = record.get(educationIndex).trim().toLowerCase();

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[educationIndex] = education.startsWith("basic") ? "basic" : education;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
