package com.michaelmiklavcic.falconer;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.io.*;
import java.util.Properties;

import javax.xml.bind.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

    private void generate(File mainConfig, File artifactDir, File outputDir) throws IOException, JAXBException, SAXException {
        outputDir.mkdirs();
        EntityConfig config = EntityConfigLoader.getInstance().load(mainConfig);

        String defaultFeedTemplate = getFeedLines(artifactDir, config);
        for (Mapping m : config.getFeedMappings()) {
            Properties props = null;
            if (isEmpty(config.getDefaultProperties())) {
                props = propertyBuilder.merge(new File(artifactDir, m.getPropertyFile()));
            } else {
                props = propertyBuilder.merge(new File(artifactDir, m.getPropertyFile()),
                                              new File(artifactDir, config.getDefaultProperties()));
            }
            String template = null;
            String filtered = null;
            EntityMerger entityMerger = null;
            if (isNotEmpty(m.getTemplate())) {
                template = FileUtils.readFileToString(new File(artifactDir, m.getTemplate()));
                filtered = tokenReplacer.apply(props, template);
                entityMerger = EntityMerger.create(filtered, defaultFeedTemplate);
            } else {
                filtered = tokenReplacer.apply(props, defaultFeedTemplate);
                entityMerger = EntityMerger.create(filtered);
            }
            Feed entity = (Feed) entityMerger.merge();
            marshall(entity, new File(outputDir, entity.getName() + ".xml"));
        }

        String defaultProcessTemplate = getProcessLines(artifactDir, config);
        for (Mapping m : config.getProcessMappings()) {
            Properties props = propertyBuilder.merge(new File(artifactDir, m.getPropertyFile()),
                                                     new File(artifactDir, config.getDefaultProperties()));
            String template = FileUtils.readFileToString(new File(artifactDir, m.getTemplate()));
            String filtered = tokenReplacer.apply(props, template);
            EntityMerger entityMerger = EntityMerger.create(filtered, defaultProcessTemplate);
            Process entity = (Process) entityMerger.merge();
            marshall(entity, new File(outputDir, entity.getName() + ".xml"));
        }
    }

    private String getFeedLines(File configDir, EntityConfig config) throws IOException {
        if (StringUtils.isNotBlank(config.getDefaultFeedTemplate())) {
            return FileUtils.readFileToString(new File(configDir, config.getDefaultFeedTemplate()));
        }
        return null;
    }

    private String getProcessLines(File configDir, EntityConfig config) throws IOException {
        if (StringUtils.isNotBlank(config.getDefaultProcessTemplate())) {
            return FileUtils.readFileToString(new File(configDir, config.getDefaultProcessTemplate()));
        }
        return null;
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
