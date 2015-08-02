package com.michaelmiklavcic.falconer.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MergeStrategy {
    MERGE,
    IGNORE_PROTOTYPE;

    @JsonCreator
    public static MergeStrategy fromString(String val) {
        return MergeStrategy.valueOf(val.toUpperCase().replace("-", "_"));
    }
}
