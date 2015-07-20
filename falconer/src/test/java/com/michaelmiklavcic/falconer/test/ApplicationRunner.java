package com.michaelmiklavcic.falconer.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Arrays;

import org.apache.falcon.entity.v0.Entity;
import org.apache.falcon.entity.v0.process.Process;

import com.michaelmiklavcic.falconer.Falconer;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class ApplicationRunner {

    private File mainConfig;
    private File configDir;
    private File outDir;

    public void run(File mainConfig, File configDir, File outDir) throws Exception {
        this.mainConfig = mainConfig;
        this.configDir = configDir;
        this.outDir = outDir;
        Falconer.main(new String[] {
                mainConfig.getAbsolutePath(),
                configDir.getAbsolutePath(),
                outDir.getAbsolutePath() });
    }

    public void outputsNumFiles(int n) {
        assertThat(outDir.exists(), equalTo(true));
        assertThat(outDir.listFiles().length, equalTo(n));
    }

    public void matchesProcessOutput(String processContent) throws Exception {
        final String extension = ".xml";
        Entity expected = TestUtils.unmarshallEntity(processContent);
        File actualFile = new File(outDir, expected.getName() + extension);
        assertThat(actualFile.getName(), actualFile.exists(), equalTo(true));
        Entity actual = TestUtils.unmarshallEntity(actualFile);
        assertThat(Arrays.asList(outDir.list()).contains(expected.getName() + extension), equalTo(true));
        TestUtils.assertEquals(expected, actual);
    }

    public void matchesFeedOutput(String feedOneMerged) {
    }

}
