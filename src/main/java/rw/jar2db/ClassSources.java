package rw.jar2db;

import javassist.ClassPool;
import javassist.CtClass;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import rw.jar2db.util.Noex;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class ClassSources {

    private final ClassMaker maker;

    private final Map<ClassSource, List<CtClass>> sourceClassesMap = new LinkedHashMap<>();

    public ClassSources() {this(ClassPool.getDefault());}

    public ClassSources(ClassPool classPool) {this.maker = new ClassMaker(classPool);}

    private List<CtClass> getClasses(ClassSource classSource) {
        return sourceClassesMap.computeIfAbsent(classSource, __ -> new ArrayList<>());
    }

    private void addClass(ClassSource classSource, CtClass clazz) {
        getClasses(classSource).add(clazz);
    }

    public ClassSources addClassRoot(File classRoot) throws IOException {
        return addClassRoot(ClassSource.local(classRoot.toString()), classRoot);
    }

    public ClassSources addClassRoot(ClassSource classSource, File classRoot) throws IOException {
        ClassFileFinder.getClassFiles(classRoot).map(Noex.Function.noex(maker::makeClass)).forEach(clazz -> addClass(classSource, clazz));
        return this;
    }

    public ClassSources addMavenArtifact(Artifact artifact) throws IOException {
        return addClassRoot(ClassSource.maven(artifact), ClassFileFinder.getClassRoot(artifact));
    }

    public ClassSources addMavenProject(MavenProject project, boolean includeDependencies) throws IOException {
        addClassRoot(ClassSource.maven(project), ClassFileFinder.getClassRoot(project));
        if (includeDependencies)
            ((Set<Artifact>) project.getArtifacts()).forEach(Noex.Consumer.noex(this::addMavenArtifact));
        return this;
    }

    public Map<ClassSource, List<CtClass>> getSourceClassesMap() {
        return sourceClassesMap;
    }

    public Map<CtClass, ClassSource> getClassSourceMap() {
        Map<CtClass, ClassSource> retval = new LinkedHashMap<>();
        sourceClassesMap.forEach((source, classes) -> classes.forEach(clazz -> retval.put(clazz, source)));
        return retval;
    }

    public Stream<ClassSource> getSources() {
        return sourceClassesMap.keySet().stream();
    }

    public Stream<CtClass> getClasses() {
        return getClassSourceMap().keySet().stream();
    }

}
