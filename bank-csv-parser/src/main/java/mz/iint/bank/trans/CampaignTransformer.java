package mz.iint.bank.trans;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Field;

public class CampaignTransformer implements RecordTransformer {
    private static Field valuesField;

    static {
        try {
            valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private int campaignIndex;

    public CampaignTransformer(int campaignIndex) {
        this.campaignIndex = campaignIndex;
    }

    @Override
    public CSVRecord transform(CSVRecord record) {
        final Integer campaign = Integer.valueOf(record.get(campaignIndex));
        String scampaign;

        if (campaign == 1) scampaign = "eq1";
        else if (campaign == 2) scampaign = "eq2";
        else scampaign = "gt2";

        try {
            final String[] values = (String[]) valuesField.get(record);
            values[campaignIndex] = scampaign;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }

}
