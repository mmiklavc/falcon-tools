package com.michaelmiklavcic.falconer.test;

import static java.lang.String.format;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import org.adrianwalker.multilinestring.Multiline;
import org.apache.falcon.entity.v0.feed.Feed;
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
     *feed.tags=key2=value2
     *feed.frequency=hours(8)
     *feed.timezone=PDT
     *feed.cutoff=days(1)
     *feed.cluster-two.delay=days(5)
     *feed.cluster-two.name=cluster-two
     *feed.cluster-two.validity.start=2015-01-23T05:00Z
     *feed.cluster-two.validity.end=2016-01-23T05:00Z
     *feed.cluster-two.retention.limit=months(9999)
     *feed.group=test
     *feed.owner=test
     *feed.permission=*
     */
    @Multiline private static String defaultFeedProps;

    /**
     *feed.description=Feed One
     *feed.name=feedOneMerged
     *feed.cluster-two.delay=days(2)
     *feed.cluster-two.partition=part2
     *feed.cluster-two.location1.path=/foo/bar/baz
     *feed.cluster-two.location2.path=/foo/bar/boo
     *feed.path=/foo/bar
     */
    @Multiline private static String feedOneProps;

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
    @Multiline private static String defaultFeedTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="##feed.description##" name="##feed.name##" xmlns="uri:falcon:feed:0.1">
     *  <tags>##feed.tags##</tags>
     *  <frequency>##feed.frequency##</frequency>
     *  <timezone>##feed.timezone##</timezone>
     *  <late-arrival cut-off="##feed.cutoff##"/>
     *  <clusters>
     *    <cluster delay="##feed.cluster-two.delay##" name="##feed.cluster-two.name##" partition="##feed.cluster-two.partition##" type="target">
     *      <validity start="##feed.cluster-two.validity.start##" end="##feed.cluster-two.validity.end##" />
     *      <retention action="archive" limit="##feed.cluster-two.retention.limit##" type="instance"/>
     *      <locations>
     *        <location path="##feed.cluster-two.location1.path##" type="data"/>
     *        <location path="##feed.cluster-two.location2.path##" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="##feed.path##" type="data"/>
     *  </locations>
     *  <ACL group="##feed.group##" owner="##feed.owner##" permission="##feed.permission##"/>
     *</feed>
     */
    @Multiline private static String feedOneTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed One" name="feedOneMerged" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>PDT</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *    </cluster>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
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
    @Multiline private static String feedOneMerged;

    @Test
    public void builds_feed_from_templates_and_properties() throws Exception {
        TestUtils.write(new File(configDir, "default.properties"), defaultFeedProps);
        TestUtils.write(new File(configDir, "feedOne.properties"), feedOneProps);
        TestUtils.write(new File(configDir, "default-feed.xml"), defaultFeedTemplate);
        TestUtils.write(new File(configDir, "feedOne.xml"), feedOneTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, feedOnlyConfig);

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(1);
        application.matchesEntityOutput(feedOneMerged);
    }

    /**
    {
     "pipeline" : "clickstream",
     "feed-mappings" : [
         { 
             "property-file" : "feedOne.properties",
             "template" : "feedOne.xml"
         }
      ]
    }
    */
    @Multiline private static String feedNoDefaultsConfig;

    /**
     *feed.tags=key2=value2
     *feed.frequency=hours(8)
     *feed.timezone=PDT
     *feed.cutoff=days(1)
     *feed.cluster-two.name=cluster-two
     *feed.cluster-two.validity.start=2015-01-23T05:00Z
     *feed.cluster-two.validity.end=2016-01-23T05:00Z
     *feed.cluster-two.retention.limit=months(9999)
     *feed.group=test
     *feed.owner=test
     *feed.permission=*
     *feed.description=Feed One
     *feed.name=feedOneMerged
     *feed.cluster-two.delay=days(2)
     *feed.cluster-two.partition=part2
     *feed.cluster-two.location1.path=/foo/bar/baz
     *feed.cluster-two.location2.path=/foo/bar/boo
     *feed.path=/foo/bar
     */
    @Multiline private static String feedNoDefaultsProps;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="##feed.description##" name="##feed.name##" xmlns="uri:falcon:feed:0.1">
     *  <tags>##feed.tags##</tags>
     *  <frequency>##feed.frequency##</frequency>
     *  <timezone>##feed.timezone##</timezone>
     *  <late-arrival cut-off="##feed.cutoff##"/>
     *  <clusters>
     *    <cluster delay="##feed.cluster-two.delay##" name="##feed.cluster-two.name##" partition="##feed.cluster-two.partition##" type="target">
     *      <validity start="##feed.cluster-two.validity.start##" end="##feed.cluster-two.validity.end##" />
     *      <retention action="archive" limit="##feed.cluster-two.retention.limit##" type="instance"/>
     *      <locations>
     *        <location path="##feed.cluster-two.location1.path##" type="data"/>
     *        <location path="##feed.cluster-two.location2.path##" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="##feed.path##" type="data"/>
     *  </locations>
     *  <ACL group="##feed.group##" owner="##feed.owner##" permission="##feed.permission##"/>
     *  <schema location="/none" provider="none"/>
     *</feed>
     */
    @Multiline private static String feedNoDefaultsTemplate;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed One" name="feedOneMerged" xmlns="uri:falcon:feed:0.1">
     *  <tags>key2=value2</tags>
     *  <frequency>hours(8)</frequency>
     *  <timezone>PDT</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
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
    @Multiline private static String feedNoDefaultsMerged;

    @Test
    public void builds_feed_from_templates_and_no_defaults() throws Exception {
        TestUtils.write(new File(configDir, "feedOne.properties"), feedNoDefaultsProps);
        TestUtils.write(new File(configDir, "feedOne.xml"), feedNoDefaultsTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, feedNoDefaultsConfig);

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(1);
        application.matchesEntityOutput(feedNoDefaultsMerged);
    }

    /**
    {
     "pipeline" : "clickstream",
     "default-properties" : "default-properties.properties",
     "default-feed-template" : "default-feed.xml",
     "feed-mappings" : [
         "feed1.properties",
         "feed2.properties",
         "feed3.properties",
         "feed4.properties",
         "feed5.properties"
      ]
    }
    */
    @Multiline private static String feedAllDefaultsConfig;

    /**
     *feed.tags=key2=value2
     *feed.frequency=hours(8)
     *feed.timezone=PDT
     *feed.cutoff=days(1)
     *feed.cluster-two.name=cluster-two
     *feed.cluster-two.validity.start=2015-01-23T05:00Z
     *feed.cluster-two.validity.end=2016-01-23T05:00Z
     *feed.cluster-two.retention.limit=months(9999)
     *feed.group=test
     *feed.owner=test
     *feed.permission=*
     *feed.cluster-two.delay=days(2)
     *feed.cluster-two.partition=part2
     *feed.cluster-two.location1.path=/foo/bar/baz
     *feed.cluster-two.location2.path=/foo/bar/boo
     */
    @Multiline private static String feedAllDefaultsProps;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed 3" name="feed3" xmlns="uri:falcon:feed:0.1">
     *  <tags>key2=value2</tags>
     *  <frequency>hours(8)</frequency>
     *  <timezone>PDT</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <locations>
     *        <location path="/foo/bar/baz" type="data"/>
     *        <location path="/foo/bar/boo" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="/foo/bar/3" type="data"/>
     *  </locations>
     *  <ACL group="test" owner="test" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *</feed>
     */
    @Multiline private static String feedThreeAllDefaultsMerged;

    @Test
    public void builds_feed_from_only_default_template_and_defaults() throws Exception {
        TestUtils.write(new File(configDir, "default-properties.properties"), feedAllDefaultsProps);
        TestUtils.write(new File(configDir, "default-feed.xml"), feedNoDefaultsTemplate);
        File mainConfig = new File(configDir, "main-config.json");
        TestUtils.write(mainConfig, feedAllDefaultsConfig);
        for (int i = 1; i <= 5; i++) {
            TestUtils.write(new File(configDir, format("feed%s.properties", i)), new String[] {
                    "feed.name=feed" + i,
                    "feed.description=Feed " + i,
                    "feed.path=/foo/bar/" + i });
        }

        application.run(mainConfig, configDir, outDir);
        application.outputsNumFiles(5);
        application.matchesEntityOutput(feedThreeAllDefaultsMerged);
    }

    @Ignore
    @Test
    public void build_getters() {
        Class c = Feed.class;
        Set<String> getters = new TreeSet<String>();
        for (Method m : c.getDeclaredMethods()) {
            String name = m.getName();
            if (name.startsWith("get")) {
                getters.add(name);
            }
        }
        for (String g : getters) {
            System.out.println(String.format("assertEquals(expected.%s(), actual.%s());", g, g));
        }

        Set<String> setters = new TreeSet<String>();
        for (Method m : c.getDeclaredMethods()) {
            String name = m.getName();
            if (name.startsWith("set")) {
                setters.add(name);
            }
        }
        for (String s : setters) {
            String mergeName = s.substring(3);
            System.out.println(String.format("merged.%s(merge_%s(main, defaults));", s, mergeName));
        }
    }

}
