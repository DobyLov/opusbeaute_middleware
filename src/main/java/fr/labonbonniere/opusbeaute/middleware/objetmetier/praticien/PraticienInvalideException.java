package fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien;

public class PraticienInvalideException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PraticienInvalideException() {
		super();
	}

	public PraticienInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public PraticienInvalideException(String message) {
		super(message);
	}

	public PraticienInvalideException(Throwable cause) {
		super(cause);
	}

}
