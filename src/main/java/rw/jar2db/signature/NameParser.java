package rw.jar2db.signature;

public class NameParser {

    private static String typeNameToSignature(String name) {
        if (PrimitiveType.isSourceCodePrimitive(name))
            return PrimitiveType.sourceCodeToByteCode(name).toString();
        else if (name.endsWith("[]"))
            return '[' + typeNameToSignature(name.substring(0, name.length() - 2));
        else
            return 'L' + name.replace('.', '/');
    }

    public static Class parseTypeName(String className) {
        return SignatureParser.parseTypeSignature(typeNameToSignature(className));
    }

}
