package fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv;

public class RdvDateIncorrecte extends Exception {
	
	private static final long serialVersionUID = 1L;
	

	public RdvDateIncorrecte () {
		super();
	}
	
	public RdvDateIncorrecte (String message, Throwable cause) {
		super(message, cause);
	}
	
	public RdvDateIncorrecte (String message) {
		super(message);
	}
	
	public RdvDateIncorrecte (Throwable cause) {
		super(cause);
	}
	
}
