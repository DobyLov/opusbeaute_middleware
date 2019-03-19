package fr.labonbonniere.opusbeaute.middleware.service.mail;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Moteur de mail
 * 
 * @author fred
 *
 */
@Stateless
public class MailEngine {

	static final Logger logger = LogManager.getLogger(MailEngine.class.getSimpleName());
	
	// credentiel du compt mail
//	private static final String compteEmailLogin = "opusbeaute";
//	private static final String compteEmailPwd = "!1DemoOpusBeaute";
	private static final String compteEmailLogin = "dobylov";
	private static final String compteEmailPwd = "lasouris";
	// @IP du serveur ou est hebergé l application.
//	private static final String ipAppSrvHoster = "192.168.1.100";
	
	/**
	 * Moteur d envoie d Email
	 * 
	 * @param customMessageDynamic String
	 * @param email String
	 * @param customMailSubject String
	 * 
	 */
	public void moteurEmailEvoyer(String customMessageDynamic, String email, String customMailSubject) {

		// Creation du mail

		// logger.info("MailRemiderSender log : Tentative Envoi de l EMail.");

		// Declare recipient's & sender's e-mail id.
		String destmailid = email;
		String sendrmailid = "dobylov@gmail.com";
		// Mention user name and password as per your configuration
		final String uname = compteEmailLogin;
		final String pwd = compteEmailPwd;
		Properties propvls = new Properties();
		propvls.put("mail.smtp.auth", "true");
		propvls.put("mail.smtp.starttls.enable", "true");
		propvls.put("mail.smtp.host", "smtp.gmail.com");
		propvls.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		propvls.put("mail.smtp.port", "587");
		Session sessionobj = Session.getInstance(propvls, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(uname, pwd);
			}
		});

		try {
			// Create MimeMessage object & set values
			MimeMessage messageobj = new MimeMessage(sessionobj);
			messageobj.setFrom(new InternetAddress(sendrmailid));
			messageobj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destmailid));
			// messageobj.addRecipients(Message.RecipientType.CC,InternetAddress.parse(myCc));
			messageobj.setSubject(customMailSubject);
			messageobj.setContent(customMessageDynamic, "text/html");
			// Now send the message
			Transport.send(messageobj);

			logger.info("MailEngine log : Envoi de l EMail ");
		} catch (MessagingException exp) {
			logger.error("MailEngine log : Echec Envoi de l EMail.");
			throw new RuntimeException(exp);
		}
	}

}
