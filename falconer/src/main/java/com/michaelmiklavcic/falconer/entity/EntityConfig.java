package com.michaelmiklavcic.falconer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityConfig {
    private String pipeline;
    private String defaultProperties;
    private String defaultFeedTemplate;
    private String defaultProcessTemplate;
    private Mapping[] feedMappings;
    private Mapping[] processMappings;

    public EntityConfig() {
        pipeline = "";
        defaultProperties = "";
        defaultFeedTemplate = "";
        defaultProcessTemplate = "";
        feedMappings = new Mapping[0];
        processMappings = new Mapping[0];
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    @JsonProperty("default-properties")
    public String getDefaultProperties() {
        return defaultProperties;
    }

    @JsonProperty("default-properties")
    public void setDefaultProperties(String defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @JsonProperty("default-process-template")
    public String getDefaultProcessTemplate() {
        return defaultProcessTemplate;
    }

    @JsonProperty("default-feed-template")
    public String getDefaultFeedTemplate() {
        return defaultFeedTemplate;
    }

    @JsonProperty("default-feed-template")
    public void setDefaultFeedTemplate(String defaultFeedTemplate) {
        this.defaultFeedTemplate = defaultFeedTemplate;
    }

    @JsonProperty("default-process-template")
    public void setDefaultProcessemplate(String defaultProcessTemplate) {
        this.defaultProcessTemplate = defaultProcessTemplate;
    }

    @JsonProperty("feed-mappings")
    public Mapping[] getFeedMappings() {
        return feedMappings;
    }

    @JsonProperty("feed-mappings")
    public void setFeedMappings(Mapping[] mappings) {
        this.feedMappings = mappings;
    }

    @JsonProperty("process-mappings")
    public Mapping[] getProcessMappings() {
        return processMappings;
    }

    @JsonProperty("process-mappings")
    public void setProcessMappings(Mapping[] mappings) {
        this.processMappings = mappings;
    }

}
