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

/**
 * Gere L envoi de Mail de rappel de Rdv 
 * au Praticien
 * 
 * @author fred
 *
 */
@Stateless
public class SendMailReminderPraticienService {

	@EJB
	private RdvDao rdvdao;

	static final Logger logger = LogManager.getLogger(SendMailReminderPraticienService.class);

	private static final String compteEmailLogin = "dobylov";
	private static final String compteEmailPwd = "lasouris";
	private static String customMailSubject;
	private static String customMessageDynamic;
	private String lieuRdv;
	private String introSingPlur;
	
	/**
	 * Methode chargee d envoyer un email
	 * a un ou des praticiens pour rappeler un ou des Rdvs
	 * L envoie est automatise et plannifie a 20H	 
	 * grace au service Timer annote "@SCHEDULE"
	 * 
	 * @return ArrayList
	 * @throws DaoException Exception
	 */	
	// Service Timer: Envoyer un mail de rappel au client.
	// Freq: Tous les jours de la semaine à 20H00.
	@Schedule(dayOfWeek = "*", hour = "20", minute = "00")
//	@Schedule(dayOfWeek = "*", hour = "8-21", minute = "30")
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
					
					// appel à la methode pour savoir si l idPraticien a plusieur rdv dan rdvList
					// envoie a la methode Arraylist<Rdv>listIdPratt et Integer IdPraticien
					logger.info("Avant detecteur d ocurences rdvLsit : " + rdvList.get(numRdv).getIdRdv());
					logger.info("Avant detecteur d ocurences idPratitien : " + rdvList.get(numRdv).getPraticien().getIdPraticien());
					logger.info("Avant detecteur d ocurences : liste idPratt: " + listIdPratt.toString());
					Boolean idPrattMoreThanOnce = numberOfidPrattMoreThanOnceDetector(listIdPratt, rdvList.get(numRdv).getPraticien().getIdPraticien());
					logger.info("SmsPraticien log: idpraticien > 1 : " + idPrattMoreThanOnce);
					
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
					String clientPrenom = WordUtils.capitalize(rdvList.get(numRdv).getClient().getPrenomClient());
					String clientNom = rdvList.get(numRdv).getClient().getNomClient().toUpperCase();
					String activite = rdvList.get(numRdv).getPrestation().getActivite().toUpperCase();
					String prestationsoin = WordUtils.capitalize(rdvList.get(numRdv).getPrestation().getSoin());
					Integer idLieuRdv = rdvList.get(numRdv).getLieuRdv().getIdLieuRdv();
					String adresseLieuRdvNumeroRue, adresseLieuRdvRue, adresseLieuRdvZipCode, adresseLieuRdvVille;

					if (idLieuRdv == 3) {

						lieuRdv = "Domicile clientèle";
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

					// Si plus d un Rdv mettre au singulier ou pluriel
					if (idPrattMoreThanOnce == true) {					
						introSingPlur = "Vos rendez-vous de demain :";					
					} else {					
						introSingPlur = "Votre rendez-vous de demain :";				
					}
					
					// Liste des Rdv à integrer dans l Email version HTML
					lignesRdvToMail += "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
							+ "<b>" + rdvHeure + "</b>&nbsp" + activite + "_"
							+ prestationsoin + "." + "<br>&emsp;&emsp;Clientèle:&nbsp"
							+ clientNom + "&nbsp" + clientPrenom + "<br>" + "&emsp;&emsp;Lieu:&nbsp"
							+ lieuRdv + "</span></p>"
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
					+ "Bonjour " + praticienPrenom + ",</span></p>"
					+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
					+ introSingPlur + "</span></p>" + lignesRdvToMail
					+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
					+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
					+ "La Bonbonni&egrave;re d'Audrey</span></p><p>&nbsp;</p><br>";

			// Envoyer le mail au praticien(s)
			moteurEmail(message, destinataire, customMailSubject);

			iterRdv = rdvList.iterator();
		} // Sortie dde la boucle for nb IdPraticien

		return rdvList;

	}

	/**
	 * Recupere la date du jour et ajoute 1 jour
	 * 
	 * @return String
	 * @throws DaoException Exception
	 */
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

	/**
	 * Retourne si il y a plus d un Praticien
	 * dans la liste des rdv plannifies a J+1
	 * 
	 * @param listIdPratt ArrayList
	 * @param nbIdPrattFiltreeUnique Integer
	 * @return Boolean
	 */
	private Boolean numberOfidPrattMoreThanOnceDetector(ArrayList<Integer> listIdPratt, Integer nbIdPrattFiltreeUnique) {
		logger.info("Occurrences checker : Reception de la liste listIdPratt " + listIdPratt.toString());
		logger.info("Occurrences checker : Taille de la liste listIdPratt " + listIdPratt.size());
		logger.info("Occurrences checker : verifier si occurence de l idPratt suivant : " + nbIdPrattFiltreeUnique);
		
		// Conversion ArrayList en array
		Integer arrayOfIdPrattFromArrayListOfIdPratt [] = new Integer [listIdPratt.size()];
		listIdPratt.toArray(arrayOfIdPrattFromArrayListOfIdPratt);
		logger.info("Occurrences checker : Conversion de la liste listIdPratt en Array taille : " 
				+ arrayOfIdPrattFromArrayListOfIdPratt.length);
		// detcter les occurences
        int iFound =0;
        Boolean idPrattMoreThanOnce = false;
        
        for (int i = 0 ; i < arrayOfIdPrattFromArrayListOfIdPratt.length; i++) {
        	if (arrayOfIdPrattFromArrayListOfIdPratt[i] == nbIdPrattFiltreeUnique) {
        		iFound++;
        	}  
        }  
        
      	if (iFound > 1) {
    		logger.info("Occurrences checker : Occurrence idPratt => " + nbIdPrattFiltreeUnique + ", trouve "+ iFound + " fois");
    		idPrattMoreThanOnce = true;
    	} else {
    		logger.info("Occurrences checker : Pas d occurrence pour l idPratt => " + nbIdPrattFiltreeUnique);
    		idPrattMoreThanOnce = false;
    	}

        logger.info("Occurrences checker : Valeur du Boolean idPrattMoreThanOnce => " + idPrattMoreThanOnce);
		return idPrattMoreThanOnce;
	}
	
	/**
	 * Moteur d envoi d Email
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

			logger.info("MailRemiderSender log : Tentative Envoi de l EMail.");
		} catch (MessagingException exp) {
			logger.error("MailRemiderSender log : Echec Envoi de l EMail.");
			throw new RuntimeException(exp);
		}
	}

}
