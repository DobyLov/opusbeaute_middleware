package fr.labonbonniere.opusbeaute.middleware.service.client;

public class NbCharPrenomException extends Exception {
	private static final long serialVersionUID = 1L;

	public NbCharPrenomException() {
		super();
	}

	public NbCharPrenomException(String message) {
		super(message);
	}

	public NbCharPrenomException(Throwable cause) {
		super(cause);
	}

	public NbCharPrenomException(String message, Throwable cause) {
		super(message, cause);
	}
}
