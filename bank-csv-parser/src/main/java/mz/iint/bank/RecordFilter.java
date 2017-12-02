package mz.iint.bank;

import org.apache.commons.csv.CSVRecord;

public interface RecordFilter {
    /**
     * Determina si un registro debe agregarse.
     *
     * @param record   Registro a verificar.
     * @param csvStats Estado actual de los registros.
     * @return True si el registro debe agregarse, false en caso contrario.
     */
    boolean filter(CSVRecord record, CsvStats csvStats);
}
