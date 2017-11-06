package fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv;

public class LieuRdvExistantException  extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LieuRdvExistantException() {
		super();
	}

	public LieuRdvExistantException(String message, Throwable cause) {
		super(message, cause);
	}

	public LieuRdvExistantException(String message) {
		super(message);
	}

	public LieuRdvExistantException(Throwable cause) {
		super(cause);
	}
}
