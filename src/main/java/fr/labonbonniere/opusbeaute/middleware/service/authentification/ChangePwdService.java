package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;

/**
 * A la demande de l utilisateur
 * Remplacer l ancien mot de passe par un nouveau
 * l utilisateur fourni son mail
 * l ancien mot de passe, le nouveau mot de passe
 * 
 * 
 * @author fred
 *
 */
@Stateless
public class ChangePwdService {

	private static final Logger logger = LogManager.getLogger(ChangePwdService.class.getSimpleName());
	
	@EJB
	UtilisateurService utilisateurservice;
	
	@EJB
	LoginService loginservice;
	
	@EJB
	PasswordHasherOrVerifyService passwordhandlerorverifyservice;
	
	/**
	 * Valide que les nouveaux mot de passe A et B sont identiques
	 * recherche l utilisateur recupere le Hash de son pwd
	 * comparaison du Hash avec l ancien mot de pass fourni
	 * si cas nominal sauvegarde de l utilisateur avec 
	 * le Hash de son nouveau mot de passe  
	 * 
	 * 
	 * @param email String
	 * @param oldPwd String
	 * @param newPwdA String
	 * @param newPwdB String
	 * @return Boolean
	 * @throws Exception Exception
	 */
	public boolean changeOldPwd(String email, String oldPwd, String newPwdA, String newPwdB) throws Exception {
		
		logger.info("ChangePwdService log : Tentative de changement de mot de passe par l utilisateur");
		
		try {
			
			// Verifie que les nouveau mots de passe correspondent
			verifieNewABPwd(newPwdA, newPwdB);
			// Recherche de l utilisateur
			logger.info("ChangePwdService log : String email : " + email);
			logger.info("ChangePwdService log : String newPwdA : " + newPwdA);
			logger.info("ChangePwdService log : String newPwdB : " + newPwdB);
			Utilisateur userRecupere = utilisateurservice.recupererUnUtilisateurViaeMail(email);
			// Verification que le mot de passe BDD match avec le mot de passe actuel
						
			if (verifieBddPwdOldPwd(oldPwd, userRecupere) == false) {
				throw new Exception("ChangePwdService Exception : le Mot de passe fourni ne correspond pas avec celui de la Bdd");
			}
			// remplace l ancien mot de passe par le nouveau
			userRecupere.setMotDePasse(newPwdA);
			// Persistance d entite avec le nouveau mot de passe 
			saveNewPwd(newPwdA, userRecupere);		
			
			
		} catch (UtilisateurInexistantException message) {
			logger.info("ChangePwdService Exception : La procedure de changement de mot de passe n a pas fonctionnee");
			throw new Exception ("");
			
		} catch (Exception message) {
			throw new Exception("ChangePwdService Exception : probleme de mot de passse.");
		}
		
		return false;
	}
	
	/**
	 * Verifie que le l ancien mot de passe correspond au Hash 
	 * du mot de passe dans la Bdd
	 * 
	 * @param oldPwd String
	 * @param userRecupere Utilisateur
	 * @return Boolean
	 * @throws Exception Exception
	 */
	private boolean verifieBddPwdOldPwd(String oldPwd, Utilisateur userRecupere ) throws Exception {
		boolean pwdBddMatchWithOldPwd = false;
		
		logger.info("ChangePwdService log : Verification que les mot de passe BDD et actuel match");	
		pwdBddMatchWithOldPwd = passwordhandlerorverifyservice.verifyHash(oldPwd, userRecupere.getMotDePasse());
		logger.info("ChangePwdService log : valdu booboolean si les pwd match : " + pwdBddMatchWithOldPwd);
		
		if ( pwdBddMatchWithOldPwd == true) {
			logger.info("ChangePwdService log : Les mots de passe Bdd et oldPwd correspondent");			
			 
		} else {
			logger.info("ChangePwdService log : Les mots de passes de la Bdd et oldPwd ne correspondent pas");
			pwdBddMatchWithOldPwd = false;
			throw new Exception("ChangePwdService Exception : Les mots de passe de la Bdd et oldPwd ne correspondent pas");
		}
		
		return pwdBddMatchWithOldPwd;
	}
	
	/**
	 * Verifie que les nouveaux mots de passe correspondent
	 * 
	 * @param newPwdA String
	 * @param newPwdB String
	 * @return	Boolean
	 * @throws Exception Exception 
	 */
	private boolean verifieNewABPwd(String newPwdA, String newPwdB) throws Exception {
		
		boolean newPwdAMatchNewPwdB = false;
		logger.info("ChangePwdService log : Verification que les newPwdA et newPwdB correspondent"); 
		if ( newPwdA.contentEquals(newPwdB) ) {
			logger.info("ChangePwdService log : Les nouveaux mots de passes correspondent");
			newPwdAMatchNewPwdB = true;
			
		} else {
			logger.info("ChangePwdService log : Les nouveaux mots de passes ne correspondent pas");
			throw new Exception("ChangePwdService Exception :Les nouveaux mots de passe ne correspondent pas");
		}
		
		return newPwdAMatchNewPwdB;
	}
	
	/**
	 * Persiste l utilisateur avec le Hash de son nouveau mot de passe
	 * 
	 * @param newPwdA String
	 * @param userRecupere Utilisateur
	 * @return Boolean
	 * @throws Exception 
	 */
	private boolean saveNewPwd(String newPwdA, Utilisateur userRecupere) throws Exception {
		boolean pwdHasBeenUpdated = false;
		logger.info("ChangePwdService log : Persistance du noouveau mot de passe"); 
		try {

			utilisateurservice.modifierUnUtilisateur(userRecupere);
			
		} catch (Exception message) {
			logger.info("ChangePwdService log : Le mot de passe n a pas ete persiste");
			throw new Exception("hangePwdService Exception : Les nouveau mots de passe ne match pas");
		}
		
		return pwdHasBeenUpdated;
	}
	


}
