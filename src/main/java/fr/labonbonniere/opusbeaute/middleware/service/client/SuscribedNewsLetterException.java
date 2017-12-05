package fr.labonbonniere.opusbeaute.middleware.service.client;

public class SuscribedNewsLetterException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public SuscribedNewsLetterException () {
		super();
	}
	public SuscribedNewsLetterException(String message) {
		super(message);
	}
	public SuscribedNewsLetterException (Throwable cause) {
		super(cause);
	}
	public SuscribedNewsLetterException(String message, Throwable cause) {
		super(message, cause);
	
	}
}
