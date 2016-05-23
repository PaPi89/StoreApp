package com.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="General Exception")
public class StoreException extends Exception {

	private static final long serialVersionUID = 6296998529379623664L;

}
