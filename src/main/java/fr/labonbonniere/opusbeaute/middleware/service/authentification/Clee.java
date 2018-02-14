package fr.labonbonniere.opusbeaute.middleware.service.authentification;

/**
 * Definition de la cee de securite
 * pour decrypter les token's (JWT)
 * 
 * @author fred
 *
 */
public class Clee {
	
	private String cleeDuToken = "lAseCkeY";
	
	public Clee () {
		super();
	}

	public String getCleeDuToken() {
		return cleeDuToken;
	}
	

}
