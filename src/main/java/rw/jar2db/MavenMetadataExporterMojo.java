package rw.jar2db;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import rw.jar2db.util.Noex;

import java.util.Optional;

@Mojo(name = "jar2db", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class MavenMetadataExporterMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "jar2db.out")
    private String outFilePath;

    public void execute() {
        Noex.exec(() -> {
            try (ModelCreator modelCreator = new ModelCreator(Optional.ofNullable(outFilePath).orElse(project.getName() + ".sqlite"))) {
                modelCreator.classSources.addMavenProject(project, true);
                modelCreator.export();
            }
        });
    }

}
