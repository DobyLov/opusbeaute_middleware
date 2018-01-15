package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class PasswordHandler {

	@EJB
	PasswordHasherOrVerify passwordHasherOrVerify;

	static final Logger logger = LogManager.getLogger(PasswordHandler.class);
	// This should be updated every year or two.
	//	private static final UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);
	//	private static final PasswordHasherOrVerify bcrypt = new PasswordHasherOrVerify(11);
		
	public  String hashPwd(String password) {
//		logger.info("PasswordHandler log : Demande de hashage du Pwd : " + password);
		String passwordHashed = passwordHasherOrVerify.hash(password);
		logger.info("PasswordHandler log :Hash du Pwd : " + passwordHashed);
		
	    return passwordHashed;
	}
	
	
	public boolean ashVerifier(String password, String hash) {
		logger.info("PasswordHandler log : Verification du Pwd et du hash");
		Boolean isPwdValid = passwordHasherOrVerify.verifyHash(password, hash);
		logger.info("PasswordHandler log : Le hash est : " + isPwdValid);
		
		return isPwdValid; 
//	    return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
	}
	
	public String ashVerifierAndReGenerate(String password, String hash) throws Exception {
		logger.info("PasswordHandler log : Generation d un nouveau Hash ");
		String newHash = passwordHasherOrVerify.updateHash(password, hash);
		logger.info("PasswordHandler log : Generation d un nouveau Hash " + newHash);
		
		return newHash;
		
	}
	
	
}
