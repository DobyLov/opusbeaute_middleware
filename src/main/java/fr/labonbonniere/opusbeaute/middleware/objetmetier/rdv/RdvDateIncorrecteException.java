package fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv;

public class RdvDateIncorrecteException extends Exception {
	
	private static final long serialVersionUID = 1L;
	

	public RdvDateIncorrecteException () {
		super();
	}
	
	public RdvDateIncorrecteException (String message, Throwable cause) {
		super(message, cause);
	}
	
	public RdvDateIncorrecteException (String message) {
		super(message);
	}
	
	public RdvDateIncorrecteException (Throwable cause) {
		super(cause);
	}
	
}
