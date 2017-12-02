package mz.iint.bank;

import org.apache.commons.csv.CSVRecord;

public interface RecordTransformer {
    CSVRecord transform(CSVRecord record);
}
