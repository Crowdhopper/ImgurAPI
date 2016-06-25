package com.crowdhopper.imgurapi.Exceptions;

public class InvalidParameterException extends Exception {
	private static final long serialVersionUID = -1797677918968260165L;
	private static final String DEFAULT = "That was an invalid parameter.";

	public InvalidParameterException() {
		super(DEFAULT);
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(Throwable cause) {
		super(DEFAULT, cause);
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParameterException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}