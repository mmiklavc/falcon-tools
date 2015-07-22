package com.michaelmiklavcic.falconer.entity;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.*;

import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.util.FalconerException;

public class MarshallerFactory {

    private static final String PROCESS_SCHEMA = "process-0.1.xsd";
    private static final String FEED_SCHEMA = "feed-0.1.xsd";

    public static Marshaller create(JAXBContext jc, Process entity) {
        return create(jc, PROCESS_SCHEMA);
    }

    private static Marshaller create(JAXBContext jc, String schemaName) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        Marshaller marshaller = null;
        try {
            schema = schemaFactory.newSchema(MarshallerFactory.class.getClassLoader().getResource(schemaName));
            marshaller = jc.createMarshaller();
        } catch (SAXException | JAXBException e) {
            throw new FalconerException("Unable to create unmarshaller", e);
        }
        marshaller.setSchema(schema);
        return marshaller;
    }

    public static Marshaller create(JAXBContext jc, Feed entity) {
        return create(jc, FEED_SCHEMA);
    }

}
