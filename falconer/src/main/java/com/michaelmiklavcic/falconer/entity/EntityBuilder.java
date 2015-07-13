package com.michaelmiklavcic.falconer.entity;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.*;

import org.apache.falcon.entity.v0.process.*;
import org.apache.falcon.entity.v0.process.Process;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EntityBuilder {

    public Process merge(File processParentFile, File processChildFile) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(Process.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Process parent = (Process) unmarshaller.unmarshal(processParentFile);
        Process child = (Process) unmarshaller.unmarshal(processChildFile);
        Process merged = new Process();
        merge_ACL(merged, parent, child);
        merge_clusters(merged, parent, child);
        merged.setFrequency(parent.getFrequency());
        merged.setInputs(child.getInputs());
        merged.setLateProcess(null);
        merged.setName(child.getName());
        merged.setOrder(parent.getOrder());
        merged.setOutputs(child.getOutputs());
        merged.setParallel(parent.getParallel());
        merged.setPipelines(null);
        merged.setProperties(null);
        merged.setRetry(parent.getRetry());
        merged.setSla(null);
        merge_tags(merged, parent, child);
        merged.setTimeout(parent.getTimeout());
        merged.setTimezone(parent.getTimezone());
        merged.setWorkflow(child.getWorkflow());

//        new EntityValidator().validateProcess(merged);

        return merged;
    }

    private void merge_clusters(Process merged, Process parent, Process child) {
        Clusters clusters = new Clusters();
        clusters.getClusters().addAll(parent.getClusters().getClusters());
        for (Cluster cluster : child.getClusters().getClusters()) {
            for (Cluster exc : clusters.getClusters()) {
                if (exc.getName().equals(cluster.getName())) {
                    clusters.getClusters().remove(exc);
                }
            }
            clusters.getClusters().add(cluster);
        }
        merged.setClusters(clusters);
    }

    private void merge_ACL(Process merged, Process parent, Process child) {
        merged.setACL(parent.getACL());
    }

    private void merge_tags(Process merged, Process parent, Process child) {
        final String delim = ",";
        merged.setTags(parent.getTags().concat(delim).concat(child.getTags()));
    }
}
