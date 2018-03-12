package fr.labonbonniere.opusbeaute.middleware.service.renewpwd;

import java.util.Random;

import javax.ejb.Stateless;

@Stateless
public class RandomStringGeneratorService {

	public String randomStringGenerator() {

        String alphabet= "01234aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ56789";
        String stringRandom = "";
        Random random = new Random();
        int randomLen = 1+random.nextInt(15);
        for (int i = 0; i < randomLen; i++) {
            char c = alphabet.charAt(random.nextInt(62));
            stringRandom+=c;
        }
        
		return stringRandom;
		
	}

}
