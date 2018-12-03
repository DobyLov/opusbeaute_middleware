package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class RdvNonIntegrableException extends Exception {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	
	public RdvNonIntegrableException() {
		super();
	}
	
	public RdvNonIntegrableException(String message) {
		super(message);
	}
	
	public RdvNonIntegrableException(Throwable cause) {
		super(cause);
	}
	
	public RdvNonIntegrableException(String message, Throwable cause) {
		super(message, cause);
	}
}
