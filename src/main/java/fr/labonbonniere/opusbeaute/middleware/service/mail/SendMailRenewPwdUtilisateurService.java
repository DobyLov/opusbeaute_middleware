package fr.labonbonniere.opusbeaute.middleware.service.mail;


import java.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class SendMailRenewPwdUtilisateurService {

	static final Logger logger = LogManager.getLogger(SendMailRenewPwdUtilisateurService.class.getSimpleName());
	
	
	@EJB
	MailEngine mailengine;
	
	LocalDate dateTimeDuJour = LocalDate.now();		

	public void envoyerEmailRenewPwd(String email, String nouveauPwd) {


		LocalDate dateTimeDuJour = LocalDate.now();
		String customSubject = "Opus-Beaut&eacute;_Mot de passe temporaire.";

		String customMessageDynamic = "<br><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
						+ "Bonjour  ,</span></p>"
						+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "Vous avez demand&eacute; un nouveau mot de passe temporaire le " + dateTimeDuJour.toString() + ".</span></p>"
						+ "<br><p>Voici votre mot de passe : " + nouveauPwd + "</p>"
						+ "<br><p>Faites un copier / coller du mot de passe dans la fen&ecirc;tre d'authentification d'OpusBeaut&eacute;</p>"
						+ "<br><p>Pour plus de s&eacute;curit&eacute;, changez rapidemant ce mot de passe temporaire par un personel.</p>"
						+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "La Bonbonni&egrave;re d'Audrey</span></p><p>&nbsp;</p>";

		mailengine.moteurEmailEvoyer(customMessageDynamic, email, customSubject);
		
	}

	
	
}
