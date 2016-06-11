package oop.ex6.method_signatures;

import oop.ex6.main.SJavaException;

/**
 * This exception class represents an exception related to methods.
 */
public class MethodException extends SJavaException {
    public static final String NO_METHOD_EXIST = "Method does not exist";
    public static final String INVALID_ARGUMANTS = "Invalid arguments";
    public static final String MISSING_RETURN_STATEMENT = "Missing return statement";

    private final static long serialVersionUID = 1L;

    /**
     * Construct a new MethodException.
     * @param message the message of the exception.
     */
    public MethodException(String message){
        super(message);
    }
}