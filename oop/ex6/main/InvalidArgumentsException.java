package oop.ex6.main;

import java.io.IOException;

/**
 * Created by Alon on 14/6/2015.
 */
public class InvalidArgumentsException extends IOException {
    private final static long serialVersionUID = 1L;
    
    /**
     * Initialize the exception
     * @param message details of the exception.
     */
    public InvalidArgumentsException(String message){
        super(message);
    }
}
