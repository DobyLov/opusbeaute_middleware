package fr.labonbonniere.opusbeaute.middleware.service.activite;

public class ActiviteInvalideException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ActiviteInvalideException() {
		super();
	}

	public ActiviteInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActiviteInvalideException(String message) {
		super(message);
	}

	public ActiviteInvalideException(Throwable cause) {
		super(cause);
	}

}
