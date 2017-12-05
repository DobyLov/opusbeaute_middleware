package fr.labonbonniere.opusbeaute.middleware.service.adresse;

public class NbCharPaysException extends Exception {
	private static final long serialVersionUID = 1L;

	public NbCharPaysException() {
		super();
	}

	public NbCharPaysException(String message) {
		super(message);
	}

	public NbCharPaysException(Throwable cause) {
		super(cause);
	}

	public NbCharPaysException(String message, Throwable cause) {
		super(message, cause);
	}
}
