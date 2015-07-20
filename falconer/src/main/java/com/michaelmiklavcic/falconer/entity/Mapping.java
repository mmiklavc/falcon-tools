package com.michaelmiklavcic.falconer.entity;

import com.fasterxml.jackson.annotation.*;

public class Mapping {
    private String propertyFile;
    private String template;

    public Mapping(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    @JsonCreator
    public Mapping(@JsonProperty("property-file") String propertyFile, @JsonProperty("template") String template) {
        this.propertyFile = propertyFile;
        this.template = template;
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

}
