package com.michaelmiklavcic.falconer.maven;

import java.io.File;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class FalconerMojo extends AbstractMojo {

    @Parameter(property = "configFile", defaultValue = "${project.basedir}/src/main/falcon/falconer-config.json") private File configFile;

    @Parameter(property = "artifactDir", defaultValue = "${project.basedir}/src/main/falcon/artifacts") private File artifactDir;

    @Parameter(property = "outDir", defaultValue = "${project.build.directory}/entities") private File outDir;

    @Component private Shape shape;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Hello from Falconer!");
        getLog().info("config file=" + configFile);
        getLog().info("artifactDir=" + artifactDir);
        getLog().info("outDir=" + outDir);
        getLog().info("Shape message is: " + shape.sayHello());
    }

}
