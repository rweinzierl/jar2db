package rw.jar2db.signature;

import rw.jar2db.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SignatureParser {

    public static Class parseTypeSignature(String signature) {
        return parseTypeSignature0(signature)._1;
    }

    public static Pair<Class[], Class> parseMethodSignature(String signature) {
        signature = signature.substring(1);
        List<Class> args = new ArrayList<>();
        while (signature.charAt(0) != ')') {
            Pair<Class, Integer> p = parseTypeSignature0(signature);
            args.add(p._1);
            signature = signature.substring(p._2);
        }
        signature = signature.substring(1);
        Class retval = parseTypeSignature(signature);
        return new Pair<>(args.toArray(new Class[0]), retval);
    }

    private static Pair<Class, Integer> parseTypeSignature0(String signature) {
        char firstCharacter = signature.charAt(0);
        if (PrimitiveType.isByteCodePrimitive(firstCharacter))
            return new Pair<>(Class.primitive(PrimitiveType.byteCodeToSourceCode(firstCharacter)), 1);
        else switch (firstCharacter) {
            case 'V':
                ;
                return new Pair<>(null, 1);
            case '[':
                ;
                Pair<Class, Integer> elementSignature = parseTypeSignature0(signature.substring(1));
                return new Pair<>(Class.array(elementSignature._1), 1 + elementSignature._2);
            case 'L':
                int firstSemicolon = signature.indexOf(';');
                signature = signature.substring(1, firstSemicolon >= 0 ? firstSemicolon : signature.length()).replace('/', '.');
                int lastDot = signature.lastIndexOf('.');
                String simpleName = signature.substring(lastDot + 1);
                String packageName = signature.substring(0, lastDot);
                return new Pair<>(new Class(packageName, simpleName, false, false, null), 1 + signature.length() + (firstSemicolon >= 0 ? 1 : 0));
            default:
                throw new RuntimeException(signature);
        }
    }

}
