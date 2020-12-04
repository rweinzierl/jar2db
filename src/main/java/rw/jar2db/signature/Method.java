package rw.jar2db.signature;

public class Method extends Thing {
    public final boolean isConstructor;
    public final Class declaringType;
    public final String name;
    public final Class[] argumentTypes;
    public final Class returnType;

    public Method(boolean isConstructor, Class declaringType, String name, Class[] argumentTypes, Class returnType) {
        this.isConstructor = isConstructor;
        this.name = name;
        this.declaringType = declaringType;
        this.argumentTypes = argumentTypes;
        this.returnType = returnType;
    }

    public String getLongName() {
        return null;
    }
}
