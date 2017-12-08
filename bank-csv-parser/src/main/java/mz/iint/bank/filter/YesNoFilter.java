package mz.iint.bank.filter;

import mz.iint.bank.CsvStats;
import org.apache.commons.csv.CSVRecord;

import java.util.Optional;

public class YesNoFilter implements RecordFilter {
    private int classIndex;
    private int yesLimit;
    private int noLimit;
    private int limit;

    /**
     * Filtro de registros por valores si y no.
     *
     * @param limit      Limite de registros a aceptar
     * @param yesRatio   Porcentaje de registros con valor si
     * @param classIndex Indice de columna de tipo clase (empezando en 0)
     */
    public YesNoFilter(int limit, double yesRatio, int classIndex) {
        this.limit = limit;
        this.yesLimit = (int) Math.floor(yesRatio * limit);
        this.noLimit = (int) Math.floor(limit - yesLimit);
        this.classIndex = classIndex;
    }

    @Override
    public boolean filter(CSVRecord record, CsvStats csvStats) {
        if (limit == csvStats.getTotalCount()) return false;

        final String value = Optional.ofNullable(record.get(classIndex)).orElse("");
        final boolean yes = "yes".equalsIgnoreCase(value);
        final boolean no = "no".equalsIgnoreCase(value);

        return (yes && csvStats.getYescount() < yesLimit ||
                no && csvStats.getNocount() < noLimit);
    }

}
