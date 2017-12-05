package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class RdvEnglobantException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdvEnglobantException(Throwable cause) {
		super(cause);
	}

	public RdvEnglobantException(String message) {
		super(message);
	}

	public RdvEnglobantException(String message, Throwable cause) {
		super(message, cause);
	}
}
