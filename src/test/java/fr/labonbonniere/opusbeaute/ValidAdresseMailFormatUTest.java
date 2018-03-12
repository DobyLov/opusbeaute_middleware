package fr.labonbonniere.opusbeaute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class ValidAdresseMailFormatUTest {

	static final Logger logger = LogManager.getLogger(ValidAdresseMailFormatUTest.class.getSimpleName());

	
	@Test
	public void validFormatAdresseMail() {
	
		String adresseMail = "test@test.com";
//		logger.info("ValidAdresseMailFormatTestU log : Adresse mail a valider " + adresseMail);
		
		Assert.assertEquals("L adresse mail doit valider : ", "test@test.com", adresseMail);
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(adresseMail);
//		logger.info("ValidAdresseMailFormatTestU log : Apres traitement");
//		logger.info("ValidAdresseMailFormatTestU log : Check mail doit renvoyer True : " + m.matches());
		Assert.assertTrue("Le format doit revoyer true : ",  m.matches());
		
	}
	
	@Test
	public void setToFalseAWrongMailOne() {
		
		String adresseMail = "test@test!com";
//		logger.info("ValidAdresseMailFormatTestU log : Adresse mail a valider " + adresseMail);
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(adresseMail);
//		logger.info("ValidAdresseMailFormatTestU log : Apres traitement");
//		logger.info("ValidAdresseMailFormatTestU log : Check mail doit renvoyer False : " + m.matches());
		Assert.assertFalse("Le format doit revoyer False : ",  m.matches());
		
		
	}
	
	@Test
	public void setToFalseAWrongMailTwo() {
		
		String adresseMail = "test-test.com";
//		logger.info("ValidAdresseMailFormatTestU log : Adresse mail a valider " + adresseMail);
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(adresseMail);
//		logger.info("ValidAdresseMailFormatTestU log : Apres traitement");
//		logger.info("ValidAdresseMailFormatTestU log : Check mail doit renvoyer False : " + m.matches());
		Assert.assertFalse("Le format doit revoyer False : ",  m.matches());
		
		
	}
	
}
