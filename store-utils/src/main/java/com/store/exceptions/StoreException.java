package com.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

public class StoreException extends Exception {

	private static final long serialVersionUID = 6296998529379623664L;
	
	public StoreException(String errorMessage) {
		super(errorMessage);
	}
	
}
