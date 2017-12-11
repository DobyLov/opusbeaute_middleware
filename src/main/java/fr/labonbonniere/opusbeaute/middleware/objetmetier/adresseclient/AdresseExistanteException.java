package fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient;

public class AdresseExistanteException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AdresseExistanteException() {
		super();
	}

	public AdresseExistanteException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdresseExistanteException(String message) {
		super(message);
	}

	public AdresseExistanteException(Throwable cause) {
		super(cause);
	}
}
