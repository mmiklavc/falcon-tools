package com.michaelmiklavcic.falconer.test.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.process.Process;

public class TestUtils {
    private static JAXBContext jc;

    public static void write(File file, String contents) throws IOException {
        com.google.common.io.Files.createParentDirs(file);
        com.google.common.io.Files.write(contents, file, StandardCharsets.UTF_8);
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

    public static Process unmarshallProcess(String process) throws JAXBException {
        Unmarshaller unmarshaller = getProcessContext().createUnmarshaller();
        return (Process) unmarshaller.unmarshal(new ByteArrayInputStream(process.getBytes(StandardCharsets.UTF_8)));
    }

    public static Process unmarshallProcess(File file) throws FileNotFoundException, JAXBException {
        Unmarshaller unmarshaller = getProcessContext().createUnmarshaller();
        return (Process) unmarshaller.unmarshal(new FileInputStream(file));
    }

    public static void assertEquals(Process expected, Process actual) throws JAXBException {
        Marshaller marshaller = getProcessContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        ByteArrayOutputStream actualStream = new ByteArrayOutputStream();
        marshaller.marshal(expected, expectedStream);
        marshaller.marshal(actual, actualStream);
        try {
            assertThat(actualStream.toString(StandardCharsets.UTF_8.toString()),
                       equalTo(expectedStream.toString(StandardCharsets.UTF_8.toString())));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Something bad happened with encodings.", e);
        }
    }

    private static JAXBContext getProcessContext() throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(Process.class);
        }
        return jc;
    }

    /**
     * Cleans up after test run via runtime shutdown hooks
     */
    public static File createTempDir(String prefix) throws IOException {
        final Path tmpDir = Files.createTempDirectory(prefix);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    cleanDir(tmpDir);
                } catch (IOException e) {
                    System.out.println("Warning: Unable to clean tmp folder.");
                }
            }

        });
        return tmpDir.toFile();
    }

    public static void cleanDir(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    return FileVisitResult.CONTINUE;
                } else {
                    throw exc;
                }
            }
        });
    }

}
