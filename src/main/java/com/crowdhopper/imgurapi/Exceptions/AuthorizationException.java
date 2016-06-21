package com.crowdhopper.imgurapi.Exceptions;

public class AuthorizationException extends Exception {
	private static final long serialVersionUID = 7390587587670143529L;

	public AuthorizationException() {
		super("You must be logged in to access this.");
	}

	public AuthorizationException(Throwable arg0) {
		super("You must be logged in to access this.", arg0);
	}

	public AuthorizationException(Throwable arg1, boolean arg2, boolean arg3) {
		super("You must be logged in to access this.", arg1, arg2, arg3);
	}

}
