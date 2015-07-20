package com.michaelmiklavcic.falconer.entity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Matcher;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.process.Process;

public class TokenReplacer {

    public String apply(Properties props, String in) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(in));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while((line = reader.readLine()) != null) {
            for (String prop : props.stringPropertyNames()) {
                line = line.replaceAll("##" + prop + "##", Matcher.quoteReplacement(props.getProperty(prop)));
            }
            builder.append(line);
        }
        return builder.toString();
//        JAXBContext jc = JAXBContext.newInstance(Process.class);
//        Unmarshaller um = jc.createUnmarshaller();
//        return (Process) um.unmarshal(new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8)));
    }

}
