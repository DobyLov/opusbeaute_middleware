package fr.labonbonniere.opusbeaute.middleware.service.praticien;

public class MailNonTrouvePraticienException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public MailNonTrouvePraticienException() {
		super();
	}
	
	public MailNonTrouvePraticienException(String message) {
		super(message);
	}
	
	public MailNonTrouvePraticienException(Throwable cause) {
		super(cause);
	}
	
	public MailNonTrouvePraticienException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
