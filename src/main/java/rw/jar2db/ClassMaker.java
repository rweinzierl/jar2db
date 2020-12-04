package rw.jar2db;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClassMaker {
    private final ClassPool classPool;

    public ClassMaker(ClassPool classPool) {this.classPool = classPool;}

    public CtClass makeClass(Path classFile) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(classFile, baos);
        ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
        return classPool.makeClass(in);
    }
}
