package fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse;

public class AdresseInvalideException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AdresseInvalideException() {
		super();
	}

	public AdresseInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdresseInvalideException(String message) {
		super(message);
	}

	public AdresseInvalideException(Throwable cause) {
		super(cause);
	}
}
