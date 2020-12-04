package rw.jar2db.model;

public class Class extends Thing {
    public final String packageName;
    public final String simpleName;
    public final boolean isArray;
    public final boolean isPrimitive;
    public final Class componentType;
    public final Class declaringClass;
    public final Class superClass;
    public final Integer modifiers;
    public final Long majorVersion;


    public Class(String packageName, String simpleName, boolean isArray, boolean isPrimitive, Class componentType, Class declaringClass, Class superClass, Integer modifiers, Long majorVersion) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.isArray = isArray;
        this.isPrimitive = isPrimitive;
        this.componentType = componentType;
        this.declaringClass = declaringClass;
        this.superClass = superClass;
        this.modifiers = modifiers;
        this.majorVersion = majorVersion;
    }

    @Override
    public String getLongName() {
        return (packageName != null ? packageName + '.' : "") + simpleName;
    }

}
