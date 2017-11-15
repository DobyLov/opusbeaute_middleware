package fr.labonbonniere.opusbeaute.middleware.service;

public class RdvFinChevauchementException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdvFinChevauchementException(Throwable cause) {
		super(cause);
	}

	public RdvFinChevauchementException(String message) {
		super(message);
	}

	public RdvFinChevauchementException(String message, Throwable cause) {
		super(message, cause);
	}

}