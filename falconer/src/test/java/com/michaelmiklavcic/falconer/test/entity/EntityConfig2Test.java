package com.michaelmiklavcic.falconer.test.entity;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.*;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class EntityConfig2Test {

    private File testDir;

    @Before
    public void setUp() throws Exception {
        testDir = TestUtils.createTempDir(getClass().toString());
    }

    /**
     * {
     *  "mappings": [
     *          {
     *              "main" : "props1.properties",
     *              "parent" : "otherdefault.properties"
     *          }
     *   ]
     * }
     */
    @Multiline
    private static String props;

    @Test
    public void loads_parent_props() throws IOException {
        File mainProps = new File(testDir, "props.json");
        TestUtils.write(mainProps, props);
        EntityConfig config = EntityConfigLoader.getInstance().load(mainProps);
        assertThat(config.getMappings()[0].getMain(), equalTo("props1.properties"));
        assertThat(config.getMappings()[0].getParents()[0], equalTo("otherdefault.properties"));
    }
    
    /**
     * {
     *  "mappings": [
     *          "props1.properties",
     *          {
     *              "main": "props2.properties",
     *              "parent": "otherdefault.properties"
     *          },
     *          {
     *              "main": "props3.properties",
     *              "parent": [ "otherdefault.properties", "more.properties" ]
     *          }
     *   ]
     * }
     */
    @Multiline
    private static String props2;

    @Test
    public void loads_parent_props2() throws IOException {
        File mainProps = new File(testDir, "props.json");
        TestUtils.write(mainProps, props2);
        EntityConfig config = EntityConfigLoader.getInstance().load(mainProps);
        assertThat(config.getMappings()[0].getMain(), equalTo("props1.properties"));
        assertThat(config.getMappings()[1].getMain(), equalTo("props2.properties"));
        assertThat(config.getMappings()[1].getParents()[0], equalTo("otherdefault.properties"));
        assertThat(config.getMappings()[2].getMain(), equalTo("props3.properties"));
        assertThat(config.getMappings()[2].getParents()[0], equalTo("otherdefault.properties"));
        assertThat(config.getMappings()[2].getParents()[1], equalTo("more.properties"));
    }
    /**
     * {
     *  "mappings": [
     *          "props1.properties",
     *          [ "props2.properties", "otherdefault.properties" ],
     *          [ "props3.properties", "otherdefault.properties", "more.properties" ]
     *   ]
     * }
     */
    @Multiline
    private static String props3;
    
    @Test
    public void loads_parent_props3() throws IOException {
        File mainProps = new File(testDir, "props.json");
        TestUtils.write(mainProps, props3);
        EntityConfig config = EntityConfigLoader.getInstance().load(mainProps);
        assertThat(config.getMappings()[0].getMain(), equalTo("props1.properties"));
        assertThat(config.getMappings()[1].getMain(), equalTo("props2.properties"));
        assertThat(config.getMappings()[1].getParents()[0], equalTo("otherdefault.properties"));
        assertThat(config.getMappings()[2].getMain(), equalTo("props3.properties"));
        assertThat(config.getMappings()[2].getParents()[0], equalTo("otherdefault.properties"));
        assertThat(config.getMappings()[2].getParents()[1], equalTo("more.properties"));
    }
    
    /**
     * {
     *  "entity-configs" : [
     *      "props1.properties",
     *      "props2.properties",
     *      { 
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
        assertThat(config.getFoo()[0].getPropertyFile(), equalTo("props1.properties"));
        assertThat(config.getFoo()[0].getTemplate(), equalTo(null));
        assertThat(config.getFoo()[1].getPropertyFile(), equalTo("props2.properties"));
        assertThat(config.getFoo()[1].getTemplate(), equalTo(null));
        assertThat(config.getFoo()[2].getPropertyFile(), equalTo("feed-out1.properties"));
        assertThat(config.getFoo()[2].getTemplate(), equalTo("out-template.xml"));
    }
    
}
