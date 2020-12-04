package rw.jar2db.model;

import javassist.*;
import rw.jar2db.signature.NameParser;
import rw.jar2db.signature.SignatureParser;
import rw.jar2db.util.Noex;
import rw.jar2db.util.Pair;

import java.util.Optional;
import java.util.stream.Stream;

public class FromJavassist {
    private final ClassPool classPool;

    public FromJavassist(ClassPool classPool) {this.classPool = classPool;}

    private Optional<CtClass> getFromPool(rw.jar2db.signature.Class c0) {
        return c0 == null ? Optional.empty() : Noex.silent(() -> classPool.get(c0.getLongName()));
    }

    private Class convert(rw.jar2db.signature.Class c0) {
        return c0 == null ? null : new Class(c0.packageName, c0.simpleName, c0.isArray, c0.isPrimitive, get(c0.componentType), null, null, null, null);
    }

    private Class get(rw.jar2db.signature.Class c0) {
        return getFromPool(c0).map(this::get).orElse(convert(c0));
    }

    public Class get(CtClass clazz) {
        rw.jar2db.signature.Class c0 = NameParser.parseTypeName(clazz.getName());
        Class componentType = get(c0.componentType);
        Class declaringClass = Noex.silent(clazz::getDeclaringClass).map(this::get).orElse(null);
        Class superClass = Noex.silent(clazz::getSuperclass).map(this::get).orElse(null);
        return new Class(c0.packageName, c0.simpleName, c0.isArray, c0.isPrimitive, componentType, declaringClass, superClass, clazz.getModifiers(), (long) clazz.getClassFile2().getMajorVersion());
    }

    public Field get(CtField field) {
        return new Field(
                get(field.getDeclaringClass()),
                field.getName(),
                get(SignatureParser.parseTypeSignature(field.getSignature())),
                field.getModifiers());
    }

    public Method get(CtBehavior method) {
        Pair<rw.jar2db.signature.Class[], rw.jar2db.signature.Class> signature = SignatureParser.parseMethodSignature(method.getSignature());
        return new Method(
                method instanceof CtConstructor,
                get(method.getDeclaringClass()),
                method.getName(),
                Stream.of(signature._1).map(this::get).toArray(Class[]::new),
                get(signature._2),
                method.getModifiers());
    }

    public Method getMethod(boolean isConstructor, String className, String methodName, String signature) {
        rw.jar2db.signature.Class declaringClazz = NameParser.parseTypeName(className);
        Pair<rw.jar2db.signature.Class[], rw.jar2db.signature.Class> args = SignatureParser.parseMethodSignature(signature);
        return new Method(
                isConstructor,
                get(declaringClazz),
                methodName,
                Stream.of(args._1).map(this::get).toArray(Class[]::new),
                get(args._2),
                null);
    }

    public Field getField(String className, String fieldName, String signature) {
        rw.jar2db.signature.Class declaringClazz = NameParser.parseTypeName(className);
        rw.jar2db.signature.Class clazz = SignatureParser.parseTypeSignature(signature);
        return new Field(
                get(declaringClazz),
                fieldName,
                get(clazz),
                null);
    }

}
