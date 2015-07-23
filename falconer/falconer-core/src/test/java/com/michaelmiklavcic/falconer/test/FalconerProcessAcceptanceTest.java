package com.michaelmiklavcic.falconer.test;

import static java.lang.String.format;

import java.io.File;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class FalconerProcessAcceptanceTest {
    private ApplicationRunner application;
    private File testDir;
    private File inputDir;
    private File configDir;
    private File outDir;

    @Before
    public void setUp() throws Exception {
        application = new ApplicationRunner();
        testDir = TestUtils.createTempDir(getClass().getName());
        inputDir = new File(testDir, "input");
        configDir = new File(inputDir, "config");
        outDir = new File(testDir, "output");
    }

    /**
        {
         "pipeline" : "clickstream",
         "default-properties" : "default.properties",
         "default-process-template" : "default-process.xml",
         "process-mappings" : [
             { 
                 "property-file" : "processOne.properties",
                 "template" : "processOne.xml"
             }
          ]
        }
    */
    @Multiline private static String processOnlyConfig;

    /**
     * process.tags=env=prod,category=etl
     * process.clusterone.start=2014-03-26T05:00Z
     * process.clusterone.end=2015-03-26T05:00Z
     * process.workflow.engine=pig
     * process.acl.owner=test-user
     * process.acl.group=test-group
     * process.acl.permission=*
     */
    @Multiline private static String defaultProcessProps;

    /**
     * process.name=SnazzyProcess
     * process.clusterone.start=2014-05-26T05:00Z
     * process.clusterone.end=2015-03-26T05:00Z
     * process.clustertwo.start=2015-02-23T05:00Z
     * process.clustertwo.end=2016-02-23T05:00Z
     * process.clusterthree.start=2020-01-23T05:00Z
     * process.clusterthree.end=2020-01-23T05:00Z
     * process.timeout=hours(8)
     * process.workflow.engine=pig
     * process.input1.name=snapshotin
     * process.input1.feed=snapshot
     * process.input2.name=deltain
     * process.input2.feed=delta
     */
    @Multiline private static String processOneProps;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="ProcessTemplate" xmlns="uri:falcon:process:0.1">
     *  <tags>something=more</tags>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z"/>
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(16)</timeout>
     *  <frequency>days(7)</frequency>
     *  <timezone>UTC</timezone>
     * 
     *  <retry policy="periodic" delay="minutes(20)" attempts="3"/>
     *  <acl owner="test-user" group="test-group" permission="*"/>
     *  
     *</process>
     */
    @Multiline private static String defaultProcessTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="##process.name##" xmlns="uri:falcon:process:0.1">
     *  <tags>##process.tags##</tags>
     *  
     *  <clusters>
     *    <cluster name="cluster-two">
     *      <validity start="##process.clustertwo.start##" end="##process.clustertwo.end##" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="##process.clusterthree.start##" end="##process.clusterthree.end##"/>
     *    </cluster>
     *  </clusters>
     *  
     *  <timeout>##process.timeout##</timeout>
     *  
     *  <inputs>
     *    <input name="##process.input1.name##" feed="##process.input1.feed##" start="yesterday(5,0)" end="yesterday(5,0)"/>
     *    <input name="##process.input2.name##" feed="##process.input2.feed##" start="now(0,0)" end="now(0,0)"/>
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
    @Multiline private static String processOneTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="SnazzyProcess" xmlns="uri:falcon:process:0.1">
     *  <tags>something=more,env=prod,category=etl</tags>
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
     *  <frequency>days(7)</frequency>
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
    @Multiline private static String processOneMerged;

    @Test
    public void builds_process_from_templates_and_properties() throws Exception {
        TestUtils.write(new File(configDir, "default.properties"), defaultProcessProps);
        TestUtils.write(new File(configDir, "processOne.properties"), processOneProps);
        TestUtils.write(new File(configDir, "default-process.xml"), defaultProcessTemplate);
        TestUtils.write(new File(configDir, "processOne.xml"), processOneTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, processOnlyConfig);

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(1);
        application.matchesEntityOutput(processOneMerged);
    }

    /**
    {
     "pipeline" : "clickstream",
     "process-mappings" : [
         { 
             "property-file" : "processOne.properties",
             "template" : "processOne.xml"
         }
      ]
    }
    */
    @Multiline private static String processNoDefaultsConfig;

    /**
     *clusters.cluster1.name=cluster-one
     *clusters.cluster1.validity.start=2015-01-23T00:00Z
     *clusters.cluster1.validity.end=2016-01-23T00:00Z
     *process.name=FinalProcess
     *process.tags=key1=value1
     *process.parallel=1
     *process.order=FIFO
     *process.timeout=hours(8)
     *process.frequency=days(7)
     *process.timezone=UTC
     *process.input1.name=input1
     *process.input1.feed=feed-in1
     *process.output1.name=output1
     *process.output1.feed=feed-out1
     *process.workflow.script=snapshot.pig
     */
    @Multiline private static String processAllDefaultsProps;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="##process.name##" xmlns="uri:falcon:process:0.1">
     *  <tags>##process.tags##</tags>
     *  <clusters>
     *    <cluster name="##clusters.cluster1.name##">
     *      <validity start="##clusters.cluster1.validity.start##" end="##clusters.cluster1.validity.end##" />
     *    </cluster>
     *  </clusters>
     *  <parallel>##process.parallel##</parallel>
     *  <order>##process.order##</order>
     *  <timeout>##process.timeout##</timeout>
     *  <frequency>##process.frequency##</frequency>
     *  <timezone>##process.timezone##</timezone>
     *  <inputs>
     *    <input name="##process.input1.name##" feed="##process.input1.feed##" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="##process.output1.name##" feed="##process.output1.feed##" instance="now(0,0)"/>
     *  </outputs>
     *  <workflow engine="pig" path="/foo/bar/baz/##process.workflow.script##" />
     *  <retry policy="periodic" delay="minutes(20)" attempts="3"/>
     *</process>
     */
    @Multiline private static String processNoDefaultsTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="FinalProcess" xmlns="uri:falcon:process:0.1">
     *  <tags>key1=value1</tags>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(8)</timeout>
     *  <frequency>days(7)</frequency>
     *  <timezone>UTC</timezone>
     *  <inputs>
     *    <input name="input1" feed="feed-in1" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="output1" feed="feed-out1" instance="now(0,0)"/>
     *  </outputs>
     *  <workflow engine="pig" path="/foo/bar/baz/snapshot.pig" />
     *  <retry policy="periodic" delay="minutes(20)" attempts="3"/>
     *</process>
     */
    @Multiline private static String processNoDefaultsMerged;

    @Test
    public void builds_feed_from_templates_and_no_defaults() throws Exception {
        TestUtils.write(new File(configDir, "processOne.properties"), processAllDefaultsProps);
        TestUtils.write(new File(configDir, "processOne.xml"), processNoDefaultsTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, processNoDefaultsConfig);

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(1);
        application.matchesEntityOutput(processNoDefaultsMerged);
    }

    /**
    {
     "pipeline" : "clickstream",
     "default-properties" : "default-properties.properties",
     "default-process-template" : "default-process.xml",
     "process-mappings" : [
         "process1.properties",
         "process2.properties",
         "process3.properties",
         "process4.properties",
         "process5.properties"
      ]
    }
    */
    @Multiline private static String processAllDefaultsConfig;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="FinalProcess3" xmlns="uri:falcon:process:0.1">
     *  <tags>key1=value1,key3=value3</tags>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(8)</timeout>
     *  <frequency>days(7)</frequency>
     *  <timezone>UTC</timezone>
     *  <inputs>
     *    <input name="input1" feed="feed-in1" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="output1" feed="feed-out1" instance="now(0,0)"/>
     *  </outputs>
     *  <workflow engine="pig" path="/foo/bar/baz/snapshot3.pig" />
     *  <retry policy="periodic" delay="minutes(20)" attempts="3"/>
     *</process>
     */
    @Multiline private static String processThreeAllDefaultsMerged;

    @Test
    public void builds_process_from_only_default_template_and_defaults() throws Exception {
        TestUtils.write(new File(configDir, "default-properties.properties"), processAllDefaultsProps);
        TestUtils.write(new File(configDir, "default-process.xml"), processNoDefaultsTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, processAllDefaultsConfig);
        for (int i = 1; i <= 5; i++) {
            TestUtils.write(new File(configDir, format("process%s.properties", i)), new String[] {
                    "process.name=FinalProcess" + i,
                    "process.tags=key1=value1,key" + i + "=value" + i,
                    "process.workflow.script=snapshot" + i + ".pig" });
        }

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(5);
        application.matchesEntityOutput(processThreeAllDefaultsMerged);
    }

}
