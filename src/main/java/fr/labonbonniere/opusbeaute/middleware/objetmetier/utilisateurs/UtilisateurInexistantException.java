package fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs;

public class UtilisateurInexistantException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UtilisateurInexistantException() {
		super();
	}

	public UtilisateurInexistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilisateurInexistantException(String message) {
		super(message);
	}

	public UtilisateurInexistantException(Throwable cause) {
		super(cause);
	}
}
