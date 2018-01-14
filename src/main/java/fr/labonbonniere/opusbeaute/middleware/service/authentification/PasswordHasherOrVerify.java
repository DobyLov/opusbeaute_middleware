package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.util.function.Function;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class PasswordHasherOrVerify {

	static final Logger logger = LogManager.getLogger(PasswordHasherOrVerify.class);
	// Retention max 30
	private int logRounds = 14;	
	
//	logger.info("PasswordHasherOrVerify log : Retention niveau " + logRounds + " / 14");
	public void UpdatableBCrypt(int logRounds) {
		this.logRounds = logRounds;
	}

	public String hash(String password) {
		logger.info("PasswordHasherOrVerify log : Hashage Bcrypt de : " + password);
		return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
	}

	public boolean verifyHash(String password, String hash) {
		logger.info("PasswordHasherOrVerify log : Test de correspondance entre le mot de passe et le hash");
		boolean isValid = BCrypt.checkpw(password, hash);
		return  isValid;
	}

	public boolean verifyAndUpdateHash(String password, String hash, 
										Function<String, Boolean> updateFunc) {

		if (BCrypt.checkpw(password, hash)) {
			int rounds = getRounds(hash);
			// It might be smart to only allow increasing the rounds.
			// If someone makes a mistake the ability to undo it would be nice
			// though.
			if (rounds != logRounds) {
				logger.info("Updating password from {} rounds to {}", rounds, logRounds);
				String newHash = hash(password);
				return updateFunc.apply(newHash);
			}
			return true;
		}
		return false;
	}

	/*
	 * Copy pasted from BCrypt internals :(. Ideally a method to exports parts
	 * would be public. We only care about rounds currently.
	 */
	private int getRounds(String salt) {
		
		char minor = (char) 0;
		int off = 0;

		if (salt.charAt(0) != '$' || salt.charAt(1) != '2') {
			logger.error("PasswordHasherOrVerify Exception : Invalid Salt");
			throw new IllegalArgumentException("PasswordHasherOrVerify Exception : Invalid salt version");

		}
		if (salt.charAt(2) == '$') {
			off = 3;

		} else {
			minor = salt.charAt(2);
			if (minor != 'a' || salt.charAt(3) != '$') {
				off = 4;
				logger.error("PasswordHasherOrVerify Exception : Invalid Salt Revision");
				throw new IllegalArgumentException("PasswordHasherOrVerify Exception : Invalid salt revision");


			}
			if (salt.charAt(off + 2) > '$') {// Extract number of rounds
				logger.error("PasswordHasherOrVerify Exception : Missing salt rounds");
				throw new IllegalArgumentException("PasswordHasherOrVerify Exception : Missing salt rounds");
			}
		}

	return Integer.parseInt(salt.substring(off, off + 2));
	}
}
