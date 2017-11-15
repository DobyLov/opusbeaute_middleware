package fr.labonbonniere.opusbeaute.middleware.service;

public class RdvEgaliteChevauchementException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdvEgaliteChevauchementException(Throwable cause) {
		super(cause);
	}

	public RdvEgaliteChevauchementException(String message) {
		super(message);
	}

	public RdvEgaliteChevauchementException(String message, Throwable cause) {
		super(message, cause);
	}
}
