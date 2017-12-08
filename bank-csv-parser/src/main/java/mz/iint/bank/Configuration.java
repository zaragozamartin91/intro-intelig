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
    private String outNumFile;

    private Integer recordsToKeep;
    private Integer lastIndex;

    private Boolean yesnoFilterActive;
    private Double yesnoRatio;
    private Boolean zeroDurationFilterActive;

    private Integer durationIndex;
    private Integer ageIndex;
    private Integer pdaysIndex;

    private Boolean ageTransformerActive;
    private Boolean pdaysTransformerActive;
    private Boolean monthTransformerActive;
    private Boolean durationTransformerActive;
    private Boolean educationTransformerActive;
    private Boolean unknownDurationFilterActive;
    private Boolean previousTransformerActive;
    private Boolean campaignTransformerActive;

    private Integer[] mixedCategories;
    private Boolean maritalTransformerActive;
    private Integer previousIndex;
    private Boolean previousFilterActive;
    private Double previousYesnoRatio;
    private Boolean campaignFilterActive;
    private Double campaignOneRatio;
    private Integer campaignIndex;


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

        final String smixedCategories = properties.getProperty("mixed.categories");
        final JsonArray jsonMixedCategories = Json.parse(smixedCategories).asArray();
        List<Integer> lMixedCategories = new ArrayList<>();
        jsonMixedCategories.forEach(item -> lMixedCategories.add(item.asInt()));
        configuration.mixedCategories = lMixedCategories.toArray(new Integer[0]);

        final String sclassidx = properties.getProperty("class.index");
        configuration.classIndex = Integer.valueOf(sclassidx);

        configuration.inFile = properties.getProperty("in.file");
        configuration.outFile = properties.getProperty("out.file");
        configuration.outNumFile = properties.getProperty("outnum.file");

        configuration.recordsToKeep = Integer.valueOf(properties.getProperty("records.limit"));

        configuration.lastIndex = configuration.headerIndexes.length - 1;


        configuration.yesnoFilterActive = Boolean.valueOf(properties.getProperty("filters.yesno.active", "false").toLowerCase());
        configuration.yesnoRatio = Double.valueOf(properties.getProperty("filters.yesno.ratio", "0.5"));

        configuration.zeroDurationFilterActive = Boolean.valueOf(properties.getProperty("filters.zeroduration.active", "false").toLowerCase());
        configuration.unknownDurationFilterActive = Boolean.valueOf(properties.getProperty("filters.unknown.active", "false").toLowerCase());

        configuration.previousFilterActive = Boolean.valueOf(properties.getProperty("filters.previous.active", "false").toLowerCase());
        configuration.previousYesnoRatio = Double.valueOf(properties.getProperty("filters.previous.yesno.ratio", "0.5"));

        configuration.campaignFilterActive = Boolean.valueOf(properties.getProperty("filters.campaign.active", "false").toLowerCase());
        configuration.campaignOneRatio = Double.valueOf(properties.getProperty("filters.campaign.one.ratio", "0.5"));


        configuration.ageTransformerActive = Boolean.valueOf(properties.getProperty("transformers.age.active", "false").toLowerCase());
        configuration.pdaysTransformerActive = Boolean.valueOf(properties.getProperty("transformers.pdays.active", "false").toLowerCase());
        configuration.monthTransformerActive = Boolean.valueOf(properties.getProperty("transformers.month.active", "false").toLowerCase());
        configuration.durationTransformerActive = Boolean.valueOf(properties.getProperty("transformers.duration.active", "false").toLowerCase());
        configuration.educationTransformerActive = Boolean.valueOf(properties.getProperty("transformers.education.active", "false").toLowerCase());
        configuration.previousTransformerActive = Boolean.valueOf(properties.getProperty("transformers.previous.active", "false").toLowerCase());
        configuration.campaignTransformerActive = Boolean.valueOf(properties.getProperty("transformers.campaign.active", "false").toLowerCase());
        configuration.maritalTransformerActive = Boolean.valueOf(properties.getProperty("transformers.marital.active", "false").toLowerCase());


        configuration.ageIndex = Integer.valueOf(properties.getProperty("age.index", "0"));
        configuration.pdaysIndex = Integer.valueOf(properties.getProperty("pdays.index", "12"));
        configuration.durationIndex = Integer.valueOf(properties.getProperty("duration.index"));
        configuration.previousIndex = Integer.valueOf(properties.getProperty("previous.index"));
        configuration.campaignIndex = Integer.valueOf(properties.getProperty("campaign.index"));


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

    public Boolean pdaysTransformerActive() {
        return pdaysTransformerActive;
    }

    public Integer pdaysIndex() {
        return pdaysIndex;
    }

    public Boolean monthTransformerActive() {
        return monthTransformerActive;
    }

    public String outNumFile() {
        return outNumFile;
    }

    public Boolean durationTransformerActive() {
        return durationTransformerActive;
    }

    public Boolean educationTransformerActive() {
        return educationTransformerActive;
    }

    public Boolean unknownDurationFilterActive() {
        return unknownDurationFilterActive;
    }

    public Boolean previousTransformerActive() {
        return previousTransformerActive;
    }

    public Boolean campaignTransformerActive() {
        return campaignTransformerActive;
    }

    public Integer[] mixedCategories() {
        return mixedCategories;
    }

    public Boolean maritalTransformerActive() {
        return maritalTransformerActive;
    }

    public Integer previousIndex() {
        return previousIndex;
    }

    public Boolean previousFilterActive() {
        return previousFilterActive;
    }

    public Double previousYesnoRatio() {
        return previousYesnoRatio;
    }

    public Boolean campaignFilterActive() {
        return campaignFilterActive;
    }

    public Double campaignOneRatio() {
        return campaignOneRatio;
    }

    public Integer campaignIndex() {
        return campaignIndex;
    }
}
