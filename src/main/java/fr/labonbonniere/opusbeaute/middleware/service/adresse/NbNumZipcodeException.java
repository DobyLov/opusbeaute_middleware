package fr.labonbonniere.opusbeaute.middleware.service.adresse;

public class NbNumZipcodeException extends Exception {
	private static final long serialVersionUID = 1L;

	public NbNumZipcodeException() {
		super();
	}

	public NbNumZipcodeException(String message) {
		super(message);
	}

	public NbNumZipcodeException(Throwable cause) {
		super(cause);
	}

	public NbNumZipcodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
