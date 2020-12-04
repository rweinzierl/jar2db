package rw.jar2db;

import javassist.CtClass;
import javassist.Modifier;

public class Modifiers {
    public static Boolean hasModifier(Integer modifiers, int modifier) {
        if (modifiers == null) return null;
        else
            return (modifiers & modifier) != 0;
    }

    public static String getVisibility(Integer modifiers) {
        if (modifiers == null)
            return null;
        else if (hasModifier(modifiers, Modifier.PUBLIC))
            return "public";
        else if (hasModifier(modifiers, Modifier.PROTECTED))
            return "protected";
        else if (hasModifier(modifiers, Modifier.PRIVATE))
            return "private";
        else
            return "package";
    }

    public static String getClassType(Integer modifiers) {
        if (modifiers == null)
            return null;
        else if (hasModifier(modifiers, Modifier.INTERFACE))
            return "interface";
        else if (hasModifier(modifiers, Modifier.ENUM))
            return "enum";
        else if (hasModifier(modifiers, Modifier.ANNOTATION))
            return "annotation";
        else
            return "class";
    }

}
