package fr.labonbonniere.opusbeaute.middleware.objetmetier.genre;

public class GenreExistantException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GenreExistantException() {
		super();
	}

	public GenreExistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenreExistantException(String message) {
		super(message);
	}

	public GenreExistantException(Throwable cause) {
		super(cause);
	}
}
