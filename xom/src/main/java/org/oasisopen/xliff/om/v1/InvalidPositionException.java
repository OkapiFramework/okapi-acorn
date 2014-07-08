package org.oasisopen.xliff.om.v1;

/**
 * Indicates that an action was using the second special character of a tag reference
 * as a normal character. For example, the start position of an insert command was pointing to
 * the second character of the tag reference.
 */
public class InvalidPositionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception with a given text.
	 * @param text Text to go with the new exception.
	 */
	public InvalidPositionException (String text) {
		super(text);
	}
	
	/**
	 * Creates a new exception with a given parent exception.
	 * @param e The parent exception.
	 */
	public InvalidPositionException (Throwable e) {
		super(e);
	}

}
