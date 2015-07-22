package com.michaelmiklavcic.falconer.entity;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.*;
import com.michaelmiklavcic.falconer.util.ConfigurationException;

public class Mapping {
    private String propertyFile;
    private String template;

    public Mapping(String propertyFile) {
        this.propertyFile = propertyFile;
        this.template = "";
        validate();
    }

    @JsonCreator
    public Mapping(@JsonProperty("property-file") String propertyFile, @JsonProperty("template") String template) {
        this.propertyFile = propertyFile;
        this.template = template != null ? template : "";
        validate();
    }

    private void validate() {
        if (StringUtils.isEmpty(propertyFile)) {
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

}
