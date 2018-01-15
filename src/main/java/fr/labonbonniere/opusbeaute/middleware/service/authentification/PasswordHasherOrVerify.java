package fr.labonbonniere.opusbeaute.middleware.service.authentification;


import javax.ejb.Stateless;

import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class PasswordHasherOrVerify {

	static final Logger logger = LogManager.getLogger(PasswordHasherOrVerify.class);
	// Retention max 30
	
	private Integer randomRetention(){
		
		Integer randRetentionRound = RandomUtils.nextInt(12, 16);
		int logRounds = randRetentionRound;
		
		return logRounds;	
	}
	

	public String hash(String password) {
		Integer logRounds = randomRetention();
		logger.info("PasswordHasherOrVerify log : Retention niveau " + logRounds + " / 16");
//		logger.info("PasswordHasherOrVerify log : Hashage Bcrypt de : " + password);
		String salt =  BCrypt.gensalt(logRounds);
		return BCrypt.hashpw(password, salt);
	}

	public boolean verifyHash(String password, String hash) {
		
		logger.info("PasswordHasherOrVerify log : Test de correspondance entre le mot de passe et le hash");
//		logger.info("PasswordHasherOrVerify log : Algo de Hashage : " + BCrypt.class.getName());
		boolean isValid = BCrypt.checkpw(password, hash);
		return  isValid;
	}

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
