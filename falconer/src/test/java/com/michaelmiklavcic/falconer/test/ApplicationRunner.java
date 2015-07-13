package com.michaelmiklavcic.falconer.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.*;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.apache.falcon.entity.v0.process.Process;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.Falconer;
import com.michaelmiklavcic.falconer.test.util.TestUtils;

public class ApplicationRunner {

    private File inputDir;
    private File templateDir;
    private File outDir;

    public void run(File inputDir, File templateDir, File outDir) throws FileNotFoundException, IOException, JAXBException, SAXException {
        this.inputDir = inputDir;
        this.templateDir = templateDir;
        this.outDir = outDir;
        Falconer.main(new String[] {
                inputDir.getAbsolutePath(),
                templateDir.getAbsolutePath(),
                outDir.getAbsolutePath() });
    }

    public void outputsNumFiles(int n) {
        assertThat(outDir.exists(), equalTo(true));
        assertThat(outDir.listFiles().length, equalTo(n));
    }

    public void matchesProcessOutput(String processContent) throws JAXBException, UnsupportedEncodingException, FileNotFoundException {
        final String extension = ".xml";
        Process expected = TestUtils.unmarshallProcess(processContent);
        assertThat(Arrays.asList(outDir.list()).contains(expected.getName() + extension), equalTo(true));
        TestUtils.assertEquals(expected, TestUtils.unmarshallProcess(new File(outDir, expected.getName() + extension)));
    }

}
