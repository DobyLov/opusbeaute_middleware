package fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations;

public class PrestationExistanteException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PrestationExistanteException() {
		super();
	}

	public PrestationExistanteException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrestationExistanteException(String message) {
		super(message);
	}

	public PrestationExistanteException(Throwable cause) {
		super(cause);
	}

}
