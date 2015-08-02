package com.michaelmiklavcic.falconer.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityConfig {
    private String pipeline;
    private String defaultProperties;
    private String feedPrototype;
    private String processPrototype;
    private Mapping[] feedMappings;
    private Mapping[] processMappings;

    public EntityConfig() {
        pipeline = "";
        defaultProperties = "";
        feedPrototype = "";
        processPrototype = "";
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

    @JsonProperty("process-prototype")
    public String getProcessPrototype() {
        return processPrototype;
    }

    @JsonProperty("feed-prototype")
    public String getFeedPrototype() {
        return feedPrototype;
    }

    @JsonProperty("feed-prototype")
    public void setFeedPrototype(String feedPrototype) {
        this.feedPrototype = feedPrototype;
    }

    @JsonProperty("process-prototype")
    public void setProcessPrototype(String processPrototype) {
        this.processPrototype = processPrototype;
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
