
package fr.labonbonniere.opusbeaute.middleware.service.rdv;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;

import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvInexistantException;

/**
 * Gere les Regles metier des Rdv
 * 
 * @author fred
 *
 */
@Stateless
public class RdvService {
	static final Logger logger = LogManager.getLogger(RdvService.class);

	@EJB
	private RdvDao rdvdao;

	/**
	 * Recupere la liste des Rdv
	 * 
	 * @return List Liste de Rdv
	 * @throws DaoException Si pb Bdd
	 */
	public List<Rdv> recupereListeRdv() throws DaoException {

		try {

			logger.info("RdvService log : Demande au Dao la liste des Rdv's");
			List<Rdv> lalisterdv = rdvdao.obtenirListeRdv();
			logger.info("RdvService - Liste des Rdv's recuperee");
			logger.info("RdvService - Nombre d items recuperes : " + lalisterdv.size());
			return lalisterdv;

		} catch (DaoException message) {
			logger.error("RdvService log : Probleme de la bdd.");
			throw new DaoException("RdvService Exception : Probleme de la bdd.");
		}
	}
	
	/**
	 * recupere la liste de Rdv par Client
	 * 
	 * @param idClient Integer
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> recupereListRdvParClient(final Integer idClient) throws DaoException {
		
		try {
			logger.info("RdvService log : Demande au Dao la liste des Rdv's par client");
			List<Rdv> laListeRdvParClient = rdvdao.obtenirListeRdvParClient(idClient);
			logger.info("RdvService - Liste des Rdv's recuperee");
			logger.info("RdvService - Nombre d items recuperes : " + laListeRdvParClient.size());
			return laListeRdvParClient;
			
		} catch (DaoException message) {
			logger.error("RdvService log : Probleme de la bdd.");
			throw new DaoException("RdvService Exception : Probleme de la bdd.");
		}
	}
	
	/**
	 * Recupere la liste de Rdv par date
	 * et par praticien
	 * 
	 * @param dateJJ String
	 * @param idPraticien Integer
	 * @return List
	 * @throws DaoException Exception
	 * @throws RdvDateIncorrecteException Exception
	 */
	public List<Rdv> recupereLaListeRdvParDateJJPraticien(final String dateJJ, final Integer idPraticien) throws DaoException, RdvDateIncorrecteException {
		
		try {
			isDateStringFormatValid(dateJJ);
			logger.info("RdvService log : Demande au Dao la liste des Rdv's par praticien et par date");
			List<Rdv> laListeRdvParDatePraticien = rdvdao.obtenirListeRdvDuJJParPraticien(dateJJ, idPraticien);
			logger.info("RdvService - Liste des Rdv's recuperee");
			logger.info("RdvService - Nombre d items recuperes : " + laListeRdvParDatePraticien.size());
			return laListeRdvParDatePraticien;
			
		} catch (DaoException message) {
			logger.error("RdvService log : Probleme de la bdd.");
			throw new DaoException("RdvService Exception : Probleme de la bdd.");
		}	
		
	}
	
	/**
	 * recupere la liste de Rdv via plage de date
	 * et par praticien
	 * 
	 * @param dateA String
	 * @param dateB String
	 * @param idPraticien Integer
	 * @return List 
	 * @throws DaoException Exception
	 * @throws RdvDateIncorrecteException Exception
	 */
	public List<Rdv> recupereLaListeRdvParPlageDatePraticien(final String dateA, final String dateB, final Integer idPraticien) throws DaoException, RdvDateIncorrecteException {
		
		try {
			isDateStringFormatValid(dateA);
			isDateStringFormatValid(dateB);
			logger.info("RdvService log : Demande au Dao la liste des Rdv's par praticien et par plage de date");
			List<Rdv> laListeRdvParplageDeDatePraticien = rdvdao.obtenirListeRdvParPlageDeDateParPraticien(dateA, dateB, idPraticien);
			logger.info("RdvService - Liste des Rdv's recuperee");
			logger.info("RdvService - Nombre d items dans la liste : " + laListeRdvParplageDeDatePraticien.size());
			return laListeRdvParplageDeDatePraticien;
			
		} catch (DaoException message) {
			logger.error("RdvService log : Probleme de la bdd.");
			throw new DaoException("RdvService Exception : Probleme de la bdd.");
		}	
		
	}
	
	/**
	 * Recupere un Rdv par son Id
	 * 
	 * @param idRdv Integer
	 * @return Rdv rdv
	 * @throws RdvInexistantException Si n existe pas
	 */
	public Rdv recupererUnRdv(final Integer idRdv) throws RdvInexistantException {

		try {
			logger.info("RdvService log : Demande a la bdd le Rdv id : " + idRdv);
			Rdv rdv = rdvdao.obtenirRdv(idRdv);
			logger.info("RdvService log : Rdv id : " + idRdv + " trouve, envoie de l objet Rdv a RdvWS");
			return rdv;

		} catch (RdvInexistantException message) {
			logger.error("RdvService log : Le rdv demande est introuvable");
			throw new RdvInexistantException("RdvService Exception : l' Id est introuvable dans la base");
		}
	}

	/**
	 * Recupere un liste de Rdv par Date
	 * entre 00H00 et 23H59
	 * 
	 * @param dateFournie String
	 * @return List
	 * @throws DaoException si pb Bdd
	 * @throws RdvDateIncorrecteException Exception
	 */
	public List<Rdv> recupereListeRdvParDate(final String dateFournie) throws DaoException, RdvDateIncorrecteException {

		try {
			isDateStringFormatValid(dateFournie);
			logger.info("Rdvservice log : Demande au Dao la liste des Rdv's par date selectionnee.");
			List<Rdv> lalisterdvpardate = rdvdao.obtenirListeRdvParDate(dateFournie);
			logger.info("Rdvservice log : Envoie a RdvWs de la liste des Rdv's par date selectionnee.");
			return lalisterdvpardate;

		} catch (DaoException message) {
			logger.error("Rdvservice Exception : probleme sur le format de la date.");
			throw new DaoException("RdvService Exception : probleme sur le format de la date.");
		}
	}

	/**
	 * Recupere une liste de Rdv par 
	 * plage de dates de rdv
	 * JourA 00h00
	 * JourB 23H59
	 * 
	 * @param dateA String
	 * @param dateB String
	 * @return List lalisterdvplagedate
	 * @throws DaoException Exception
	 */
	public List<Rdv> recupereListeRdvViaPlageDate(String dateA, String dateB) throws DaoException {

		try {
			isDateStringFormatValid(dateA);
			isDateStringFormatValid(dateB);
			logger.info("RdvService log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees.");
			List<Rdv> lalisterdvplagedate = rdvdao.obtenirListeRdvViaPlageDate(dateA, dateB);
			logger.info("RdvService log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			return lalisterdvplagedate;

		} catch (Exception message) {
			logger.error("RdvWs log : probleme sur le format de la/des date(s).");
			throw new DaoException("RdvWs Exception : probleme sur le format de la/des date(s).");
		}
	}

	/**
	 * Ajoute un nouveau Rdv
	 * 
	 * 
	 * @param rdv Rdv 
	 * @throws RdvDateIncorrecteException 
	 * @throws DateConversionException 
	 * @throws Exception RdvExistantException Si Rdv deja Existant
	 * @throws Exception DaoException	Si pb Bdd
	 * @throws Exception RdvEgaliteChevauchementException Si chevauchement de Rdv
	 * @throws Exception NoRdvException Si pas de rdv
	 * @throws Exception RdvNouveauDateDebutChevaucheRdvExistantDateFinException 
	 * @throws Exception RdvNouveauEnglobeRdvExistantException 
	 * @throws Exception RdvNouveauEnglobeParRdvExistantException 
	 * @throws Exception RdvNouveauDateFinChevaucheRdvExistantDateDebutException 
	 * @throws Exception RdvNonIntegrableException 
	 * @throws Exception TimestampToInstantConvertionException 
	 * @throws Exception RdvDebutChevauchementException Si Chevauchement au debut
	 * @throws Exception RdvFinChevauchementException	Si chevauchement a la fin de Rdv
	 * @throws Exception RdvEnglobantException probleme de Rdv engloabant
	 */
	public void ajoutRdv(Rdv rdv) throws RdvExistantException, DaoException, RdvEgaliteChevauchementException,
			NoRdvException, TimestampToZoneDateTimeConvertionException, RdvNonIntegrableException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException, RdvNouveauEnglobeParRdvExistantException, RdvNouveauEnglobeRdvExistantException, RdvNouveauDateDebutChevaucheRdvExistantDateFinException, RdvDateIncorrecteException, DateConversionException {
//		rdvdao.ajouterUnRdv(rdv);

		logger.info(
				"Rdvservice log : Debut traitement metier pour ajouter Du Rdv.");
//		logger.info(
//				"Rdvservice log : Requeter la bdd avec la date du Rdv afin de recuperer la liste des Rdv du jour demande.");
//
//		// Recuperation des ts debut et fin du rdv proposé
//		logger.info("Rdvservice log : Recuperation du TimeStamp tsRdvDebut : " + rdv.getDateHeureDebut());
//		Timestamp tsRdvDebut = rdv.getDateHeureDebut();
//		logger.info("Rdvservice log : Convert du TimeStamp tsRdvDebut : " + tsRdvDebut);
//		long longTsRdvDebutMilliSec = tsRdvDebut.getTime();
//		logger.info("Rdvservice log : Recuperation du TimeStamp tsRdvDebut : " + longTsRdvDebutMilliSec);
//
//		Timestamp tsRdvFin = rdv.getDateHeureFin();
//		long longTsRdvFinMilliSec = tsRdvFin.getTime();
//		logger.info("Rdvservice log : Recuperation du TimeStamp tsRdvDebut : " + tsRdvFin);
//
//		// Conversion du TS date début prestation en String date yyyy-MM-dd
//		logger.info("Rdvservice log : Conversion du tsRdvDebut en STR pour requeter la bdd.");
//		// traitement du TS récupéré
////		String convTsToStr = Long.toString(longTsRdvDebutMilliSec) + "000";
////		logger.info("Rdvservice log : Conversion du TS en STR plus 000 : " + convTsToStr);
////		Date tsToDate = new Date(Long.parseLong(convTsToStr));
//		Date tsToDate = new Date(longTsRdvDebutMilliSec);
//		logger.info("Rdvservice log : Conversion de la STR en Date : " + tsToDate);
//		String dateformatpattern = "yyyy-MM-dd";
////		String dateformatpattern = "dd/MM/yyyy";
//		SimpleDateFormat sdf = new SimpleDateFormat(dateformatpattern);
//		logger.info("Rdvservice log : Formatage de la date avec le pattern yyyy-MM-dd.");
//		String rdvDateDuJour = (String) sdf.format(tsToDate);
//		logger.info("Rdvservice log : Date formatee : " + rdvDateDuJour);
//		logger.info("Rdvservice log : Date prete a etre envoyee a la Bdd pour requeter.");
		List<Rdv> listRdvFournie = recupereLaListeDeRdvSelonLeJourFourniDansLeRdv(rdv);
		try {
			// Recupération du nombre de rdv programmés le jourj
//			logger.info(
//					"Rdvservice log : Demande a la Bdd le nb de Rdv a la date du : " + rdvDateDuJour);
//			Integer compteurRdvDuJour = rdvdao.renvoyerLeNbDeRdvDuJour(rdvDateDuJour);
//			logger.info(
//					"Rdvservice log : Il y a : " + compteurRdvDuJour + " Rdv a la date du : " + rdvDateDuJour);

			// recuperation des la dates de debut et fin du rdv a plannifier
			// Compteur de rdv ds la bdd, si different de Zero rdv poursuivre l
			// enregistrement du rdv
			logger.info("Rdvservice log : Liste Rdv : " + listRdvFournie.size() + " items a la date : " + tsToZdt(rdv.getDateHeureDebut()) );
			if (listRdvFournie.size() > 0 ) {
				// recuperer la liste des rdv selon la date du rdv au format
				// Json et la gere en JSONARRAY
//				List<Rdv> listeRdvJson = rdvdao.obtenirListeRdvParDate(rdvDateDuJour);
//				org.json.simple.JSONArray jsonobj =  (JSONArray) listeRdvJson; // a remettre si il le faut

				// Recupere le nombre d objet json pour les intervals
//				Integer nombreObjJson = jsonobj.size(); // a remettre si il le faut
//				Integer nombreObjJson = listeRdvJson.size(); // a remettre si il le faut
//				if (nombreObjJson == 0) {// a remettre si il le faut
//					throw new NoRdvException("probleme pour les intervals Il n y a pas de rdv");// a remettre si il le faut
//				}// a remettre si il le faut

				// Genereateur d intermediare de Rdv pour check le chevauchement
//				long tsResultSoustraction = longTsRdvFinMilliSec - longTsRdvDebutMilliSec;
//				long divisionResultatSoustraction = tsResultSoustraction / 10;
//				long incremRdvDateDebut = longTsRdvDebutMilliSec + divisionResultatSoustraction;

//				for (int i = 0; i < nombreObjJson; i++) {// a remettre si il le faut
				for (int i = 0; i < listRdvFournie.size(); i++) {
//					JSONObject obj = (JSONObject) jsonobj.get(i); // a remettre si il le faut
//					Rdv obj = listeRdvJson.get(i);// a remettre si il le faut
					Rdv objRdv = listRdvFournie.get(i);
//					Long rdvBddDateDebut = (Long) obj.get("dateHeureDebut");
//					Timestamp rdvBddDateDebut = obj.getDateHeureDebut(); // a remettre si il le faut
//					Timestamp rdvBddDateFin = obj.getDateHeureFin(); // a remettre si il le faut
//					Instant testtsToInt = rdvBddDateDebut.toInstant(); // a remettre si il le faut
					logger.info("Rdvservice Log : Comparaison du nouveau Rdv avec Rdvid " + objRdv.getIdRdv() + " dela liste => " + listRdvFournie.size() + " items");
					verificationIntegrationNouveauRdvDansAgenda(rdv, objRdv);
//					Boolean valbou = testInterval(incremRdvDateDebut, rdvBddDateDebut, rdvBddDateFin);
//
//					// test si le 
//					if (rdvBddDateDebut < longTsRdvDebutMilliSec && longTsRdvDebutMilliSec < rdvBddDateFin) {
//
//						logger.info(
//								"Rdvservice log : Probleme avec la Date de DEBUT du Rdv et le Rdv de la Bdd id : " + i);
//						logger.info("Rdvservice log : objet : " + obj);
//						throw new RdvDebutChevauchementException(
//								"Probleme de chevauchement sur le debut du Rdv Propose");
//
//					} else if (rdvBddDateDebut < longTsRdvFinMilliSec && longTsRdvFinMilliSec < rdvBddDateFin) {
//
//						logger.info(
//								"Rdvservice log : Probleme avec la date de FIN du Rdv et le Rdv de la Bdd id : " + i);
//						logger.info("Rdvservice log : objet : " + obj);
//						throw new RdvFinChevauchementException("Probleme de chevauchement sur la Fin du Rdv Propose");
//
//					} else if (rdvBddDateDebut == longTsRdvDebutMilliSec && longTsRdvFinMilliSec == rdvBddDateFin) {
//
//						logger.info(
//								"Rdvservice log : Probleme ce craineau est deja occupe par un rdv de la Bdd id : " + i);
//						logger.info("Rdvservice log : objet : " + obj);
//						throw new RdvEgaliteChevauchementException(
//								"Probleme de chevauchement sur le debut et la Fin du Rdv Propose");
//
//					} else if (valbou == true) {
//
//						logger.info(
//								"Rdvservice log : Probleme ce craineau est deja occupe par un rdv de la Bdd id : " + i);
//						logger.info("Rdvservice log : objet : " + obj);
//						System.out.println("entree dans la boucle test englobage");
//						throw new RdvEnglobantException(
//								"Probleme de chevauchement, ce rdv englobe un ou plusieur Rdv de la Bdd");
//					}
				}
			}

			logger.info("RdvService log : Demande d ajout d un nouveau Rdv dans la Bdd.");
			rdvdao.ajouterUnRdv(rdv);
			logger.info("RdvService log : Nouveau Rdv ajoute, avec l id : " + rdv.getIdRdv());

		} catch (RdvExistantException message) {
			logger.error("RdvService log : Impossible de creer ce rdv dans la Bdd.");
			throw new RdvExistantException("RdvService Exception : Impossible de creer ce rdv dans la Bdd.");
		}
	}

	/**
	 * Modifie un Rdv
	 * 
	 * @param rdv Rdv
	 * @throws RdvInexistantException Si Rdv inexistant
	 */
	public void modifduRdv(Rdv rdv) throws RdvInexistantException {

		try {
			logger.info("RdvService log : Demande de modification du Rdv id : " + rdv.getIdRdv() + " dans la Bdd.");
			rdvdao.modifieUnRdv(rdv);
			logger.info("RdvService log : Rdv id : " + rdv.getIdRdv() + " a ete modifie dans la Bdd.");

		} catch (RdvInexistantException message) {
			logger.error("RdvService log : Rdv id : " + rdv.getIdRdv() + " ne peut etre modifie dans la Bdd.");
			throw new RdvInexistantException(
					"RdvService Exception : Rdv avec l Id : " + rdv.getIdRdv() + " ne peut etre modifie.");
		}
	}

	/**
	 * Supprime un Rdv
	 * 
	 * @param idRdv Integer
	 * @throws RdvInexistantException Si Rdv inexistant
	 */
	public void supprimerUnrdv(final Integer idRdv) throws RdvInexistantException {

		try {
			logger.info("RdvService log : Demande de suppression du Rdv id : " + idRdv + " dans la Bdd.");
			rdvdao.supprimeunRdv(idRdv);
			logger.info("RdvService log : Rdv id : " + idRdv + " a bien ete supprime de la Bdd.");

		} catch (RdvInexistantException message) {
			logger.error("RdvService log : Rdv id : " + idRdv + " ne peut etre supprime dans la Bdd.");
			throw new RdvInexistantException(
					"RdvService Exception : Rdv id : " + idRdv + " ne peut etre supprime dans la Bdd.");
		}
	}

//	/**
//	 * Charge de verifier le chevauchement de rdv
//	 * 
//	 * @param incremRdvDateDebut long
//	 * @param rdvBddDateDebut long
//	 * @param rdvBddDateFin long
//	 * @return boolean
//	 */
//	public static boolean testInterval(long incremRdvDateDebut, long rdvBddDateDebut, long rdvBddDateFin) {
//
//		if (rdvBddDateDebut < incremRdvDateDebut && incremRdvDateDebut < rdvBddDateFin) {
//			
//			return true; 
//		
//		} else {
//			
//			return false;
//			
//		}
//	}
	
	/**
	 * Verifie si le format (YYYY-MM-DD) de la date est respecte
	 * @param dateToCheck String
	 * @throws RdvDateIncorrecteException Exception
	 */
	private void isDateStringFormatValid(String dateToCheck) throws RdvDateIncorrecteException  {
		
		logger.info("RdvService log : Verifie le format de la date fournie : " + dateToCheck);
		try {
			
			Pattern pattern = Pattern.compile("(20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])");
			Matcher matchOrNot = pattern.matcher(dateToCheck);
			Boolean isDateFormatIsOk = matchOrNot.matches();
			
			if ( isDateFormatIsOk == true ) {
				
			logger.info("RdvService log : Le format de la date Fournie Matche Boolean : " + isDateFormatIsOk);
			logger.info("RdvService log : Le format de la date Fournie Matche Value : " + dateToCheck);
			
			
			} else {
				logger.error("RdvService log : Le format de la date est incorrecte");
				logger.error("RdvService log : Format de la date recu : " + dateToCheck);
				logger.error("RdvService log : Format attendu : YYYY-MM-DD");
				throw new RdvDateIncorrecteException("RdvService Exception : Le format de la date est incorrecte");
			}
			
		} catch (Exception message) {
			logger.error("RdvService log : Le format de la date est incorrecte");
			throw new RdvDateIncorrecteException("RdvService Exception : Le format de la date est incorrecte");
			
		}
	}
	
	private List<Rdv> recupereLaListeDeRdvSelonLeJourFourniDansLeRdv(Rdv rdvFourni) throws DaoException, RdvDateIncorrecteException, DateConversionException {
		
		logger.info("RdvService Log : Recuperation de la liste de rdv selon le jour fourni");
		
		List<Rdv> rdvList = rdvdao.obtenirListeRdvParDate( timestampToStringDate( rdvFourni.getDateHeureDebut() ) );	
		
		return rdvList;
	}
	
	/**
	 * Converti un Timestamp au format YYYY-MM-DD
	 * @param Timestamp tsFourni
	 * @return String
	 * @throws RdvDateIncorrecteException 
	 * @throws DateConversionException 
	 */
	private String timestampToStringDate(Timestamp tsFourni) throws RdvDateIncorrecteException, DateConversionException {
		
		logger.info("RdvService Log : Conversion du ts en date string : " + tsFourni);
		
		try {
			
			Instant tsFourniToInstant = tsFourni.toInstant(); 
			ZoneId zId = ZoneId.of("Europe/Paris");
			logger.info("RdvService Log : ZoneId : " + zId);
			ZonedDateTime zdt = ZonedDateTime.ofInstant(tsFourniToInstant, zId);
			logger.info("RdvService Log : Ts en zdt : " + zdt);
			String dateYYYYMMDD = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(zdt);
			logger.info("RdvService Log : Ts en date string : " + dateYYYYMMDD);			
//			isDateStringFormatValid(dateYYYYMMDD);			
			return dateYYYYMMDD;
			
		} catch (Error message) {
			logger.error("RdvService Exception : La date fournie n a pa etee convertie correctement");
			throw new DateConversionException("RdvService Exception : La date fournie n a pa etee convertie correctement");
		}

	}
	
	

	
	/**
	 * Verifie si le nouveau Rdv est Integrable dans le calendrier parmis les rdv existants
	 * 
	 * 		 Vérification si le nouveau Rdv est intégrable dans le calendrier
	 * temps 0 => (t) ( dateDébut < dateFin ) rdvDateDébut toujour inférieur à dateFin
	 *						dd							df
	 *	0					|			rdvBdd			|
	 *				df
	 *	1	|	newRdv	|
	 *							df
	 *	2			|	newRdv	|
	 *								dd			df
	 *	3							| 	newRdv	|
	 *												dd
	 *	4											|	newRdv	|
	 *														dd
	 *	5													| newRdv |
	 *					dd									df
	 *	6				|				newRdv				|					
	 * Test 1&2 si rdvNewDateFin > rdvBddDateDebut 										=> newRdvDateFin Chevauche la date de debut d un rdv existant
	 * Test	3 	si rdvBddDateDébut < newRdvDateDebut && newRdvDateFin < rdvBddDateFin 	=> rdvBdd englobe newRdv
	 * Test 4&5 si rdvNewDateDebut < rdvBddDateFin 										=> newRdvDateFin chevauche la date de fin d un rdv Existant
	 * Test 6	si rdvNewDateDebut > rdvBddDateDebut && rdvNewDateFin > rdvBddDatefin 	=> newRdv englobe rdvBdd
	 *
	 * Pour le test Retirer une minute à dateRdvdebut et dateRdvFin du nouveau Rdv pour eviter le erreures de tests
	 * 
	 * @param Rdv newRdv
	 * @param Rdv rdvFromBdd
	 * @throws Exception TimestampToInstantConvertionException 
	 * @throws Exception RdvNonIntegrableException 
	 * @throws Exception RdvNouveauDateFinChevaucheRdvExistantDateDebutException 
	 * @throws Exception RdvNouveauEnglobeParRdvExistantException 
	 * @throws Exception RdvNouveauEnglobeRdvExistantException 
	 * @throws Exception RdvNouveauDateDebutChevaucheRdvExistantDateFinException 
	 * @throws Exception RdvFinChevauchementException 
	 */
	private void verificationIntegrationNouveauRdvDansAgenda( Rdv newRdv, Rdv rdvFromBdd) throws TimestampToZoneDateTimeConvertionException, RdvNonIntegrableException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException, RdvNouveauEnglobeParRdvExistantException, RdvNouveauEnglobeRdvExistantException, RdvNouveauDateDebutChevaucheRdvExistantDateFinException {
		
		logger.info("RdvService Log : Verification de l integration du rdv dans le calendrier");
		
		try {
			
			checkChevauchementDebutRdvExistant( newRdv, rdvFromBdd );
			
//			checkNouveauRdvEnglobeParRdvExistant( tsToZdt(newRdv.getDateHeureDebut()), tsToZdt( rdvFromBdd.getDateHeureDebut()), 
//												  tsToZdt(newRdv.getDateHeureFin()), tsToZdt( rdvFromBdd.getDateHeureFin()));
//			
//			checkChevauchementFinRdvExistant( dateHeureFourniePlusUneMinute( tsToZdt(newRdv.getDateHeureDebut()) ), 
//					tsToZdt( rdvFromBdd.getDateHeureFin()) );
//			checkRdvExistantEnglobeParNouveauRdv( dateHeureFournieMoinUneMinute( tsToZdt(newRdv.getDateHeureDebut()) ), 
//					tsToZdt(rdvFromBdd.getDateHeureDebut()), 
//												  dateHeureFournieMoinUneMinute( tsToZdt(newRdv.getDateHeureFin()) ), 
//												  tsToZdt(rdvFromBdd.getDateHeureFin()) );
			
		} catch (Error msg) {
			logger.error("RdvService Log : Le rdv n est pas integrable");
			throw new RdvNonIntegrableException("RdvService Exception : Le rdv n est pas integrable");
		}
		
	}
	
	/**
	 * Vérifie si la dateDeFin du nouveau Rdv chevauche la dateDeDebut du rdv dans la Bdd
	 * @param Instant newRdvDateHeureFin
	 * @param Instant rdvFromBddDateHeureDebut
	 * @throws RdvNouveauDateFinChevaucheRdvExistantDateDebutException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 * @throws  
	 * @throws RdvFinChevauchementException 
	 */
	private void checkChevauchementDebutRdvExistant(Rdv newRdv, Rdv rdvFromBdd) 
			throws RdvNouveauDateFinChevaucheRdvExistantDateDebutException, TimestampToZoneDateTimeConvertionException  {
		
		logger.info("RdvService log : Verification si la fin nouveau Rdv chevauche le debut du rdv bdd");
		
		if (dateHeureFournieMoinUneMinute( tsToZdt(newRdv.getDateHeureFin()))
				.isAfter(tsToZdt(rdvFromBdd.getDateHeureDebut())) 
				& dateHeureFournieMoinUneMinute(tsToZdt(newRdv.getDateHeureFin()))
				.isBefore(tsToZdt(rdvFromBdd.getDateHeureFin())) ) {
			
			logger.error("RdvService Exception : La fin du nouveau Rdv chevauche le debut du rdv dans la Bdd");
			throw new RdvNouveauDateFinChevaucheRdvExistantDateDebutException("RdvService Exception : La fin du nouveau Rdv chevauche le debut du rdv dans la Bdd");
		}
		
	}
	
	/**
	 * Verifie si le nouveau Rdv n est pas englobe par le rdv existant
	 * @param Instant newRdvDateDebut
	 * @param Instant rdvBddDateDebut
	 * @param Instant newRdvDateFin
	 * @param Instant rdvBddDateFin
	 * @throws Exception RdvNouveauEnglobeParRdvExistantException
	 */
	private void checkNouveauRdvEnglobeParRdvExistant(ZonedDateTime newRdvDateDebut, ZonedDateTime rdvBddDateDebut, 
			ZonedDateTime newRdvDateFin, ZonedDateTime rdvBddDateFin) throws RdvNouveauEnglobeParRdvExistantException {
		
		logger.info("RdvService log : Verification si le nouveau Rdv est englobe par le rdv existant");
		
		if (newRdvDateDebut.isAfter(rdvBddDateDebut) & newRdvDateFin.isBefore(rdvBddDateDebut)) {
			
			logger.error("RdvService Exception : Le nouveau Rdv englobe par le rdv existant");
			throw new RdvNouveauEnglobeParRdvExistantException("RdvService Exception : Le nouveau Rdv est englobe par le rdv existant");
		}	

	}
	
	/**
	 * Verifie si le début du  nouveau Rdv chevauche le rdvBdd
	 * @param newRdvDateDebut
	 * @param rdvBddDateFin
	 * @throws RdvNouveauDateDebutChevaucheRdvExistantDateFinException
	 */
	private void checkChevauchementFinRdvExistant(ZonedDateTime newRdvDateDebut, ZonedDateTime rdvBddDateFin) throws RdvNouveauDateDebutChevaucheRdvExistantDateFinException {
		
		logger.info("RdvService log : Verification si le debut du  nouveau Rdv chevauche le rdvBdd");
		
		if (newRdvDateDebut.isBefore(rdvBddDateFin)) {
			
			logger.error("RdvService Exception : Le debut du nouveau Rdv chevauche la fin du Rdv dans la Bdd");
			throw new RdvNouveauDateDebutChevaucheRdvExistantDateFinException("RdvService Exception : Le debut du nouveau Rdv chevauche la fin du Rdv dans la Bdd");
		}
	
	}
	
	/**
	 * Verifie si le nouveau Rdv n englobe pas le rdv existant
	 * @param Instant newRdvDateDebut
	 * @param Instant rdvBddDateDebut
	 * @param Instant newRdvDateFin
	 * @param Instant rdvBddDateFin
	 * @throws Exception RdvNouveauEnglobeRdvExistantException
	 */
private void checkRdvExistantEnglobeParNouveauRdv(ZonedDateTime newRdvDateDebut, ZonedDateTime rdvBddDateDebut, ZonedDateTime newRdvDateFin, ZonedDateTime rdvBddDateFin) throws RdvNouveauEnglobeRdvExistantException {
		
		logger.info("RdvService log : Verification si le nouveau Rdv englobe le rdv existant");
		
		if (newRdvDateDebut.isBefore(rdvBddDateDebut) & newRdvDateFin.isAfter(rdvBddDateFin)) {
			
			logger.error("RdvService Exception : Le nouveau Rdv englobe le rdv existant");
			throw new RdvNouveauEnglobeRdvExistantException("RdvService Exception : Le nouveau Rdv englobe le rdv existant");
		}
		
	}
	
	/**
	 * Soustrait 60 secondes a l instant fourni
	 * @param Instant dateHeureFournie
	 * @return ZonedDateTime 
	 */
	private ZonedDateTime dateHeureFournieMoinUneMinute(ZonedDateTime dateHeureFournie) {
		
//		logger.info("RdvService Log : Soustrait 60 seconde a l'instant fourni : " + dateHeureFournie);
//		logger.info("RdvService Log : Instant moins 60S : " + dateHeureFournie.minusSeconds(60));
		return dateHeureFournie.minusSeconds(60);
	}
	
	/**
	 * Ajoute 60 secondes a l instant fourni
	 * @param Instant dateHeureFournie
	 * @return ZonedDateTime 
	 */
	private ZonedDateTime dateHeureFourniePlusUneMinute(ZonedDateTime dateHeureFournie) {
		
//		logger.info("RdvService Log : Ajoute 60 seconde a l'instant fourni : " + dateHeureFournie);
//		logger.info("RdvService Log : Instant moins 60S : " + dateHeureFournie.minusSeconds(60));
		return dateHeureFournie.plusSeconds(60);
	}
	
	/**
	 * Converti un Timestamp en Instant
	 * @param dateHeureToConvert
	 * @return dateHeureConverted Instant
	 * @throws TimestampToZoneDateTimeConvertionException
	 */
	private ZonedDateTime tsToZdt(Timestamp dateHeureToConvert) throws TimestampToZoneDateTimeConvertionException {
		
		try {
			
//			logger.info("RdvService Log : Timestamp a convertir en ZDT: " + dateHeureToConvert);
			ZonedDateTime tsToZdt = dateHeureToConvert.toInstant().atZone(ZoneId.of("Europe/Paris"));
//			logger.info("RdvService Log : TS to ZDT : " + tsToZdt);

			
			return tsToZdt;
			
		} catch (Error msg) {
			logger.error("RdvService Exception : Il y a un probleme avec la conversion du Timestamp fourni : " + dateHeureToConvert);
			throw new TimestampToZoneDateTimeConvertionException("RdvService Exception : Il y a un probleme avec la conversion du Timestamp fourni : " + dateHeureToConvert);
		}
	}

}
