package com.michaelmiklavcic.falconer.test.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.apache.falcon.entity.v0.Frequency;
import org.apache.falcon.entity.v0.feed.*;
import org.apache.falcon.entity.v0.feed.Properties;
import org.hamcrest.*;

public class FeedMatcher extends TypeSafeMatcher<Feed> {

    private Feed operand;

    public FeedMatcher(Feed operand) {
        this.operand = operand;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(operand);
    }

    @Override
    protected boolean matchesSafely(Feed item) {
        assertEquals(operand, item);
        return true;
    }

    private void assertEquals(Feed expected, Feed actual) {
        assertEquals(expected.getACL(), actual.getACL());
        Assert.assertEquals(expected.getAvailabilityFlag(), actual.getAvailabilityFlag());
        assertEquals(expected.getClusters(), actual.getClusters());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getFrequency(), actual.getFrequency());
        Assert.assertEquals(expected.getGroups(), actual.getGroups());
        assertEquals(expected.getLateArrival(), actual.getLateArrival());
        assertEquals(expected.getLocations(), actual.getLocations());
        Assert.assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPartitions(), actual.getPartitions());
        assertEquals(expected.getProperties(), actual.getProperties());
        assertEquals(expected.getSchema(), actual.getSchema());
        assertEquals(expected.getSla(), actual.getSla());
        assertEquals(expected.getTable(), actual.getTable());
        Assert.assertEquals(expected.getTags(), actual.getTags());
        assertEquals(expected.getTimezone(), actual.getTimezone());
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

    private void assertEquals(Frequency expected, Frequency actual) {
        Assert.assertEquals(expected.getFrequency(), actual.getFrequency());
        Assert.assertEquals(expected.getFrequencyAsInt(), actual.getFrequencyAsInt());
        Assert.assertEquals(expected.getTimeUnit(), actual.getTimeUnit());
    }

    private void assertEquals(LateArrival expected, LateArrival actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            assertEquals(expected.getCutOff(), actual.getCutOff());
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private void assertEquals(Locations expected, Locations actual) {
        Map<String, Location> el = asSortedLocations(expected.getLocations());
        Map<String, Location> al = asSortedLocations(actual.getLocations());
        assertThat(el.size(), equalTo(al.size()));
        for (Entry<String, Location> entry : el.entrySet()) {
            Location expectedLocation = entry.getValue();
            Location actualLocation = al.get(entry.getKey());
            assertThat(expectedLocation.getPath(), equalTo(actualLocation.getPath()));
            assertThat(expectedLocation.getType(), equalTo(actualLocation.getType()));
        }
    }

    private Map<String, Location> asSortedLocations(List<Location> locations) {
        Map<String, Location> sorted = new TreeMap<String, Location>();
        for (Location l : locations) {
            sorted.put(l.getPath(), l);
        }
        return sorted;
    }

    private void assertEquals(Partitions expected, Partitions actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            Map<String, Partition> e = asSortedPartitions(expected.getPartitions());
            Map<String, Partition> a = asSortedPartitions(actual.getPartitions());
            assertThat(e.size(), equalTo(a.size()));
            for (Entry<String, Partition> entry : e.entrySet()) {
                Partition expectedPartition = entry.getValue();
                Partition actualPartition = a.get(entry.getKey());
                assertThat(expectedPartition.getName(), equalTo(actualPartition.getName()));
            }
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private Map<String, Partition> asSortedPartitions(List<Partition> partitions) {
        Map<String, Partition> sorted = new TreeMap<String, Partition>();
        for (Partition p : partitions) {
            sorted.put(p.getName(), p);
        }
        return sorted;
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

    private void assertEquals(Schema expected, Schema actual) {
        Assert.assertEquals(expected.getLocation(), actual.getLocation());
        Assert.assertEquals(expected.getProvider(), actual.getProvider());
    }

    private void assertEquals(Sla expected, Sla actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            Assert.assertEquals(expected.getSlaHigh(), actual.getSlaHigh());
            Assert.assertEquals(expected.getSlaLow(), actual.getSlaLow());
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private void assertEquals(CatalogTable expected, CatalogTable actual) {
        if (expected != null) {
            assertThat(actual, is(notNullValue()));
            Assert.assertEquals(expected.getUri(), actual.getUri());
        } else {
            assertThat(actual, is(nullValue()));
        }
    }

    private void assertEquals(TimeZone expected, TimeZone actual) {
        Assert.assertEquals("timezone", expected.getID(), actual.getID());
    }

}
