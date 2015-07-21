package com.michaelmiklavcic.falconer.test.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.apache.falcon.entity.v0.Frequency;
import org.apache.falcon.entity.v0.process.*;
import org.apache.falcon.entity.v0.process.Process;
import org.apache.falcon.entity.v0.process.Properties;
import org.hamcrest.*;

public class ProcessMatcher extends TypeSafeMatcher<Process> {

    private Process operand;

    public ProcessMatcher(Process operand) {
        this.operand = operand;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(operand);
    }

    @Override
    protected boolean matchesSafely(Process item) {
        assertEquals(operand, item);
        return true;
    }

    private void assertEquals(Process expected, Process actual) {
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
        assertEquals(expected.getProperties(), actual.getProperties());
        assertEquals(expected.getRetry(), actual.getRetry());
        assertEquals(expected.getSla(), actual.getSla());
        Assert.assertEquals(expected.getTags(), actual.getTags());
        assertEquals(expected.getTimeout(), actual.getTimeout());
        assertEquals(expected.getTimezone(), actual.getTimezone());
        assertEquals(expected.getWorkflow(), actual.getWorkflow());
    }

    private void assertEquals(LateProcess expected, LateProcess actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            assertEquals(expected.getDelay(), actual.getDelay());
            assertEqualsLateInputs(expected.getLateInputs(), actual.getLateInputs());
            Assert.assertEquals(expected.getPolicy(), actual.getPolicy());
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private void assertEqualsLateInputs(List<LateInput> expected, List<LateInput> actual) {
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

    private Map<String, LateInput> asSortedLateInputs(List<LateInput> lateInputs) {
        Map<String, LateInput> li = new TreeMap<String, LateInput>();
        for (LateInput l : lateInputs) {
            li.put(l.getInput(), l);
        }
        return li;
    }

    private void assertEquals(Retry expected, Retry actual) {
        Assert.assertEquals(expected.getAttempts(), actual.getAttempts());
        Assert.assertEquals(expected.getDelay(), actual.getDelay());
        Assert.assertEquals(expected.getPolicy(), actual.getPolicy());
    }

    private void assertEquals(Sla expected, Sla actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            Assert.assertEquals(expected.getShouldEndIn(), actual.getShouldEndIn());
            Assert.assertEquals(expected.getShouldStartIn(), actual.getShouldStartIn());
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private void assertEquals(Frequency expected, Frequency actual) {
        Assert.assertEquals(expected.getFrequency(), actual.getFrequency());
        Assert.assertEquals(expected.getFrequencyAsInt(), actual.getFrequencyAsInt());
        Assert.assertEquals(expected.getTimeUnit(), actual.getTimeUnit());
    }

    private void assertEquals(TimeZone expected, TimeZone actual) {
        Assert.assertEquals(expected.getID(), actual.getID());
    }

    private void assertEquals(Workflow expected, Workflow actual) {
        Assert.assertEquals(expected.getLib(), actual.getLib());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getPath(), actual.getPath());
        Assert.assertEquals(expected.getVersion(), actual.getVersion());
        Assert.assertEquals(expected.getEngine(), actual.getEngine());
    }

    private void assertEquals(ACL expected, ACL actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            assertThat(actual.getGroup(), equalTo(expected.getGroup()));
            assertThat(actual.getOwner(), equalTo(expected.getOwner()));
            assertThat(actual.getPermission(), equalTo(expected.getPermission()));
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private void assertEquals(Clusters expected, Clusters actual) {
        Map<String, Cluster> ec = asSortedClusters(expected.getClusters());
        Map<String, Cluster> ac = asSortedClusters(actual.getClusters());
        assertThat(ec.size(), equalTo(ac.size()));
        for (Entry<String, Cluster> entry : ec.entrySet()) {
            Cluster expectedCluster = entry.getValue();
            Cluster actualCluster = ac.get(entry.getKey());
            assertThat(expectedCluster.getName(), expectedCluster.getName(), equalTo(actualCluster.getName()));
            assertThat(expectedCluster.getName(), expectedCluster.getValidity().getStart(), equalTo(actualCluster.getValidity().getStart()));
            assertThat(expectedCluster.getName(), expectedCluster.getValidity().getEnd(), equalTo(actualCluster.getValidity().getEnd()));
        }
    }

    private Map<String, Cluster> asSortedClusters(List<Cluster> clusters) {
        Map<String, Cluster> cl = new TreeMap<String, Cluster>();
        for (Cluster c : clusters) {
            cl.put(c.getName(), c);
        }
        return cl;
    }

    private void assertEquals(Inputs expected, Inputs actual) {
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

    private Map<String, Input> asSortedInputs(List<Input> inputs) {
        Map<String, Input> in = new TreeMap<String, Input>();
        for (Input i : inputs) {
            in.put(i.getName(), i);
        }
        return in;
    }

    private void assertEquals(Outputs expected, Outputs actual) {
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

    private Map<String, Output> asSortedOutputs(List<Output> outputs) {
        Map<String, Output> in = new TreeMap<String, Output>();
        for (Output i : outputs) {
            in.put(i.getName(), i);
        }
        return in;
    }

    private void assertEquals(Properties expected, Properties actual) {
        if (expected != null) {
            Map<String, Property> ec = asSortedProperties(expected.getProperties());
            Map<String, Property> ac = asSortedProperties(actual.getProperties());
            assertThat(ec.size(), equalTo(ac.size()));
            for (Entry<String, Property> entry : ec.entrySet()) {
                Property expectedProperty = entry.getValue();
                Property actualProperty = ac.get(entry.getKey());
                assertThat(expectedProperty.getName(), equalTo(actualProperty.getName()));
                assertThat(expectedProperty.getValue(), equalTo(actualProperty.getValue()));
            }
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private Map<String, Property> asSortedProperties(List<Property> properties) {
        Map<String, Property> in = new TreeMap<String, Property>();
        for (Property i : properties) {
            in.put(i.getName(), i);
        }
        return in;
    }

}
