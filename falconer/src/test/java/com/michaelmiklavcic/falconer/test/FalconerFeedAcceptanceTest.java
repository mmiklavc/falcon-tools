package com.michaelmiklavcic.falconer.test;

import java.io.File;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class FalconerFeedAcceptanceTest {
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
         "default-feed-template" : "default-feed.xml",
         "feed-mappings" : [
             { 
                 "property-file" : "feedOne.properties",
                 "template" : "feedOne.xml"
             }
          ]
        }
    */
    @Multiline private static String feedOnlyConfig;

    /**
     * feed.tags=env=prod,category=etl
     * feed.clusterone.start=2014-03-26T05:00Z
     * feed.clusterone.end=2015-03-26T05:00Z
     * feed.workflow.engine=pig
     * feed.acl.owner=test-user
     * feed.acl.group=test-group
     * feed.acl.permission=*
     */
    @Multiline private static String defaultProcessProps;

    /**
     * feed.name=SnazzyProcess
     * feed.clusterone.start=2014-05-26T05:00Z
     * feed.clusterone.end=2015-03-26T05:00Z
     * feed.clustertwo.start=2015-02-23T05:00Z
     * feed.clustertwo.end=2016-02-23T05:00Z
     * feed.clusterthree.start=2020-01-23T05:00Z
     * feed.clusterthree.end=2020-01-23T05:00Z
     * feed.timeout=hours(8)
     * feed.workflow.engine=pig
     * feed.input1.name=snapshotin
     * feed.input1.feed=snapshot
     * feed.input2.name=deltain
     * feed.input2.feed=delta
     */
    @Multiline private static String feedOneProps;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed name="ProcessTemplate" xmlns="uri:falcon:feed:0.1">
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
     *</feed>
     */
    @Multiline private static String defaultProcessTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed name="##feed.name##" xmlns="uri:falcon:feed:0.1">
     *  <tags>##feed.tags##</tags>
     *  
     *  <clusters>
     *    <cluster name="cluster-two">
     *      <validity start="##feed.clustertwo.start##" end="##feed.clustertwo.end##" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="##feed.clusterthree.start##" end="##feed.clusterthree.end##"/>
     *    </cluster>
     *  </clusters>
     *  
     *  <timeout>##feed.timeout##</timeout>
     *  
     *  <inputs>
     *    <input name="##feed.input1.name##" feed="##feed.input1.feed##" start="yesterday(5,0)" end="yesterday(5,0)"/>
     *    <input name="##feed.input2.name##" feed="##feed.input2.feed##" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *
     *  <outputs>
     *      <output name="snapshotout" feed="snapshot" instance="now(0,0)"/>
     *  </outputs>
     *
     *  <workflow engine="pig" path="/dev/apps/falcon/howto/gen_snapshot.pig" />
     *  
     *</feed>
     */
    @Multiline private static String feedOneTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed name="SnazzyProcess" xmlns="uri:falcon:feed:0.1">
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
     *</feed>
     */
    @Multiline private static String feedOneMerged;

    @Test
    public void builds_feed_from_templates_and_properties() throws Exception {
        TestUtils.write(new File(configDir, "default.properties"), defaultProcessProps);
        TestUtils.write(new File(configDir, "feedOne.properties"), feedOneProps);
        TestUtils.write(new File(configDir, "default-feed.xml"), defaultProcessTemplate);
        TestUtils.write(new File(configDir, "feedOne.xml"), feedOneTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, feedOnlyConfig);

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(1);
        application.matchesProcessOutput(feedOneMerged);
    }

}
