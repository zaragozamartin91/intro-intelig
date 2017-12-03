package mz.iint.bank;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration {
    private static Configuration ourInstance = new Configuration();

    private Integer[] headerIndexes;
    private Integer classIndex;
    private String inFile;
    private String outFile;
    private Integer recordsToKeep;
    private Integer lastIndex;
    private Boolean yesnoFilterActive;
    private Double yesnoRatio;
    private Boolean zeroDurationFilterActive;
    private Integer durationIndex;
    private Boolean ageTransformerActive;
    private Integer ageIndex;
    private Boolean previousTransformerActive;
    private Integer previousIndex;


    public static Configuration get() {
        return ourInstance;
    }

    private Configuration() { }

    public static Configuration load(Properties properties) {
        Configuration configuration = get();
        final String sheaders = properties.getProperty("header.indexes");
        final JsonArray jsonheaders = Json.parse(sheaders).asArray();
        List<Integer> lheaders = new ArrayList<>();
        jsonheaders.forEach(item -> lheaders.add(item.asInt()));
        configuration.headerIndexes = lheaders.toArray(new Integer[0]);

        final String sclassidx = properties.getProperty("class.index");
        configuration.classIndex = Integer.valueOf(sclassidx);

        configuration.inFile = properties.getProperty("in.file");
        configuration.outFile = properties.getProperty("out.file");

        configuration.recordsToKeep = Integer.valueOf(properties.getProperty("records.limit"));

        configuration.lastIndex = configuration.headerIndexes.length - 1;

        configuration.yesnoFilterActive = Boolean.valueOf(properties.getProperty("filters.yesno.active", "false").toLowerCase());
        configuration.yesnoRatio = Double.valueOf(properties.getProperty("filters.yesno.ratio", "0.5"));

        configuration.zeroDurationFilterActive = Boolean.valueOf(properties.getProperty("filters.zeroduration.active", "false").toLowerCase());
        configuration.durationIndex = Integer.valueOf(properties.getProperty("duration.index"));

        configuration.ageTransformerActive = Boolean.valueOf(properties.getProperty("transformers.age.active", "false").toLowerCase());
        configuration.previousTransformerActive = Boolean.valueOf(properties.getProperty("transformers.pdays.active", "false").toLowerCase());

        configuration.ageIndex = Integer.valueOf(properties.getProperty("age.index", "0"));
        configuration.previousIndex = Integer.valueOf(properties.getProperty("pdays.index", "12"));

        return configuration;
    }

    public Integer[] headerIndexes() {
        return headerIndexes;
    }

    public Integer classIndex() {
        return classIndex;
    }

    public String inFile() {
        return inFile;
    }

    public String outFile() {
        return outFile;
    }

    public Integer recordsToKeep() {
        return recordsToKeep;
    }

    public Integer lastIndex() {
        return lastIndex;
    }

    public Boolean yesnoFilterActive() {
        return yesnoFilterActive;
    }

    public Double yesnoRatio() {
        return yesnoRatio;
    }

    public Boolean zeroDurationFilterActive() {
        return zeroDurationFilterActive;
    }

    public Integer durationIndex() {
        return durationIndex;
    }

    public Boolean ageTransformerActive() {
        return ageTransformerActive;
    }

    public Integer ageIndex() {
        return ageIndex;
    }

    public Boolean previousTransformerActive() {
        return previousTransformerActive;
    }

    public Integer previousIndex() {
        return previousIndex;
    }
}
