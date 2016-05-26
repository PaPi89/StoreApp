package com.store.exceptions;

/**
 * This is exception class for Store Not Found.
 * @author parth_pithadiya
 *
 */
public class StoreNotFoundException extends Exception {

	private static final long serialVersionUID = -1214919513066878556L;
	
	public StoreNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
