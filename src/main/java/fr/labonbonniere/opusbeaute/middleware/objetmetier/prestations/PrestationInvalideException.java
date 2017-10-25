package fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations;

public class PrestationInvalideException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PrestationInvalideException() {
		super();
	}

	public PrestationInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrestationInvalideException(String message) {
		super(message);
	}

	public PrestationInvalideException(Throwable cause) {
		super(cause);
	}

}

