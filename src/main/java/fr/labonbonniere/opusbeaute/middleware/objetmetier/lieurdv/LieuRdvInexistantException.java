package fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv;

public class LieuRdvInexistantException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LieuRdvInexistantException() {
		super();
	}

	public LieuRdvInexistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public LieuRdvInexistantException(String message) {
		super(message);
	}

	public LieuRdvInexistantException(Throwable cause) {
		super(cause);
	}

}
