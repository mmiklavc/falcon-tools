package com.michaelmiklavcic.falconer.test.entity;

import java.io.*;
import java.util.Properties;

import javax.annotation.PropertyKey;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityConfig {
    private String pipeline;
    private String defaultProperties;
    private String defaultTemplate;
    private Mapping[] mappings;
    
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
    
    @JsonProperty("default-template")
    public String getDefaultTemplate() {
        return defaultTemplate;
    }
    
    @JsonProperty("default-template")
    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }
    
    public Mapping[] getMappings() {
        return mappings;
    }
    
    public void setMappings(Mapping[] mappings) {
        this.mappings = mappings;
    }
    
    
//    private Properties properties;
//    private String parent;
//    private Mapping[] mappings;
//    private Foo[] foo;
//
//    public void setParent(String parent) throws IOException {
//        this.parent = parent;
//    }
//
//    public String getParent() {
//        return parent;
//    }
//
//    public void setProperties(Properties properties) {
//        this.properties = properties;
//    }
//
//    public Properties getProperties() {
//        return properties;
//    }
//
//    /**
//     * Overlays this config object's properties on top of the passed config's properties
//     * and updates this object
//     */
//    public void merge(EntityConfig conf) {
//        conf.getProperties().putAll(properties);
//        properties = conf.getProperties();
//    }
//    
//    
//    /** testing stuff **/
//    
//    
//    public void setMappings(Mapping[] mappings) {
//        this.mappings = mappings;
//    }
//    
//    public Mapping[] getMappings() {
//        return mappings;
//    }
//    
//    @JsonProperty("entity-configs")
//    public void setEntityConfig(Foo[] foo) {
//        this.foo = foo;
//    }
//    
//    public Foo[] getFoo() {
//        return foo;
//    }
}
