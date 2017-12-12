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

@Stateless
public class SendMailReminderClientService {

	static final Logger logger = LogManager.getLogger(SendMailReminderClientService.class);

	public void envoyerUnEMail(String adresseemail, String corpsemail) {
	
		logger.info("SendMailReminderClientService log : Envoi de l EMail :" + adresseemail + " " + corpsemail);
	
	}
	
	public void envoyerUnEmailTest() {
		
		logger.info("SendMailReminderClientService log : Envoi de l EMail test.");
		
	}
		

	public void envoyerUnEmailTestAutomatique() {
		
		logger.info("SendMailReminderClientService log : Envoi de l EMail test automatique.");
		
		
		//Declare recipient's & sender's e-mail id.
	    String destmailid = "ricodoby@hotmail.com";
	    String myCc = "dobylov@gmail.com";
	    String sendrmailid = "opusbeaute@gmail.com";	  
	   //Mention user name and password as per your configuration
	    final String uname = "dobylov";
	    final String pwd = "lasouris";
	    //We are using relay.jangosmtp.net for sending emails
//		    String smtphost = "relay.jangosmtp.net";
//		    String smtphost = "smtp.gmail.com";
	   //Set properties and their values
	    Properties propvls = new Properties();
	    propvls.put("mail.smtp.auth", "true");
	    propvls.put("mail.smtp.starttls.enable", "true");
	    propvls.put("mail.smtp.host", "smtp.gmail.com");
	    propvls.put("mail.smtp.port", "587");
//		    propvls.put("mail.smtp.port", "25");
	    //Create a Session object & authenticate uid and pwd
	    Session sessionobj = Session.getInstance(propvls,
	       new javax.mail.Authenticator() {
	          protected PasswordAuthentication getPasswordAuthentication() {
	             return new PasswordAuthentication(uname, pwd);
		   }
	       });

	    try {
		   //Create MimeMessage object & set values
		   Message messageobj = new MimeMessage(sessionobj);
		   messageobj.setFrom(new InternetAddress(sendrmailid));
		   messageobj.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destmailid));
		   messageobj.addRecipients(Message.RecipientType.CC,InternetAddress.parse(myCc));
		   messageobj.setSubject("Rappel de rdv");
		   messageobj.setText("test opusbeaute oubliez pas votre rdv :)!");
		  //Now send the message
		   Transport.send(messageobj);
		   System.out.println("Your email sent successfully....");
	    } catch (MessagingException exp) {
	       throw new RuntimeException(exp);
	    }
	 }
		
}
