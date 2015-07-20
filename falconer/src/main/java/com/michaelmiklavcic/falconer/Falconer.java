package com.michaelmiklavcic.falconer;

import java.io.*;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.entity.*;

public class Falconer {
 
    private PropertyBuilder propertyBuilder;
    private TokenReplacer tokenReplacer;

    public Falconer(PropertyBuilder propertyBuilder, TokenReplacer tokenReplacer) {
        this.propertyBuilder = propertyBuilder;
        this.tokenReplacer = tokenReplacer;
    }
    
    public static void main(String[] args) throws IOException, JAXBException, SAXException {
        final File mainConfig = new File(args[0]);
        final File configDir = new File(args[1]);
        final File outputDir = new File(args[2]);
        Falconer falconer = new Falconer(new PropertyBuilder(), new TokenReplacer());
        falconer.generate(mainConfig, configDir, outputDir);
    }
    
    private void generate(File mainConfig, File configDir, File outputDir) throws IOException, JAXBException, SAXException {
        outputDir.mkdirs();
        EntityConfig config = EntityConfigLoader.getInstance().load(mainConfig);
        String defaultFeedTemplate = getFeedLines(configDir, config);
        for (Mapping m : config.getFeedMappings()) {
            Properties props = propertyBuilder.merge(new File(configDir, m.getPropertyFile()), new File(configDir, config.getDefaultProperties()));
            String template = FileUtils.readFileToString(new File(configDir, m.getTemplate()));
            String filtered = tokenReplacer.apply(props, template);
            EntityBuilder entityBuilder = EntityBuilder.create(filtered, defaultFeedTemplate);
            Feed entity = (Feed) entityBuilder.build();
            marshall(entity, new File(outputDir, entity.getName() + ".xml"));
        }
        
        String defaultProcessTemplate = getProcessLines(configDir, config);
    }

    private String getFeedLines(File configDir, EntityConfig config) throws IOException {
        if(StringUtils.isNotBlank(config.getDefaultFeedTemplate())) {
            return FileUtils.readFileToString(new File(configDir, config.getDefaultFeedTemplate()));
        }
        return null;
    }

    private String getProcessLines(File configDir, EntityConfig config) throws IOException {
        if(StringUtils.isNotBlank(config.getDefaultProcessTemplate())) {
            return FileUtils.readFileToString(new File(configDir, config.getDefaultProcessTemplate()));
        }
        return null;
    }

    private void marshall(Process entity, File out) throws JAXBException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("process-0.1.xsd"));
        JAXBContext jc = JAXBContext.newInstance(Process.class); // TODO
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setSchema(schema);
        marshaller.marshal(entity, out);
    }
    
    private void marshall(Feed entity, File out) throws JAXBException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("feed-0.1.xsd"));
        JAXBContext jc = JAXBContext.newInstance(Feed.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setSchema(schema);
        marshaller.marshal(entity, out);
    }
    
//    private void marshall(Entity entity, File out) throws JAXBException {
//        JAXBContext jc = null;
//        Marshaller marshaller = null;
//        if(entity instanceof Process) {
//            marshallEn(entity);
//            jc = JAXBContext.newInstance(Process.class);
//            marshaller = jc.createMarshaller();
//            marshaller.setSchema();
//        } else if(entity instanceof Feed) {
//            jc = JAXBContext.newInstance(Feed.class);
//            marshaller = jc.createMarshaller();
//            marshaller.setSchema()
//        }
//        
//        marshaller.marshal(entity, out);
//    }
}
