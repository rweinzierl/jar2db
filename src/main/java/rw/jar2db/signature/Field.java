package rw.jar2db.signature;

public class Field extends Thing {
    public final Class declaringType;
    public final String name;
    public final Class type;

    public Field(Class declaringType, String name, Class type) {
        this.declaringType = declaringType;
        this.name = name;
        this.type = type;
    }

    public String getLongName() {
        return null;
    }
}
