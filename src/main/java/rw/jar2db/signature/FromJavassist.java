package rw.jar2db.signature;

import javassist.CtClass;

public class FromJavassist {

    public static Class get(CtClass clazz) {
        return NameParser.parseTypeName(clazz.getName());
    }

}
