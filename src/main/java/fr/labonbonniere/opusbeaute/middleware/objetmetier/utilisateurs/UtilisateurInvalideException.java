package fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs;

public class UtilisateurInvalideException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UtilisateurInvalideException() {
		super();
	}

	public UtilisateurInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilisateurInvalideException(String message) {
		super(message);
	}

	public UtilisateurInvalideException(Throwable cause) {
		super(cause);
	}

}
