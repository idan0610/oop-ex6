package oop.ex6.main;

/**
 * Represent an exception in sJava file - the verifier marked it invalid.
 */
public class SJavaException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new SJavaException.
	 * @param message the message of the exception.
	 */
	public SJavaException(String message) {
		super(message);
	}
}
