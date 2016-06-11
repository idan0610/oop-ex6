package oop.ex6.method_signatures;

import oop.ex6.variables.Type;

/**
 * A factory for MethodSignature.
 */
public class MethodSignatureFactory {
    /**
     * Create a new MethodSignature according to the given array of types.
     *
     * @param types the array of types to set with.
     * @return the new created MethodSignature.
     */
    public static MethodSignature createMethodSignature(String[] types){
        Type[] acceptedTypes = new Type[types.length];

        for (int i = 0; i < types.length; i++)
            acceptedTypes[i] = Type.getTypeFromString(types[i]);

        return new MethodSignature(acceptedTypes);
    }
}
