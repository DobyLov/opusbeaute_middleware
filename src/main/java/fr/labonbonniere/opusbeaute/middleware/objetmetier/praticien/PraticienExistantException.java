package fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien;

public class PraticienExistantException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PraticienExistantException() {
		super();
	}

	public PraticienExistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public PraticienExistantException(String message) {
		super(message);
	}

	public PraticienExistantException(Throwable cause) {
		super(cause);
	}
}
