package com.michaelmiklavcic.falconer.property;

import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;

public class TokenReplacer {

    public String apply(Properties props, File inFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inFile))) {
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                for (String prop : props.stringPropertyNames()) {
                    line = line.replaceAll("##" + prop + "##", Matcher.quoteReplacement(props.getProperty(prop)));
                }
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }
    }

}
