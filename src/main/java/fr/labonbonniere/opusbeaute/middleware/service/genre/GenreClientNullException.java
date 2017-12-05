package fr.labonbonniere.opusbeaute.middleware.service.genre;

public class GenreClientNullException extends Exception {

	private static final long serialVersionUID = 1L;

	public GenreClientNullException() {
		super();
	}

	public GenreClientNullException(String message) {
		super(message);
	}

	public GenreClientNullException(Throwable cause) {
		super(cause);
	}

	public GenreClientNullException(String message, Throwable cause) {
		super(message, cause);
	}
}
