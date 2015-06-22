package com.michaelmiklavcic.falconer.entity;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.*;

import org.apache.falcon.entity.v0.process.Process;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EntityValidator {
    private JAXBContext jc;
    
    public EntityValidator() throws JAXBException {
        jc = JAXBContext.newInstance(Process.class);
    }
    
    public void validateProcess(Process process) throws SAXException, JAXBException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("process-0.1.xsd"));
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setSchema(schema);
        marshaller.marshal(process, new DefaultHandler());
    }
}
