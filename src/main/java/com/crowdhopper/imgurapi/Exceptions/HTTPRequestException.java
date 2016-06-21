package com.crowdhopper.imgurapi.Exceptions;

//Thrown when the status code from an HTTP Request was anything other than 200, indicating an error.
public class HTTPRequestException extends Exception {
	private static final long serialVersionUID = -6162953634325143247L;

	public HTTPRequestException(int status_code) {
		super(status(status_code));
	}

	public HTTPRequestException(String arg0) {
		super(arg0);
	}

	public HTTPRequestException(Throwable arg0) {
		super(arg0);
	}

	public HTTPRequestException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public HTTPRequestException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
	
	private static String status(int status_code) {
		switch(status_code) {
		case 400:
			return "Missing or Invalid Parameter";
		case 401:
			return "Requires User Authentication";
		case 403:
			return "Forbidden";
		case 404:
			return "Resource Does Not Exist";
		case 429:
			return "Rate Limiting";
		case 500:
			return "Internal Error";
		default:
			return "Something went wrong.";
		}
	}
}
