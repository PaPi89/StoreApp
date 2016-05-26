package com.store.exceptions;

/**
 * This is exception class for any Invalid Request.
 * @author parth_pithadiya
 *
 */
public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = 9049312411561676887L;
	
	public InvalidRequestException(String message) {
		super(message);
	}

}
