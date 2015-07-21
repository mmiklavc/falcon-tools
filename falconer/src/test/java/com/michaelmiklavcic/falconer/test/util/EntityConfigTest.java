package com.michaelmiklavcic.falconer.test.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.entity.*;
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
        "default-feed-template" : "clickstream-feed-template.xml",
        "default-process-template" : "clickstream-process-template.xml",
        "feed-mappings" : [
            "feed-in1.properties",
            "feed-in2.properties",
            { 
                "property-file" : "feed-out1.properties",
                "template" : "feed-out1-template.xml"
            }
         ],
        "process-mappings" : [
            "process-in1.properties",
            "process-in2.properties",
            { 
                "property-file" : "process-out1.properties",
                "template" : "process-out1-template.xml"
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
        assertThat(config.getDefaultFeedTemplate(), equalTo("clickstream-feed-template.xml"));
        assertThat(config.getDefaultProcessTemplate(), equalTo("clickstream-process-template.xml"));
        assertThat(config.getFeedMappings().length, equalTo(3));
        assertThat(config.getFeedMappings()[0].getPropertyFile(), equalTo("feed-in1.properties"));
        assertThat(config.getFeedMappings()[0].getTemplate(), equalTo(null));
        assertThat(config.getFeedMappings()[1].getPropertyFile(), equalTo("feed-in2.properties"));
        assertThat(config.getFeedMappings()[1].getTemplate(), equalTo(null));
        assertThat(config.getFeedMappings()[2].getPropertyFile(), equalTo("feed-out1.properties"));
        assertThat(config.getFeedMappings()[2].getTemplate(), equalTo("feed-out1-template.xml"));
        assertThat(config.getProcessMappings().length, equalTo(3));
        assertThat(config.getProcessMappings()[0].getPropertyFile(), equalTo("process-in1.properties"));
        assertThat(config.getProcessMappings()[0].getTemplate(), equalTo(null));
        assertThat(config.getProcessMappings()[1].getPropertyFile(), equalTo("process-in2.properties"));
        assertThat(config.getProcessMappings()[1].getTemplate(), equalTo(null));
        assertThat(config.getProcessMappings()[2].getPropertyFile(), equalTo("process-out1.properties"));
        assertThat(config.getProcessMappings()[2].getTemplate(), equalTo("process-out1-template.xml"));
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
        assertThat(config.getDefaultFeedTemplate(), equalTo(""));
        assertThat(config.getDefaultProcessTemplate(), equalTo(""));
        assertThat(config.getDefaultProperties(), equalTo(""));
        assertThat(config.getFeedMappings().length, equalTo(0));
        assertThat(config.getProcessMappings().length, equalTo(0));
        assertThat(config.getPipeline(), equalTo(""));
    }

}
