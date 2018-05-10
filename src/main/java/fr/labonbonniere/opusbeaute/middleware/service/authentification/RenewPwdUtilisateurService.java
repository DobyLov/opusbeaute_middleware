package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.sql.Timestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
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
	
	static private Integer expirationPwdTempInMinutes = 10; 
	static String ZonePays = "Europe/Paris";
	
	@EJB
	private UtilisateurService utilisateurservice;
	
	@EJB
	private RandomStringGeneratorService randomstringgeneratorservice;
	
	@EJB
	private PasswordHasherOrVerifyService passwordhasher;
	
	@EJB
	private SendMailRenewPwdUtilisateurService sendmailrenewpwdutilisateurservice;
	
	
	/**
	 * Defini le mot de passe temporaire
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
			logger.info("RenewPwdUtilisateurService log : Utilisateur trouve : " + chercheUtilisateurParMail.getPrenomUtilisateur());
			// Defini l expiration du mot de passe temporaire a 10 minutes
			ZonedDateTime zTimeNow = defineZTimeNow();
			ZonedDateTime zTimePlus10 = zTimeNowPlusTenMins(zTimeNow);
			Timestamp pwdTimeNowPlus10 = defineTempPwdExpirationToTenMins(zTimePlus10);
			chercheUtilisateurParMail.setPwdExpirationDateTime(pwdTimeNowPlus10);
			logger.info("RenewPwdUtilisateurService log : defini la date d expiration du mot de passe TS : " + chercheUtilisateurParMail.getPwdExpirationDateTime());
			// Sauvegarde l objet utilisateur avec le nouveau pwd
			utilisateurservice.modifierUnUtilisateur(chercheUtilisateurParMail);
			logger.info("RenewPwdUtilisateurService log : Utilisateur persiste ");
			// Envoie un email a l utilisateur pour lui remettre son pwd.
			sendmailrenewpwdutilisateurservice.envoyerEmailRenewPwd(adresseMailUtilisateur, chercheUtilisateurParMail.getPrenomUtilisateur(), nouveauPwd, zTimeNow, zTimePlus10);
			pwdRenwed = true;
			logger.info("RenewPwdUtilisateurService log : Mail de renouvellement de mot de passe envoye" );
		} catch (UtilisateurInexistantException | EmailFormatInvalidException | NbCharNomException | NbCharPrenomException | NbCharTelException message) {
			
			throw new UtilisateurInexistantException("RenewPwdUtilisateurService Exception : l e-mail n pas ete rouve dans la bdd");
		
		}
		
		
		return pwdRenwed;
	}

	/**
	 * Definie l heure actuelle avec la gestion de la Tz
	 * 
	 * @return zdtnow ZoneDateTime
	 */
	private ZonedDateTime defineZTimeNow() {
		
		ZonedDateTime zdtNow = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(ZonePays));
		
		logger.info("RenewPwdUtilisateurService log : ZoneDateTime : " + zdtNow);
		
		return zdtNow;
	}
	
	/**
	 * Definit l heure actuelle avec tz et ajoute 10 minutes
	 * 
	 * @param zdtNow ZonedDateTime
	 * @return zdTimeNowPlus10Mins ZonedDateTime
	 */
	private ZonedDateTime zTimeNowPlusTenMins(ZonedDateTime zdtNow) {
		
		ZonedDateTime zdTimeNowPlus10Mins = zdtNow.plusMinutes(expirationPwdTempInMinutes);
		
		logger.info("RenewPwdUtilisateurService log : ZoneDateTime Plus 10 min : " + zdTimeNowPlus10Mins );
		
		return zdTimeNowPlus10Mins;
	}
	
	/**
	 * Define Timestamp from Now plus ten mins
	 * 
	 * @param zdtNowPlusTen ZonedDateTime
	 * @return tsNowPlus10 Timestamp
	 */
	private Timestamp defineTempPwdExpirationToTenMins(ZonedDateTime zdtNowPlusTen) {
	
		Timestamp tsNowPlus10 = Timestamp.from(Instant.from(zdtNowPlusTen));
		logger.info("RenewPwdUtilisateurService log : Expiration date : " + tsNowPlus10);
		
		return tsNowPlus10;
	}
	
}	

