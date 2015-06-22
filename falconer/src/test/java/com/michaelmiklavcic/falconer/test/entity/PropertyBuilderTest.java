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
        propertiesDir = new File("target/properties");
    }

    /**
     * foo.bar.name=hello
     * foo.bar.description=world
     */
    @Multiline private static String parentProps;

    /**
     * foo.bar.name=newhello
     * foo.bar.title=villain suppressor
     */
    @Multiline private static String childProps1;

    /**
     * foo.bar.name=morenewhello
     * foo.ding.name=different name
     */
    @Multiline private static String childProps2;

    /**
     * foo.bar.name=morenewhello
     * foo.bar.description=world
     * foo.bar.title=villain suppressor
     * foo.ding.name=different name
     */
    @Multiline private static String expectedProps;

    @Test
    public void merges_as_identity_function_in_simplest_case() throws IOException {
        File parent = new File(propertiesDir, "parent.properties");
        Files.createParentDirs(parent);
        TestUtils.write(parent, parentProps);

        PropertyBuilder builder = new PropertyBuilder();
        Properties actual = builder.merge(parent);
        Properties expected = TestUtils.loadProperties(parentProps);

        assertThat("Expected single properties returned as identity function", actual, equalTo(expected));
    }

    @Test
    public void merges_properties_from_multiple_files() throws IOException {
        File parent = new File(propertiesDir, "parent.properties");
        File child1 = new File(propertiesDir, "child1.properties");
        File child2 = new File(propertiesDir, "child2.properties");
        Files.createParentDirs(parent);
        TestUtils.write(parent, parentProps);
        TestUtils.write(child1, childProps1);
        TestUtils.write(child2, childProps2);

        PropertyBuilder builder = new PropertyBuilder();
        Properties actual = builder.merge(parent, child1, child2);
        Properties expected = TestUtils.loadProperties(expectedProps);

        assertThat("Expected properties to override by cascade", actual, equalTo(expected));
    }

}
