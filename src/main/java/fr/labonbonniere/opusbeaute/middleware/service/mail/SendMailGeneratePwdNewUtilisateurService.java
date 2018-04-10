package fr.labonbonniere.opusbeaute.middleware.service.mail;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class SendMailGeneratePwdNewUtilisateurService {

	static final Logger logger = LogManager.getLogger(SendMailGeneratePwdNewUtilisateurService.class.getSimpleName());
	
	
	@EJB
	MailEngine mailengine;
	
	public String idZone = "UTC+2";
	
	public void envoyerEmailRenewPwd(String email,String prenom, String nouveauPwd, ZonedDateTime zTimeNow, ZonedDateTime timePlus  ) {

		DateTimeFormatter dateForm = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		DateTimeFormatter timeForm = DateTimeFormatter.ofPattern("HH:mm");
		
		String customSubject = "Opus-Beauté: Création de compte.";

		String customMessageDynamic = "<br><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
						+ "Bonjour " + prenom + ",</span></p>"
						+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "Voici vos acc&egrave;s &agrave; Opus-Beaut&eacute; :<br>"
						+ "Login : " + email +"<br>"
						+ "Mot de passe : " + nouveauPwd	
						+ "<p>Il sera valide jusqu'&agrave; <b>" + timeForm.format(timePlus) + "</b>.</p>"
						+ "<p>Pour des raison de s&eacute;curit&eacute; il est pr&eacute;f&eacute;rable<br>"
						+ "de le changer rapidement.</p>"
						+ "<p>Faites un copier / coller du mot de passe dans la fen&ecirc;tre d'authentification d'OpusBeaut&eacute;.</p>"
						+ "<p>Message g&eacute;n&eacute;r&eacute; le : " + dateForm.format(zTimeNow) + " &agrave; " + timeForm.format(zTimeNow) + ".</span></p>"
						+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "La Bonbonni&egrave;re d'Audrey</span></p><p>&nbsp;</p>";

		mailengine.moteurEmailEvoyer(customMessageDynamic, email, customSubject);
		
	}

	
	
}
