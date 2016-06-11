package oop.ex6.variables;

import oop.ex6.main.SJavaException;

/**
 * This abstract exception class represents an exception related to variables.
 */
public class VariableException extends SJavaException {
    private final static long serialVersionUID = 1L;

    /**
     * Construct a new VariableException.
     * @param message the message of the exception.
     */
    public VariableException(String message){
        super(message);
    }
}