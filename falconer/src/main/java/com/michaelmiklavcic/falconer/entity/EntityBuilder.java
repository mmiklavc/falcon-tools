package com.michaelmiklavcic.falconer.entity;

import java.io.ByteArrayInputStream;
import java.nio.charset.*;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;

import com.michaelmiklavcic.falconer.util.FalconerException;

public abstract class EntityBuilder {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private Unmarshaller unmarshaller;
//    private Marshaller marshaller;
    private String entity;
    private String defaultTemplate;

    protected EntityBuilder(Class<? extends Entity> clazz, String entity, String entityTemplate) {
        unmarshaller = createUnmarshaller(clazz);
//        marshaller = createMarshaller(clazz);
        this.entity = entity;
        this.defaultTemplate = entityTemplate;
    }

    private Unmarshaller createUnmarshaller(Class<? extends Entity> clazz) {
        try {
//            JAXBContext jc = JAXBContext.newInstance(clazz);
            JAXBContext jc = JAXBContext.newInstance(Process.class, Feed.class);
            return jc.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException("Unable to create unmarshaller for " + clazz, e);
        }
    }

//    private Marshaller createMarshaller(Class<? extends Entity> clazz) {
//        try {
//            JAXBContext jc = JAXBContext.newInstance(clazz);
//            return jc.createMarshaller();
//        } catch (JAXBException e) {
//            throw new RuntimeException("Unable to create marshaller for " + clazz, e);
//        }
//    }
//    
//    protected Marshaller getMarshaller() {
//        return marshaller;
//    }

    public static EntityBuilder create(String entity) {
        return EntityBuilder.create(entity, null);
    }

    /**
     * Merges entity xml
     * 
     * @param entity values take precedence in merging
     * @param defaultEntity provides defaults, but values in this entity will be overridden 
     * by the primary entity
     * @return
     */
    public static EntityBuilder create(String entity, String defaultEntity) {
        if (entity.contains("xmlns=\"uri:falcon:process:0.1\"")) {
            return new ProcessEntityBuilder(entity, defaultEntity);
        } else if (entity.contains("xmlns=\"uri:falcon:feed:0.1\"")) {
            return new FeedEntityBuilder(entity, defaultEntity);
        } else {
            throw new UnsupportedOperationException("Did not recognize entity type");
        }
    }

    public String getEntity() {
        return entity;
    }

    public String getDefaultTemplate() {
        return defaultTemplate;
    }

    // pool of potential types is restricted
    @SuppressWarnings("unchecked")
    protected <T> T unmarshall(String entity) {
        if (entity == null || "".equals(entity)) {
            throw new FalconerException("Error unmarshalling entity (null or empty)");
        }
        try {
            return (T) unmarshaller.unmarshal(new ByteArrayInputStream(entity.getBytes(CHARSET)));
        } catch (JAXBException e) {
            throw new RuntimeException("Unable to umarshall entity", e);
        }
    }

    public abstract Entity build();

}
