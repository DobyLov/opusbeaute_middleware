package fr.labonbonniere.opusbeaute.middleware.service.mail;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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

@Stateless
public class SendMailReminderPraticienService {

	@EJB
	private RdvDao rdvdao;

	static final Logger logger = LogManager.getLogger(SendMailReminderPraticienService.class);

	private static final String compteEmailLogin = "dobylov";
	private static final String compteEmailPwd = "lasouris";
	private static String customMailSubject;
	private static String customMessageDynamic;
	// private String clientEmail;
	private String lieuRdv;

	// Service Timer: Envoyer un mail de rappel au client.
	// Freq: Tous les jours de la semaine à 20H00.
	@Schedule(dayOfWeek = "*", hour = "20", minute = "00")
	public ArrayList<Rdv> envoyerUnMailRecapRdvPraticienScheduled() throws DaoException {

		// d ici que ca commence
		String rdvDateDuJourFormate = recuDateDuJourplusUnFormate();

		// Recuperation des Praticiens ayant un Rdv plannifé J+1 et inscrit a
		// MailReminder.
		// recupere la liste des rdv du jour J+1 dans un ArrayList
		ArrayList<Rdv> rdvList = new ArrayList<Rdv>();
		rdvList = (ArrayList<Rdv>) rdvdao.obtenirListeRdvViaDatePraticienSuscribedMailReminder(rdvDateDuJourFormate);

		// Iteration sur l ArrayList 'rdvList' pour récuperer Uniquement les
		// IdPraticiens qui on un Rdv J+1
		// Les IdPraticien sont envoyés dans un nouvel ArrayList.
		ArrayList<Integer> listIdPratt = new ArrayList<Integer>();
		Iterator<Rdv> iter = rdvList.iterator();
		Integer i = 0;
		while (iter.hasNext()) {
			listIdPratt.add(rdvList.get(i).getPraticien().getIdPraticien());
			i++;
			iter.next();
		}

		// Filtre des Id pour eviter les doublons
		HashSet<Integer> listWithSortedUniqIdPratt = new HashSet<Integer>(listIdPratt);

		// Envoi le HasSet dans un tableau pour exploiter le tri
		// Recupere le nombre d idPraticiens filtre pour qu il n y ai pas de
		// doublons.
		Integer nbIdPrattFiltreeUnique[] = new Integer[listWithSortedUniqIdPratt.size()];

		// Envoie les IdPraticiens dans tableau pour filtrer les envois de mail
		// par IdPraticien.
		listWithSortedUniqIdPratt.toArray(nbIdPrattFiltreeUnique);
		Iterator<Rdv> iterRdv = rdvList.iterator();
		iterRdv = rdvList.iterator();
		for (Integer bouclePraticien = 0; bouclePraticien < listWithSortedUniqIdPratt.size(); bouclePraticien++) {
			// Initialistion de numRdv pour
			Integer numRdv = 0;
			// inititialisation de la liste des Rdv qui sera dans le mail.
			String lignesRdvToMail = "";
			String praticienPrenom = "";
			String praticienEmail = "";
			String rdvDate = "";
			while (iterRdv.hasNext()) {
				if (rdvList.get(numRdv).getPraticien().getIdPraticien() == nbIdPrattFiltreeUnique[bouclePraticien]) {
					Timestamp rdvDateHeureTS = rdvList.get(numRdv).getDateHeureDebut();
					long rdvDHDebutTsToLong = rdvDateHeureTS.getTime();
					String rdvDHDebutLongToString = Long.toString(rdvDHDebutTsToLong);
					Date rdvDHDebuttsStringToDate = new Date(Long.parseLong(rdvDHDebutLongToString));
					String dateformatpattern = "dd-MM-yyyy";
					SimpleDateFormat sdfD = new SimpleDateFormat(dateformatpattern);
					String rdvDateConvertedToString = sdfD.format(rdvDHDebuttsStringToDate);
					String heureformatpattern = "HH:mm";
					SimpleDateFormat sdfH = new SimpleDateFormat(heureformatpattern);
					String rdvHeureConvertedToString = sdfH.format(rdvDHDebuttsStringToDate);
					rdvDate = rdvDateConvertedToString;
					String rdvHeure = rdvHeureConvertedToString;
					praticienPrenom = rdvList.get(numRdv).getPraticien().getPrenomPraticien();
					praticienEmail = rdvList.get(numRdv).getPraticien().getAdresseMailPraticien();
					String clientPrenom = rdvList.get(numRdv).getClient().getPrenomClient();
					String clientNom = rdvList.get(numRdv).getClient().getNomClient();
					String activite = rdvList.get(numRdv).getPrestation().getActivite();
					String prestationsoin = rdvList.get(numRdv).getPrestation().getSoin();
					Integer idLieuRdv = rdvList.get(numRdv).getLieuRdv().getIdLieuRdv();
					String adresseLieuRdvNumeroRue, adresseLieuRdvRue, adresseLieuRdvZipCode, adresseLieuRdvVille;

					if (idLieuRdv == 3) {

						lieuRdv = "Domicile clientelle";
						adresseLieuRdvNumeroRue = rdvList.get(numRdv).getClient().getAdresse().getNumero();
						adresseLieuRdvRue = rdvList.get(numRdv).getClient().getAdresse().getRue();
						adresseLieuRdvZipCode = rdvList.get(numRdv).getClient().getAdresse().getZipCode();
						adresseLieuRdvVille = rdvList.get(numRdv).getClient().getAdresse().getVille();

					} else {

						lieuRdv = rdvList.get(numRdv).getLieuRdv().getLieuRdv();
						adresseLieuRdvNumeroRue = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getNumero();
						adresseLieuRdvRue = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getRue();
						adresseLieuRdvZipCode = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getZipCode();
						adresseLieuRdvVille = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getVille();
					}

					// Liste des Rdv à integrer dans l Email version HTML
					lignesRdvToMail += "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
							+ "<b>" + rdvHeure + "</b>&nbsp" + activite.toUpperCase() + "_"
							+ WordUtils.capitalizeFully(prestationsoin) + "." + "<br>&emsp;&emsp;Clientelle:&nbsp"
							+ clientNom + "&nbsp" + clientPrenom + "<br>" + "&emsp;&emsp;Lieu:&nbsp"
							+ WordUtils.capitalizeFully(lieuRdv) + "</span></p>"
							+ "<address style=\"font-size: 14.4px;\"><span style=\"font-size: large;\"><span style=\"font-family: arial, helvetica, sans-serif;\">"
							+ "&emsp;&emsp;" + adresseLieuRdvNumeroRue + "&nbsp" + adresseLieuRdvRue
							+ "<br /></span></span>"
							+ "<span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
							+ "&emsp;&emsp;" + adresseLieuRdvZipCode + "&nbsp" + adresseLieuRdvVille
							+ "</span></address>";

					numRdv++;
					iterRdv.next();

				} else {

					numRdv++;
					iterRdv.next();

				} // sortie du if test idPraticienList _ idPraticienArray

			} // fin sortie du While rdvList.iteration

			// Compiler le message
			customMailSubject = "La Bonbonnière d'Audrey : Rappel de Rdv du " + rdvDate;
			String destinataire = praticienEmail;
			String message = customMessageDynamic = "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
					+ "Bonjour " + WordUtils.capitalizeFully(praticienPrenom) + ",</span></p>"
					+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
					+ "Voici le/les rendez-vous plannifié(s)<br> pour demain:</span></p>" + lignesRdvToMail
					+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
					+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
					+ "La Bonbonni&egrave;re d'Audrey</span></p><p>&nbsp;</p><br>";

			// Envoyer le mail au praticien(s)
			moteurEmail(message, destinataire, customMailSubject);

			iterRdv = rdvList.iterator();
		} // Sortie dde la boucle for nb IdPraticien

		return rdvList;

	}

	// private String timestampToStringTime(Timestamp rdvDateHeure) {
	//
	// // Formattage du timeStamp rdvHeure
	// // logger.info("MailRemiderSender log : Conversion de tu TS
	// // DatHeuredebut : " + rdvDateHeure);
	// long rdvDHDebutTsToLong = rdvDateHeure.getTime();
	// String rdvDHDebutLongToString = Long.toString(rdvDHDebutTsToLong);
	// Date rdvDHDebuttsStringToDate = new
	// Date(Long.parseLong(rdvDHDebutLongToString));
	// // logger.info("MailRemiderSender log : Conversion de tu tsToDate : " +
	// // rdvDHDebuttsStringToDate);
	//
	// String heureformatpattern = "HH:mm";
	// SimpleDateFormat sdfH = new SimpleDateFormat(heureformatpattern);
	// String rdvHeureConvertedToString = sdfH.format(rdvDHDebuttsStringToDate);
	// // logger.info("MailRemiderSender log : Conversion de heure : " +
	// // rdvHeureConvertedToString);
	//
	// String rdvHeure = rdvHeureConvertedToString;
	// // logger.info("MailRemiderSender log : Conversion TS to Date : " +
	// // rdvHeure);
	//
	// return rdvHeure;
	//
	// }

	private String recuDateDuJourplusUnFormate() throws DaoException {

		logger.info("MailRemiderSender log : Recuperation de la date J+1");

		// recuperer la date du jour et ajout 1 jours pour J+1
		LocalDate DateTimeDuJour = LocalDate.now();
		// logger.info("MailRemiderSender log : Date du jour : " +
		// DateTimeDuJour );
		Period definiPlusUnJour = Period.of(0, 0, 1);
		LocalDate jourJPlusUn = DateTimeDuJour.plus(definiPlusUnJour);
		// logger.info("MailRemiderSender log : Date du j+1 : " + jourJPlusUn);

		// formatage de J+1 String pour passage au RdvDao
		String rdvDateDuJourPlusUnFormate = jourJPlusUn.toString();
		// logger.info("MailRemiderSender log : Conversion de la DateHeure j+1
		// en Date: " + rdvDateDuJourPlusUnFormate);

		// // recuperation du nombre de Rdv plannifés à J+1
		// logger.info("MailRemiderSender log : Envoie à RdvDao la date J+1 : "
		// + rdvDateDuJourFormate);
		// Integer nbRdvJPlusUn =
		// rdvdao.renvoyerLeNbDeRdvDuJour(rdvDateDuJourFormate);
		// logger.info("MailRemiderSender log : " + nbRdvJPlusUn +" Rdv de
		// plannifes le : " + rdvDateDuJourFormate);

		return rdvDateDuJourPlusUnFormate;
	}

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

			logger.info("MailRemiderSender log : Tentative Envoi de l EMail.");
		} catch (MessagingException exp) {
			logger.error("MailRemiderSender log : Echec Envoi de l EMail.");
			throw new RuntimeException(exp);
		}
	}

}
