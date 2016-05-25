package com.store.exceptions;

public class StoreNotFoundException extends Exception {

	private static final long serialVersionUID = -1214919513066878556L;
	
	public StoreNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
