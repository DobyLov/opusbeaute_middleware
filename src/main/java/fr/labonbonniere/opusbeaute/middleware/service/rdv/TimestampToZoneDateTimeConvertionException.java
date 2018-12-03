package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class TimestampToZoneDateTimeConvertionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TimestampToZoneDateTimeConvertionException() {
		super();
	}
	
	public TimestampToZoneDateTimeConvertionException(String message) {
		super(message);
	}
	
	public TimestampToZoneDateTimeConvertionException(Throwable cause) {
		super(cause);
	}
	
	public TimestampToZoneDateTimeConvertionException(String message, Throwable cause) {
		super (message, cause);
	}
}
