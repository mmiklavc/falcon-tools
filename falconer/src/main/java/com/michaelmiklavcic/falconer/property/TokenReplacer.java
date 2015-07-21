package com.michaelmiklavcic.falconer.property;

import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;

public class TokenReplacer {

    public String apply(Properties props, String in) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(in));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            for (String prop : props.stringPropertyNames()) {
                line = line.replaceAll("##" + prop + "##", Matcher.quoteReplacement(props.getProperty(prop)));
            }
            builder.append(line);
        }
        return builder.toString();
    }

}
