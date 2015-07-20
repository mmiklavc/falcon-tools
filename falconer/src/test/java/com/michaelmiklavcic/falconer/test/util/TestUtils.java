package com.michaelmiklavcic.falconer.test.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.*;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.falcon.entity.v0.*;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.*;
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

    // public static Process unmarshallProcess(String process) throws JAXBException {
    // Unmarshaller unmarshaller = getProcessContext().createUnmarshaller();
    // return (Process) unmarshaller.unmarshal(new ByteArrayInputStream(process.getBytes(StandardCharsets.UTF_8)));
    // }

    public static Entity unmarshallEntity(File file) throws JAXBException, IOException {
        String entity = FileUtils.readFileToString(file);
        Unmarshaller unmarshaller = null;
        if (entity.contains("xmlns=\"uri:falcon:process:0.1\"")) {
            unmarshaller = getProcessContext().createUnmarshaller();
        } else if (entity.contains("xmlns=\"uri:falcon:feed:0.1\"")) {
            unmarshaller = getFeedContext().createUnmarshaller();
        } else {
            throw new UnsupportedOperationException("Entity type not recognized from xmlns");
        }
        return (Entity) unmarshaller.unmarshal(new ByteArrayInputStream(entity.getBytes(StandardCharsets.UTF_8)));
    }

    public static Entity unmarshallEntity(String entity) throws JAXBException {
        Unmarshaller unmarshaller = null;
        if (entity.contains("xmlns=\"uri:falcon:process:0.1\"")) {
            unmarshaller = getProcessContext().createUnmarshaller();
        } else if (entity.contains("xmlns=\"uri:falcon:feed:0.1\"")) {
            unmarshaller = getFeedContext().createUnmarshaller();
        } else {
            throw new UnsupportedOperationException("Entity type not recognized from xmlns");
        }
        return (Entity) unmarshaller.unmarshal(new ByteArrayInputStream(entity.getBytes(StandardCharsets.UTF_8)));
    }

    // public static Process unmarshallProcess(File file) throws FileNotFoundException, JAXBException {
    // Unmarshaller unmarshaller = getProcessContext().createUnmarshaller();
    // return (Process) unmarshaller.unmarshal(new FileInputStream(file));
    // }

    public static void assertEquals(String entity, Entity actual) throws JAXBException {
        TestUtils.assertEquals(TestUtils.unmarshallEntity(entity), actual);
    }

    public static void assertEquals(Entity expected, Entity actual) throws JAXBException {
        assertThat(actual.getClass().getName(), equalTo(expected.getClass().getName()));
        Marshaller marshaller = createMarshaller(expected);
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

    public static void assertEquals(Process expected, Process actual) {
        assertEquals(expected.getACL(), actual.getACL());
        assertEquals(expected.getClusters(), actual.getClusters());
        assertEquals(expected.getFrequency(), actual.getFrequency());
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getLateProcess(), actual.getLateProcess());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        Assert.assertEquals(expected.getParallel(), actual.getParallel());
        Assert.assertEquals(expected.getPipelines(), actual.getPipelines());
        assertEquals(expected.getRetry(), actual.getRetry());
        assertEquals(expected.getSla(), actual.getSla());
        Assert.assertEquals(expected.getTags(), actual.getTags());
        assertEquals(expected.getTimeout(), actual.getTimeout());
        assertEquals(expected.getTimezone(), actual.getTimezone());
        assertEquals(expected.getWorkflow(), actual.getWorkflow());
    }

    private static void assertEquals(LateProcess expected, LateProcess actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            assertEquals(expected.getDelay(), actual.getDelay());
            assertEqualsLateInputs(expected.getLateInputs(), actual.getLateInputs());
            Assert.assertEquals(expected.getPolicy(), actual.getPolicy());
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private static void assertEqualsLateInputs(List<LateInput> expected, List<LateInput> actual) {
        Map<String, LateInput> eli = asSortedLateInputs(expected);
        Map<String, LateInput> ali = asSortedLateInputs(actual);
        assertThat(eli.size(), equalTo(ali.size()));
        for (Entry<String, LateInput> entry : eli.entrySet()) {
            LateInput expectedLateInput = entry.getValue();
            LateInput actualLateInput = ali.get(entry.getKey());
            assertThat(expectedLateInput.getInput(), equalTo(actualLateInput.getInput()));
            assertThat(expectedLateInput.getWorkflowPath(), equalTo(actualLateInput.getWorkflowPath()));
        }
    }

    private static Map<String, LateInput> asSortedLateInputs(List<LateInput> lateInputs) {
        Map<String, LateInput> li = new TreeMap<String, LateInput>();
        for (LateInput l : lateInputs) {
            li.put(l.getInput(), l);
        }
        return li;
    }

    private static void assertEquals(Retry expected, Retry actual) {
        Assert.assertEquals(expected.getAttempts(), actual.getAttempts());
        Assert.assertEquals(expected.getDelay(), actual.getDelay());
        Assert.assertEquals(expected.getPolicy(), actual.getPolicy());
    }

    private static void assertEquals(Sla expected, Sla actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            Assert.assertEquals(expected.getShouldEndIn(), actual.getShouldEndIn());
            Assert.assertEquals(expected.getShouldStartIn(), actual.getShouldStartIn());
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private static void assertEquals(Frequency expected, Frequency actual) {
        Assert.assertEquals(expected.getFrequency(), actual.getFrequency());
        Assert.assertEquals(expected.getFrequencyAsInt(), actual.getFrequencyAsInt());
        Assert.assertEquals(expected.getTimeUnit(), actual.getTimeUnit());
    }

    private static void assertEquals(TimeZone expected, TimeZone actual) {
        Assert.assertEquals(expected.getID(), actual.getID());
    }

    private static void assertEquals(Workflow expected, Workflow actual) {
        Assert.assertEquals(expected.getLib(), actual.getLib());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getPath(), actual.getPath());
        Assert.assertEquals(expected.getVersion(), actual.getVersion());
        Assert.assertEquals(expected.getEngine(), actual.getEngine());
    }

    public static void assertEquals(ACL expected, ACL actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            assertThat(actual.getGroup(), equalTo(expected.getGroup()));
            assertThat(actual.getOwner(), equalTo(expected.getOwner()));
            assertThat(actual.getPermission(), equalTo(expected.getPermission()));
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    public static void assertEquals(Clusters expected, Clusters actual) {
        Map<String, Cluster> ec = asSortedClusters(expected.getClusters());
        Map<String, Cluster> ac = asSortedClusters(actual.getClusters());
        assertThat(ec.size(), equalTo(ac.size()));
        for (Entry<String, Cluster> entry : ec.entrySet()) {
            Cluster expectedCluster = entry.getValue();
            Cluster actualCluster = ac.get(entry.getKey());
            assertThat(expectedCluster.getName(), equalTo(actualCluster.getName()));
            assertThat(expectedCluster.getValidity().getStart(), equalTo(actualCluster.getValidity().getStart()));
            assertThat(expectedCluster.getValidity().getEnd(), equalTo(actualCluster.getValidity().getEnd()));
        }
    }

    private static Map<String, Cluster> asSortedClusters(List<Cluster> clusters) {
        Map<String, Cluster> cl = new TreeMap<String, Cluster>();
        for (Cluster c : clusters) {
            cl.put(c.getName(), c);
        }
        return cl;
    }

    public static void assertEquals(Inputs expected, Inputs actual) {
        Map<String, Input> ec = asSortedInputs(expected.getInputs());
        Map<String, Input> ac = asSortedInputs(actual.getInputs());
        assertThat(ec.size(), equalTo(ac.size()));
        for (Entry<String, Input> entry : ec.entrySet()) {
            Input expectedInput = entry.getValue();
            Input actualInput = ac.get(entry.getKey());
            assertThat(expectedInput.getEnd(), equalTo(actualInput.getEnd()));
            assertThat(expectedInput.getFeed(), equalTo(actualInput.getFeed()));
            assertThat(expectedInput.getName(), equalTo(actualInput.getName()));
            assertThat(expectedInput.getPartition(), equalTo(actualInput.getPartition()));
            assertThat(expectedInput.getStart(), equalTo(actualInput.getStart()));
        }
    }

    private static Map<String, Input> asSortedInputs(List<Input> inputs) {
        Map<String, Input> in = new TreeMap<String, Input>();
        for (Input i : inputs) {
            in.put(i.getName(), i);
        }
        return in;
    }

    public static void assertEquals(Outputs expected, Outputs actual) {
        Map<String, Output> ec = asSortedOutputs(expected.getOutputs());
        Map<String, Output> ac = asSortedOutputs(actual.getOutputs());
        assertThat(ec.size(), equalTo(ac.size()));
        for (Entry<String, Output> entry : ec.entrySet()) {
            Output expectedOutput = entry.getValue();
            Output actualOutput = ac.get(entry.getKey());
            assertThat(expectedOutput.getFeed(), equalTo(actualOutput.getFeed()));
            assertThat(expectedOutput.getName(), equalTo(actualOutput.getName()));
            assertThat(expectedOutput.getInstance(), equalTo(actualOutput.getInstance()));
        }
    }

    private static Map<String, Output> asSortedOutputs(List<Output> outputs) {
        Map<String, Output> in = new TreeMap<String, Output>();
        for (Output i : outputs) {
            in.put(i.getName(), i);
        }
        return in;
    }

    public static void assertEquals(org.apache.falcon.entity.v0.process.Properties expected,
            org.apache.falcon.entity.v0.process.Properties actual) {
        Map<String, Property> ec = asSortedProperties(expected.getProperties());
        Map<String, Property> ac = asSortedProperties(actual.getProperties());
        assertThat(ec.size(), equalTo(ac.size()));
        for (Entry<String, Property> entry : ec.entrySet()) {
            Property expectedProperty = entry.getValue();
            Property actualProperty = ac.get(entry.getKey());
            assertThat(expectedProperty.getName(), equalTo(actualProperty.getName()));
            assertThat(expectedProperty.getValue(), equalTo(actualProperty.getValue()));
        }
    }

    private static Map<String, Property> asSortedProperties(List<Property> properties) {
        Map<String, Property> in = new TreeMap<String, Property>();
        for (Property i : properties) {
            in.put(i.getName(), i);
        }
        return in;
    }

    private static Marshaller createMarshaller(Entity entity) throws JAXBException {
        Marshaller marshaller = getJAXBContext(entity).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return marshaller;
    }

    private static JAXBContext getProcessContext() throws JAXBException {
        return getJAXBContext(new Process());
    }

    private static JAXBContext getJAXBContext(Entity entity) throws JAXBException {
        return JAXBContext.newInstance(entity.getClass());
    }

    private static JAXBContext getFeedContext() throws JAXBException {
        return getJAXBContext(new Feed());
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
