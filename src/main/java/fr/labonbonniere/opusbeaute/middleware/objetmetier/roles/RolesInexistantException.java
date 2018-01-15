package fr.labonbonniere.opusbeaute.middleware.objetmetier.roles;

public class RolesInexistantException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public RolesInexistantException() {
		super();
	}
	
	public RolesInexistantException(String message) {
		super(message);
	}
	
	public RolesInexistantException(Throwable cause) {
		super(cause);
	}
	
	public RolesInexistantException(String message, Throwable cause) {
		super(message,cause);
	}

}
