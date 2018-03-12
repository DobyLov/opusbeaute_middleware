package fr.labonbonniere.opusbeaute.middleware.service.mail;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

/**
 * Gere l envoi d email de rappel de Rdv 
 * au Client
 * 
 * @author fred
 *
 */
@Stateless
public class SendMailReminderClientService {

	@EJB
	private RdvDao rdvdao;

	static final Logger logger = LogManager.getLogger(SendMailReminderClientService.class);

	// credentiel du compt mail
	private static final String compteEmailLogin = "dobylov";
	private static final String compteEmailPwd = "lasouris";
	// @IP du serveur ou est hebergé l application.
	private static final String ipAppSrvHoster = "192.168.1.100";
	
	private static String customMailSubject;
	private static String customMessageDynamic;
	private String clientEmail;
	private String lieuRdv;

	/**
	 * Methode chargee d envoyer un email
	 * a un ou des clients pour rappeler un ou des Rdvs
	 * L envoie est automatise et plannifie a 20H	 
	 * grace au service Timer annote "@SCHEDULE"
	 * 
	 * @throws DaoException Exception
	 */
	// Service Timer: Envoyer un mail de rappel au client.
	// Freq: Tous les jours de la semaine à 20H00.
//	@Schedule(dayOfWeek = "*", hour = "8-21", minute = "30")
	@Schedule(dayOfWeek = "*", hour = "20", minute = "00")
	public void envoyerEmailRappelRdvClientScheduled() throws DaoException {

		// logger.info("SendMailReminderClientService log : Envoi de EMail
		String rdvDateDuJourPlusUnFormate = recuDateDuJourplusUnFormate();

		// Rempli l ArrayList avec la liste d objets Rdv J+1 avec SuscribedMail
		ArrayList<Rdv> rdvList = new ArrayList<Rdv>();
		rdvList = (ArrayList<Rdv>) rdvdao.obtenirListeRdvViaDateAndMailSuscribedTrue(rdvDateDuJourPlusUnFormate);
		// logger.info("MailRemiderSender log : " + rdvList.size()


		// Iteration de l arrayList bien moche :
		for (Integer i = 0; i < rdvList.size(); i++) {

			String isSucribedMailreminder = rdvList.get(i).getClient().getSuscribedMailReminder();
			// logger.info("MailRemiderSender log : SUSreminder : " +

			if (isSucribedMailreminder.equalsIgnoreCase("t")) {

				// Recuperation des informations necessaires pour l envoie de l
				// Email.
				String clientPrenom = rdvList.get(i).getClient().getPrenomClient();
				clientEmail = rdvList.get(i).getClient().getAdresseMailClient();
				Timestamp rdvDateHeure = rdvList.get(i).getDateHeureDebut();
				String activite = rdvList.get(i).getPrestation().getActivite();
				String prestationsoin = rdvList.get(i).getPrestation().getSoin();
				Integer idLieuRdv = rdvList.get(i).getLieuRdv().getIdLieuRdv();
				String adresseLieuRdvNumeroRue, adresseLieuRdvRue, adresseLieuRdvZipCode, adresseLieuRdvVille;

				// LieurRdv == 3 -> Domicile du client
				if (idLieuRdv == 3) {
					lieuRdv = "A votre domicile";
					adresseLieuRdvNumeroRue = rdvList.get(i).getClient().getAdresse().getNumero();
					adresseLieuRdvRue = rdvList.get(i).getClient().getAdresse().getRue();
					adresseLieuRdvZipCode = rdvList.get(i).getClient().getAdresse().getZipCode();
					adresseLieuRdvVille = rdvList.get(i).getClient().getAdresse().getVille();

				} else {
					lieuRdv = rdvList.get(i).getLieuRdv().getLieuRdv();
					adresseLieuRdvNumeroRue = rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getNumero();
					adresseLieuRdvRue = rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getRue();
					adresseLieuRdvZipCode = rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getZipCode();
					adresseLieuRdvVille = rdvList.get(i).getLieuRdv().getAdresseLieuRdv().getVille();
				}

				String rdvHeure = timestampToStringTime(rdvDateHeure);

				customMessageDynamic = "<br><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
						+ "Bonjour " + WordUtils.capitalizeFully(clientPrenom) + ",</span></p>"
						+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "vous avez rendez-vous demain &agrave; " + rdvHeure + ".</span></p>"
						+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "Pour votre s&eacute;ance : <em>" + activite.toUpperCase() + "_"
						+ WordUtils.capitalizeFully(prestationsoin) + "." + "</em></span></p>"
						+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "Votre soin aura lieu &agrave; l'&nbspadresse suivante :</span></p>"
						+ "<address style=\"font-size: 14.4px;\"><span style=\"font-size: large;\"><span style=\"font-family: arial, helvetica, sans-serif;\">"
						+ "<u>" + lieuRdv + "&nbsp:</u><br/>" + adresseLieuRdvNumeroRue + "&nbsp" + adresseLieuRdvRue
						+ "<br /></span></span>"
						+ "<span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
						+ adresseLieuRdvZipCode + "&nbsp" + adresseLieuRdvVille + "</span></address>"
						+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
						+ "La Bonbonni&egrave;re d'Audrey</span></p><p>&nbsp;</p>"
						+ "<p style=\"font-size: 4.0px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: small;\">"
						+ "Se desinscrire au rappel de rendez-vous " 
						+ "<a href=\"http://" + ipAppSrvHoster + ":8080/opusbeaute-0/obws/unsuscribe/rdvreminder/"
						+ clientEmail 
						+ "/M7AkuQu2hGHvzdYcDfxbPkcWaP9fe42G\">ici</a>."
						+"</p>";


			} else {

				logger.info("MailRemiderSender log : le Rdv id : " + rdvList.get(i).getIdRdv() + "/" + rdvList.size()
						+ " n est pas traite car subscribeMailReminder est a : "
						+ rdvList.get(i).getClient().getSuscribedMailReminder());
			}

			// appel methode pour envoyer l Email avec les infos
			customMailSubject = "La Bonbonnière d'Audrey : Rappel de Rdv ";
			String destinataire = clientEmail;
			moteurEmail(customMessageDynamic, destinataire, customMailSubject);
			logger.info("Mail destinataire : " + clientEmail + "\n" + customMessageDynamic);

		}

	}
	
	/**
	 * Formatage de la date de TimeStamp
	 * En chaine String
	 * 
	 * @param rdvDateHeure Timestamp
	 * @return String
	 */
	private String timestampToStringTime(Timestamp rdvDateHeure) {

		// Formattage du timeStamp rdvHeure
		// logger.info("MailRemiderSender log : Conversion de tu TS
		long rdvDHDebutTsToLong = rdvDateHeure.getTime();
		String rdvDHDebutLongToString = Long.toString(rdvDHDebutTsToLong);
		Date rdvDHDebuttsStringToDate = new Date(Long.parseLong(rdvDHDebutLongToString));
		// logger.info("MailRemiderSender log : Conversion de tu tsToDate : " +

		String heureformatpattern = "HH:mm";
		SimpleDateFormat sdfH = new SimpleDateFormat(heureformatpattern);
		String rdvHeureConvertedToString = sdfH.format(rdvDHDebuttsStringToDate);
		// logger.info("MailRemiderSender log : Conversion de heure : " +

		String rdvHeure = rdvHeureConvertedToString;
		// logger.info("MailRemiderSender log : Conversion TS to Date : " +


		return rdvHeure;

	}

	/**
	 * Recupere la date du jour et lui ajoute 1 jour
	 * 
	 * @return String
	 * @throws DaoException Exception
	 */
	private String recuDateDuJourplusUnFormate() throws DaoException {

		logger.info("MailRemiderSender log : Recuperation de la date J+1");

		// recuperer la date du jour et ajout 1 jours pour J+1
		LocalDate DateTimeDuJour = LocalDate.now();
		Period definiPlusUnJour = Period.of(0, 0, 1);
		LocalDate jourJPlusUn = DateTimeDuJour.plus(definiPlusUnJour);

		// formatage de J+1 String pour passage au RdvDao
		String rdvDateDuJourPlusUnFormate = jourJPlusUn.toString();

		return rdvDateDuJourPlusUnFormate;
	}

	/**
	 * Moteur d envoie d Email
	 * Grace a Javax.mail
	 * 
	 * @param message String
	 * @param destinataire String
	 * @param customMailSubject String
	 */
	private void moteurEmail(String message, String destinataire, String customMailSubject) {

		// Creation du mail

		// logger.info("MailRemiderSender log : Tentative Envoi de l EMail.");

		// Declare recipient's & sender's e-mail id.
		String destmailid = destinataire;
		String sendrmailid = "opusbeaute@gmail.com";
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

			logger.info("MailRemiderSender log : Envoi de l EMail ");
		} catch (MessagingException exp) {
			logger.error("MailRemiderSender log : Echec Envoi de l EMail.");
			throw new RuntimeException(exp);
		}
	}

}
