package com.michaelmiklavcic.falconer.test.entity;

import static com.michaelmiklavcic.falconer.test.util.FalconEntityMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.adrianwalker.multilinestring.Multiline;
import org.apache.falcon.entity.v0.feed.Feed;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.entity.EntityMerger;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class FeedEntityMergerTest {

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Full" name="feedFull" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <partitions>
     *    <partition name="part1"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
     *    </cluster>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
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
     *  <properties>
     *    <property name="property1" value="value1"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedFull;

    @Test
    public void builds_basic_entity_from_template() throws IOException, JAXBException, SAXException {
        EntityMerger builder = EntityMerger.create(feedFull);
        Feed actual = (Feed) builder.merge();
        Feed expected = (Feed) TestUtils.unmarshallEntity(feedFull);
        assertThat(actual, equalTo(expected));
    }

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<feed name="MyFeed" xmlns="uri:falcon:feed:0.1">
     *</feed>
     */
    @Multiline private static String feedEmpty;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Full" name="MyFeed" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <partitions>
     *    <partition name="part1"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
     *    </cluster>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
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
     *  <properties>
     *    <property name="property1" value="value1"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedMerged1;

    @Test
    public void builds_entity_from_empty_entity_and_default_template() throws IOException, JAXBException, SAXException {
        EntityMerger builder = EntityMerger.create(feedEmpty, feedFull);
        Feed actual = (Feed) builder.merge();
        Feed expected = (Feed) TestUtils.unmarshallEntity(feedMerged1);
        assertThat(actual, equalTo(expected));
    }

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Full Two" name="feedFull2" xmlns="uri:falcon:feed:0.1">
     *  <tags/>
     *  <partitions>
     *    <partition name="part1"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
     *    </cluster>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
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
     *  <properties>
     *    <property name="property1" value="value1"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedFull2;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Full Two" name="feedFull2" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <partitions>
     *    <partition name="part1"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
     *    </cluster>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
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
     *  <properties>
     *    <property name="property1" value="value1"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedFull2Merged;

    @Test
    public void builds_entity_from_full_entity_overriding_all_of_default_template() throws IOException, JAXBException, SAXException {
        EntityMerger builder = EntityMerger.create(feedFull2, feedFull);
        Feed actual = (Feed) builder.merge();
        Feed expected = (Feed) TestUtils.unmarshallEntity(feedFull2Merged);
        assertThat(actual, equalTo(expected));
    }

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Merge Parent" name="feedMergeParent" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1</tags>
     *  <partitions>
     *    <partition name="part1"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
     *    </cluster>
     *    <cluster delay="days(2)" name="cluster-two" partition="part2" type="target">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
     *      <locations>
     *        <location path="/foo/bar/one" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="/foo/bar/one" type="data"/>
     *    <location path="/foo/bar/two" type="data"/>
     *  </locations>
     *  <ACL group="test" owner="test" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *  <properties>
     *    <property name="property1" value="value1"/>
     *    <property name="property2" value="value2"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedMergeParent;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Merge Parent" name="feedMergeParent" xmlns="uri:falcon:feed:0.1">
     *  <tags>key2=value2</tags>
     *  <partitions>
     *    <partition name="part2"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(3)" name="cluster-two" partition="part22" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(22)" slaLow="days(21)"/>
     *      <locations>
     *        <location path="/foo/bar/two" type="data"/>
     *      </locations>
     *    </cluster>
     *    <cluster delay="days(3)" name="cluster-three" partition="part3" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(3)" slaLow="days(2)"/>
     *      <locations>
     *        <location path="/foo/bar/two" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="/foo/bar/two" type="tmp"/>
     *    <location path="/foo/bar/three" type="data"/>
     *  </locations>
     *  <ACL group="test" owner="test" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *  <properties>
     *    <property name="property2" value="value2-revised"/>
     *    <property name="property3" value="value3"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedMergeChild;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Merge Parent" name="feedMergeParent" xmlns="uri:falcon:feed:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <partitions>
     *    <partition name="part1"/>
     *    <partition name="part2"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(1)" name="cluster-one" partition="part1" type="source">
     *      <validity start="2015-01-23T00:00Z" end="2016-01-23T00:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(2)" slaLow="days(1)"/>
     *    </cluster>
     *    <cluster delay="days(3)" name="cluster-two" partition="part22" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(22)" slaLow="days(21)"/>
     *      <locations>
     *        <location path="/foo/bar/two" type="data"/>
     *      </locations>
     *    </cluster>
     *    <cluster delay="days(3)" name="cluster-three" partition="part3" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(3)" slaLow="days(2)"/>
     *      <locations>
     *        <location path="/foo/bar/two" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="/foo/bar/one" type="data"/>
     *    <location path="/foo/bar/two" type="tmp"/>
     *    <location path="/foo/bar/three" type="data"/>
     *  </locations>
     *  <ACL group="test" owner="test" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *  <properties>
     *    <property name="property1" value="value1"/>
     *    <property name="property2" value="value2-revised"/>
     *    <property name="property3" value="value3"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedMergeMerged;

    @Test
    public void builds_entity_and_merges_special_elements() throws IOException, JAXBException, SAXException {
        EntityMerger builder = EntityMerger.create(feedMergeChild, feedMergeParent);
        Feed actual = (Feed) builder.merge();
        Feed expected = (Feed) TestUtils.unmarshallEntity(feedMergeMerged);
        assertThat(actual, equalTo(expected));
    }

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Merge Parent" name="feedMergeParent" xmlns="uri:falcon:feed:0.1">
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <ACL group="test" owner="test" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *</feed>
     */
    @Multiline private static String feedMergeParentPartial;

    /**
     *<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
     *<feed description="Feed Merge Parent" name="feedMergeParent" xmlns="uri:falcon:feed:0.1">
     *  <tags>key2=value2</tags>
     *  <partitions>
     *    <partition name="part2"/>
     *  </partitions>
     *  <groups>group1</groups>
     *  <availabilityFlag>_SUCCESS</availabilityFlag>
     *  <frequency>hours(8)</frequency>
     *  <sla slaHigh="days(2)" slaLow="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <late-arrival cut-off="days(1)"/>
     *  <clusters>
     *    <cluster delay="days(3)" name="cluster-two" partition="part22" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(22)" slaLow="days(21)"/>
     *      <locations>
     *        <location path="/foo/bar/two" type="data"/>
     *      </locations>
     *    </cluster>
     *    <cluster delay="days(3)" name="cluster-three" partition="part3" type="target">
     *      <validity start="2015-01-23T05:00Z" end="2016-01-23T05:00Z" />
     *      <retention action="archive" limit="months(9999)" type="instance"/>
     *      <sla slaHigh="days(3)" slaLow="days(2)"/>
     *      <locations>
     *        <location path="/foo/bar/two" type="data"/>
     *      </locations>
     *    </cluster>
     *  </clusters>
     *  <locations>
     *    <location path="/foo/bar/two" type="tmp"/>
     *    <location path="/foo/bar/three" type="data"/>
     *  </locations>
     *  <ACL group="test" owner="test" permission="*"/>
     *  <schema location="/none" provider="none"/>
     *  <properties>
     *    <property name="property2" value="value2-revised"/>
     *    <property name="property3" value="value3"/>
     *  </properties>
     *</feed>
     */
    @Multiline private static String feedMergePartialMerged;

    @Test
    public void builds_entity_and_merges_special_elements_from_partial_parent() throws IOException, JAXBException, SAXException {
        EntityMerger builder = EntityMerger.create(feedMergeChild, feedMergeParentPartial);
        Feed actual = (Feed) builder.merge();
        Feed expected = (Feed) TestUtils.unmarshallEntity(feedMergePartialMerged);
        assertThat(actual, equalTo(expected));
    }

}
