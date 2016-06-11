package oop.ex6.main;

/**
 * This class is used to store all the regex strings used in this program.
 * The RegexPatterns enum uses this class to initialize its members.
 */
public class RegexStrings {
	// Regular expressions to catch each type's valid argument.
    public static final String INTEGER = "-?\\d+";
    public static final String DOUBLE = "-?\\d*\\.?\\d+";
    public static final String CHARACTER = "\'.{1}\'";
    public static final String STRING = "\".*\"";
    public static final String BOOLEAN = ReservedWords.TRUE + "|" + ReservedWords.FALSE + "|" + DOUBLE;
	
    // Regular expressions to catch white spaces.
    public static final String POSSIBLE_WHITE_SPACES = "\\s*";
    public static final String WHITE_SPACES = "\\s+";

    public static final String VARIABLE_NAME = "(?:_\\w+|[a-zA-Z]+\\w*)";

    private static final String METHOD_NAME = "[a-zA-Z]+\\w*";

    private static final String VARIABLE_TYPE = "(?:" + ReservedWords.INT + "|" + ReservedWords.DOUBLE + "|"
            + ReservedWords.BOOLEAN + "|" + ReservedWords.CHAR + "|" + ReservedWords.STRING + ")";

    // EXPRESSION is used to catch variable assignment values which are legal.
    // They can be actual values, or another variables, etc.
    private static final String EXPRESSION = "(?:" + CHARACTER + "|" + STRING +
    		"|" + BOOLEAN + "|" + VARIABLE_NAME + ")";
    
    public static final String METHOD_DECLARATION = ReservedWords.VOID +
            WHITE_SPACES + "("+ METHOD_NAME +")" + POSSIBLE_WHITE_SPACES + "\\(("
            + "(?:" + POSSIBLE_WHITE_SPACES + ")|(?:" + "(?:" + POSSIBLE_WHITE_SPACES +
            "(?:" + ReservedWords.FINAL + WHITE_SPACES + ")?" +
            VARIABLE_TYPE + WHITE_SPACES  + VARIABLE_NAME + POSSIBLE_WHITE_SPACES +
            ReservedWords.COMMA + POSSIBLE_WHITE_SPACES + ")*" + POSSIBLE_WHITE_SPACES +
            "(?:"  + ReservedWords.FINAL + WHITE_SPACES + ")?" +
            VARIABLE_TYPE  + WHITE_SPACES + VARIABLE_NAME + POSSIBLE_WHITE_SPACES +")"+
             ")\\)" + POSSIBLE_WHITE_SPACES + "\\" + ReservedWords.BEGIN_SCOPE;

    public static final String VARIABLE_DECLARATION = "(" + ReservedWords.FINAL + "\\s+)?" +
             "(" + VARIABLE_TYPE + ")"  + WHITE_SPACES + "(" +
            "(:?" + VARIABLE_NAME + "(?:" + POSSIBLE_WHITE_SPACES + ReservedWords.ASSIGNMENT +
            POSSIBLE_WHITE_SPACES + EXPRESSION + ")?" + POSSIBLE_WHITE_SPACES + ReservedWords.COMMA +
            POSSIBLE_WHITE_SPACES +")*" +
            VARIABLE_NAME + "(?:" + POSSIBLE_WHITE_SPACES + ReservedWords.ASSIGNMENT +
            POSSIBLE_WHITE_SPACES + EXPRESSION + ")?"     + ")" +
            POSSIBLE_WHITE_SPACES + ReservedWords.END_LINE;

    public static final String VARIABLE_ASSIGNMENT = "(" + VARIABLE_NAME + ")" + POSSIBLE_WHITE_SPACES +
    		ReservedWords.ASSIGNMENT + POSSIBLE_WHITE_SPACES + "("  + EXPRESSION + ")" +
            POSSIBLE_WHITE_SPACES + ReservedWords.END_LINE;

    public static final String VALID_SUBCONDITION = ReservedWords.TRUE + "|" + ReservedWords.FALSE +
    		"|[a-zA-Z_]\\w*|" + DOUBLE;
    
    public static final String OR = "\\|\\|";
    public static final String AND = "&&";
    
    public static final String CONDITION = POSSIBLE_WHITE_SPACES + "\\(" + POSSIBLE_WHITE_SPACES
            + "(((:?(:?" + VALID_SUBCONDITION + ")?" + POSSIBLE_WHITE_SPACES + OR + POSSIBLE_WHITE_SPACES + 
            ")?|(:?(:?" + VALID_SUBCONDITION + ")?" + POSSIBLE_WHITE_SPACES + AND + POSSIBLE_WHITE_SPACES 
            + ")?)*(:?" + VALID_SUBCONDITION + ")?)"
            + POSSIBLE_WHITE_SPACES + "\\)" + POSSIBLE_WHITE_SPACES + "\\{";
    
    public static final String IF_STATEMENT = "if" + CONDITION;

    public static final String WHILE_STATEMENT = "while" + CONDITION;

    public static final String RETURN_STATEMENT = ReservedWords.RETURN + POSSIBLE_WHITE_SPACES +
                                                  ReservedWords.END_LINE;
    
    public static final String SEPERATE_CONDITIONS = "\\s*\\|\\|\\s*|\\s*&&\\s*";
    
    public static final String SEPERATE_ARGUMENTS = "\\s*,\\s*";
    
    public static final String METHOD_CALL = "(" + METHOD_NAME + ")" + POSSIBLE_WHITE_SPACES + "\\(" +
    		POSSIBLE_WHITE_SPACES + "(.*)" + POSSIBLE_WHITE_SPACES + "\\)" + POSSIBLE_WHITE_SPACES +
    		ReservedWords.END_LINE;
}