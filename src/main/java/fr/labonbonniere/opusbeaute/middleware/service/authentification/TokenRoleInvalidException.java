package fr.labonbonniere.opusbeaute.middleware.service.authentification;

public class TokenRoleInvalidException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public TokenRoleInvalidException() {
		super();
	}
	
	public TokenRoleInvalidException(String message) {
		super(message);
	}
	
	public TokenRoleInvalidException(Throwable cause) {
		super(cause);
	}
	
	public TokenRoleInvalidException(String message, Throwable cause) {
		super(message, cause);
	}
}
