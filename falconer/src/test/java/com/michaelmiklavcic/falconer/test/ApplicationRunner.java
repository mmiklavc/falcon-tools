package com.michaelmiklavcic.falconer.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.apache.falcon.entity.v0.process.Process;

import com.michaelmiklavcic.falconer.Falconer;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class ApplicationRunner {

    private File inputDir;
    private File templateDir;
    private File outDir;

    public void run(File inputDir, File templateDir, File outDir) {
        this.inputDir = inputDir;
        this.templateDir = templateDir;
        this.outDir = outDir;
        Falconer.main(new String[] {
                inputDir.getAbsolutePath(),
                templateDir.getAbsolutePath(),
                outDir.getAbsolutePath() });
    }

    public void outputsNumFiles(int n) {
        assertThat(outDir.listFiles().length, equalTo(n));
    }

    public void matchesProcessOutput(String processContent) throws JAXBException {
        Process expected = TestUtils.unmarshallProcess(processContent);
        assertThat(Arrays.asList(outDir.list()).contains(expected.getName()), equalTo(true));
    }

}
