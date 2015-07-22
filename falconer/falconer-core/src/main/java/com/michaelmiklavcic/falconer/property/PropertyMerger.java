package com.michaelmiklavcic.falconer.property;

import java.io.*;
import java.util.Properties;

public class PropertyMerger {

    /**
     * Merge properties in decreasing order of importance
     * 
     * @param props
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Properties merge(File... props) throws FileNotFoundException, IOException {
        Properties merged = new Properties();

        for (int i = props.length - 1; i >= 0; i--) {
            try (InputStream is = new FileInputStream(props[i])) {
                merged.load(is);
            }
        }

        return merged;
    }

}
