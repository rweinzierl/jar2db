package rw.jar2db;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

public class ClassSource {

    public enum Type {
        Jre, Maven, Local
    }

    public final Type type;
    public String groupId;
    public String artifactId;
    public String version;
    public String localPath;

    public ClassSource(Type type) {this.type = type;}

    public static ClassSource jre = new ClassSource(Type.Jre);

    public static ClassSource maven(String groupId, String artifactId, String version) {
        ClassSource classSource = new ClassSource(Type.Maven);
        classSource.groupId = groupId;
        classSource.artifactId = artifactId;
        classSource.version = version;
        return classSource;
    }

    public static ClassSource local(String localPath) {
        ClassSource classSource = new ClassSource(Type.Local);
        classSource.localPath = localPath;
        return classSource;
    }

    public static ClassSource maven(Artifact artifact) {
        return maven(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
    }

    public static ClassSource maven(MavenProject project) {
        return maven(project.getArtifact());
    }
}
