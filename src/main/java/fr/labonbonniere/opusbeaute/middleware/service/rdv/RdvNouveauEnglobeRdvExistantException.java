package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class RdvNouveauEnglobeRdvExistantException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdvNouveauEnglobeRdvExistantException(Throwable cause) {
		super(cause);
	}

	public RdvNouveauEnglobeRdvExistantException(String message) {
		super(message);
	}

	public RdvNouveauEnglobeRdvExistantException(String message, Throwable cause) {
		super(message, cause);
	}
}
