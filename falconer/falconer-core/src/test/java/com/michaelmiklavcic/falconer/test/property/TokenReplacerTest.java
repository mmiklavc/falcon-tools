package com.michaelmiklavcic.falconer.test.property;

import static com.michaelmiklavcic.falconer.test.util.TestUtils.write;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.util.Properties;

import org.adrianwalker.multilinestring.Multiline;
import org.junit.*;

import com.michaelmiklavcic.falconer.property.TokenReplacer;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class TokenReplacerTest {

    private File testDir;

    @Before
    public void setUp() throws Exception {
        testDir = TestUtils.createTempDir(getClass().toString());
    }

    /**
     * This is ##key1## here line 1
     * This is ##key2## here line 2
     * This is ##key3## here line 3
     */
    @Multiline private static String textFile;

    /**
     * This is value1 here line 1
     * This is value2 here line 2
     * This is value3 here line 3
     */
    @Multiline private static String expected;

    @Test
    public void token_replaces_file() throws IOException {
        Properties props = new Properties() {
            {
                setProperty("key1", "value1");
                setProperty("key2", "value2");
                setProperty("key3", "value3");
            }
        };
        File inFile = write(new File(testDir, "inFile"), textFile);
        TokenReplacer tr = new TokenReplacer();
        String actual = tr.apply(props, inFile);
        assertThat("Strings don't match", actual, equalTo(expected));
    }

}
