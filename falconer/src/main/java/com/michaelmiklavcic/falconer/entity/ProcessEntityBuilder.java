package com.michaelmiklavcic.falconer.entity;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.falcon.entity.v0.*;
import org.apache.falcon.entity.v0.process.*;
import org.apache.falcon.entity.v0.process.Process;
import org.apache.falcon.entity.v0.process.Properties;

public class ProcessEntityBuilder extends EntityBuilder {

    public ProcessEntityBuilder(String entity, String entityTemplate) {
        super(Process.class, entity, entityTemplate);
    }

    public Entity build() {
        if (getDefaultTemplate() == null) {
            return unmarshall(getEntity());
        }
        Process defaults = unmarshall(getDefaultTemplate());
        Process main = unmarshall(getEntity());
        Process merged = new Process();
        merged.setACL(merge_ACL(main, defaults));
        merged.setClusters(merge_clusters(main, defaults));
        merged.setFrequency(merge_frequency(main, defaults));
        merged.setInputs(merge_inputs(main, defaults));
        merged.setLateProcess(merge_lateProcess(main, defaults));
        merged.setName(merge_name(main, defaults));
        merged.setOrder(merge_order(main, defaults));
        merged.setOutputs(merge_outputs(main, defaults));
        merged.setParallel(merge_parallel(main, defaults));
        merged.setPipelines(merge_pipelines(main, defaults));
        merged.setProperties(merge_properties(main, defaults));
        merged.setRetry(merge_retry(main, defaults));
        merged.setSla(merge_sla(main, defaults));
        merged.setTags(merge_tags(main, defaults));
        merged.setTimeout(merge_timeout(main, defaults));
        merged.setTimezone(merge_timezone(main, defaults));
        merged.setWorkflow(merge_workflow(main, defaults));

        return merged;
    }

    private ACL merge_ACL(Process main, Process defaults) {
        return (ACL) binCond(main.getACL(), defaults.getACL());
    }

    private Object binCond(Object main, Object defaults) {
        if (main != null) {
            return main;
        }
        return defaults;
    }

    private Clusters merge_clusters(Process main, Process defaults) {
        Clusters defaultClusters = defaults.getClusters();
        Clusters mainClusters = main.getClusters();
        if(defaultClusters == null) {
            defaultClusters = new Clusters();
        }
        if(mainClusters == null) {
            mainClusters = new Clusters();
        }
        List<Cluster> merged = new ArrayList<Cluster>();
        merged.addAll(mainClusters.getClusters());
        for (Cluster de : defaultClusters.getClusters()) {
            boolean match = false;
            for (Cluster ma : mainClusters.getClusters()) {
                if (ma.getName().equals(de.getName())) {
                    match = true;
                }
            }
            if (!match) {
                merged.add(de);
            }
        }
        Clusters newClusters = new Clusters();
        newClusters.getClusters().addAll(merged);
        return newClusters;
    }

    private Frequency merge_frequency(Process main, Process defaults) {
        return (Frequency) binCond(main.getFrequency(), defaults.getFrequency());
    }

    private Inputs merge_inputs(Process main, Process defaults) {
        Inputs defaultInputs = defaults.getInputs();
        Inputs mainInputs = main.getInputs();
        if(defaultInputs == null) {
            defaultInputs = new Inputs();
        }
        if(mainInputs == null) {
            mainInputs = new Inputs();
        }
        List<Input> merged = new ArrayList<Input>();
        merged.addAll(mainInputs.getInputs());
        for (Input de : defaultInputs.getInputs()) {
            boolean match = false;
            for (Input ma : mainInputs.getInputs()) {
                if (ma.getName().equals(de.getName())) {
                    match = true;
                }
            }
            if (!match) {
                merged.add(de);
            }
        }
        Inputs newInputs = new Inputs();
        newInputs.getInputs().addAll(merged);
        return newInputs;
    }

    private LateProcess merge_lateProcess(Process main, Process defaults) {
        return (LateProcess) binCond(main.getLateProcess(), defaults.getLateProcess());
    }

    private String merge_name(Process main, Process defaults) {
        return (String) binCond(main.getName(), defaults.getName());
    }

    private ExecutionType merge_order(Process main, Process defaults) {
        return (ExecutionType) binCond(main.getOrder(), defaults.getOrder());
    }

    private Outputs merge_outputs(Process main, Process defaults) {
        Outputs defaultOutputs = defaults.getOutputs();
        Outputs mainOutputs = main.getOutputs();
        if(defaultOutputs == null) {
            defaultOutputs = new Outputs();
        }
        if(mainOutputs == null) {
            mainOutputs = new Outputs();
        }
        List<Output> merged = new ArrayList<Output>();
        merged.addAll(mainOutputs.getOutputs());
        for (Output de : defaultOutputs.getOutputs()) {
            boolean match = false;
            for (Output ma : mainOutputs.getOutputs()) {
                if (ma.getName().equals(de.getName())) {
                    match = true;
                }
            }
            if (!match) {
                merged.add(de);
            }
        }
        Outputs newOutputs = new Outputs();
        newOutputs.getOutputs().addAll(merged);
        return newOutputs;
    }

    private int merge_parallel(Process main, Process defaults) {
        if (main.getParallel() != 0) {
            return main.getParallel();
        }
        return defaults.getParallel();
    }

    private String merge_pipelines(Process main, Process defaults) {
        return (String) binCond(main.getPipelines(), defaults.getPipelines());
    }

    private Properties merge_properties(Process main, Process defaults) {
        Properties defaultProperties = defaults.getProperties();
        Properties mainProperties = main.getProperties();
        if(defaultProperties == null) {
            defaultProperties = new Properties();
        }
        if(mainProperties == null) {
            mainProperties = new Properties();
        }
        List<Property> merged = new ArrayList<Property>();
        merged.addAll(mainProperties.getProperties());
        for (Property de : defaultProperties.getProperties()) {
            boolean match = false;
            for (Property ma : mainProperties.getProperties()) {
                if (ma.getName().equals(de.getName())) {
                    match = true;
                }
            }
            if (!match) {
                merged.add(de);
            }
        }
        Properties newProperties = new Properties();
        newProperties.getProperties().addAll(merged);
        return newProperties;
    }

    private Retry merge_retry(Process main, Process defaults) {
        return (Retry) binCond(main.getRetry(), defaults.getRetry());
    }

    private Sla merge_sla(Process main, Process defaults) {
        return (Sla) binCond(main.getSla(), defaults.getSla());
    }

    private String merge_tags(Process main, Process defaults) {
        String defaultTags = isNotEmpty(defaults.getTags()) ? defaults.getTags() : "";
        String mainTags = isNotEmpty(main.getTags()) ? main.getTags() : "";
        if (isNotEmpty(defaultTags) && isNotEmpty(mainTags)) {
            return defaultTags.concat(",").concat(mainTags);
        } else if(isNotEmpty(defaultTags)) {
            return defaultTags;
        } else {
            return mainTags;
        }
    }

    private Frequency merge_timeout(Process main, Process defaults) {
        return (Frequency) binCond(main.getTimeout(), defaults.getTimeout());
    }

    private TimeZone merge_timezone(Process main, Process defaults) {
        return (TimeZone) binCond(main.getTimezone(), defaults.getTimezone());
    }

    private Workflow merge_workflow(Process main, Process defaults) {
        return (Workflow) binCond(main.getWorkflow(), defaults.getWorkflow());
    }

    // private void merge_clusters(Process merged, Process parent, Process child) {
    // Clusters clusters = new Clusters();
    // clusters.getClusters().addAll(parent.getClusters().getClusters());
    // for (Cluster cluster : child.getClusters().getClusters()) {
    // for (Cluster exc : clusters.getClusters()) {
    // if (exc.getName().equals(cluster.getName())) {
    // clusters.getClusters().remove(exc);
    // }
    // }
    // clusters.getClusters().add(cluster);
    // }
    // merged.setClusters(clusters);
    // }
    //

}
