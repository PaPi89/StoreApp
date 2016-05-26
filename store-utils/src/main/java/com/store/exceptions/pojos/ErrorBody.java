package com.store.exceptions.pojos;

/**
 * POJO which will be thrown to end client when any error occurred.
 * 
 * @author parth_pithadiya
 *
 */
public class ErrorBody {

	private String code, message, requestParams;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

}
