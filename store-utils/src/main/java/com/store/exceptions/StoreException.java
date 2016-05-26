package com.store.exceptions;

/**
 * This is exception class for any generic exception.
 * @author parth_pithadiya
 *
 */
public class StoreException extends Exception {

	private static final long serialVersionUID = 6296998529379623664L;

	public StoreException(String errorMessage) {
		super(errorMessage);
	}

}
