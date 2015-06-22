package com.michaelmiklavcic.falconer.entity;

import java.io.*;
import java.util.Properties;

public class PropertyBuilder {

    public Properties merge(File parent, File... otherProps) throws FileNotFoundException, IOException {
        Properties baseProps = new Properties();
        try (InputStream parentStream = new FileInputStream(parent)) {
            baseProps.load(parentStream);
        }

        for(File childPropFile: otherProps) {
            try (InputStream childStream = new FileInputStream(childPropFile)) {
                baseProps.load(childStream);
            }
        }

        return baseProps;
    }

}
