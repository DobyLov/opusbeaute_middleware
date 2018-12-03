package fr.labonbonniere.opusbeaute.middleware.service.rdv;

public class DateConversionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DateConversionException() {
		super();
	}
	
	public DateConversionException(String message) {
		super(message);
	}
	
	public DateConversionException(Throwable cause) {
		super(cause);
	}
	
	public DateConversionException(String message, Throwable cause) {
		super(message,cause);
	}

}
