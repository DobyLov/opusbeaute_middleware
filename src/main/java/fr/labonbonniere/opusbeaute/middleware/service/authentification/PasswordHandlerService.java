package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hashage du Mot de passe pour Persistance
 * Verification du Mot de Passe
 * 
 * @author fred
 *
 */
@Stateless
public class PasswordHandlerService {

	@EJB
	PasswordHasherOrVerifyService passwordHasherOrVerify;

	static final Logger logger = LogManager.getLogger(PasswordHandlerService.class);
	// This should be updated every year or two.
	//	private static final UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);
	//	private static final PasswordHasherOrVerify bcrypt = new PasswordHasherOrVerify(11);
	
	/**
	 * Hashage du Mot de Passe pour Persistance
	 * 
	 * @param password String
	 * @return String
	 */
	public  String hashPwd(String password) {
//		logger.info("PasswordHandler log : Demande de hashage du Pwd : " + password);
		String passwordHashed = passwordHasherOrVerify.hash(password);
		logger.info("PasswordHandler log :Hash du Pwd : " + passwordHashed);
		
	    return passwordHashed;
	}
	
	/**
	 * Verification du Hashage avec le mot de passe
	 * 
	 * @param password String
	 * @param hash String
	 * @return boolean
	 */
	public boolean ashVerifier(String password, String hash) {
		logger.info("PasswordHandler log : Verification du Pwd et du hash");
		Boolean isPwdValid = passwordHasherOrVerify.verifyHash(password, hash);
		logger.info("PasswordHandler log : Le hash est : " + isPwdValid);
		
		return isPwdValid; 
//	    return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
	}
	
	/**
	 * Genere un nouveau Hash du MEME Mot de Passe
	 * 
	 * @param password String
	 * @param hash String
	 * @return String
	 * @throws Exception Exception
	 */
	public String ashVerifierAndReGenerate(String password, String hash) throws Exception {
		logger.info("PasswordHandler log : Generation d un nouveau Hash ");
		String newHash = passwordHasherOrVerify.updateHash(password, hash);
		logger.info("PasswordHandler log : Generation d un nouveau Hash " + newHash);
		
		return newHash;
		
	}
}
