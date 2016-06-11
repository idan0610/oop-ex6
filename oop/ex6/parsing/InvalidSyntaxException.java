package oop.ex6.parsing;

import oop.ex6.main.SJavaException;

/**
 * Represent a syntax error.
 */
public class InvalidSyntaxException extends SJavaException {
	private final static long serialVersionUID = 1L;

	/**
	 * Construct a new InvalidSyntaxException
	 * @param message the message of the exception.
	 */
	public InvalidSyntaxException(String message) {
		super(message);
	}
}
