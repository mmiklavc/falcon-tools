package com.michaelmiklavcic.falconer.test;

import java.io.File;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class FalconerFullAcceptanceTest {
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
         "feed-prototype" : "feed-prototype.xml",
         "process-prototype" : "process-prototype.xml",
         "process-mappings" : [
             {
                 "property-file" : "process.properties",
                 "template" : "process.xml",
                 "merge-strategy" : "merge"
             }
          ],
          "feed-mappings" : [
             { 
                 "property-file" : "feed.properties",
                 "template" : "feed.xml"
             }
          ]
        }
    */
    @Multiline private static String config;

    /**
     * clusters.clustertwo.name=cluster-two
     * clusters.clustertwo.validity.start=2014-03-26T05:00Z
     * clusters.clustertwo.validity.end=2015-03-26T05:00Z
     * clusters.clustertwo.retention.limit=months(9999)
     * 
     * process.tags=env=prod,category=etl
     * process.workflow.engine=pig
     * process.acl.owner=test-user
     * process.acl.group=test-group
     * process.acl.permission=*
     * 
     * feed.tags=key2=value2
     * feed.acl.owner=test
     * feed.acl.group=test
     * feed.acl.permission=*
     * 
     */
    @Multiline private static String defaultProps;

    /**
     * clusters.clusterthree.validity.start=2020-01-23T05:00Z
     * clusters.clusterthree.validity.end=2020-01-23T05:00Z
     * 
     * process.name=SnazzyProcess
     * process.timeout=hours(8)
     * process.workflow.engine=pig
     * process.input1.name=snapshotin
     * process.input1.feed=snapshot
     * process.input2.name=deltain
     * process.input2.feed=delta
     */
    @Multiline private static String processProps;

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
    @Multiline private static String processPrototype;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="##process.name##" xmlns="uri:falcon:process:0.1">
     *  <tags>##process.tags##</tags>
     *  
     *  <clusters>
     *    <cluster name="##clusters.clustertwo.name##">
     *      <validity start="##clusters.clustertwo.validity.start##" end="##clusters.clustertwo.validity.end##" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="##clusters.clusterthree.validity.start##" end="##clusters.clusterthree.validity.end##"/>
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
    @Multiline private static String processTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<process name="SnazzyProcess" xmlns="uri:falcon:process:0.1">
     *  <tags>something=more,env=prod,category=etl</tags>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *    </cluster>
     *    <cluster name="cluster-two">
     *      <validity start="2014-03-26T05:00Z" end="2015-03-26T05:00Z" />
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
    @Multiline private static String processMerged;

    /**
     *clusters.clustertwo.delay=days(2)
     *clusters.clustertwo.partition=part2
     *clusters.clustertwo.location1.path=/foo/bar/baz
     *clusters.clustertwo.location2.path=/foo/bar/boo
     *
     *feed.description=Feed One
     *feed.name=feedOneMerged
     *feed.path=/foo/bar
     *feed.cutoff=days(2)
     */
    @Multiline private static String feedProps;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Template" name="feedTemplate" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1</tags>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="/foo/bar" type="data"/>
     *  </locations>
     *  <ACL group="basegroup" owner="baseowner" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *</feed>
     */
    @Multiline private static String feedPrototype;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="##feed.description##" name="##feed.name##" xmlns="uri:falcon:feed:0.1">
     *  <tags>##feed.tags##</tags>
     *  <frequency>##feed.frequency##</frequency>
     *  <timezone>##feed.timezone##</timezone>
     *  <late-arrival cut-off="##feed.cutoff##"/>
     *  <clusters>
     *    <cluster delay="##clusters.clustertwo.delay##" name="##clusters.clustertwo.name##" partition="##clusters.clustertwo.partition##" type="target">
     *      <validity start="##clusters.clustertwo.validity.start##" end="##clusters.clustertwo.validity.end##" />
     *      <retention action="archive" limit="##clusters.clustertwo.retention.limit##" type="instance"/>
     *      <locations>
     *        <location path="##clusters.clustertwo.location1.path##" type="data"/>
     *        <location path="##clusters.clustertwo.location2.path##" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="##feed.path##" type="data"/>
     *  </locations>
     *  <ACL group="##feed.acl.group##" owner="##feed.acl.owner##" permission="##feed.acl.permission##"/>
     *</feed>
     */
    @Multiline private static String feedTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed One" name="feedOneMerged" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>PDT</timezone>
     *  <late-arrival cut-off="days(2)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *    </cluster>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2014-03-26T05:00Z" end="2015-03-26T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <locations>
     *        <location path="/foo/bar/baz" type="data"/>
     *        <location path="/foo/bar/boo" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="/foo/bar" type="data"/>
     *  </locations>
     *  <ACL group="test" owner="test" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *</feed>
     */
    @Multiline private static String feedMerged;

    @Test
    public void builds_entities_from_templates_and_properties() throws Exception {
        TestUtils.write(new File(configDir, "default.properties"), defaultProps);
        TestUtils.write(new File(configDir, "process.properties"), processProps);
        TestUtils.write(new File(configDir, "feed.properties"), feedProps);
        TestUtils.write(new File(configDir, "process-prototype.xml"), processPrototype);
        TestUtils.write(new File(configDir, "feed-prototype.xml"), feedPrototype);
        TestUtils.write(new File(configDir, "process.xml"), processTemplate);
        TestUtils.write(new File(configDir, "feed.xml"), feedTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, config);

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(2);
        application.matchesEntityOutput(processMerged);
        application.matchesEntityOutput(feedMerged);
    }

}
