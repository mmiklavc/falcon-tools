package com.michaelmiklavcic.falconer.test.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.process.Process;

import com.google.common.io.Files;

public class TestUtils {
    private static JAXBContext jc;

    public static void write(File file, String contents) throws IOException {
        Files.createParentDirs(file);
        Files.write(contents, file, StandardCharsets.UTF_8);
    }
    
    /**
     * Load properties from string into properties object
     */
    public static Properties loadProperties(String contents) throws IOException {
        Properties props = new Properties();
        try (ByteArrayInputStream is = new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8))) {
            props.load(is);
        }
        return props;
    }

    public static void assertEquals(Process expected, Process actual) throws JAXBException, UnsupportedEncodingException {
        Marshaller marshaller = getJAXBContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        ByteArrayOutputStream actualStream = new ByteArrayOutputStream();
        marshaller.marshal(expected, expectedStream);
        marshaller.marshal(actual, actualStream);
        assertThat(actualStream.toString(StandardCharsets.UTF_8.toString()),
                   equalTo(expectedStream.toString(StandardCharsets.UTF_8.toString())));
    }

    private static JAXBContext getJAXBContext() throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(Process.class);
        }
        return jc;
    }

}
