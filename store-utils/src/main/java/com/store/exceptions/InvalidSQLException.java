package com.store.exceptions;

public class InvalidSQLException extends Exception {

	private static final long serialVersionUID = -2353360584149238747L;
	
	public InvalidSQLException(String errorMessage) {
		super(errorMessage);
	}
	
}
