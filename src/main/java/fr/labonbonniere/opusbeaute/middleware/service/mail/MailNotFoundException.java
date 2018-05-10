package fr.labonbonniere.opusbeaute.middleware.service.mail;

public class MailNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public MailNotFoundException() {
		super();
	}

	public MailNotFoundException(String message) {
		super(message);
	}
	
	public MailNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public MailNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
