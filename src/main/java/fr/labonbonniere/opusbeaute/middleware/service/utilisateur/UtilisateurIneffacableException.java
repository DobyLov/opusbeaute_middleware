package fr.labonbonniere.opusbeaute.middleware.service.utilisateur;

public class UtilisateurIneffacableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UtilisateurIneffacableException() {
		super();
	}
	
	public UtilisateurIneffacableException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UtilisateurIneffacableException(String message) {
		super (message);
	}
	
	public UtilisateurIneffacableException(Throwable cause) {
		super(cause);
	}
	
}
