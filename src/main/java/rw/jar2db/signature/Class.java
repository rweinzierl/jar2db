package rw.jar2db.signature;

public class Class extends Thing {
    public final String packageName;
    public final String simpleName;
    public final boolean isArray;
    public final boolean isPrimitive;
    public final Class componentType;

    public Class(String packageName, String simpleName, boolean isArray, boolean isPrimitive, Class componentType) {
        this.simpleName = simpleName;
        this.packageName = packageName;
        this.isArray = isArray;
        this.isPrimitive = isPrimitive;
        this.componentType = componentType;
    }

    public static Class primitive(String name) {
        return new Class(null, name, false, true, null);
    }

    public static Class array(Class componentClass) {
        return new Class(null, componentClass.simpleName + "[]", true, false, componentClass);
    }
    public String getLongName() {
        return null;
    }

}
