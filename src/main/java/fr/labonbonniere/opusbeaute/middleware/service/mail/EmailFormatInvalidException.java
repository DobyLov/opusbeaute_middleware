package fr.labonbonniere.opusbeaute.middleware.service.mail;

public class EmailFormatInvalidException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public EmailFormatInvalidException() {
		super();
	}
	public EmailFormatInvalidException(String message) {
		super(message);
	}
	public EmailFormatInvalidException(Throwable cause) {
		super(cause);
	}
	public EmailFormatInvalidException(String message, Throwable cause) {
		super(message, cause);
	}
}
