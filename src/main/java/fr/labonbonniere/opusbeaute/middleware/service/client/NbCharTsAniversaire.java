package fr.labonbonniere.opusbeaute.middleware.service.client;

public class NbCharTsAniversaire extends Exception {
	private static final long serialVersionUID = 1L;

	public NbCharTsAniversaire() {
		super();
	}

	public NbCharTsAniversaire(String message) {
		super(message);
	}

	public NbCharTsAniversaire(Throwable cause) {
		super(cause);
	}

	public NbCharTsAniversaire(String message, Throwable cause) {
		super(message, cause);
	}
}
