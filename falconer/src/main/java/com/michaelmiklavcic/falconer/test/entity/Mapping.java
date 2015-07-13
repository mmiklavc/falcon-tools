package com.michaelmiklavcic.falconer.test.entity;

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

//    public Mapping() {
//    }
//
//    @JsonCreator
//    public Mapping(String[] vals) {
//        this.main = vals[0];
//        if (vals.length > 1) {
//            this.parents = Arrays.copyOfRange(vals, 1, vals.length);
//        }
//    }
//
//    public Mapping(String main) {
//        this.main = main;
//    }
//
//    private String main;
//    private String[] parents;
//
//    public void setMain(String main) {
//        this.main = main;
//    }
//
//    public void setParent(String... parents) {
//        this.parents = parents;
//    }
//
//    public String getMain() {
//        return main;
//    }
//
//    public String[] getParents() {
//        return parents;
//    }

}
