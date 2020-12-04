package rw.jar2db.model;


import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Method extends Thing {
    public final boolean isConstructor;
    public final Class declaringType;
    public final String name;
    public final Class[] argumentTypes;
    public final Class returnType;
    public final Integer modifiers;

    public Method(boolean isConstructor, Class declaringType, String name, Class[] argumentTypes, Class returnType, Integer modifiers) {
        this.isConstructor = isConstructor;
        this.declaringType = declaringType;
        this.name = name;
        this.argumentTypes = argumentTypes;
        this.returnType = returnType;
        this.modifiers = modifiers;
    }

    @Override
    public String getLongName() {
        return declaringType.getLongName() + '.' + name + '(' + Stream.of(argumentTypes).map(Class::getLongName).collect(Collectors.joining(", ")) + ')';
    }
}
