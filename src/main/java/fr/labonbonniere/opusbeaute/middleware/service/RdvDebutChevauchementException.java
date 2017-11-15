package fr.labonbonniere.opusbeaute.middleware.service;

public class RdvDebutChevauchementException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdvDebutChevauchementException(String message) {
		super(message);
	}

	public RdvDebutChevauchementException(Throwable cause) {
		super(cause);
	}

	public RdvDebutChevauchementException(Throwable cause, String message) {
		super(message, cause);
	}
}
