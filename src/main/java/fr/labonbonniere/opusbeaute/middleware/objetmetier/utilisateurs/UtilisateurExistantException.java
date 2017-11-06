package fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs;

public class UtilisateurExistantException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UtilisateurExistantException() {
		super();
	}

	public UtilisateurExistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilisateurExistantException(String message) {
		super(message);
	}

	public UtilisateurExistantException(Throwable cause) {
		super(cause);
	}
}
