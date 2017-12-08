package mz.iint.bank.trans;

import mz.iint.bank.Configuration;
import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;
import java.util.Random;

public class EducationTransformer implements RecordTransformer {
    private static Random random = new Random();
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

    static int unkcounter = 0;
    static int yunkcounter = 0;

    @Override
    public CSVRecord transform(CSVRecord record) {
        String y = record.get(Configuration.get().classIndex()).trim().toLowerCase();
        String education = record.get(educationIndex).trim().toLowerCase();

        education = education.startsWith("basic") ? "primary" : education;
        education = education.startsWith("high") ? "secondary" : education;
        education = education.startsWith("university") ? "tertiary" : education;
        education = education.startsWith("professional") ? "tertiary" : education;

//        if (education.startsWith("unknown")) {
//            double rand = random.nextDouble();
//            if (rand < 0.241258653) education = "secondary";
//            else if (rand < 0.558533432) education = "primary";
//            else education = "tertiary";
//            unkcounter++;
//            if ("yes".equalsIgnoreCase(y)) yunkcounter++;
//        }
//
//        System.out.println("unkcounter: " + unkcounter);
//        System.out.println("yunkcounter: " + yunkcounter);

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[educationIndex] = education;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
