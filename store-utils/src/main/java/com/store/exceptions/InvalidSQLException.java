package com.store.exceptions;

/**
 * This is exception class for any Invalid SQL Operations.
 * @author parth_pithadiya
 *
 */
public class InvalidSQLException extends Exception {

	private static final long serialVersionUID = -2353360584149238747L;
	
	public InvalidSQLException(String errorMessage) {
		super(errorMessage);
	}
	
}
