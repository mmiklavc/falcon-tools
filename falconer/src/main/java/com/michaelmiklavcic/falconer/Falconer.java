package com.michaelmiklavcic.falconer;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.*;
import java.util.Properties;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;
import org.xml.sax.SAXException;

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

    public static void main(String[] args) throws IOException, JAXBException, SAXException {
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
    private void generate(File mainConfig, File artifactDir, File outputDir) throws IOException, JAXBException, SAXException {
        setup(outputDir);
        EntityConfig config = load(mainConfig);
        buildFeeds(artifactDir, outputDir, config);
        buildProcesses(artifactDir, outputDir, config);
    }

    private void buildFeeds(File artifactDir, File outputDir, EntityConfig config) throws FileNotFoundException, IOException {
        for (Mapping m : config.getFeedMappings()) {
            Properties props = resolveProperties(new File(artifactDir, m.getPropertyFile()),
                                                 new File(artifactDir, config.getDefaultProperties()));
            String defaultFeedTemplate = resolveTemplate(new File(artifactDir, config.getDefaultFeedTemplate()), props);
            String primaryFeedTemplate = resolveTemplate(new File(artifactDir, m.getTemplate()), props);
            Feed feed = (Feed) mergeTemplates(primaryFeedTemplate, defaultFeedTemplate);
            marshall(feed, new File(outputDir, feed.getName() + ".xml"));
        }
    }

    private void buildProcesses(File artifactDir, File outputDir, EntityConfig config) throws FileNotFoundException, IOException {
        for (Mapping m : config.getProcessMappings()) {
            Properties props = resolveProperties(new File(artifactDir, m.getPropertyFile()),
                                                 new File(artifactDir, config.getDefaultProperties()));
            String defaultProcessTemplate = resolveTemplate(new File(artifactDir, config.getDefaultProcessTemplate()), props);
            String primaryProcessTemplate = resolveTemplate(new File(artifactDir, m.getTemplate()), props);
            Process process = (Process) mergeTemplates(primaryProcessTemplate, defaultProcessTemplate);
            marshall(process, new File(outputDir, process.getName() + ".xml"));
        }
    }

    private void setup(File outputDir) {
        outputDir.mkdirs();
    }

    private EntityConfig load(File mainConfig) throws IOException {
        return EntityConfigLoader.getInstance().load(mainConfig);
    }

    private Properties resolveProperties(File propertyFile, File defaultPropertyFile) throws FileNotFoundException, IOException {
        if (propertyFile.isFile() && defaultPropertyFile.isFile()) {
            return propertyBuilder.merge(propertyFile, defaultPropertyFile);
        } else if (propertyFile.isFile()) {
            return propertyBuilder.merge(propertyFile);
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

    private Entity mergeTemplates(String primary, String defaults) {
        if (isNotEmpty(primary)) {
            return EntityMerger.create(primary, defaults).merge();
        } else {
            return EntityMerger.create(defaults).merge();
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
