package com.michaelmiklavcic.falconer.test.entity;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.adrianwalker.multilinestring.Multiline;
import org.apache.falcon.entity.v0.feed.Feed;
import org.apache.falcon.entity.v0.process.Process;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.entity.EntityBuilder;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class FeedEntityBuilderTest {

    /**
     * 
     */
    @Multiline private static String feedFull;

    @Test
    public void builds_basic_entity_from_template() throws IOException, JAXBException, SAXException {
        EntityBuilder builder = EntityBuilder.create(feedFull);
        Feed entity = (Feed) builder.build();
        TestUtils.assertEquals(feedFull, entity);
    }

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<process name="MyProcess" xmlns="uri:falcon:process:0.1">
     *</process>
     */
    @Multiline private static String processEmpty;

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<process name="MyProcess" xmlns="uri:falcon:process:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <pipelines>pipeline1,pipeline2</pipelines>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-01T01:00Z" end="2020-01-01T01:00Z" />
     *    </cluster>
     *    <cluster name="cluster-two">
     *      <validity start="2015-02-02T02:00Z" end="2020-02-02T02:00Z" />
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(8)</timeout>
     *  <frequency>days(1)</frequency>
     *  <sla shouldEndIn="hours(8)" shouldStartIn="hours(4)"/>
     *  <timezone>UTC</timezone>
     *  <inputs>
     *    <input name="in1" feed="snapshot" start="now(0,0)" end="now(0,0)"/>
     *    <input name="in2" feed="delta" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="out1" feed="outavro" instance="now(0,0)"/>
     *      <output name="out2" feed="outhive" instance="now(0,0)"/>
     *  </outputs>
     *  <properties>
     *      <property name="queueName" value="hadoopQueue"/>
     *      <property name="jobPriority" value="VERY_HIGH"/>
     *  </properties>
     *  <workflow engine="pig" lib="/apps/falcon/workflows/myapp" name="myapp" path="/apps/falcon/workflows/myapp/dostuff.pig" version="1.0"/>
     *  <retry attempts="2" delay="hours(3)" policy="periodic"/>
     *  <late-process delay="days(1)" policy="periodic">
     *    <late-input input="/some/input" workflow-path="/apps/falcon/workflows/myapp-late"/>
     *  </late-process>
     *  <ACL group="test" owner="test" permission="*"/>
     *</process>
     */
    @Multiline private static String processMerged1;

    @Test
    public void builds_entity_from_empty_entity_and_default_template() throws IOException, JAXBException, SAXException {
        EntityBuilder builder = EntityBuilder.create(processEmpty, feedFull);
        Process entity = (Process) builder.build();
        TestUtils.assertEquals(processMerged1, entity);
    }

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<process name="MyProcessA" xmlns="uri:falcon:process:0.1">
     *  <tags />
     *  <pipelines>pipeline1a,pipeline2a</pipelines>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-01T01:01Z" end="2020-01-01T01:01Z" />
     *    </cluster>
     *    <cluster name="cluster-two">
     *      <validity start="2015-02-02T02:01Z" end="2020-02-02T02:01Z" />
     *    </cluster>
     *  </clusters>
     *  <parallel>2</parallel>
     *  <order>LIFO</order>
     *  <timeout>hours(16)</timeout>
     *  <frequency>days(2)</frequency>
     *  <sla shouldEndIn="days(1)" shouldStartIn="days(1)"/>
     *  <timezone>PDT</timezone>
     *  <inputs>
     *    <input name="in1" feed="snapshot_overridden" start="now(0,0)" end="now(0,0)"/>
     *    <input name="in2" feed="delta_overridden" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="out1" feed="outavro_overridden" instance="now(0,0)"/>
     *      <output name="out2" feed="outhive_overridden" instance="now(0,0)"/>
     *  </outputs>
     *  <properties>
     *      <property name="queueName" value="newQueue"/>
     *      <property name="jobPriority" value="LOW"/>
     *  </properties>
     *  <workflow engine="oozie" lib="/apps/falcon/workflows/myappa" name="myappa" path="/apps/falcon/workflows/myappa/dostuffa.pig" version="2.0"/>
     *  <retry attempts="4" delay="days(3)" policy="periodic"/>
     *  <late-process delay="days(5)" policy="periodic">
     *    <late-input input="/some/inputa" workflow-path="/apps/falcon/workflows/myappa-late"/>
     *  </late-process>
     *  <ACL group="testa" owner="testa" permission="*"/>
     *</process>
     */
    @Multiline private static String processFull2;

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<process name="MyProcessA" xmlns="uri:falcon:process:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <pipelines>pipeline1a,pipeline2a</pipelines>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-01T01:01Z" end="2020-01-01T01:01Z" />
     *    </cluster>
     *    <cluster name="cluster-two">
     *      <validity start="2015-02-02T02:01Z" end="2020-02-02T02:01Z" />
     *    </cluster>
     *  </clusters>
     *  <parallel>2</parallel>
     *  <order>LIFO</order>
     *  <timeout>hours(16)</timeout>
     *  <frequency>days(2)</frequency>
     *  <sla shouldEndIn="days(1)" shouldStartIn="days(1)"/>
     *  <timezone>PDT</timezone>
     *  <inputs>
     *    <input name="in1" feed="snapshot_overridden" start="now(0,0)" end="now(0,0)"/>
     *    <input name="in2" feed="delta_overridden" start="now(0,0)" end="now(0,0)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="out1" feed="outavro_overridden" instance="now(0,0)"/>
     *      <output name="out2" feed="outhive_overridden" instance="now(0,0)"/>
     *  </outputs>
     *  <properties>
     *      <property name="queueName" value="newQueue"/>
     *      <property name="jobPriority" value="LOW"/>
     *  </properties>
     *  <workflow engine="oozie" lib="/apps/falcon/workflows/myappa" name="myappa" path="/apps/falcon/workflows/myappa/dostuffa.pig" version="2.0"/>
     *  <retry attempts="4" delay="days(3)" policy="periodic"/>
     *  <late-process delay="days(5)" policy="periodic">
     *    <late-input input="/some/inputa" workflow-path="/apps/falcon/workflows/myappa-late"/>
     *  </late-process>
     *  <ACL group="testa" owner="testa" permission="*"/>
     *</process>
     */
    @Multiline private static String processFull2Merged;

    @Test
    public void builds_entity_from_full_entity_and_default_template() throws IOException, JAXBException, SAXException {
        EntityBuilder builder = EntityBuilder.create(processFull2, feedFull);
        Process entity = (Process) builder.build();
        TestUtils.assertEquals(processFull2Merged, entity);
    }

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<process name="ProcessMergeParent" xmlns="uri:falcon:process:0.1">
     *  <tags>key1=value1</tags>
     *  <pipelines>pipeline1a</pipelines>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-01T01:00Z" end="2020-01-01T01:00Z" />
     *    </cluster>
     *    <cluster name="cluster-two">
     *      <validity start="2015-02-02T02:00Z" end="2020-02-02T02:00Z" />
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(8)</timeout>
     *  <frequency>days(1)</frequency>
     *  <sla shouldEndIn="days(1)" shouldStartIn="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <inputs>
     *      <input name="in1" feed="in1feed" start="now(0,1)" end="now(0,2)"/>
     *      <input name="in2" feed="in2feed" start="now(0,1)" end="now(0,2)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="out1" feed="out1feed" instance="now(0,3)"/>
     *      <output name="out2" feed="out2feed" instance="now(0,3)"/>
     *  </outputs>
     *  <properties>
     *      <property name="queueName" value="newQueue"/>
     *      <property name="jobPriority" value="LOW"/>
     *      <property name="custom1" value="custom1value"/>
     *  </properties>
     *  <workflow engine="pig" lib="/apps/falcon/workflows/myapp" name="myappa" path="/apps/falcon/workflows/myapp/dostuff.pig" version="1.0"/>
     *  <retry attempts="2" delay="days(1)" policy="periodic"/>
     *  <late-process delay="days(1)" policy="periodic">
     *    <late-input input="/some/input" workflow-path="/apps/falcon/workflows/myapp-late"/>
     *  </late-process>
     *  <ACL group="test" owner="test" permission="*"/>
     *</process>
     */
    @Multiline private static String processMergeParent;

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<process name="ProcessMergeChild" xmlns="uri:falcon:process:0.1">
     *  <tags>key2=value2</tags>
     *  <clusters>
     *    <cluster name="cluster-two">
     *      <validity start="2014-02-02T02:22Z" end="2022-02-02T02:22Z" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="2015-03-03T03:00Z" end="2020-03-03T03:00Z" />
     *    </cluster>
     *  </clusters>
     *  <inputs>
     *      <input name="in2" feed="in2feed" start="now(2,1)" end="now(2,2)"/>
     *      <input name="in3" feed="in3feed" start="now(0,1)" end="now(0,2)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="out2" feed="out2feed" instance="now(2,3)"/>
     *      <output name="out3" feed="out3feed" instance="now(0,3)"/>
     *  </outputs>
     *  <properties>
     *      <property name="queueName" value="newQueue"/>
     *      <property name="jobPriority" value="HIGH"/>
     *      <property name="custom2" value="custom2value"/>
     *  </properties>
     *</process>
     */
    @Multiline private static String processMergeChild;

    /**
     *<?xml version="1.0" encoding="UTF-8"?>
     *<process name="ProcessMergeChild" xmlns="uri:falcon:process:0.1">
     *  <tags>key1=value1,key2=value2</tags>
     *  <pipelines>pipeline1a</pipelines>
     *  <clusters>
     *    <cluster name="cluster-one">
     *      <validity start="2015-01-01T01:00Z" end="2020-01-01T01:00Z" />
     *    </cluster>
     *    <cluster name="cluster-two">
     *      <validity start="2014-02-02T02:22Z" end="2022-02-02T02:22Z" />
     *    </cluster>
     *    <cluster name="cluster-three">
     *      <validity start="2015-03-03T03:00Z" end="2020-03-03T03:00Z" />
     *    </cluster>
     *  </clusters>
     *  <parallel>1</parallel>
     *  <order>FIFO</order>
     *  <timeout>hours(8)</timeout>
     *  <frequency>days(1)</frequency>
     *  <sla shouldEndIn="days(1)" shouldStartIn="days(1)"/>
     *  <timezone>UTC</timezone>
     *  <inputs>
     *      <input name="in1" feed="in1feed" start="now(0,1)" end="now(0,2)"/>
     *      <input name="in2" feed="in2feed" start="now(2,1)" end="now(2,2)"/>
     *      <input name="in3" feed="in3feed" start="now(0,1)" end="now(0,2)"/>
     *  </inputs>
     *  <outputs>
     *      <output name="out1" feed="out1feed" instance="now(0,3)"/>
     *      <output name="out2" feed="out2feed" instance="now(2,3)"/>
     *      <output name="out3" feed="out3feed" instance="now(0,3)"/>
     *  </outputs>
     *  <properties>
     *      <property name="queueName" value="newQueue"/>
     *      <property name="jobPriority" value="HIGH"/>
     *      <property name="custom1" value="custom1value"/>
     *      <property name="custom2" value="custom2value"/>
     *  </properties>
     *  <workflow engine="pig" lib="/apps/falcon/workflows/myapp" name="myappa" path="/apps/falcon/workflows/myapp/dostuff.pig" version="1.0"/>
     *  <retry attempts="2" delay="days(1)" policy="periodic"/>
     *  <late-process delay="days(1)" policy="periodic">
     *    <late-input input="/some/input" workflow-path="/apps/falcon/workflows/myapp-late"/>
     *  </late-process>
     *  <ACL group="test" owner="test" permission="*"/>
     *</process>
     */
    @Multiline private static String processMergeMerged;

    @Test
    public void builds_entity_and_merges_special_elements() throws IOException, JAXBException, SAXException {
        EntityBuilder builder = EntityBuilder.create(processMergeChild, processMergeParent);
        Process actual = (Process) builder.build();
        Process expected = TestUtils.unmarshallProcess(processMergeMerged);
        assertThat(expected.getName(), equalTo(actual.getName()));
        TestUtils.assertEquals(expected.getClusters(), actual.getClusters());
        TestUtils.assertEquals(expected.getInputs(), actual.getInputs());
        TestUtils.assertEquals(expected.getOutputs(), actual.getOutputs());
        TestUtils.assertEquals(expected.getProperties(), actual.getProperties());
    }

}
