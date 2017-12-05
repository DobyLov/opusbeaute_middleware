package fr.labonbonniere.opusbeaute.middleware.service.client;

public class EmailFormatInvalid extends Exception {
	private static final long serialVersionUID = 1L;
	
	public EmailFormatInvalid() {
		super();
	}
	public EmailFormatInvalid(String message) {
		super(message);
	}
	public EmailFormatInvalid(Throwable cause) {
		super(cause);
	}
	public EmailFormatInvalid(String message, Throwable cause) {
		super(message, cause);
	}
}
