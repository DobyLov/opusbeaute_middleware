package fr.labonbonniere.opusbeaute.middleware.service.renewpwd;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.PasswordHasherOrVerifyService;
import fr.labonbonniere.opusbeaute.middleware.service.client.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.SendMailRenewPwdUtilisateurService;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;

/**
 * renouvelle le mot d epasse  par une string aleatoire
 * 
 * @author fred
 *
 */
@Stateless
public class RenewPwdUtilisateurService {

	private static final Logger logger = LogManager.getLogger(RenewPwdUtilisateurService.class.getSimpleName());
	
	@EJB
	private UtilisateurService utilisateurservice;
	
	@EJB
	private RandomStringGeneratorService randomstringgeneratorservice;
	
	@EJB
	private PasswordHasherOrVerifyService passwordhasher;
	
	@EJB
	private SendMailRenewPwdUtilisateurService sendmailrenewpwdutilisateurservice;
	
	
	/**
	 * 
	 * 
	 * @param adresseMailUtilisateur String
	 * @return boolean
	 * @throws UtilisateurInexistantException Exception
	 * @throws DaoException Exception
	 */
	public boolean changePwd (String adresseMailUtilisateur) throws UtilisateurInexistantException, DaoException {
		boolean pwdRenwed = false;
		
		try {
			logger.info("RenewPwdUtilisateurService log : recherche du mail dans la bdd");
			// recupere l utilisateur par son email
			Utilisateur chercheUtilisateurParMail = utilisateurservice.recupererUnUtilisateurViaeMail(adresseMailUtilisateur);
			// Recupere un string aleatoire
			String nouveauPwd = randomstringgeneratorservice.randomStringGenerator();
			logger.info("RenewPwdUtilisateurService log : Mdp temporaire " + nouveauPwd);			
			// Affecte le nouveau mot de passe Hashe a l utilisateur
			chercheUtilisateurParMail.setMotDePasse(nouveauPwd);
			// Sauvegarde l objet utilisateur avec le nouveau pwd
			utilisateurservice.modifierUnUtilisateur(chercheUtilisateurParMail);
			// Envoie un email a l utilisateur pour lui remettre son pwd.
			sendmailrenewpwdutilisateurservice.envoyerEmailRenewPwd(adresseMailUtilisateur, nouveauPwd);
			pwdRenwed = true;
		} catch (UtilisateurInexistantException | EmailFormatInvalidException | NbCharNomException | NbCharPrenomException | NbCharTelException message) {
			
			throw new UtilisateurInexistantException("RenewPwdUtilisateurService Exception : l e-mail n pas ete rouve dans la bdd");
		
		}
		
		
		return pwdRenwed;
	}
	// recherche l utilisateur par son email
	
	// si trouve recupere l objet
	
	// fait appel au generateur de string
	
	
	
	
}
