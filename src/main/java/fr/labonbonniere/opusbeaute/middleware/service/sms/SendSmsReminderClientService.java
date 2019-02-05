package fr.labonbonniere.opusbeaute.middleware.service.sms;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

/**
 * Gere l envoi de Sms de rappel de Rdv 
 * au Client
 * 
 * @author fred
 *
 */
@Stateless
public class SendSmsReminderClientService {

	private static final Logger logger = LogManager.getLogger(SendSmsReminderClientService.class);

	// RASPISMS ip et authentificatio
	private String adresseIp = "78.234.179.176";
	private String raspiSmsLogin = "admin@example.fr";
	private String raspiSmsPwd = "admin";

	@EJB
	private RdvDao rdvdao;

	/**
	 * Envoie un Sms automatiquemen
	 * chaques jours a 20H00
	 * 
	 * @throws Exception Exception
	 */
//	@Schedule(dayOfWeek = "*", hour = "8-21", minute = "30")
	@Schedule(dayOfWeek = "*", hour = "20", minute = "00")
	public void sendSmsClientScheduled() throws Exception {

		logger.info("SmsReminderClientservice log : Envoi d SMS Client.");
		// logger.info("MailRemiderSender log : Recuperation de la date J+1" );

		// recuperer la date du jour et ajout 1 jours pour J+1
		LocalDate DateTimeDuJour = LocalDate.now();
		// logger.info("MailRemiderSender log : Date du jour : " +
		// DateTimeDuJour );
		Period definiPlusUnJour = Period.of(0, 0, 1);
		LocalDate jourJPlusUn = DateTimeDuJour.plus(definiPlusUnJour);
		// logger.info("MailRemiderSender log : Date du j+1 : " + jourJPlusUn);

		// formatage de J+1 String pour passage au RdvDao
		String rdvDateDuJourFormate = jourJPlusUn.toString();
		// logger.info("MailRemiderSender log : Conversion de la DateHeure j+1
		// en Date: " + rdvDateDuJourFormate);

		// recuperation du nombre de Rdv plannifés à J+1
		// logger.info("MailRemiderSender log : Envoie à RdvDao la date J+1 : "
		// + rdvDateDuJourFormate);
		// Integer nbRdvJPlusUn =
		// rdvdao.renvoyerLeNbDeRdvDuJour(rdvDateDuJourFormate);
		// logger.info("MailRemiderSender log : " + nbRdvJPlusUn +" Rdv de
		// plannifes le : " + rdvDateDuJourFormate);

		// Rempli l ArrayList avec la liste d objets Rdv J+1 avec SuscribedMail
		// a T
		ArrayList<Rdv> rdvList = new ArrayList<Rdv>();
		rdvList = (ArrayList<Rdv>) rdvdao.obtenirListeRdvViaDateAndMailSuscribedTrue(rdvDateDuJourFormate);
		// logger.info("MailRemiderSender log : " + rdvList.size()
		// + " mails de rappel de rdv seront evoyes sur les " + rdvList.size() +
		// " plannifies");

		// Parcour l Array et extrait les informations de l object RDV etendu
		// necessaires pour envoyer un mail
		// Info necessaires : Prenom, Email, HeureRdv, PrestationSoin, LieuRdv,
		// adresseLieuRdv

		// Iteration de l arrayList bien moche :
		for (Integer i = 0; i < rdvList.size(); i++) {

			boolean isSucribedSmsReminder = rdvList.get(i).getClient().getSuscribedMailReminder();
			logger.info("SmsReminderClientservice log : Valeur de SmsRemider du client : " + isSucribedSmsReminder);
			// logger.info("MailRemiderSender log : SUSreminder : " +
			// isSucribedSmsReminder);
			if (isSucribedSmsReminder = true) {
				logger.info("SmsReminderClientservice log : Entre dans la boucle de traitement d envoi de SMS");
				// Recuperation des informations necessaires pour l envoie de l
				// Email.

				Timestamp rdvDateHeure = rdvList.get(i).getDateHeureDebut();
				// Formatage du timeStamp rdvHeure
				// logger.info("MailRemiderSender log : Conversion de tu TS
				// DatHeuredebut : " + rdvDateHeure);
				long rdvDHDebutTsToLong = rdvDateHeure.getTime();
				String rdvDHDebutLongToString = Long.toString(rdvDHDebutTsToLong);
				Date rdvDHDebuttsStringToDate = new Date(Long.parseLong(rdvDHDebutLongToString));
				// logger.info("MailRemiderSender log : Conversion de tu
				// tsToDate : " + rdvDHDebuttsStringToDate);


				String heureformatpattern = "HH:mm";
				SimpleDateFormat sdfH = new SimpleDateFormat(heureformatpattern);
				String rdvHeureConvertedToString = sdfH.format(rdvDHDebuttsStringToDate);
				// logger.info("MailRemiderSender log : Conversion de heure : "


				String rdvHeure = rdvHeureConvertedToString;
				// logger.info("MailRemiderSender log : Conversion TS to Date :
				// " + rdvHeure);

				String clientPrenom = WordUtils.capitalize(rdvList.get(i).getClient().getPrenomClient());
				String clientTelMobile = rdvList.get(i).getClient().getTelMobileClient();
				String activite = rdvList.get(i).getPrestation().getActivite().getActiviteNom();
				String prestationsoin = WordUtils.capitalize(rdvList.get(i).getPrestation().getSoin());
				String lieuRdv = null;
				String adresseLieuRdvNumeroRue = null;
				String adresseLieuRdvRue = null;
				String adresseLieuRdvZipCode = null;
				String adresseLieuRdvVille = null;
				Integer IdLieuRdv = rdvList.get(i).getLieuRdv().getIdLieuRdv();

				if (IdLieuRdv == 3) {
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

				// Creation du text pour SMS

				logger.info("SmsReminderClientservice log : Tentative Envoi SMS.");

				String messageClientPrenom = "Bonjour%20" + clientPrenom + ",%0d%0a%0d%0a";
				String messageCorps01 = "La%20Bonbonnière%20d%27Audrey%0d%0avous%20rappelle%20votre%20rendez-vous%0d%0ade%20demain%20à%20"
						+ rdvHeure + ".";
				String messagePrestaSoin = "%0d%0aPour%20le%20soin%20:%0d%0a"
						+ activite.toUpperCase() + "_" + WordUtils.capitalizeFully(prestationsoin)
								.replaceAll(" ", "%20").replace("è", "%C3%A8").replace("é", "%C3%A9")
						+ "%0d%0a" + "%0d%0a";
				String messageAnnonceAdresse = "À%20l%27adresse%20suivante%20:%0d%0a";
				String messageLieuRdv = lieuRdv + "%0d%0a";
				String messageAdressePart1 = adresseLieuRdvNumeroRue + "%20" + adresseLieuRdvRue + "%0d%0a";
				String messageAdressePart2 = adresseLieuRdvZipCode + "%20" + adresseLieuRdvVille;
				String messageFinPart1 = "%0d%0a%0d%0aCordialement,";
				String messageFinPart2 = "%0d%0aLa Bonbonnière.";

				// messageAdressePart1 = messageAdressePart1.replace(" ",
				// "%20");
				String messSms = messageClientPrenom.replace(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8")
						.replace("é", "%C3%A9").replace("À", "%C3%80")
						+ messageCorps01.replaceAll(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8")
								.replace("é", "%C3%A9").replace("À", "%C3%80")
						+ messagePrestaSoin.replaceAll(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8")
								.replace("é", "%C3%A9").replace("À", "%C3%80")
						+ messageAnnonceAdresse.replaceAll(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8")
								.replace("é", "%C3%A9").replace("À", "%C3%80")
						+ messageLieuRdv.replaceAll(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8")
								.replace("é", "%C3%A9").replace("À", "%C3%80")
						+ messageAdressePart1.replaceAll(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8")
								.replace("é", "%C3%A9").replace("À", "%C3%80")
						+ messageAdressePart2.replaceAll(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8")
								.replace("é", "%C3%A9")
						+ messageFinPart1 + messageFinPart2.replaceAll(" ", "%20").replace("à", "%C3%A0")
								.replace("è", "%C3%A8").replace("é", "%C3%A9");
				// URL url = new
				// URL("http://192.168.1.50/RaspiSMS/smsAPI/?email=admin@example.fr&password=admin"
				// + msg);

				String buildUrl = "http://" + adresseIp + "/RaspiSMS/smsAPI/?email=" + raspiSmsLogin + "&password="
						+ raspiSmsPwd + "&numbers=" + clientTelMobile + "&text=" + messSms;

				URL url = new URL(buildUrl);
				logger.info("SmsReminderClientservice log : sms url : " + url);
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.setDoOutput(true);
				httpCon.setRequestMethod("GET");
				OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
				logger.info("SmsReminderClientservice Exception : code reponse : " + httpCon.getResponseCode());
				logger.info("SmsReminderClientService Exception : code reponse : " + httpCon.getResponseMessage());
				out.close();
				logger.info("SmsReminderClientWs Exception : Fin de procedure.");

			} else { // fin de la boucle check suscribed to F
				logger.info("SmsReminderClientService log : le Rdv id : " + rdvList.get(i).getIdRdv() + "/" + rdvList.size()
						+ " n est pas traite car subscribeSmsReminder est a : "
						+ rdvList.get(i).getClient().getSuscribedSmsReminder());
			}
			// fin de l iteration dela liste d objets Rdv
		}

	}
}
