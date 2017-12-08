package mz.iint.bank.trans;

import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;
import mz.iint.bank.Configuration;
import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;
import java.util.Random;

public class MaritalTransformer implements RecordTransformer {
    private static Field valuesField;
    private static Random random = new Random();

    static {
        try {
            valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int maritalIndex;

    private int unkyes = 0;

    public MaritalTransformer(int maritalIndex) {
        this.maritalIndex = maritalIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        String marital = record.get(maritalIndex).trim().toLowerCase().trim();

//        if (marital.startsWith("unknown")) {
//            double rand = random.nextDouble();
//            if (rand < 0.112192274) marital = "divorced";
//            else if (rand < 0.393597353) marital = "single";
//            else marital = "married";
//
//            if (record.get(Configuration.get().classIndex()).equalsIgnoreCase("yes")) unkyes++;
//        }

        if (marital.startsWith("unknown")) {
            if (record.get(Configuration.get().classIndex()).equalsIgnoreCase("yes")) {
                unkyes++;
                marital = "married";
            } else marital = "single";
        }

        System.out.println("marital unkyes:" + unkyes);

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[maritalIndex] = marital;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
