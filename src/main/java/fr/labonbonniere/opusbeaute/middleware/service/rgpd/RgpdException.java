package fr.labonbonniere.opusbeaute.middleware.service.rgpd;

public class RgpdException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public RgpdException() {
		super();
	}
	
	public RgpdException(String message) {
		super(message);
	}
	
	public RgpdException(Throwable cause) {
		super(cause);
	}
	
	public RgpdException(String message, Throwable cause) {
		super(message, cause);
	}

}
