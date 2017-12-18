package fr.labonbonniere.opusbeaute.middleware.service.sms;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Properties;

import javax.ejb.EJB;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

public class toto {
	
	static final Logger logger = LogManager.getLogger();
	
	@EJB
	private RdvDao rdvdao;
	
	public void mailReminderClient() throws DaoException {
		
		logger.info("MailRemiderSender log : Recuperation de la date J+1" );
		
		// recuperer la date du jour et ajout 1 jours pour J+1
		logger.info("MailRemiderSender log : Date du jour : " + LocalDate.now() );
		Period definiPlusUnJour = Period.of(0,0,1);  
		LocalDate jourJPlusUn = LocalDate.now().plus(definiPlusUnJour);
		logger.info("MailRemiderSender log : Date du j+1 : " + jourJPlusUn);
		
		// formatage de J+1 en yyyy-MM-dd
		String dateformatpattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateformatpattern);
		String rdvDateDuJourFormate = (String) sdf.format(jourJPlusUn);
		
		// creer un methode ---- qui gère les exceptions
		// recuperation du nombre de Rdv plannifés à J+1		
		Integer nbRdvJPlusUn = rdvdao.renvoyerLeNbDeRdvDuJour(rdvDateDuJourFormate);
		logger.info("MailRemiderSender log : " + nbRdvJPlusUn +" Rdv de plannifes le : " + rdvDateDuJourFormate);
		
		
		// Rempli l ArrayList avec la liste d objets Rdv J+1 avec SuscribedMail a T
		ArrayList <Rdv> rdvList = new ArrayList<Rdv>();
		rdvList = (ArrayList<Rdv>) rdvdao.obtenirListeRdvViaDateAndMailSuscribedTrue(rdvDateDuJourFormate);
		logger.info("MailRemiderSender log : " + rdvList.size() 
					+ " mails de rappel de rdv seron evoyes sur les " + rdvDateDuJourFormate + " plannifies"); 
		
		// Parcour l Array et extrait les informations de l object RDV etendu necessaires pour envoyer un mail
		// Info necessaires : Prenom, Email, HeureRdv, PrestationSoin, LieuRdv, adresseLieuRdv
		
		// Iteration de l arrayList 
		Integer i=0;
		for (Rdv rdv : rdvList) {
			
			// Recuperation des informations necessaires pour l envoie de l Email.
			String clientPrenom = rdvList.get(i).getClient().getPrenomClient();
			String clientEmail = rdvList.get(i).getClient().getAdresseMailClient();
			Timestamp rdvDateHeure = rdvList.get(i).getDateHeureDebut();
			String prestationsoin = rdvList.get(i).getPrestation().getSoin();
			String lieuRdv = rdvList.get(i).getLieuRdv().getLieuRdv();
			String adresseLieuRdvNumeroRue = rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getNumero();
			String adresseLieuRdvRue= rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getRue();
			String adresseLieuRdvZipCode= rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getZipCode();
			String adresseLieuRdvVille= rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getVille();
			
			// Formattage du timeStamp rdvHeure
			logger.info("MailRemiderSender log : Conversion de tu TS DatHeuredebut : " + rdvDateHeure);
			String rdvDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format((TemporalAccessor) rdvDateHeure);
			String rdvHeure = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format((TemporalAccessor) rdvDateHeure);
			logger.info("MailRemiderSender log : Conversion TS to Date : " + rdvDate);
			logger.info("MailRemiderSender log : Conversion TS to Date : " + rdvHeure);
			
			// Creation du mail
			
			logger.info("MailRemiderSender log : Tentative Envoi de l EMail.");
			
			
			//Declare recipient's & sender's e-mail id.
		    String destmailid = clientEmail;
//		    String myCc = "dobylov@gmail.com";
		    String sendrmailid = "opusbeaute@gmail.com";	  
		   //Mention user name and password as per your configuration
		    final String uname = "dobylov";
		    final String pwd = "lasouris";
		    //We are using relay.jangosmtp.net for sending emails
//			    String smtphost = "relay.jangosmtp.net";
//			    String smtphost = "smtp.gmail.com";
		   //Set properties and their values
		    Properties propvls = new Properties();
		    propvls.put("mail.smtp.auth", "true");
		    propvls.put("mail.smtp.starttls.enable", "true");
		    propvls.put("mail.smtp.host", "smtp.gmail.com");
		    propvls.put("mail.smtp.port", "587");
//			    propvls.put("mail.smtp.port", "25");
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
//			   messageobj.addRecipients(Message.RecipientType.CC,InternetAddress.parse(myCc));
			   messageobj.setSubject("Rappel de rdv La Bonbonnière");
			   messageobj.setText("Bonjour " + clientPrenom + ",\n\n"  
					   + "Vous avez Rendez_Vous demain, \n"
					   + "le " + rdvDateDuJourFormate + " à " + rdvHeure + ".\n"
					   + "Pour la prestation suivante : \n"
					   + "Soin : " + prestationsoin + ".\n\n"
					   + "A l'adresse suivante : \n"
					   + lieuRdv + ", \n"
					   + "Au " + adresseLieuRdvNumeroRue + " " + adresseLieuRdvRue + "\n"
					   + adresseLieuRdvZipCode + " " + adresseLieuRdvVille + "\n\n"
					   + "Cordialement.");		   
			   
			   //Now send the message
			   Transport.send(messageobj);
			   logger.info("MailRemiderSender log : Tentative Envoi de l EMail.");
		    } catch (MessagingException exp) {
		    	logger.error("MailRemiderSender log : Echec Envoi de l EMail.");
		       throw new RuntimeException(exp);
		    }
			
			// Incremente afin de se deplacer dans l ArrayList.
			i=i+1;
		}
		
	}
}
