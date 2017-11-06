package fr.labonbonniere.opusbeaute.middleware.objetmetier.client;

public class ClientInexistantException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ClientInexistantException() {
		super();
	}

	public ClientInexistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientInexistantException(String message) {
		super(message);
	}

	public ClientInexistantException(Throwable cause) {
		super(cause);
	}
}
