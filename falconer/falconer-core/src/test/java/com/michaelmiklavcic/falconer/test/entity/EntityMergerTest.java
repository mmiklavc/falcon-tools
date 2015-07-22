package com.michaelmiklavcic.falconer.test.entity;

import static com.michaelmiklavcic.falconer.test.util.TestUtils.assertException;
import static com.michaelmiklavcic.falconer.test.util.TypedVal.list;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.entity.*;
import com.michaelmiklavcic.falconer.util.FalconerException;

public class EntityMergerTest {

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
        assertThat(EntityMerger.create(processChild), instanceOf(ProcessEntityMerger.class));
    }

    @Test
    public void creates_process_builder_with_default_template() throws IOException, JAXBException, SAXException {
        assertThat(EntityMerger.create(processChild, processParent), instanceOf(ProcessEntityMerger.class));
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
        assertThat(EntityMerger.create(feedChild), instanceOf(FeedEntityMerger.class));
    }

    @Test
    public void creates_feed_builder_with_default_template() throws IOException, JAXBException, SAXException {
        assertThat(EntityMerger.create(feedChild, feedParent), instanceOf(FeedEntityMerger.class));
    }

    @Test
    public void throws_exception_on_empty_primary_entity() throws Exception {
        assertException(FalconerException.class, EntityMerger.class, "create", list(String.class, ""));
        assertException(FalconerException.class, EntityMerger.class, "create", list(String.class, null));
    }

}
