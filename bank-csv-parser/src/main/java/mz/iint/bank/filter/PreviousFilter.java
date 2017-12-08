package mz.iint.bank.filter;

import mz.iint.bank.CsvStats;
import org.apache.commons.csv.CSVRecord;

import java.util.Optional;

public class PreviousFilter implements RecordFilter {
    private int previousIndex;
    private int yesLimit;
    private int noLimit;
    private int limit;

    /**
     * Filtro de registros por valores si y no.
     *
     * @param limit         Limite de registros a aceptar
     * @param yesRatio      Porcentaje de registros con valor si
     * @param previousIndex Indice de columna 'previous' (empezando en 0)
     */
    public PreviousFilter(int limit, double yesRatio, int previousIndex) {
        this.limit = limit;
        this.yesLimit = (int) Math.floor(yesRatio * limit);
        this.noLimit = (int) Math.floor(limit - yesLimit);
        this.previousIndex = previousIndex;
    }

    @Override
    public boolean filter(CSVRecord record, CsvStats csvStats) {
        if (limit == csvStats.getTotalCount()) return false;

        final String value = Optional.ofNullable(record.get(previousIndex)).orElse("");
        try {
            int ivalue = Integer.valueOf(value);
            boolean yes = isYes(ivalue);
            boolean no = !yes;
            return (yes && csvStats.getPrevYesCount() < yesLimit ||
                    no && csvStats.getPrevNoCount() < noLimit);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isYes(int ivalue) {
        return ivalue > 0;
    }

    public static boolean isYes(CSVRecord record, int previousIndex) {
        try {
            Integer ivalue = Integer.valueOf(record.get(previousIndex));
            return isYes(ivalue);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNo(CSVRecord record, int previousIndex) {
        try {
            Integer ivalue = Integer.valueOf(record.get(previousIndex));
            return !isYes(ivalue);
        } catch (Exception e) {
            return false;
        }
    }
}
