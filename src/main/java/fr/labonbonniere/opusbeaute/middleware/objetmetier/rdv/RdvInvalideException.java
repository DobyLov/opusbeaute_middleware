package fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv;

public class RdvInvalideException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public RdvInvalideException() {
		super();
	}

	public RdvInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public RdvInvalideException(String message) {
		super(message);
	}

	public RdvInvalideException(Throwable cause) {
		super(cause);
	}

}
