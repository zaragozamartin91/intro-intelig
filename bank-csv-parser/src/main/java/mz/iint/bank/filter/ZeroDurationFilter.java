package mz.iint.bank.filter;

import mz.iint.bank.CsvStats;
import org.apache.commons.csv.CSVRecord;

import java.util.Optional;

/**
 * Rechaza registros con duracion 0
 */
public class ZeroDurationFilter implements RecordFilter {
    private int durationIndex;

    public ZeroDurationFilter(int durationIndex) {
        this.durationIndex = durationIndex;
    }

    @Override
    public boolean filter(CSVRecord record, CsvStats csvStats) {
        final String sduration = Optional.ofNullable(record.get(durationIndex)).orElse("");
        return !sduration.trim().equals("0");
    }
}
