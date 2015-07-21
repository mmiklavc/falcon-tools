package com.michaelmiklavcic.falconer.test.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;

import javax.xml.bind.*;

import org.apache.commons.io.FileUtils;
import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;

public class TestUtils {

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

    public static Entity unmarshallEntity(File file) throws JAXBException, IOException {
        return unmarshallEntity(FileUtils.readFileToString(file));
    }

    private static JAXBContext getJAXBContext() throws JAXBException {
        return JAXBContext.newInstance(Process.class, Feed.class);
    }

    public static Entity unmarshallEntity(String entity) throws JAXBException {
        return (Entity) getJAXBContext().createUnmarshaller().unmarshal(new ByteArrayInputStream(entity.getBytes(StandardCharsets.UTF_8)));
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
