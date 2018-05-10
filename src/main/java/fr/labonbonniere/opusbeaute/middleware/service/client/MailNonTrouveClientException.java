package fr.labonbonniere.opusbeaute.middleware.service.client;

public class MailNonTrouveClientException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public MailNonTrouveClientException() {
		super();
	}
	
	public MailNonTrouveClientException(String message) {
		super(message);
	}
	
	public MailNonTrouveClientException(Throwable cause) {
		super(cause);
	}
	
	public MailNonTrouveClientException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
