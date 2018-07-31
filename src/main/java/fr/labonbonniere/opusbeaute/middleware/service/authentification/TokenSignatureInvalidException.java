package fr.labonbonniere.opusbeaute.middleware.service.authentification;

public class TokenSignatureInvalidException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TokenSignatureInvalidException(){
		super();
	}
	
	public TokenSignatureInvalidException(String message) {
		super(message);
	}
	
	public TokenSignatureInvalidException(Throwable cause) {
		super(cause);
	}
	
	public TokenSignatureInvalidException(String message, Throwable cause) {
		super(message,cause);
	}
}
