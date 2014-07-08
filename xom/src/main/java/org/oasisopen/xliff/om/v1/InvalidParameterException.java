package org.oasisopen.xliff.om.v1;

/**
 * Indicates that a method was called with one or more invalid parameters.
 */
public class InvalidParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception with a given text.
	 * @param text Text to go with the new exception.
	 */
	public InvalidParameterException (String text) {
		super(text);
	}
	
	/**
	 * Creates a new exception with a given parent exception.
	 * @param e The parent exception.
	 */
	public InvalidParameterException (Throwable e) {
		super(e);
	}

}
