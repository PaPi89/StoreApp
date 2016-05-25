package com.store.exceptions.handlers;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.store.exceptions.InvalidRequestException;
import com.store.exceptions.InvalidSQLException;
import com.store.exceptions.StoreException;
import com.store.exceptions.StoreNotFoundException;
import com.store.exceptions.codes.ErrorCodes;
import com.store.exceptions.pojos.ErrorBody;

@ControllerAdvice
public class StoreExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(StoreException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorBody handleStoreException(
			HttpServletRequest request, Exception ex) {
		ErrorBody response = new ErrorBody();
	    response.setCode(ErrorCodes.BAD_REQUEST.name());
	    response.setMessage(ex.getMessage());
	    StringBuilder requestParams = new StringBuilder();
	    
	    response.setRequestParams(requestParams.toString());
	    return response;
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public @ResponseBody ErrorBody handleInvalidRequestException(
			HttpServletRequest request, Exception ex) {
		ErrorBody response = new ErrorBody();
	    response.setCode(ErrorCodes.INVALID_REQUEST_PARAMETERS.name());
	    response.setMessage(ex.getMessage());
	    StringBuilder requestParams = new StringBuilder();
	    response.setRequestParams(requestParams.toString());
	    return response;
	}
	
	@ExceptionHandler(InvalidSQLException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorBody handleInvalidSQLException(
			HttpServletRequest request, Exception ex) {
		ErrorBody response = new ErrorBody();
	    response.setCode(ErrorCodes.SQL_EXCEPTION.name());
	    response.setMessage(ex.getMessage());
	    StringBuilder requestParams = new StringBuilder();
	    response.setRequestParams(requestParams.toString());
	    return response;
	}
	
	@ExceptionHandler(StoreNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorBody handleStoreNotFoundException(
			HttpServletRequest request, Exception ex) {
		ErrorBody response = new ErrorBody();
	    response.setCode(ErrorCodes.STORE_NOT_FOUND.name());
	    response.setMessage(ex.getMessage());
	    StringBuilder requestParams = new StringBuilder();
	    Map<String, String[]> requestParamsMap = request.getParameterMap();
	    for(String key : requestParamsMap.keySet()){
	    	requestParams.append(key + ":" + requestParamsMap.get(key));
	    }
	    response.setRequestParams(requestParams.toString());
	    return response;
	}
}
