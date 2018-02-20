package fr.labonbonniere.opusbeaute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Assert;
import org.junit.Test;

public class SupprimeLesCaracteresSpeciauxUTest {

	static final Logger logger = LogManager.getLogger(SupprimeLesCaracteresSpeciauxUTest.class.getSimpleName());
	
	@Test
	public void supprimeLesCaracteresSpeciauxTest() {
		
		logger.info("SupprimeLesCaracteresSpeciauxTest : Test de suppression de caracteres speciaux");
		
		String stringSpeciale = "Se:j!o$u_r{ a* S)ta=i&n:t-T}ro]p%e#z";
		Assert.assertEquals("String attendue avant traitement : ", "Se:j!o$u_r{ a* S)ta=i&n:t-T}ro]p%e#z", stringSpeciale);
		
		logger.info("SupprimeLesCaracteresSpeciauxTest : Avant traitement:");
		logger.info("SupprimeLesCaracteresSpeciauxTest : String speciale : " + stringSpeciale);

		String stringNormale = stringSpeciale .replaceAll("[^\\s+^a-zA-Z^-]", "");
		logger.info("SupprimeLesCaracteresSpeciauxTest : Apres tratement :");
		logger.info("SupprimeLesCaracteresSpeciauxTest : String normale : " + stringNormale);
		
		Assert.assertEquals("String attendue apres traitement : ", "Sejour a Staint-Tropez", stringNormale);
		
		
	}

}
