package fr.labonbonniere.opusbeaute.middleware.service.authentification;



public class TokenInvalideException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TokenInvalideException() {
		super();
	}
	
	public TokenInvalideException (String message) {
		super(message);
	}
	
	public TokenInvalideException (Throwable cause) {
		super(cause);
	}
	
	public TokenInvalideException (String message, Throwable cause) {
		super(message,cause);
	}

}
