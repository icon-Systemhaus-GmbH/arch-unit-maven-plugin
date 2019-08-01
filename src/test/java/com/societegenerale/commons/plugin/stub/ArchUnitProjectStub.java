package com.societegenerale.commons.plugin.stub;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.assertj.core.util.Lists;
import org.codehaus.plexus.util.ReaderFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArchUnitProjectStub extends MavenProjectStub {
    /**
     * Default constructor
     */
    public ArchUnitProjectStub() {
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model;
        try {
            model = pomReader.read(ReaderFactory.newXmlReader(new File(getBasedir(), "test-pom.xml")));
            setModel(model);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setTestClasspathElements(Lists.emptyList());
        setGroupId(model.getGroupId());
        setArtifactId(model.getArtifactId());
        setVersion(model.getVersion());
        setName(model.getName());
        setUrl(model.getUrl());
        setPackaging(model.getPackaging());

        Build build = new Build();
        build.setFinalName(model.getArtifactId());
        build.setDirectory("../../../target/aut-target/");
        build.setSourceDirectory(getBasedir() + "/src/main/java");
        build.setOutputDirectory("../../../target/aut-target/classes");
        build.setTestSourceDirectory(getBasedir() + "/src/test/java");
        build.setTestOutputDirectory("../../../target/aut-target/test-classes");
        setBuild(build);

        List compileSourceRoots = new ArrayList();
        compileSourceRoots.add(getBasedir() + "/src/main/java");
        setCompileSourceRoots(compileSourceRoots);

        List testCompileSourceRoots = new ArrayList();
        testCompileSourceRoots.add(getBasedir() + "/src/test/java");
        setTestCompileSourceRoots(testCompileSourceRoots);
    }

    /**
     * {@inheritDoc}
     */
    public File getBasedir() {
        return new File(super.getBasedir() + "/src/test/resources/");
    }
}