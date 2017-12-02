package mz.iint.bank;

import org.apache.commons.csv.CSVRecord;

import java.util.Optional;

/**
 * Rechaza registros cuyo valor de campo sea value
 */
public class ValueFilter implements RecordFilter {
    private int index;
    private String value;

    /**
     * Crea un filtro de valores.
     *
     * @param index Indice de columna del campo (comienza en 0)
     * @param value Valor que se debe rechazar
     */
    public ValueFilter(int index, String value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public boolean filter(CSVRecord record, CsvStats csvStats) {
        final String val = Optional.ofNullable(record.get(index)).orElse("");
        return !val.trim().equalsIgnoreCase(value);
    }
}
