package com.michaelmiklavcic.falconer.maven;

import java.io.*;
import java.util.*;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import com.michaelmiklavcic.falconer.Falconer;
import com.michaelmiklavcic.falconer.property.TokenReplacer;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class FalconerMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true) private MavenProject project;

    @Parameter private List<App> apps;
    
    @Parameter(property = "falconUrl", defaultValue = "http://localhost:15000") private String falconUrl;

    // @Parameter(defaultValue = "${project.build.directory}/", readonly = true) private File falconBuildDir;

    @Parameter(property = "configFile", defaultValue = "${project.basedir}/src/main/falcon/falconer-config.json") private File configFile;

    @Parameter(property = "artifactDir", defaultValue = "${project.basedir}/src/main/falcon") private File artifactDir;

    @Parameter(property = "outDir", defaultValue = "${project.build.directory}/falcon-target") private String outDir;

    /*
     * step 1: falcon-sources - entities/ - deploy.sh
     * 
     * ...
     * 
     * step 2: tarball - entities/ - apps/ - deploy.sh
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Executing Falconer plugin");
        getLog().info("\tconfig file=" + configFile);
        getLog().info("\tartifactDir=" + artifactDir);
        getLog().info("\tbaseDir=" + project.getBuild().getDirectory());
        File outDirFile = new File(outDir);
        if (!outDirFile.isAbsolute()) {
            outDirFile = new File(project.getBuild().getDirectory(), outDirFile.toString());
        }
        getLog().info("outDir=" + outDirFile.getAbsolutePath());

        if (!outDirFile.exists()) {
            outDirFile.mkdirs();
        }

        File entitiesDir = new File(outDirFile, "entities");
        File appsDir = new File(outDirFile, "apps");

        getLog().info("Copying apps");
        for (App app : apps) {
            File appSrc = new File(project.getBasedir(), app.getSrc());
            getLog().info("app src=" + appSrc.getAbsolutePath());
            try {
                FileUtils.copyDirectory(appSrc, appsDir);
            } catch (IOException e) {
                throw new MojoFailureException("Failed to copy applications from " + appSrc, e);
            }
        }

        getLog().info("Generating deploy tool");

        getLog().info("deploy script exists?=" + this.getClass().getClassLoader().getResource("deploy.sh").getFile());
        getLog().info("deploy script=" + new File(this.getClass().getClassLoader().getResource("deploy.sh").getFile()));

        Properties props = new Properties();
        props.setProperty("falcon-url", falconUrl);
        TokenReplacer r = new TokenReplacer();
        String result = "";
        try {
            result = r.apply(props, this.getClass().getClassLoader().getResourceAsStream("deploy.sh"));
        } catch (IOException e) {
            getLog().error("Falconer exception caught", e);
            throw new MojoFailureException("Falconer exception caught", e);
        }

        try {
            FileUtils.write(new File(outDirFile, "deploy.sh"), result);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to write deploy script ", e);
        }

        getLog().info("running Falconer tool");
        try {
            Falconer.main(new String[] {
                    configFile.getAbsolutePath(),
                    artifactDir.getAbsolutePath(),
                    entitiesDir.getAbsolutePath() });
        } catch (JAXBException e) {
            getLog().error("Falconer exception caught", e);
            throw new MojoFailureException("Falconer exception caught", e);
        }

    }

}
