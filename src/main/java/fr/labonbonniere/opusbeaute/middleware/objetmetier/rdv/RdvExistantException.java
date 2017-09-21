package fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv;

public class RdvExistantException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public RdvExistantException() {
		super();
	}

	public RdvExistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public RdvExistantException(String message) {
		super(message);
	}

	public RdvExistantException(Throwable cause) {
		super(cause);
	}

}

