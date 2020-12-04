package rw.jar2db.signature;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PrimitiveType {
    Z("boolean"), C("char"), B("byte"), S("short"), I("int"), J("long"), F("float"), D("double");

    private final String nameInSourceCode;

    PrimitiveType(String nameInSourceCode) {this.nameInSourceCode = nameInSourceCode;}

    public Character nameInByteCode() {return name().charAt(0);}

    public String nameInSourceCode() {return nameInSourceCode;}

    private static Map<String, Character> sourceCodeToByteCode = Arrays.stream(values()).collect(Collectors.toMap(PrimitiveType::nameInSourceCode, PrimitiveType::nameInByteCode));
    private static Map<Character, String> byteCodeToSourceCode = Arrays.stream(values()).collect(Collectors.toMap(PrimitiveType::nameInByteCode, PrimitiveType::nameInSourceCode));

    public static String byteCodeToSourceCode(Character nameInByteCode) {return byteCodeToSourceCode.get(nameInByteCode);}

    public static Character sourceCodeToByteCode(String nameInSourceCode) {return sourceCodeToByteCode.get(nameInSourceCode);}

    public static boolean isByteCodePrimitive(Character nameInByteCode) {return byteCodeToSourceCode.containsKey(nameInByteCode);}

    public static boolean isSourceCodePrimitive(String nameInSourceCode) {return sourceCodeToByteCode.containsKey(nameInSourceCode);}
}
