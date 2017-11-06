package fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv;

public class LieuRdvInvalideException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LieuRdvInvalideException() {
		super();
	}

	public LieuRdvInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public LieuRdvInvalideException(String message) {
		super(message);
	}

	public LieuRdvInvalideException(Throwable cause) {
		super(cause);
	}

}
