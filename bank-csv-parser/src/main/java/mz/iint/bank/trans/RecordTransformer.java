package mz.iint.bank.trans;

import org.apache.commons.csv.CSVRecord;

public interface RecordTransformer {
    CSVRecord transform(CSVRecord record);
}