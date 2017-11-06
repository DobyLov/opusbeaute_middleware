package fr.labonbonniere.opusbeaute.middleware.objetmetier.genre;

public class GenreInvalideException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GenreInvalideException() {
		super();
	}

	public GenreInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

	public GenreInvalideException(String message) {
		super(message);
	}

	public GenreInvalideException(Throwable cause) {
		super(cause);
	}
}
