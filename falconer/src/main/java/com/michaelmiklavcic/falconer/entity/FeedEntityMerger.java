package com.michaelmiklavcic.falconer.entity;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.util.*;

import org.apache.falcon.entity.v0.*;
import org.apache.falcon.entity.v0.feed.*;
import org.apache.falcon.entity.v0.feed.Properties;

public class FeedEntityMerger extends EntityMerger {

    public FeedEntityMerger(String entity, String entityTemplate) {
        super(entity, entityTemplate);
    }

    @Override
    public Entity merge() {
        if (getDefaultTemplate() == null) {
            return unmarshall(getEntity());
        }
        Feed defaults = unmarshall(getDefaultTemplate());
        Feed main = unmarshall(getEntity());
        Feed merged = new Feed();
        merged.setACL(merge_ACL(main, defaults));
        merged.setAvailabilityFlag(merge_AvailabilityFlag(main, defaults));
        merged.setClusters(merge_Clusters(main, defaults));
        merged.setDescription(merge_Description(main, defaults));
        merged.setFrequency(merge_Frequency(main, defaults));
        merged.setGroups(merge_Groups(main, defaults));
        merged.setLateArrival(merge_LateArrival(main, defaults));
        merged.setLocations(merge_Locations(main, defaults));
        merged.setName(merge_Name(main, defaults));
        merged.setPartitions(merge_Partitions(main, defaults));
        merged.setProperties(merge_Properties(main, defaults));
        merged.setSchema(merge_Schema(main, defaults));
        merged.setSla(merge_Sla(main, defaults));
        merged.setTable(merge_Table(main, defaults));
        merged.setTags(merge_Tags(main, defaults));
        merged.setTimezone(merge_Timezone(main, defaults));
        return merged;
    }

    private ACL merge_ACL(Feed main, Feed defaults) {
        return (ACL) binCond(main.getACL(), defaults.getACL());
    }

    private Object binCond(Object main, Object defaults) {
        if (main != null) {
            return main;
        }
        return defaults;
    }

    private String merge_AvailabilityFlag(Feed main, Feed defaults) {
        return (String) binCond(main.getAvailabilityFlag(), defaults.getAvailabilityFlag());
    }

    private Clusters merge_Clusters(Feed main, Feed defaults) {
        Clusters defaultClusters = defaults.getClusters();
        Clusters mainClusters = main.getClusters();
        if (defaultClusters == null) {
            defaultClusters = new Clusters();
        }
        if (mainClusters == null) {
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

    private String merge_Description(Feed main, Feed defaults) {
        return (String) binCond(main.getDescription(), defaults.getDescription());
    }

    private Frequency merge_Frequency(Feed main, Feed defaults) {
        return (Frequency) binCond(main.getFrequency(), defaults.getFrequency());
    }

    private String merge_Groups(Feed main, Feed defaults) {
        return (String) binCond(main.getGroups(), defaults.getGroups());
    }

    private LateArrival merge_LateArrival(Feed main, Feed defaults) {
        return (LateArrival) binCond(main.getLateArrival(), defaults.getLateArrival());
    }

    private Locations merge_Locations(Feed main, Feed defaults) {
        Locations defaultLocations = defaults.getLocations();
        Locations mainLocations = main.getLocations();
        if (defaultLocations == null) {
            defaultLocations = new Locations();
        }
        if (mainLocations == null) {
            mainLocations = new Locations();
        }
        List<Location> merged = new ArrayList<Location>();
        merged.addAll(mainLocations.getLocations());
        for (Location de : defaultLocations.getLocations()) {
            boolean match = false;
            for (Location ma : mainLocations.getLocations()) {
                if (ma.getPath().equals(de.getPath())) {
                    match = true;
                }
            }
            if (!match) {
                merged.add(de);
            }
        }
        if (merged.size() != 0) {
            Locations newLocations = new Locations();
            newLocations.getLocations().addAll(merged);
            return newLocations;
        } else {
            return null;
        }
    }

    private String merge_Name(Feed main, Feed defaults) {
        return (String) binCond(main.getName(), defaults.getName());
    }

    private Partitions merge_Partitions(Feed main, Feed defaults) {
        Partitions defaultPartitions = defaults.getPartitions();
        Partitions mainPartitions = main.getPartitions();
        if (defaultPartitions == null) {
            defaultPartitions = new Partitions();
        }
        if (mainPartitions == null) {
            mainPartitions = new Partitions();
        }
        List<Partition> merged = new ArrayList<Partition>();
        merged.addAll(mainPartitions.getPartitions());
        for (Partition de : defaultPartitions.getPartitions()) {
            boolean match = false;
            for (Partition ma : mainPartitions.getPartitions()) {
                if (ma.getName().equals(de.getName())) {
                    match = true;
                }
            }
            if (!match) {
                merged.add(de);
            }
        }
        if (merged.size() != 0) {
            Partitions newPartitions = new Partitions();
            newPartitions.getPartitions().addAll(merged);
            return newPartitions;
        } else {
            return null;
        }
    }

    private Properties merge_Properties(Feed main, Feed defaults) {
        Properties defaultProperties = defaults.getProperties();
        Properties mainProperties = main.getProperties();
        if (defaultProperties == null) {
            defaultProperties = new Properties();
        }
        if (mainProperties == null) {
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
        if (merged.size() != 0) {
            Properties newProperties = new Properties();
            newProperties.getProperties().addAll(merged);
            return newProperties;
        } else {
            return null;
        }
    }

    private Schema merge_Schema(Feed main, Feed defaults) {
        return (Schema) binCond(main.getSchema(), defaults.getSchema());
    }

    private Sla merge_Sla(Feed main, Feed defaults) {
        return (Sla) binCond(main.getSla(), defaults.getSla());
    }

    private CatalogTable merge_Table(Feed main, Feed defaults) {
        return (CatalogTable) binCond(main.getTable(), defaults.getTable());
    }

    private String merge_Tags(Feed main, Feed defaults) {
        String defaultTags = isNotEmpty(defaults.getTags()) ? defaults.getTags() : "";
        String mainTags = isNotEmpty(main.getTags()) ? main.getTags() : "";
        if (isNotEmpty(defaultTags) && isNotEmpty(mainTags)) {
            return defaultTags.concat(",").concat(mainTags);
        } else if (isNotEmpty(defaultTags)) {
            return defaultTags;
        } else {
            return mainTags;
        }
    }

    private TimeZone merge_Timezone(Feed main, Feed defaults) {
        return (TimeZone) binCond(main.getTimezone(), defaults.getTimezone());
    }

}
