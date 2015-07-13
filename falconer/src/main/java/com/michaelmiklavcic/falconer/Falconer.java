package com.michaelmiklavcic.falconer;

import java.io.*;
import java.util.*;

import javax.xml.bind.*;

import org.apache.falcon.entity.v0.process.Process;
import org.xml.sax.SAXException;

import com.michaelmiklavcic.falconer.entity.*;

public class Falconer {
 
    // TODO need to token replace in the template files first before merging properties if 
    // we want to continue using the jaxb client classes
    public static void main(String[] args) throws FileNotFoundException, IOException, JAXBException, SAXException {
        final File inputDir = new File(args[0]);
        final File templatesDir = new File(args[1]);
        final File outputDir = new File(args[2]);
        Falconer falconer = new Falconer();
        falconer.generate(inputDir, templatesDir, outputDir);
    }

    public void generate(File propertiesDir, File templateDir, File outDir) throws FileNotFoundException, IOException, JAXBException,
            SAXException {
        PropertyBuilder pb = new PropertyBuilder();
        List<File> childProps = new ArrayList<File>();
        File parent = null;
        for (File f : propertiesDir.listFiles()) {
            if (f.isFile()) {
                if (f.getName().toLowerCase().contains("parent")) {
                    parent = f;
                } else {
                    childProps.add(f);
                }
            }
        }
        Properties mergedProps = pb.merge(parent, childProps.toArray(new File[] {}));

        List<File> childTemplates = new ArrayList<File>();
        File parentTemplate = null;
        for (File f : templateDir.listFiles()) {
            if (f.isFile()) {
                if (f.getName().toLowerCase().contains("parent")) {
                    parentTemplate = f;
                } else {
                    childTemplates.add(f);
                }
            }
        }
        EntityBuilder eb = new EntityBuilder();
        Process mergedProcess = eb.merge(parentTemplate, childTemplates.get(0));
        TokenReplacer replacer = new TokenReplacer(mergedProps);
        Process finalProcess = replacer.apply(marshall(mergedProcess));
        outDir.mkdirs();
        marshall(finalProcess, new File(outDir, finalProcess.getName() + ".xml"));
    }

    private String marshall(Process mergedProcess) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Process.class);
        Marshaller marshaller = jc.createMarshaller();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(mergedProcess, out);
        return out.toString();
    }

    private void marshall(Process mergedProcess, File out) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Process.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.marshal(mergedProcess, out);
    }
}
