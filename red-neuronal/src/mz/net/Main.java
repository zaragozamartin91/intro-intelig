package mz.net;

import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        Collection<RawRecord> records = new ArrayList<>();
        Collection<ClassifiedRecord> classifiedRecords = SomAlgorithm.buildNew()
                .withCategories(4)
                .applyOn(records);
    }
}
