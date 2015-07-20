package com.michaelmiklavcic.falconer.test.entity;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.entity.*;

public class EntityBuilderTest {

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="ProcessParent" xmlns="uri:falcon:process:0.1">
     *</process>
     */
    @Multiline private static String processParent;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="ProcessA" xmlns="uri:falcon:process:0.1">
     *</process>
     */
    @Multiline private static String processChild;

    @Test
    public void creates_process_builder_with_single_template() throws IOException, JAXBException, SAXException {
        assertThat(EntityBuilder.create(processChild), instanceOf(ProcessEntityBuilder.class));
    }

    @Test
    public void creates_process_builder_with_default_template() throws IOException, JAXBException, SAXException {
        assertThat(EntityBuilder.create(processChild, processParent), instanceOf(ProcessEntityBuilder.class));
    }

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed name="FeedParent" xmlns="uri:falcon:feed:0.1">
     *</feed>
     */
    @Multiline private static String feedParent;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed name="FeedA" xmlns="uri:falcon:feed:0.1">
     *</feed>
     */
    @Multiline private static String feedChild;

    @Test
    public void creates_feed_builder_with_single_template() throws IOException, JAXBException, SAXException {
        assertThat(EntityBuilder.create(feedChild), instanceOf(FeedEntityBuilder.class));
    }

    @Test
    public void creates_feed_builder_with_default_template() throws IOException, JAXBException, SAXException {
        assertThat(EntityBuilder.create(feedChild, feedParent), instanceOf(FeedEntityBuilder.class));
    }

}
