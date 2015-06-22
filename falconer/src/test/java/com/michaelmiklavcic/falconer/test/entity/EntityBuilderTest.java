package com.michaelmiklavcic.falconer.test.entity;

import static com.michaelmiklavcic.falconer.test.util.TestUtils.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.*;

import org.adrianwalker.multilinestring.Multiline;
import org.apache.falcon.entity.v0.process.Process;
import org.junit.*;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.entity.EntityBuilder;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class EntityBuilderTest {
    private JAXBContext jc;
    private File templates;

    @Before
    public void setUp() throws Exception {
        jc = JAXBContext.newInstance(Process.class);
        templates = new File("target/templates");
    }

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="ProcessParent" xmlns="uri:falcon:process:0.1">
     *  <tags>env=production,department=rx</tags>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z"/>
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z"/>
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(8)</timeout>
     *  <frequency>days(1)</frequency>
     *  <timezone>UTC</timezone>
     * 
     *  <retry policy="periodic" delay="minutes(20)" attempts="3"/>
     *  <acl owner="test-user" group="test-group" permission="*"/>
     *</process>
     */
    @Multiline private static String processParent;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="ProcessA" xmlns="uri:falcon:process:0.1">
     *  <tags>pipeline=etl</tags>
     *  
     *  <clusters>
     *    <cluster name="cluster-two">
     *      <validity start="2015-02-23T05:00Z" end="2016-02-23T05:00Z" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="2020-01-23T05:00Z" end="2020-01-23T05:00Z"/>
     *    </cluster>
     *  </clusters>
     *  
     *  <inputs>
     *    <input name="snapshotin" feed="snapshot" start="yesterday(5,0)" end="yesterday(5,0)"/>
     *    <input name="deltain" feed="delta" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *
     *  <outputs>
     *      <output name="snapshotout" feed="snapshot" instance="now(0,0)"/>
     *  </outputs>
     *
     *  <workflow engine="pig" path="/dev/apps/falcon/howto/gen_snapshot.pig" />
     *  
     *</process>
     */
    @Multiline private static String processChild;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="ProcessA" xmlns="uri:falcon:process:0.1">
     *  <tags>env=production,department=rx,pipeline=etl</tags>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *    </cluster>
     *    <cluster name="cluster-two">
     *      <validity start="2015-02-23T05:00Z" end="2016-02-23T05:00Z" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="2020-01-23T05:00Z" end="2020-01-23T05:00Z"/>
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(8)</timeout>
     *  <frequency>days(1)</frequency>
     *  <timezone>UTC</timezone>
     *  
     *  <inputs>
     *    <input name="snapshotin" feed="snapshot" start="yesterday(5,0)" end="yesterday(5,0)"/>
     *    <input name="deltain" feed="delta" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *
     *  <outputs>
     *      <output name="snapshotout" feed="snapshot" instance="now(0,0)"/>
     *  </outputs>
     *
     *  <workflow engine="pig" path="/dev/apps/falcon/howto/gen_snapshot.pig" />
     *  
     *  <retry policy="periodic" delay="minutes(20)" attempts="3"/>
     *</process>
     */
    @Multiline private static String processMerged;

    @Test
    public void builds_entity_from_hierarchy() throws IOException, JAXBException, SAXException {
        File processParentFile = new File(templates, "processParent.xml");
        File processChildFile = new File(templates, "processChild.xml");
        TestUtils.write(processParentFile, processParent);
        TestUtils.write(processChildFile, processChild);
        Process processActual = new EntityBuilder().merge(processParentFile, processChildFile);
        Process processExpected = TestUtils.unmarshallProcess(processMerged);

        assertEquals(processExpected, processActual);
    }

}
