package com.michaelmiklavcic.falconer.test.entity;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.util.Properties;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.google.common.io.Files;
import com.michaelmiklavcic.falconer.entity.PropertyBuilder;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class PropertyBuilderTest {

    private File propertiesDir;

    @Before
    public void setUp() throws Exception {
        propertiesDir = TestUtils.createTempDir(getClass().toString());
    }

    /**
     * foo.bar.name=hello
     * foo.bar.description=world
     */
    @Multiline private static String props1;

    /**
     * foo.bar.name=newhello
     * foo.bar.title=villain suppressor
     */
    @Multiline private static String props2;

    /**
     * foo.bar.name=morenewhello
     * foo.ding.name=different name
     */
    @Multiline private static String props3;

    /**
     * foo.bar.name=morenewhello
     * foo.bar.description=world
     * foo.bar.title=villain suppressor
     * foo.ding.name=different name
     */
    @Multiline private static String mergedProps;

    @Test
    public void merges_as_identity_function_in_simple_case() throws IOException {
        File propsFile = new File(propertiesDir, "props.properties");
        Files.createParentDirs(propsFile);
        TestUtils.write(propsFile, props1);

        PropertyBuilder builder = new PropertyBuilder();
        Properties actual = builder.merge(propsFile);
        Properties expected = TestUtils.loadProperties(props1);

        assertThat("Expected single properties returned as identity function", actual, equalTo(expected));
    }

    @Test
    public void merges_properties_from_multiple_files() throws IOException {
        File props1File = new File(propertiesDir, "props1.properties");
        File props2File = new File(propertiesDir, "props2.properties");
        File props3File = new File(propertiesDir, "props3.properties");
        Files.createParentDirs(props1File);
        TestUtils.write(props1File, props1);
        TestUtils.write(props2File, props2);
        TestUtils.write(props3File, props3);

        PropertyBuilder builder = new PropertyBuilder();
        Properties actual = builder.merge(props3File, props2File, props1File);
        Properties expected = TestUtils.loadProperties(mergedProps);

        assertThat("Expected properties to override by cascade", actual, equalTo(expected));
    }

}
