package com.michaelmiklavcic.falconer;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.*;
import java.util.Properties;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;

import com.michaelmiklavcic.falconer.entity.*;
import com.michaelmiklavcic.falconer.property.*;
import com.michaelmiklavcic.falconer.util.*;

public class Falconer {

    private PropertyMerger propertyBuilder;
    private TokenReplacer tokenReplacer;
    private JAXBContext jc;

    public Falconer(PropertyMerger propertyBuilder, TokenReplacer tokenReplacer, JAXBContext jc) {
        this.propertyBuilder = propertyBuilder;
        this.tokenReplacer = tokenReplacer;
        this.jc = jc;
    }

    public static void main(String[] args) throws JAXBException {
        final File mainConfig = new File(args[0]);
        final File artifactDir = new File(args[1]);
        final File outputDir = new File(args[2]);
        Falconer falconer = new Falconer(new PropertyMerger(), new TokenReplacer(), JAXBContext.newInstance(Process.class, Feed.class));
        falconer.generate(mainConfig, artifactDir, outputDir);
    }

    /**
     * resolve props
     * apply default template
     * apply concrete template
     * merge templates 
     */
    private void generate(File mainConfig, File artifactDir, File outputDir) {
        setup(outputDir);
        EntityConfig config = load(mainConfig);
        buildFeeds(artifactDir, outputDir, config);
        buildProcesses(artifactDir, outputDir, config);
    }

    private void buildFeeds(File artifactDir, File outputDir, EntityConfig config) {
        for (Mapping m : config.getFeedMappings()) {
            Properties props = resolveProperties(new File(artifactDir, m.getPropertyFile()),
                                                 new File(artifactDir, config.getDefaultProperties()));
            String feedPrototype = resolveTemplate(new File(artifactDir, config.getFeedPrototype()), props);
            String feedTemplate = resolveTemplate(new File(artifactDir, m.getTemplate()), props);
            Feed feed = (Feed) merge(feedTemplate, feedPrototype);
            marshall(feed, new File(outputDir, feed.getName() + ".xml"));
        }
    }

    private void buildProcesses(File artifactDir, File outputDir, EntityConfig config) {
        for (Mapping m : config.getProcessMappings()) {
            Properties props = resolveProperties(new File(artifactDir, m.getPropertyFile()),
                                                 new File(artifactDir, config.getDefaultProperties()));
            String processPrototype = resolveTemplate(new File(artifactDir, config.getProcessPrototype()), props);
            String processTemplate = resolveTemplate(new File(artifactDir, m.getTemplate()), props);
            Process process = (Process) merge(processTemplate, processPrototype);
            marshall(process, new File(outputDir, process.getName() + ".xml"));
        }
    }

    private void setup(File outputDir) {
        outputDir.mkdirs();
    }

    private EntityConfig load(File mainConfig) {
        try {
            return EntityConfigLoader.getInstance().load(mainConfig);
        } catch (IOException e) {
            throw new FalconerException("Failed to load config " + mainConfig, e);
        }
    }

    private Properties resolveProperties(File propertyFile, File defaultPropertyFile) {
        if (propertyFile.isFile() && defaultPropertyFile.isFile()) {
            try {
                return propertyBuilder.merge(propertyFile, defaultPropertyFile);
            } catch (IOException e) {
                throw new FalconerException("Failed to merge files", e);
            }
        } else if (propertyFile.isFile()) {
            try {
                return propertyBuilder.merge(propertyFile);
            } catch (IOException e) {
                throw new FalconerException("Failed to merge files", e);
            }
        } else {
            throw new FalconerException("Check main config - entity property file missing");
        }
    }

    private String resolveTemplate(File entityTemplate, Properties props) {
        if (!entityTemplate.isFile()) {
            return null;
        }
        try {
            return tokenReplacer.apply(props, entityTemplate);
        } catch (IOException e) {
            throw new FalconerException("Unable to token replace", e);
        }
    }

    private Entity merge(String template, String prototype) {
        if (isNotEmpty(template)) {
            return EntityMerger.create(template, prototype).merge();
        } else {
            return EntityMerger.create(prototype).merge();
        }
    }

    private void marshall(Process entity, File out) {
        marshall(MarshallerFactory.create(jc, entity), entity, out);
    }

    private void marshall(Marshaller marshaller, Entity entity, File out) {
        try {
            marshaller.marshal(entity, out);
        } catch (JAXBException e) {
            throw new FalconerException("Failed to marshall process: " + entity.getName(), e);
        }
    }

    private void marshall(Feed entity, File out) {
        marshall(MarshallerFactory.create(jc, entity), entity, out);
    }

}
