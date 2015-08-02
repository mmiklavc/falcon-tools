package com.michaelmiklavcic.falconer.test.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.util.*;

public class EntityConfigTest {

    private File testDir;

    @Before
    public void setUp() throws Exception {
        testDir = TestUtils.createTempDir(getClass().toString());
    }

    /**
       {
        "pipeline" : "clickstream",
        "default-properties" : "default.properties",
        "feed-prototype" : "clickstream-feed-prototype.xml",
        "process-prototype" : "clickstream-process-prototype.xml",
        "feed-mappings" : [
            "feed-in1.properties",
            "feed-in2.properties",
            { 
                "property-file" : "feed-out1.properties",
                "template" : "feed-out1-template.xml"
            },
            { 
                "property-file" : "feed-out2.properties",
                "template" : "feed-out2-template.xml",
                "merge-strategy" : "ignore-prototype"
            },
            { 
                "property-file" : "feed-in3.properties",
                "template" : "feed-in3-template.xml",
                "merge-strategy" : "merge"
            }
         ],
        "process-mappings" : [
            "process-in1.properties",
            "process-in2.properties",
            { 
                "property-file" : "process-out1.properties",
                "template" : "process-out1-template.xml"
            },
            { 
                "property-file" : "process-out2.properties",
                "template" : "process-out2-template.xml",
                "merge-strategy" : "ignore-prototype"
            },
            { 
                "property-file" : "process-in3.properties",
                "template" : "process-in3-template.xml",
                "merge-strategy" : "merge"
            }
         ]
       }
     */
    @Multiline private static String configProps;

    @Test
    public void loads_config_values() throws IOException {
        File mainProps = new File(testDir, "props.json");
        TestUtils.write(mainProps, configProps);
        EntityConfig config = EntityConfigLoader.getInstance().load(mainProps);
        assertThat(config.getPipeline(), equalTo("clickstream"));
        assertThat(config.getDefaultProperties(), equalTo("default.properties"));
        assertThat(config.getFeedPrototype(), equalTo("clickstream-feed-prototype.xml"));
        assertThat(config.getProcessPrototype(), equalTo("clickstream-process-prototype.xml"));
        assertThat(config.getFeedMappings().length, equalTo(5));
        assertThat(config.getFeedMappings()[0].getPropertyFile(), equalTo("feed-in1.properties"));
        assertThat(config.getFeedMappings()[0].getTemplate(), equalTo(""));
        assertThat(config.getFeedMappings()[0].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
        assertThat(config.getFeedMappings()[1].getPropertyFile(), equalTo("feed-in2.properties"));
        assertThat(config.getFeedMappings()[1].getTemplate(), equalTo(""));
        assertThat(config.getFeedMappings()[1].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
        assertThat(config.getFeedMappings()[2].getPropertyFile(), equalTo("feed-out1.properties"));
        assertThat(config.getFeedMappings()[2].getTemplate(), equalTo("feed-out1-template.xml"));
        assertThat(config.getFeedMappings()[2].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
        assertThat(config.getFeedMappings()[3].getPropertyFile(), equalTo("feed-out2.properties"));
        assertThat(config.getFeedMappings()[3].getTemplate(), equalTo("feed-out2-template.xml"));
        assertThat(config.getFeedMappings()[3].getMergeStrategy(), equalTo(MergeStrategy.IGNORE_PROTOTYPE));
        assertThat(config.getFeedMappings()[4].getPropertyFile(), equalTo("feed-in3.properties"));
        assertThat(config.getFeedMappings()[4].getTemplate(), equalTo("feed-in3-template.xml"));
        assertThat(config.getFeedMappings()[4].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
        assertThat(config.getProcessMappings().length, equalTo(5));
        assertThat(config.getProcessMappings()[0].getPropertyFile(), equalTo("process-in1.properties"));
        assertThat(config.getProcessMappings()[0].getTemplate(), equalTo(""));
        assertThat(config.getProcessMappings()[0].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
        assertThat(config.getProcessMappings()[1].getPropertyFile(), equalTo("process-in2.properties"));
        assertThat(config.getProcessMappings()[1].getTemplate(), equalTo(""));
        assertThat(config.getProcessMappings()[1].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
        assertThat(config.getProcessMappings()[2].getPropertyFile(), equalTo("process-out1.properties"));
        assertThat(config.getProcessMappings()[2].getTemplate(), equalTo("process-out1-template.xml"));
        assertThat(config.getProcessMappings()[2].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
        assertThat(config.getProcessMappings()[3].getPropertyFile(), equalTo("process-out2.properties"));
        assertThat(config.getProcessMappings()[3].getTemplate(), equalTo("process-out2-template.xml"));
        assertThat(config.getProcessMappings()[3].getMergeStrategy(), equalTo(MergeStrategy.IGNORE_PROTOTYPE));
        assertThat(config.getProcessMappings()[4].getPropertyFile(), equalTo("process-in3.properties"));
        assertThat(config.getProcessMappings()[4].getTemplate(), equalTo("process-in3-template.xml"));
        assertThat(config.getProcessMappings()[4].getMergeStrategy(), equalTo(MergeStrategy.MERGE));
    }

    /**
    {
    }
     */
    @Multiline private static String configEmpty;

    @Test
    public void loads_config_no_default_properties() throws IOException {
        File mainProps = new File(testDir, "props.json");
        TestUtils.write(mainProps, configEmpty);
        EntityConfig config = EntityConfigLoader.getInstance().load(mainProps);
        assertThat(config.getFeedPrototype(), equalTo(""));
        assertThat(config.getProcessPrototype(), equalTo(""));
        assertThat(config.getDefaultProperties(), equalTo(""));
        assertThat(config.getFeedMappings().length, equalTo(0));
        assertThat(config.getProcessMappings().length, equalTo(0));
        assertThat(config.getPipeline(), equalTo(""));
    }

    /**
     *{
        "pipeline" : "clickstream",
        "feed-mappings" : [
            "feed-in1.properties",
            { 
                "property-file" : "feed-out1.properties"
            }
         ],
        "process-mappings" : [
            "process-in1.properties",
            { 
                "property-file" : "process-out1.properties"
            }
         ]
     *}
     */
    @Multiline private static String configPrimaryTemplatesEmpty;

    @Test
    public void loads_config_no_primary_templates() throws IOException {
        File mainProps = new File(testDir, "props.json");
        TestUtils.write(mainProps, configPrimaryTemplatesEmpty);
        EntityConfig config = EntityConfigLoader.getInstance().load(mainProps);
        assertThat(config.getFeedMappings().length, equalTo(2));
        assertThat(config.getFeedMappings()[0].getPropertyFile(), equalTo("feed-in1.properties"));
        assertThat(config.getFeedMappings()[0].getTemplate(), equalTo(""));
        assertThat(config.getFeedMappings()[1].getPropertyFile(), equalTo("feed-out1.properties"));
        assertThat(config.getFeedMappings()[1].getTemplate(), equalTo(""));
        assertThat(config.getProcessMappings().length, equalTo(2));
        assertThat(config.getProcessMappings()[0].getPropertyFile(), equalTo("process-in1.properties"));
        assertThat(config.getProcessMappings()[0].getTemplate(), equalTo(""));
        assertThat(config.getProcessMappings()[1].getPropertyFile(), equalTo("process-out1.properties"));
        assertThat(config.getProcessMappings()[1].getTemplate(), equalTo(""));
    }

    /**
     *{
        "pipeline" : "clickstream",
        "feed-mappings" : [
            { 
                "template" : "template-file.xml"
            }
         ]
     *}
     */
    @Multiline private static String configMissingFeedProperties1;

    /**
     *{
        "pipeline" : "clickstream",
        "feed-mappings" : [
            " "
         ]
     *}
     */
    @Multiline private static String configMissingFeedProperties2;

    @Test
    public void exception_on_missing_feed_property_file() throws IOException {
        {
            File config = TestUtils.write(new File(testDir, "config.json"), configMissingFeedProperties1);
            assertConfigException(config);
        }
        {
            File config = TestUtils.write(new File(testDir, "config.json"), configMissingFeedProperties2);
            assertConfigException(config);
        }
    }

    private void assertConfigException(File config) {
        try {
            EntityConfigLoader.getInstance().load(config);
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof ConfigurationException);
        }
    }

    /**
     *{
        "pipeline" : "clickstream",
        "process-mappings" : [
            { 
                "template" : "template-file.xml"
            }
         ]
     *}
     */
    @Multiline private static String configMissingProcessProperties1;

    /**
     *{
        "pipeline" : "clickstream",
        "process-mappings" : [
            " "
         ]
     *}
     */
    @Multiline private static String configMissingProcessProperties2;

    @Test
    public void exception_on_missing_process_property_file() throws IOException {
        {
            File config = TestUtils.write(new File(testDir, "config.json"), configMissingProcessProperties1);
            assertConfigException(config);
        }
        {
            File config = TestUtils.write(new File(testDir, "config.json"), configMissingProcessProperties2);
            assertConfigException(config);
        }
    }

}
