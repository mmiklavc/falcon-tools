package com.michaelmiklavcic.falconer.util;

import static org.apache.commons.lang.StringUtils.isEmpty;

import com.fasterxml.jackson.annotation.*;

public class Mapping {
    private String propertyFile;
    private String template;
    private MergeStrategy mergeStrategy;

    public Mapping(String propertyFile) {
        this.propertyFile = propertyFile;
        this.template = "";
        this.mergeStrategy = MergeStrategy.MERGE;
        validate();
    }

    @JsonCreator
    public Mapping(@JsonProperty("property-file") String propertyFile, @JsonProperty("template") String template,
            @JsonProperty("merge-strategy") MergeStrategy mergeStrategy) {
        this.propertyFile = propertyFile;
        this.template = template != null ? template : "";
        this.mergeStrategy = mergeStrategy != null ? mergeStrategy : MergeStrategy.MERGE;
        validate();
    }

    private void validate() {
        if (isEmpty(propertyFile)) {
            throw new ConfigurationException("Missing property file definition");
        }
    }

    @JsonProperty("property-file")
    public String getPropertyFile() {
        return propertyFile;
    }

    public String getTemplate() {
        return template;
    }

    @JsonProperty("property-file")
    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty("merge-strategy")
    public void setMergeStrategy(MergeStrategy mergeStrategy) {
        this.mergeStrategy = mergeStrategy;
    }

    @JsonProperty("merge-strategy")
    public MergeStrategy getMergeStrategy() {
        return mergeStrategy;
    }

}
