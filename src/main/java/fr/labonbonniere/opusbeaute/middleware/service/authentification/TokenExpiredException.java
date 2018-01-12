package fr.labonbonniere.opusbeaute.middleware.service.authentification;

public class TokenExpiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public TokenExpiredException() {
		super();
	}

	public TokenExpiredException(String message) {
		super(message);
	}

	public TokenExpiredException(Throwable cause) {
		super(cause);
	}

	public TokenExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

}
