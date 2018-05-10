package fr.labonbonniere.opusbeaute.middleware.service.utilisateur;

public class MailNonTrouveUtilisateurException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public MailNonTrouveUtilisateurException() {
		super();
	}
	
	public MailNonTrouveUtilisateurException(String message) {
		super(message);
	}
	
	public MailNonTrouveUtilisateurException(Throwable cause) {
		super(cause);
	}
	
	public MailNonTrouveUtilisateurException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
