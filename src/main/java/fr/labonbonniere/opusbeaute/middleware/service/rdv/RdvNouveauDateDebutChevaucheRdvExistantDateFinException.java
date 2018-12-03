package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class RdvNouveauDateDebutChevaucheRdvExistantDateFinException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdvNouveauDateDebutChevaucheRdvExistantDateFinException(Throwable cause) {
		super(cause);
	}

	public RdvNouveauDateDebutChevaucheRdvExistantDateFinException(String message) {
		super(message);
	}

	public RdvNouveauDateDebutChevaucheRdvExistantDateFinException(String message, Throwable cause) {
		super(message, cause);
	}

}