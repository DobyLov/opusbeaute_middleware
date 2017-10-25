package fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations;

public class PrestationInexistanteException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PrestationInexistanteException() {
		super();
	}

	public PrestationInexistanteException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrestationInexistanteException(String message) {
		super(message);
	}

	public PrestationInexistanteException(Throwable cause) {
		super(cause);
	}

}
