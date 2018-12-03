package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class RdvNouveauDateFinChevaucheRdvExistantDateDebutException extends Exception {

	private static final long serialVersionUID = 1L;

	public RdvNouveauDateFinChevaucheRdvExistantDateDebutException(String message) {
		super(message);
	}

	public RdvNouveauDateFinChevaucheRdvExistantDateDebutException(Throwable cause) {
		super(cause);
	}

	public RdvNouveauDateFinChevaucheRdvExistantDateDebutException(Throwable cause, String message) {
		super(message, cause);
	}
}
