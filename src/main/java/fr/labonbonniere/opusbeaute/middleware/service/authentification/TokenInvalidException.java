package fr.labonbonniere.opusbeaute.middleware.service.authentification;



public class TokenInvalidException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TokenInvalidException() {
		super();
	}
	
	public TokenInvalidException (String message) {
		super(message);
	}
	
	public TokenInvalidException (Throwable cause) {
		super(cause);
	}
	
	public TokenInvalidException (String message, Throwable cause) {
		super(message,cause);
	}

}
