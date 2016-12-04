package moe.crowdhopper.imgurapi.Exceptions;

public class RateLimitException extends Exception {
	private static final long serialVersionUID = -8870011690583778897L;
	private static final String message = "You've run out of credits.";

	public RateLimitException() {
		super(message);
	}

	public RateLimitException(Throwable arg0) {
		super(message, arg0);
	}

	public RateLimitException(Throwable arg1, boolean arg2, boolean arg3) {
		super(message, arg1, arg2, arg3);
	}

}
