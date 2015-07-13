package com.michaelmiklavcic.falconer.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.*;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;
import org.xml.sax.SAXException;

import com.google.common.io.Files;
import com.michaelmiklavcic.falconer.Falconer;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class FalconerAcceptanceTest {
    private ApplicationRunner application;
    private File testDir;
    private File inputDir;
    private File templateDir;
    private File outDir;

    @Before
    public void setUp() throws Exception {
        application = new ApplicationRunner();
        testDir = TestUtils.createTempDir(getClass().getName());
        inputDir = new File(testDir, "input");
        templateDir = new File(inputDir, "templates");
        outDir = new File(testDir, "output");
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * process.tags=category=etl
     * process.clusterone.start=2014-03-26T05:00Z
     * process.clusterone.end=2015-03-26T05:00Z
     * process.parallel=1
     * process.order=FIFO
     * process.timeout=hours(8)
     * process.frequency=days(7)
     * process.timezone=UTC
     * process.workflow.engine=pig
     * process.acl.owner=test-user
     * process.acl.group=test-group
     * process.acl.permission=*
     */
    @Multiline
    private static String parentProps;

    /**
     * @template.name=
     * process.name=SnazzyProcess
     * process.tags=subcat=rx
     * process.clusterone.start=2014-05-26T05:00Z
     * process.clusterone.end=2015-03-26T05:00Z
     * process.parallel=1
     * process.order=FIFO
     * process.timeout=hours(3)
     * process.frequency=days(1)
     * process.timezone=UTC
     * process.workflow.engine=pig
     * process.input1.name=snapshotin
     * process.input1.feed=snapshot
     * process.input2.name=deltain
     * process.input2.feed=delta
     * 
     */
    @Multiline
    private static String childProps;
    
    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="ProcessParentTemplate" xmlns="uri:falcon:process:0.1">
     *  <tags>##process.tags##</tags>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="##process.clusterone.start##" end="##process.clusterone.end##"/>
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z"/>
     *    </cluster>
     *  </clusters>
     *  <parallel>##process.parallel##</parallel>
     *  <order>##process.order##</order>
     *  <timeout>##process.timeout##</timeout>
     *  <frequency>##process.frequency##</frequency>
     *  <timezone>##process.timezone##</timezone>
     * 
     *  <retry policy="periodic" delay="minutes(20)" attempts="3"/>
     *  <acl owner="##process.acl.owner##" group="##process.acl.group##" permission="##process.acl.permission##"/>
     *</process>
     */
    @Multiline private static String processParent;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="##process.name##" xmlns="uri:falcon:process:0.1">
     *  <tags>##process.tags##</tags>
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
     *    <input name="##process.input1.name##" feed="##process.input1.feed##" start="yesterday(5,0)" end="yesterday(5,0)"/>
     *    <input name="##process.input2.name" feed="##process.input2.feed##" start="now(0,0)" end="now(0,0)"/>
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
     *<process name="SnazzyProcess" xmlns="uri:falcon:process:0.1">
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
    public void builds_process_from_templates_and_properties() throws IOException, JAXBException, SAXException {
        TestUtils.write(new File(inputDir, "parentProcess.properties"), parentProps);
        TestUtils.write(new File(inputDir, "childProcess.properties"), childProps);
        TestUtils.write(new File(templateDir, "processParent.xml"), processParent);
        TestUtils.write(new File(templateDir, "processChild.xml"), processChild);
        application.run(inputDir, templateDir, outDir);
        application.outputsNumFiles(1);
        application.matchesProcessOutput(processMerged);
    }
    
    @Test
    public void testprops() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream("@hello=world".getBytes());
        Properties p = new Properties();
        p.load(is);
        is.close();
        assertThat("world", equalTo(p.getProperty("@hello")));
    }

}
