package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class NoRdvException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NoRdvException(String message) {
		super(message);
	}
	
	public NoRdvException(Throwable cause) {
		super(cause);
	}
	
	public NoRdvException(String message, Throwable cause) {
		super(message, cause);
	}

}
