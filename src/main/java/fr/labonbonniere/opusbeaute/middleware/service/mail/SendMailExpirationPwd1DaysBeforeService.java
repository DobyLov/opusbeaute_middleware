package fr.labonbonniere.opusbeaute.middleware.service.mail;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.UtilisateurDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;
/**
 * Envoyer un Email pour informer l utilisateur
 * que son mot de passe va expirer
 * 
 * 
 * @author fred
 *
 */
@Stateless
public class SendMailExpirationPwd1DaysBeforeService {
	
	// Specifie le nombre de jours avant expiration.
	static Integer nbJrs = 3;
	static String ZonePays = "Europe/Paris";
	static String customMailSubject = "OpusBeaute: Expiration du mot de passe dans : " + nbJrs + " jours."  ;

	static final Logger logger = LogManager.getLogger(SendMailExpirationPwd1DaysBeforeService.class.getSimpleName());
	
	
	@EJB
	MailEngine mailengine;
	
	@EJB
	UtilisateurDao utilisateurdao;
	
	@EJB
	UtilisateurService utilisateurservice;
	
	/**
	 * Envoyer un Mail si le Pwd expire dans 7 jours
	 * 
	 * @throws Exception
	 */
	// tous les jours a 19H50
	@Schedule(dayOfWeek = "*", hour = "19", minute = "34")
	public void envoyerMailUtilisateurPwdExpireDansXJours() throws Exception {
		
		List<Utilisateur> listUsers = recupereListeUtilisateurs();
		Integer nbUtilisateurs = listUsers.size();		
		logger.info("SendMailExpirationPwd1DaysBeforeService log : Nombre d objets Utilisateurs : " + nbUtilisateurs);
		
		// Iterateur pour extraire les utilisateurs et envoyer le mail si PwdExpiration = XJrs
		for (int nbObj = 0;  nbObj <= nbUtilisateurs-1; nbObj++) {			
			logger.info("SendMailExpirationPwd1DaysBeforeService log : Position dans la liste d objets Utilisateur : " + nbObj);	
			
			Utilisateur jsonObj = listUsers.get(nbObj);
			logger.info("SendMailExpirationPwd1DaysBeforeService log : Objet traite : " + jsonObj.toString());

			Timestamp dateExp = checkIfPwdExpirationDateIsNull(jsonObj);

			if(isDatePwdMatchingWithDateMinusXDays(plususXDaysFromLaDateDuJourZDT(), convertTimestampToDate(dateExp)) == true) {
				logger.info("SendMailExpirationPwd1DaysBeforeService log : Le mot de passe utilisateur expire dans " + nbJrs + " jours");
				logger.info("SendMailExpirationPwd1DaysBeforeService log : Envoie de mail pour prevenir l utilisateur");
				// envoyer le mail de rappel Expiration pwd.
				mailengine.moteurEmailEvoyer(mailMessage(jsonObj.getPrenomUtilisateur()), jsonObj.getAdresseMailUtilisateur(), customMailSubject);
				
			} else {
				logger.info("SendMailExpirationPwd1DaysBeforeService log : pas d Email a "
						+ "envoyer car la date d expiration ne correspond pas a " + nbJrs);
			}
			
		}
		
	}
	
	/**
	 * Recupere la liste des utilisateurs
	 * 
	 * @return List
	 * @throws Exception
	 */
	private List<Utilisateur> recupereListeUtilisateurs() throws Exception {
			
		List<Utilisateur> listUsers;
		
		try {
			
			listUsers = utilisateurservice.recupereListeUtilisateur();
			logger.info("SendMailExpirationPwd1DaysBeforeService log : listeUtilisateurs  : " + listUsers.toString());
		} catch (DaoException message) {
			throw new Exception("SendMailExpirationPwd1DaysBeforeService Exception : "
					+ "il y a un probleme de recuperation de la liste.");
		}
		
		return listUsers;
	}
	
	/**
	 * Retire 7 jours a la date du jour
	 * 
	 * @return Date moins 7 jours
	 */
	private Date plususXDaysFromLaDateDuJourZDT() {

		Date dateminXDays = Date.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(ZonePays)).plusDays(nbJrs).toInstant());
		logger.info("SendMailExpirationPwd1DaysBeforeService log : ZDT to Date -" + nbJrs + " jours : " + dateminXDays);
		
		return dateminXDays;
	}
	
	/**
	 * Conversion de TS vers Date
	 * 
	 * @param dateTsPwdExpiration
	 * @return
	 */
	private Date convertTimestampToDate(Timestamp dateTsPwdExpiration) {
		logger.info("SendMailExpirationPwd1DaysBeforeService log : dateTs : " + dateTsPwdExpiration);
		Date datePwdExp = Date.from(dateTsPwdExpiration.toInstant());
		logger.info("SendMailExpirationPwd1DaysBeforeService log : Ts to Date : " + datePwdExp);
		
		return datePwdExp;
	}
	
	/**
	 * Detecte si la Date du Pwd est null
	 * Si oui defini la date du jour.
	 * 
	 * @param userPwdToCheck
	 * @return
	 */
	private Timestamp checkIfPwdExpirationDateIsNull(Utilisateur userPwdToCheck) {
		
		logger.info("SendMailExpirationPwd1DaysBeforeService log : Recuperation de la date ");
		if (userPwdToCheck.getPwdExpirationDateTime() == null) {
			logger.info("SendMailExpirationPwd1DaysBeforeService log : La date est nulle");
			Timestamp setTsToNow = Timestamp.from(Instant.now());
			
			return setTsToNow;
		}
		
		return userPwdToCheck.getPwdExpirationDateTime();
		
	}
	
	/**
	 * Verifie si la date d expiration
	 * correspond a la date plus 7 jours
	 * 
	 * @param dateJJMinusXdays Date
	 * @param datePwdExp Date
	 * @return Boolean
	 */
	private boolean isDatePwdMatchingWithDateMinusXDays(Date dateJJPlusXdays, Date datePwdExp) {
	
		logger.info("SendMailExpirationPwd1DaysBeforeService log : val de datminus : " + dateJJPlusXdays);
		logger.info("SendMailExpirationPwd1DaysBeforeService log : val de pwdExpire: " + datePwdExp);
		
		if (DateUtils.isSameDay(dateJJPlusXdays, datePwdExp) == true) {
			logger.info("SendMailExpirationPwd1DaysBeforeService log : la date du pwd est encore valide");
			return true;
		} else {
			logger.info("SendMailExpirationPwd1DaysBeforeService log : la date du pwd n est plus valide valide");
			return false;
		}		

	}
	
	/**
	 * Defini le message(corps) pour le mail
	 * 
	 * @param prenom
	 * @return
	 */
	private String mailMessage(String prenom) {
		
		String msg = "";
		
		msg = "<br><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
				+ "Bonjour " + prenom + ",</span></p>"
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "Votre mot de passe d'authentification, <br>"
				+ "expire dans " + nbJrs + " jour.</span></p>"
				+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "La Bonbonni&egrave;re d'Audrey</span></p><p>&nbsp;</p>"
				+ "<p style=\"font-size: 4.0px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: small;\">";
		
		return msg;
	}
	
	
}
