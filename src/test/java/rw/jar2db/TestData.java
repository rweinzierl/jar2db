package rw.jar2db;

import javassist.ClassPool;
import org.apache.maven.plugin.AbstractMojo;
import rw.jar2db.ClassFileFinder;
import rw.jar2db.ClassMaker;
import rw.jar2db.ClassSources;

import java.io.IOException;

public class TestData {
    public static ClassSources getClassSources(ClassPool classPool) throws IOException {
        ClassSources classSources = new ClassSources(classPool);

        addClasses(classSources);
        return classSources;
    }

    public static void addClasses(ClassSources classSources) throws IOException {
        java.lang.Class<?> mavenClass = AbstractMojo.class;
        java.lang.Class<?> projectClass = ClassMaker.class;

        classSources.addClassRoot(ClassFileFinder.getClassRoot(mavenClass));
        classSources.addClassRoot(ClassFileFinder.getClassRoot(projectClass));
    }
}
