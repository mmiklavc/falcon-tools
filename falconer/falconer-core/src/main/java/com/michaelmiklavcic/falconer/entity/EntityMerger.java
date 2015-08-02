package com.michaelmiklavcic.falconer.entity;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.ByteArrayInputStream;
import java.nio.charset.*;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;

import com.michaelmiklavcic.falconer.util.FalconerException;

public abstract class EntityMerger {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private JAXBContext jc;
    private String entity;
    private String prototype;

    protected EntityMerger(String entity, String prototype) {
        jc = createJAXBContext();
        this.entity = entity;
        this.prototype = prototype;
    }

    private JAXBContext createJAXBContext() {
        try {
            return JAXBContext.newInstance(Process.class, Feed.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Unable to create unmarshaller", e);
        }
    }

    protected JAXBContext getJAXBContext() {
        return jc;
    }

    public static EntityMerger create(String entity) {
        return EntityMerger.create(entity, null);
    }

    /**
     * Merges entity xml
     * 
     * @param entity values take precedence in merging
     * @param prototype provides defaults, but values in this entity will be overridden 
     * by the primary entity
     * @return
     */
    public static EntityMerger create(String entity, String prototype) {
        if (isEmpty(entity)) {
            throw new FalconerException("Primary entity cannot be null");
        }
        if (entity.contains("xmlns=\"uri:falcon:process:0.1\"")) {
            return new ProcessEntityMerger(entity, prototype);
        } else if (entity.contains("xmlns=\"uri:falcon:feed:0.1\"")) {
            return new FeedEntityMerger(entity, prototype);
        } else {
            throw new UnsupportedOperationException("Did not recognize entity type");
        }
    }

    public String getEntity() {
        return entity;
    }

    public String getDefaultTemplate() {
        return prototype;
    }

    // unchecked: pool of potential types is restricted
    @SuppressWarnings("unchecked")
    protected <T> T unmarshall(String entity) {
        if (entity == null || "".equals(entity)) {
            throw new FalconerException("Error unmarshalling entity (null or empty)");
        }
        try {
            return (T) jc.createUnmarshaller().unmarshal(new ByteArrayInputStream(entity.getBytes(CHARSET)));
        } catch (JAXBException e) {
            throw new RuntimeException("Unable to umarshall entity", e);
        }
    }

    public abstract Entity merge();

}
