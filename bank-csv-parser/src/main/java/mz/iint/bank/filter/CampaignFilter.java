package mz.iint.bank.filter;

import mz.iint.bank.CsvStats;
import org.apache.commons.csv.CSVRecord;

import java.util.Optional;

public class CampaignFilter implements RecordFilter {
    private int campaignIndex;
    private int oneLimit;
    private int greaterLimit;
    private int limit;

    /**
     * Filtro de registros por valores si y no.
     *
     * @param limit         Limite de registros a aceptar
     * @param oneRatio      Porcentaje de registros == 1
     * @param campaignIndex Indice de columna 'campaign' (empezando en 0)
     */
    public CampaignFilter(int limit, double oneRatio, int campaignIndex) {
        this.limit = limit;
        this.oneLimit = (int) Math.floor(oneRatio * limit);
        this.greaterLimit = (int) Math.floor(limit - oneLimit);
        this.campaignIndex = campaignIndex;
    }

    @Override
    public boolean filter(CSVRecord record, CsvStats csvStats) {
        if (limit == csvStats.getTotalCount()) return false;

        final String value = Optional.ofNullable(record.get(campaignIndex)).orElse("");
        try {
            int ivalue = Integer.valueOf(value);
            boolean one = isOne(ivalue);
            boolean greater = !one;
            return (one && csvStats.getCampOneCount() < oneLimit ||
                    greater && csvStats.getCampGreaterCount() < greaterLimit);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isOne(int ivalue) {
        return ivalue == 1;
    }

    public static boolean isGreater(int ivalue) {
        return !isOne(ivalue);
    }

    public static boolean isOne(CSVRecord record, int campaignIndex) {
        try {
            Integer ivalue = Integer.valueOf(record.get(campaignIndex));
            return isOne(ivalue);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isGreater(CSVRecord record, int campaignIndex) {
        try {
            Integer ivalue = Integer.valueOf(record.get(campaignIndex));
            return isGreater(ivalue);
        } catch (Exception e) {
            return false;
        }
    }
}
