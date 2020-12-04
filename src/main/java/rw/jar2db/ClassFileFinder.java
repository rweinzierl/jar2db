package rw.jar2db;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import rw.jar2db.util.Noex;
import rw.jar2db.util.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ClassFileFinder {

    public static File getClassRoot(Class<?> clazz) throws IOException {
        String file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getFile()).getCanonicalFile().toString();
        String currentDir = new File(System.getProperty("user.dir")).getCanonicalFile().toString();
        if (file.startsWith(currentDir)) file = file.substring(currentDir.length() + 1);
        return new File(file);
    }

    public static File getClassRoot(Artifact artifact) {
        return artifact.getFile();
    }

    public static File getClassRoot(MavenProject project) {
        return new File(project.getBuild().getOutputDirectory());
    }

    public static Stream<Path> getClassFiles(File classRoot) throws IOException {
        Stream<Path> classFiles;
        if (classRoot.isDirectory())
            classFiles = Files.walk(classRoot.toPath()).filter(path -> !path.toFile().isDirectory());
        else
            classFiles = Stream.of(classRoot).
                    map(Noex.Function.noex(path -> FileSystems.newFileSystem(path.toPath(), (ClassLoader)null).getRootDirectories())).
                    flatMap(StreamUtil::toStream).
                    flatMap(Noex.Function.noex(Files::walk)).
                    filter(p -> p.toString().endsWith(".class"));
        return classFiles.filter(path -> path.toString().endsWith(".class") && !path.getFileName().toString().equals("package-info.class"));
    }


}
