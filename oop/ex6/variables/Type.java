package oop.ex6.variables;

import oop.ex6.main.RegexStrings;
import oop.ex6.main.ReservedWords;

import java.util.regex.Pattern;

/**
 * This enum represents the different types of variables.
 * Each type also has a regex pattern that represents it.
 */
public enum Type {
    INTEGER(RegexStrings.INTEGER),
    DOUBLE(RegexStrings.DOUBLE),
    CHARACTER(RegexStrings.CHARACTER),
    STRING(RegexStrings.STRING),
    BOOLEAN(RegexStrings.BOOLEAN);

    private Pattern pattern;

    Type(String regex){
        this.pattern = Pattern.compile(regex);
    }

    /**
     * @param str the string to match.
     * @return true if the string matches the Type regex pattern, and false otherwise.
     */
    public boolean accept(String str){
        return this.pattern.matcher(str).matches();
    }

    /**
     * The method converts a type represented with a string to a Type enum.
     * The string should represent the type's name (not a variable accepted value, etc.).
     *
     * @param typeStr the string represents the type's name.
     * @return the corresponding type. null if the string doesn't represent a type.
     */
    public static Type getTypeFromString(String typeStr){
        switch (typeStr){
            case ReservedWords.INT:
                return INTEGER;
            case ReservedWords.DOUBLE:
                return DOUBLE;
            case ReservedWords.BOOLEAN:
                return BOOLEAN;
            case ReservedWords.CHAR:
                return CHARACTER;
            case ReservedWords.STRING:
                return STRING;
        }

        return null;    // The string does not represent any type.
    }
}
