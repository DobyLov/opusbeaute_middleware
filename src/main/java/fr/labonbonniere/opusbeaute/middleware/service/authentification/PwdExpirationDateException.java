package fr.labonbonniere.opusbeaute.middleware.service.authentification;

public class PwdExpirationDateException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public PwdExpirationDateException() {
		super();
	}

	public PwdExpirationDateException(String message) {
		super(message);
	}
	
	public PwdExpirationDateException(Throwable cause) {
		super(cause);
	}
	public PwdExpirationDateException (String message, Throwable cause) {
		super(message, cause);
	}
	
}
