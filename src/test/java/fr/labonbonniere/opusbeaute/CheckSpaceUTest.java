package fr.labonbonniere.opusbeaute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Assert;
import org.junit.Test;

public class CheckSpaceUTest {

	static final Logger logger = LogManager.getLogger(CheckSpaceUTest.class.getSimpleName());
	


	@Test
	public void shouldFoundASpaceTest() {

		String strWithSpaceAtBegin = " detecteurEspaceAuDebutString";
		
		logger.info("CheckSpaceTest log : Check si la String debute avec un espace." + strWithSpaceAtBegin);
		
		Assert.assertEquals("La String de test comporte bien un espace au debut", " detecteurEspaceAuDebutString", strWithSpaceAtBegin);
		
		if (strWithSpaceAtBegin.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _" + strWithSpaceAtBegin);

			int nbLengthStr = strWithSpaceAtBegin.length();
			String firstCaracsubstitue = strWithSpaceAtBegin.substring(1, nbLengthStr);
			
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + firstCaracsubstitue);
			
			Assert.assertEquals("La String de test comporte bien un espace au debut", "detecteurEspaceAuDebutString", firstCaracsubstitue);
			

		}
	}
	
}
