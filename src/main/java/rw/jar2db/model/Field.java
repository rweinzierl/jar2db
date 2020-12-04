package rw.jar2db.model;

public class Field extends Thing {
    public final Class declaringType;
    public final String name;
    public final Class type;
    public final Integer modifiers;

    public Field(Class declaringType, String name, Class type, Integer modifiers) {
        this.declaringType = declaringType;
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    @Override
    public String getLongName() {
        return declaringType.getLongName() + '.' + name;
    }
}
