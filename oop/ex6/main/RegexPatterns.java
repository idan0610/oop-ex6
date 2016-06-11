package oop.ex6.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This enum is used to store all the regex patterns used in the program.
 */
public enum RegexPatterns {
    POSSIBLE_WHITE_SPACES(RegexStrings.POSSIBLE_WHITE_SPACES),
    METHOD_DECLARATION(RegexStrings.METHOD_DECLARATION),
    VARIABLE_DECLARATION(RegexStrings.VARIABLE_DECLARATION),
    VARIABLE_ASSIGNMENT(RegexStrings.VARIABLE_ASSIGNMENT),
    IF_STATEMENT(RegexStrings.IF_STATEMENT),
    WHILE_STATEMENT(RegexStrings.WHILE_STATEMENT),
    RETURN_STATEMENT(RegexStrings.RETURN_STATEMENT),
    DOUBLE(RegexStrings.DOUBLE),
    INTEGER(RegexStrings.INTEGER),
    CHARACTER(RegexStrings.CHARACTER),
    STRING(RegexStrings.STRING),
    BOOLEAN(RegexStrings.BOOLEAN),
    METHOD_CALL(RegexStrings.METHOD_CALL);

    private Pattern pattern;

    /**
     * Initiate an enum variable.
     * @param regex The requested enum variable.
     */
    RegexPatterns(String regex){
        this.pattern = Pattern.compile(regex);
    }

    /**
     * This method match the given string to the current Type acceptable pattern.
     * @param input the input string to match.
     * @return true if the string match, false otherwise.
     */
    public Matcher matcher(String input){
        return pattern.matcher(input);
    }
}
