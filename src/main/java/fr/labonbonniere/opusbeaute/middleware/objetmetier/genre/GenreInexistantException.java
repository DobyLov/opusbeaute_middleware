package fr.labonbonniere.opusbeaute.middleware.objetmetier.genre;

public class GenreInexistantException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GenreInexistantException() {
		super();
	}

	public GenreInexistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenreInexistantException(String message) {
		super(message);
	}

	public GenreInexistantException(Throwable cause) {
		super(cause);
	}
}
