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
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

/**
 * Envoie un sms au Praticien
 * 
 * @author fred
 *
 */
@Stateless
public class SendSmsReminderPraticienService {
	
	// RASPISMS ip et authentification

	private String adresseIp = "78.234.179.176";
	private String raspiSmsLogin = "admin@example.fr";
	private String raspiSmsPwd = "admin";
	private String lignesDeRdv = "";
	private String lieuRdv, introSingPlur, rdvHeure, praticienTelMobile, praticienPrenom;
	
	private static final Logger logger = LogManager.getLogger(SendSmsReminderPraticienService.class);
	
	@EJB
	private RdvDao rdvdao;
	
	/**
	 * Envoie un Sms au Praticien
	 * chaque joursà 20H00
	 * 
	 * @throws Exception Exception
	 */
//	@Schedule(dayOfWeek = "*", hour = "8-21", minute = "30")
	@Schedule(dayOfWeek = "*", hour = "20", minute = "00")
	public void sendSmsPraticiensScheduled() throws Exception {
	
	String rdvDateDuJourFormate = recuDateDuJourplusUnFormate();

	// recupere la liste des rdv du jour J+1 dans un ArrayList
	ArrayList<Rdv> rdvList = new ArrayList<Rdv>();
	rdvList = (ArrayList<Rdv>) rdvdao.obtenirListeRdvViaDatePraticienSuscribedMailReminder(rdvDateDuJourFormate);
	logger.info("SmsReminderPraticienService Log: Liste des rdv recupereree dans un ArrayList");
	// Les IdPraticien sont envoyés dans un nouvel ArrayList.
//	ArrayList<Integer> listIdPratt = new ArrayList<Integer>();
	ArrayList<Integer> listIdPratt = new ArrayList<Integer>();
	Iterator<Rdv> iter = rdvList.iterator();
	Integer i = 0;while(iter.hasNext())
	{
		listIdPratt.add(rdvList.get(i).getPraticien().getIdPraticien());
		i++;
		iter.next();
	}
	logger.info("SmsReminderPraticienService Log : creation ArrayList avec uniquement les IdPratt");
	//	Si idPratt > 1 ipPrattMoreThanOnce = (boolean) True or False
	//		Mettre les idPraticien dan un tableau pour compter les occurence
	// 			recupere les 
	
	// mettre la liste dans un tableau
	// detecter les occurence si 1 < occurence passer a true  

	// Filtre des Id pour eviter les doublons
	HashSet<Integer> listWithSortedUniqIdPratt = new HashSet<Integer>(listIdPratt);
	logger.info("SmsReminderPraticienService Log: tri des IdPratt pour enlever les doulons avec HashSet");
	// Envoi le HasSet dans un tableau pour exploiter le tri 
	Integer nbIdPrattFiltreeUnique[] = new Integer[listWithSortedUniqIdPratt.size()];
	logger.info("SmsReminderPraticienService Log : Creation d un tableau IdPratt pour iteration");
	// Envoie les IdPraticiens dans tableau pour Iteration
	listWithSortedUniqIdPratt.toArray(nbIdPrattFiltreeUnique);
	Iterator<Rdv> iterRdv = rdvList.iterator();
	iterRdv = rdvList.iterator();	
	
	for(Integer bouclePraticien = 0;bouclePraticien<listWithSortedUniqIdPratt.size();bouclePraticien++)	{
		logger.info("SmsReminderPraticienService Log log: Entree dans boucle principale FOR");
		logger.info("SmsReminderPraticienService Log log: Position de la Boucle : " + ((bouclePraticien) + 1 ) + " / " + listWithSortedUniqIdPratt.size());
		
		// Initialistion de numRdv pour
		Integer numRdv = 0;
		// inititialisation de la liste des Rdv qui sera dans le mail.
		
		while (iterRdv.hasNext()) {
			logger.info("SmsReminderPraticienService Log log: Entree dans le WHILE");
			logger.info("SmsReminderPraticienService Log log: getIdPraticien_rdvlist : " + rdvList.get(numRdv).getPraticien().getIdPraticien());
			logger.info("SmsReminderPraticienService Log log: getIdPraticien_Array : " + nbIdPrattFiltreeUnique[bouclePraticien]);
			if (rdvList.get(numRdv).getPraticien().getIdPraticien() == nbIdPrattFiltreeUnique[bouclePraticien]) {
				
				// appel à la methode pour savoir si l idPraticien a plusieur rdv dan rdvList
				// envoie a la methode Arraylist<Rdv>listIdPratt et Integer IdPraticien
				logger.info("SmsReminderPraticienService Log :Avant detecteur d ocurences rdvLsit : " + rdvList.get(numRdv).getIdRdv());
				logger.info("SmsReminderPraticienService Log : Avant detecteur d ocurences idPratitien : " + rdvList.get(numRdv).getPraticien().getIdPraticien());
				logger.info("SmsReminderPraticienService Log : liste idPratt: " + listIdPratt.toString());
				Boolean idPrattMoreThanOnce = numberOfidPrattMoreThanOnceDetector(listIdPratt, rdvList.get(numRdv).getPraticien().getIdPraticien());
				logger.info("SmsReminderPraticienService log: idpraticien > 1 : " + idPrattMoreThanOnce);
				
				Timestamp rdvDateHeureTS = rdvList.get(numRdv).getDateHeureDebut();
				long rdvDHDebutTsToLong = rdvDateHeureTS.getTime();
				String rdvDHDebutLongToString = Long.toString(rdvDHDebutTsToLong);
				Date rdvDHDebuttsStringToDate = new Date(Long.parseLong(rdvDHDebutLongToString));
//				String dateformatpattern = "dd-MM-yyyy";
//				SimpleDateFormat sdfD = new SimpleDateFormat(dateformatpattern);
//				String rdvDateConvertedToString = sdfD.format(rdvDHDebuttsStringToDate);
				String heureformatpattern = "HH:mm";
				SimpleDateFormat sdfH = new SimpleDateFormat(heureformatpattern);
				String rdvHeureConvertedToString = sdfH.format(rdvDHDebuttsStringToDate);
//				rdvDate = rdvDateConvertedToString;
				rdvHeure = rdvHeureConvertedToString;
				praticienPrenom = WordUtils.capitalize(rdvList.get(numRdv).getPraticien().getPrenomPraticien());
				praticienTelMobile = rdvList.get(numRdv).getPraticien().getTeleMobilePraticien();
				String clientPrenom = WordUtils.capitalize(rdvList.get(numRdv).getClient().getPrenomClient());
				String clientNom = (rdvList.get(numRdv).getClient().getNomClient()).toUpperCase();
				String activite = (rdvList.get(numRdv).getPrestation().getActivite().getActiviteNom()).toUpperCase();
				String prestationsoin = WordUtils.capitalizeFully(rdvList.get(numRdv).getPrestation().getSoin());
				
				Integer idLieuRdv = rdvList.get(numRdv).getLieuRdv().getIdLieuRdv();
				Integer idClientGenre = rdvList.get(numRdv).getClient().getGenreClient().getIdGenre();
				String clientCliente, adresseLieuRdvNumeroRue, adresseLieuRdvRue, adresseLieuRdvZipCode, 
						adresseLieuRdvVille;
				// Selon genre mettre client ou client 1F/2H
				if (idClientGenre == 1) {					
					clientCliente = "Cliente :";					
				} else {					
					clientCliente = "Client :";					
				}			
				
				// Si plus d un Rdv mettre au singulier ou pluriel
				if (idPrattMoreThanOnce == true) {					
					introSingPlur = "Vos rendez-vous de demain :";					
				} else {					
					introSingPlur = "Votre rendez-vous de demain :";				
				}
				
				// Selon l idRdv l adresse sera adaptee. 
				if (idLieuRdv == 3) {
					lieuRdv = "Domicile " + clientCliente;
					adresseLieuRdvNumeroRue = rdvList.get(numRdv).getClient().getAdresse().getNumero();
					adresseLieuRdvRue = rdvList.get(numRdv).getClient().getAdresse().getRue();
					adresseLieuRdvZipCode = rdvList.get(numRdv).getClient().getAdresse().getZipCode();
					adresseLieuRdvVille = rdvList.get(numRdv).getClient().getAdresse().getVille();
				} else {
					lieuRdv = WordUtils.capitalize(rdvList.get(numRdv).getLieuRdv().getLieuRdv());
					adresseLieuRdvNumeroRue = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getNumero();
					adresseLieuRdvRue = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getRue();
					adresseLieuRdvZipCode = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getZipCode();
					adresseLieuRdvVille = rdvList.get(numRdv).getLieuRdv().getAdresseLieuRdv().getVille();
				}

				// Liste des Rdv à integrer dans l Email version HTML
				lignesDeRdv += "";
				lignesDeRdv += "%0d%0a" + rdvHeure + " " +activite + "_"	+ prestationsoin
									.replaceAll(" ", "%20").replace("è", "%C3%A8").replace("é", "%C3%A9")
								+ "." + "%0d%0a";
				lignesDeRdv += clientCliente + " " + clientNom + " " 
								+ clientPrenom + "." + "%0d%0a";
				
				lignesDeRdv += "Lieu : " + lieuRdv + "%0d%0a";
				lignesDeRdv += adresseLieuRdvNumeroRue + "%20" + adresseLieuRdvRue + "%0d%0a";
				lignesDeRdv += adresseLieuRdvZipCode + "%20" + adresseLieuRdvVille + "%0d%0a";
				
				numRdv++;
				iterRdv.next();
			} else {

				numRdv++;
				iterRdv.next();

			} // sortie du if test idPraticienList _ idPraticienArray

		} // fin sortie du While rdvList.iteration

		// Compiler le message
		String messagedebut = "Bonjour " + praticienPrenom + ",".replaceAll(" ", "%20").replace("è", "%C3%A8").replace("é", "%C3%A9");
		String messagefin = "%0d%0a%0d%0aCordialement,%0d%0aOpus Beauté%0d%0a";
 		String messSms = messagedebut.replaceAll(" ", "%20").replace("è", "%C3%A8").replace("é", "%C3%A9")
 				+ "%0d%0a" + introSingPlur.replaceAll(" ", "%20").replace("è", "%C3%A8").replace("é", "%C3%A9")
 				+ "%0d%0a" + lignesDeRdv.replaceAll(" ", "%20").replace("è", "%C3%A8").replace("é", "%C3%A9")
 				+ messagefin.replaceAll(" ", "%20").replace("è", "%C3%A8").replace("é", "%C3%A9");
 		
 		
		logger.info("retour ligne sms : " + messSms);		
		logger.info("numero portable : " + praticienTelMobile);
		sendSmsPraticiens(messSms, praticienTelMobile);
		lignesDeRdv = "";
		messagedebut = "";
		messagefin = "";
		messSms = "";
		
		
		// .replace(" ", "%20").replace("à", "%C3%A0").replace("è", "%C3%A8").replace("é", "%C3%A9").replace("À", "%C3%80")
				
		// Envoyer le(s) SMS au praticien(s)


		iterRdv = rdvList.iterator();
	} // Sortie dde la boucle for nb IdPraticien
	
	return;

	}
	
	/**
	 * Verifie si il y a un ou plusieurs Praticien
	 * 
	 * @param listIdPratt ArrayList
	 * @param nbIdPrattFiltreeUnique Integer
	 * @return boolean
	 */
	private Boolean numberOfidPrattMoreThanOnceDetector(ArrayList<Integer> listIdPratt, Integer nbIdPrattFiltreeUnique) {
		logger.info("SmsReminderPraticienService Log : Reception de la liste listIdPratt " + listIdPratt.toString());
		logger.info("SmsReminderPraticienService Log : Taille de la liste listIdPratt " + listIdPratt.size());
		logger.info("SmsReminderPraticienService Log : verifier si occurence de l idPratt suivant : " + nbIdPrattFiltreeUnique);
		
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
    		logger.info("SmsReminderPraticienService Log : Occurrence idPratt => " + nbIdPrattFiltreeUnique + ", trouve "+ iFound + " fois");
    		idPrattMoreThanOnce = true;
    	} else {
    		logger.info("SmsReminderPraticienService Log : Pas d occurrence pour l idPratt => " + nbIdPrattFiltreeUnique);
    		idPrattMoreThanOnce = false;
    	}

        logger.info("SmsReminderPraticienService Log : Valeur du Boolean idPrattMoreThanOnce => " + idPrattMoreThanOnce);
		return idPrattMoreThanOnce;
	}

	/**
	 * Recupere la date du jour et ajout 1 jour
	 * 
	 * @return String
	 * @throws DaoException Exception
	 */
	private String recuDateDuJourplusUnFormate() throws DaoException {
	
		logger.info("SmsReminderPraticienService log : Recuperation de la date J+1");
	
		// recuperer la date du jour et ajout 1 jours pour J+1
		LocalDate DateTimeDuJour = LocalDate.now();
		// logger.info("MailRemiderSender log : Date du jour : " +
		// DateTimeDuJour );
		Period definiPlusUnJour = Period.of(0, 0, 1);
		LocalDate jourJPlusUn = DateTimeDuJour.plus(definiPlusUnJour);
		// logger.info("MailRemiderSender log : Date du j+1 : " + jourJPlusUn);
	
		// formatage de J+1 String pour passage au RdvDao
		String rdvDateDuJourPlusUnFormate = jourJPlusUn.toString();
	
		return rdvDateDuJourPlusUnFormate;
	}

	/**
	 * Envoi un SMS au Praticien 
	 * 
	 * @param messSms String
	 * @param praticienTelMobile String
	 * @throws Exception Exception
	 */
	private void sendSmsPraticiens(String messSms, String praticienTelMobile) throws Exception {
		
		String buildUrl = "http://" + adresseIp + "/RaspiSMS/smsAPI/?email=" + raspiSmsLogin + "&password="
				+ raspiSmsPwd + "&numbers=" + praticienTelMobile + "&text=" + messSms;

		URL url = new URL(buildUrl);
		logger.info("SmsReminderPraticienService log : sms url : " + url);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("GET");
		OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
		logger.info("SmsReminderPraticienService Exception : code reponse : " + httpCon.getResponseCode());
		logger.info("SmsReminderPraticienService Exception : code reponse : " + httpCon.getResponseMessage());
		out.close();
		logger.info("SmsReminderPraticienService Exception : Fin de procedure.");
		
	}

}