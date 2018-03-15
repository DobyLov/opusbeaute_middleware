package fr.labonbonniere.opusbeaute.middleware.service.renewpwd;

import java.util.Random;

import javax.ejb.Stateless;

/**
 * Genere une String aleatoire
 * pour le mot de passe temporaire
 * en cas d oublie ou perte de mot de passe
 * 
 * @author fred
 *
 */
@Stateless
public class RandomStringGeneratorService {

	/**
	 * Genere une string aleatoire sur 62 caracteres
	 * taile comprise entre 7 et 15 caracteres
	 * alphanumerique min et maj
	 * 
	 * @return String
	 */
	public String randomStringGenerator() {

        String alphabet= "01234aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ56789";
        String stringRandom = "";
        Integer pwdMinsize = 7;
        Random random = new Random();
        int randomLen = pwdMinsize+random.nextInt(15);
        for (int i = 0; i < randomLen; i++) {
            char c = alphabet.charAt(random.nextInt(62));
            stringRandom+=c;
        }
        
		return stringRandom;
		
	}

}
