package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class RdvNouveauEnglobeParRdvExistantException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public RdvNouveauEnglobeParRdvExistantException() {
		super();
	}
	
	public RdvNouveauEnglobeParRdvExistantException(String message) {
		super(message);
	}
	
	public RdvNouveauEnglobeParRdvExistantException(Throwable cause) {
		super(cause);
	}
	
	public RdvNouveauEnglobeParRdvExistantException(String message, Throwable cause) {
		super(message, cause);
	}

}
