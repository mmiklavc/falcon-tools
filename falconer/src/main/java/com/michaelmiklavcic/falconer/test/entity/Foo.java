package com.michaelmiklavcic.falconer.test.entity;

import com.fasterxml.jackson.annotation.*;

public class Foo {
    private String propertyFile;
    private String template;
    
    public Foo(String propertyFile) {
        this.propertyFile = propertyFile;
    }
    
    @JsonCreator
    public Foo(@JsonProperty("property-file") String propertyFile, @JsonProperty("template") String template) {
        this.propertyFile = propertyFile;
        this.template = template;
    }
    
    @JsonProperty("property-file")
    public String getPropertyFile() {
        return propertyFile;
    }

    @JsonProperty("property-file")
    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
