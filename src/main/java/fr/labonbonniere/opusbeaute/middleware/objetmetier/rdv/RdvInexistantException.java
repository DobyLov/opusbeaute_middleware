package fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv;

public class RdvInexistantException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public RdvInexistantException() {
		super();
	}

	public RdvInexistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public RdvInexistantException(String message) {
		super(message);
	}

	public RdvInexistantException(Throwable cause) {
		super(cause);
	}

}