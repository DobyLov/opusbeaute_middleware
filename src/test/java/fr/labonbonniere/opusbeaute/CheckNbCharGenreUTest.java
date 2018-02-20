package fr.labonbonniere.opusbeaute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Assert;
import org.junit.Test;

public class CheckNbCharGenreUTest {

	static final Logger logger = LogManager.getLogger(CheckNbCharGenreUTest.class.getSimpleName());
	
	@Test
	public void CheckNbCharGenreHommeUTest() {
		
		logger.info("CheckNbCharGenreUTest log : Test si la String depasse 5 caracteres");
		String genre = "Homme";
		boolean moreThanFiveChar;
		logger.info("CheckNbCharGenreUTest log : Compte le nb de char de la String : " + genre);
		Integer nbCharStringGenre = genre.length();
		if (nbCharStringGenre > 5) {
			moreThanFiveChar = true;
		} else {
			moreThanFiveChar = false;
		}
		Assert.assertFalse("Le nombre de caracteres depasse 5 charactere ", moreThanFiveChar);
		
	}
	
	@Test
	public void CheckNbCharGenreFemmeUTest() {
		
		logger.info("CheckNbCharGenreUTest log : Test si la String depasse 5 caracteres");
		String genre = "Femme";
		boolean moreThanFiveChar;
		logger.info("CheckNbCharGenreUTest log : Compte le nb de char de la String : " + genre);
		Integer nbCharStringGenre = genre.length();
		if (nbCharStringGenre > 5) {
			moreThanFiveChar = true;
		} else {
			moreThanFiveChar = false;
		}
		Assert.assertFalse("Le nombre de caracteres depasse 5 charactere ", moreThanFiveChar);
		
	}
	
	@Test
	public void CheckNbCharGenreWrongNumberFemmeUTest() {
		
		logger.info("CheckNbCharGenreUTest log : Test si la String depasse 5 caracteres");
		String genre = "Femmmee";
		boolean moreThanFiveChar;
		logger.info("CheckNbCharGenreUTest log : Compte le nb de char de la String : " + genre);
		Integer nbCharStringGenre = genre.length();
		if (nbCharStringGenre > 5) {
			moreThanFiveChar = true;
		} else {
			moreThanFiveChar = false;
		}
		Assert.assertTrue("Le nombre de caracteres depasse 5 charactere ", moreThanFiveChar);
		
	}

}
