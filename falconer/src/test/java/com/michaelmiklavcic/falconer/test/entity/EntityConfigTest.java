package com.michaelmiklavcic.falconer.test.entity;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.*;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class EntityConfigTest {

    private File testDir;
    
    @Before
    public void setUp() throws Exception {
        testDir = TestUtils.createTempDir(getClass().toString());
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
       {
        "pipeline" : "clickstream",
        "default-properties" : "default.properties",
        "default-template" : "clickstream-template.xml",
        "mappings" : [
            "feed-in1.properties",
            "feed-in2.properties",
            "feed-in3.properties",
            { 
                "property-file" : "feed-out1.properties",
                "template" : "out-template.xml"
            }
         ]
       }
     */
    @Multiline
    private static String configProps;
    
    @Test
    public void loads_configProps() throws IOException {
        File mainProps = new File(testDir, "props.json");
        TestUtils.write(mainProps, configProps);
        EntityConfig config = EntityConfigLoader.getInstance().load(mainProps);
        assertThat(config.getPipeline(), equalTo("clickstream"));
        assertThat(config.getDefaultProperties(), equalTo("default.properties"));
        assertThat(config.getDefaultTemplate(), equalTo("clickstream-template.xml"));
        assertThat(config.getMappings().length, equalTo(4));
        assertThat(config.getMappings()[0].getPropertyFile(), equalTo("feed-in1.properties"));
        assertThat(config.getMappings()[0].getTemplate(), equalTo(null));
        assertThat(config.getMappings()[1].getPropertyFile(), equalTo("feed-in2.properties"));
        assertThat(config.getMappings()[1].getTemplate(), equalTo(null));
        assertThat(config.getMappings()[2].getPropertyFile(), equalTo("feed-in3.properties"));
        assertThat(config.getMappings()[2].getTemplate(), equalTo(null));
        assertThat(config.getMappings()[3].getPropertyFile(), equalTo("feed-out1.properties"));
        assertThat(config.getMappings()[3].getTemplate(), equalTo("out-template.xml"));
    }

//    /**
//     * {
//     *  "properties": {
//     *      "eat at":"joes",
//     *      "hello":"blah"
//     *   }
//     * }
//     */
//    @Multiline
//    private static String parent2;
//    
//    /**
//     * {
//     *  "parent":"parent2.json",
//     *  "properties": {
//     *      "foo.bar":"baz",
//     *      "hello":"world"
//     *   }
//     * }
//     */
//    @Multiline
//    private static String parent1;
//    
//    /**
//     * {
//     *  "parent":"parent1.json",
//     *  "properties": {
//     *      "big":"shoe",
//     *      "hello":"mike"
//     *  }
//     * }
//     */
//    @Multiline
//    private static String child;
    
//    @Test
//    public void loads_parent_props() throws IOException {
//        File parentConfig = new File(testDir, "parent2.json");
//        TestUtils.write(parentConfig, parent2);
//        EntityConfig config = EntityConfigLoader.getInstance().load(parentConfig);
//        assertThat(config.getProperties().getProperty("hello"), equalTo("blah"));
//        assertThat(config.getParent(), equalTo(null));
//    }
//    
//    @Test
//    public void overlays_child_on_parents() throws IOException {
//        File parent2Config = new File(testDir, "parent2.json");
//        File parent1Config = new File(testDir, "parent1.json");
//        File childConfig = new File(testDir, "child.json");
//        TestUtils.write(parent2Config, parent2);
//        TestUtils.write(parent1Config, parent1);
//        TestUtils.write(childConfig, child);
//        EntityConfig config = EntityConfigLoader.getInstance().load(childConfig);
//        assertThat(config.getProperties().getProperty("foo.bar"), equalTo("baz"));
//        assertThat(config.getProperties().getProperty("big"), equalTo("shoe"));
//        assertThat(config.getProperties().getProperty("hello"), equalTo("mike"));
//        assertThat(config.getProperties().getProperty("eat at"), equalTo("joes"));
//    }

}
