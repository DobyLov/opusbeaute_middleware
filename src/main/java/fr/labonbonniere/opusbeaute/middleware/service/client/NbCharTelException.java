package fr.labonbonniere.opusbeaute.middleware.service.client;

public class NbCharTelException extends Exception {
	private static final long serialVersionUID = 1L;

	public NbCharTelException() {
		super();
	}

	public NbCharTelException(String message) {
		super(message);
	}

	public NbCharTelException(Throwable cause) {
		super(cause);
	}

	public NbCharTelException(String message, Throwable cause) {
		super(message, cause);
	}
}
