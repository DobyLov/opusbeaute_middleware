package fr.labonbonniere.opusbeaute.middleware.service.authentification;


import javax.ejb.Stateless;

import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;


/**
 * Gestionnaire de mot de passe et Hashage
 * Hashage basé sur blocs Cypher
 * 
 * @author fred
 *
 */
@Stateless
public class PasswordHasherOrVerifyService {

	static final Logger logger = LogManager.getLogger(PasswordHasherOrVerifyService.class);
	// Retention max 30
	
	/**
	 * Selectionne aleatoirement la retention 
	 * Retention (cout processeur)
	 * 
	 * @return Integer
	 */
	private Integer randomRetention(){
		
		Integer randRetentionRound = RandomUtils.nextInt(12, 16);
		int logRounds = randRetentionRound;
		
		return logRounds;	
	}
	
	/**
	 * Hashage du Mot de Passe
	 * en fonction de la rentention
	 * 
	 * @param password String
	 * @return String
	 */
	public String hash(String password) {
		Integer logRounds = randomRetention();
		logger.info("PasswordHasherOrVerify log : Retention niveau " + logRounds + " / 16");
//		logger.info("PasswordHasherOrVerify log : Hashage Bcrypt de : " + password);
		String salt =  BCrypt.gensalt(logRounds);
		return BCrypt.hashpw(password, salt);
	}

	/**
	 * Verification si le Mot de Passe Hashe
	 * et le mot passe correspondent 
	 * 
	 * @param password String
	 * @param hash String
	 * @return boolean
	 */
	public boolean verifyHash(String password, String hash) {
		
		logger.info("PasswordHasherOrVerify log : Test de correspondance entre le mot de passe et le hash");
//		logger.info("PasswordHasherOrVerify log : Algo de Hashage : " + BCrypt.class.getName());
		boolean isValid = BCrypt.checkpw(password, hash);
		return  isValid;
	}

	/**
	 * Mise a jour du Mot de Passe Hashe
	 * 
	 * @param password String
	 * @param hash String
	 * @return String
	 * @throws Exception Exception
	 */
	public String updateHash(String password, String hash) throws Exception {
		
		String newHash = null;
		try {
			
			if (BCrypt.checkpw(password, hash)) {
				logger.info("PasswordHasherOrVerify log : Ancient Hash : " + hash);
				newHash = hash(password);	
				logger.info("PasswordHasherOrVerify log : Nouveau Hash : " + newHash);
					
			} 
			
		} catch (Exception e) {
			logger.info("PasswordHasherOrVerify log : Probleme de procedure de controle de Hashage");
				throw new Exception("PasswordHasherOrVerify Exception : Probleme de procedure de controle de Hashage.");
		}
		return newHash;

	}

}
