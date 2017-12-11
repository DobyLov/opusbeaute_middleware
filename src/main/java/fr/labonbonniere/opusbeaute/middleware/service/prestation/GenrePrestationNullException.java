package fr.labonbonniere.opusbeaute.middleware.service.prestation;

public class GenrePrestationNullException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GenrePrestationNullException () {
		super();
	}
	
	public GenrePrestationNullException (String message) {
		super(message);
	}
	
	public GenrePrestationNullException(Throwable cause) {
		super(cause);
	}
	
	public GenrePrestationNullException(String message, Throwable cause) {
		super(message, cause);
	}
}
