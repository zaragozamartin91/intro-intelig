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
}