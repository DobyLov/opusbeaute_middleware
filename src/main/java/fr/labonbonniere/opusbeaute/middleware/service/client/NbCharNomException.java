package fr.labonbonniere.opusbeaute.middleware.service.client;

public class NbCharNomException extends Exception {
	private static final long serialVersionUID = 1L;

	public NbCharNomException() {
		super();
	}

	public NbCharNomException(String message) {
		super(message);
	}

	public NbCharNomException(Throwable cause) {
		super(cause);
	}

	public NbCharNomException(String message, Throwable cause) {
		super(message, cause);
	}
}
