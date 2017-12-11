package fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien;

public class PraticienInexistantException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PraticienInexistantException() {
		super();
	}

	public PraticienInexistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public PraticienInexistantException(String message) {
		super(message);
	}

	public PraticienInexistantException(Throwable cause) {
		super(cause);
	}
}
