package fr.labonbonniere.opusbeaute.middleware.service.rgpd;

public class EmptyListException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public EmptyListException() {
		super();
	}
	
	public EmptyListException(String message) {
		super(message);
	}
	
	public EmptyListException(Throwable cause) {
		super(cause);
	}
	
	public EmptyListException(String message, Throwable cause) {
		super(message, cause);
	}
}
