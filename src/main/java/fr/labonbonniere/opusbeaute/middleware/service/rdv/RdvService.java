
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
	 * @param idClient Integer
	 * @return List
	 * @throws DaoException Exception
	 * @throws RdvDateIncorrecteException Exception
	 */
	public List<Rdv> recupereLaListeRdvParDateJJClient(final String dateJJ, final Integer idClient) throws DaoException, RdvDateIncorrecteException {
		
		try {
			isDateStringFormatValid(dateJJ);
			logger.info("RdvService log : Demande au Dao la liste des Rdv's par Client et par date");
			List<Rdv> laListeRdvParDate = rdvdao.obtenirListeRdvDuJJParClient(dateJJ, idClient);
			logger.info("RdvService - Liste des Rdv's recuperee");
			logger.info("RdvService - Nombre d items recuperes : " + laListeRdvParDate.size());
			return laListeRdvParDate;
			
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
	 * @param idClient Integer
	 * @return List 
	 * @throws DaoException Exception
	 * @throws RdvDateIncorrecteException Exception
	 */
	public List<Rdv> recupereLaListeRdvParPlageDateClient(final String dateA, final String dateB, final Integer idClient) throws DaoException, RdvDateIncorrecteException {
		
		try {
			isDateStringFormatValid(dateA);
			isDateStringFormatValid(dateB);
			logger.info("RdvService log : Demande au Dao la liste des Rdv's par Client et par plage de date");
			List<Rdv> laListeRdvParplageDeDate = rdvdao.obtenirListeRdvParPlageDeDateParClient(dateA, dateB, idClient);
			logger.info("RdvService - Liste des Rdv's recuperee");
			logger.info("RdvService - Nombre d items dans la liste : " + laListeRdvParplageDeDate.size());
			return laListeRdvParplageDeDate;
			
		} catch (DaoException message) {
			logger.error("RdvService log : Probleme de la bdd.");
			throw new DaoException("RdvService Exception : Probleme de la bdd.");
		}	
		
	}

	/**
	 * recupere la liste de Rdv par Client
	 * 
	 * @param idPraticien Integer
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> recupereListRdvParPraticien(final Integer idPraticien) throws DaoException {
		
		try {
			logger.info("RdvService log : Demande au Dao la liste des Rdv's par Praticien");
			List<Rdv> laListeRdv = rdvdao.obtenirListeRdvParClient(idPraticien);
			logger.info("RdvService - Liste des Rdv's recuperee");
			logger.info("RdvService - Nombre d items recuperes : " + laListeRdv.size());
			return laListeRdv;
			
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
	 * @param rdv Rdv 
	 * @throws DateConversionException 
	 * @throws RdvDateIncorrecteException 
	 * @throws DaoException 
	 * @throws RdvNouveauDateDebutChevaucheRdvExistantDateFinException 
	 * @throws RdvNouveauEnglobeRdvExistantException 
	 * @throws RdvNouveauEnglobeParRdvExistantException 
	 * @throws RdvNouveauDateFinChevaucheRdvExistantDateDebutException 
	 * @throws RdvNonIntegrableException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 * @throws RdvExistantException 
	 */
	public void ajoutRdv(Rdv rdv) throws TimestampToZoneDateTimeConvertionException, RdvNonIntegrableException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException, RdvNouveauEnglobeParRdvExistantException, RdvNouveauEnglobeRdvExistantException, RdvNouveauDateDebutChevaucheRdvExistantDateFinException, DaoException, RdvDateIncorrecteException, DateConversionException, RdvExistantException {

		logger.info("Rdvservice log : Debut traitement metier pour ajouter Du Rdv.");
		try {

			logger.info("RdvService log : Demande d ajout d un nouveau Rdv dans la Bdd.");
			verificationIntegrationDuRdvDansAgenda(rdv);
			rdvdao.ajouterUnRdv(rdv);
			logger.info("RdvService log : Nouveau Rdv ajoute, avec l id : " + rdv.getIdRdv());

		} catch (RdvExistantException message) {
			logger.error("RdvService log : Impossible de creer ce rdv dans la Bdd.");
			throw new RdvExistantException("RdvService Exception : Impossible de creer ce rdv dans la Bdd.");
		}
	}

	/**
	 * Modifie un Rdv
	 * @param rdv Rdv
	 * @throws DateConversionException 
	 * @throws RdvDateIncorrecteException 
	 * @throws DaoException 
	 * @throws RdvNouveauDateDebutChevaucheRdvExistantDateFinException 
	 * @throws RdvNouveauEnglobeRdvExistantException 
	 * @throws RdvNouveauEnglobeParRdvExistantException 
	 * @throws RdvNouveauDateFinChevaucheRdvExistantDateDebutException 
	 * @throws RdvNonIntegrableException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 * @throws RdvInexistantException 

	 */
	public void modifduRdv(Rdv rdv) throws TimestampToZoneDateTimeConvertionException, RdvNonIntegrableException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException, RdvNouveauEnglobeParRdvExistantException, RdvNouveauEnglobeRdvExistantException, RdvNouveauDateDebutChevaucheRdvExistantDateFinException, DaoException, RdvDateIncorrecteException, DateConversionException, RdvInexistantException {

		try {
			
			logger.info("RdvService log : Demande de modification du Rdv id : " + rdv.getIdRdv() + " dans la Bdd.");
			verificationIntegrationDuRdvDansAgenda(rdv);
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
	 * @throws RdvInexistantException
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
	
	/**
	 * Verifie si le format (YYYY-MM-DD) de la date est respecte
	 * @param dateToCheck String
	 * @throws RdvDateIncorrecteException
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
	
	/**
	 * Recuepere la liste de Rdv via le jour fourni par le Rdv transmi
	 * @param rdvFourni
	 * @return
	 * @throws DaoException
	 * @throws RdvDateIncorrecteException
	 * @throws DateConversionException
	 */
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
	 * ----------------------------------------------------------------------------------
	 *						dd							df
	 *	0					|			rdvBdd			|
	 *				dd			df
	 *	1			|	newRdv	|
	 *												dd			df
	 *	2											|	newRdv	|
	 *					dd									df
	 *	3				|				newRdv				|					
	 *------------------------------------------------------------------------------------
	 * Pour le test Retirer / Ajouter  une minute à dateRdvdebut et dateRdvFin du nouveau Rdv pour eviter le erreures de tests
	 * 
	 * @param Rdv newRdv
	 * @param Rdv rdvFromBdd
	 * @throws DateConversionException 
	 * @throws RdvDateIncorrecteException 
	 * @throws DaoException 
	 */
	private void verificationIntegrationDuRdvDansAgenda(Rdv newRdv) throws TimestampToZoneDateTimeConvertionException, 
	RdvNonIntegrableException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException, 
	RdvNouveauEnglobeParRdvExistantException, 
	RdvNouveauEnglobeRdvExistantException, RdvNouveauDateDebutChevaucheRdvExistantDateFinException, DaoException, RdvDateIncorrecteException, DateConversionException {
		
		logger.info("RdvService Log : Verification de l integration du rdv dans le calendrier");
		
		try {
			
			List<Rdv> listRdvFournie = recupereLaListeDeRdvSelonLeJourFourniDansLeRdv(newRdv);
			logger.info("Rdvservice log : Liste Rdv : " + listRdvFournie.size() + " items a la date : " 
					+ tsToZdt(newRdv.getDateHeureDebut()) );
			
			if (listRdvFournie.size() > 0 ) {
				
				for (int i = 0; i < listRdvFournie.size(); i++) {
					
					Rdv objRdvFromList = listRdvFournie.get(i);
					logger.info("Rdvservice Log : Comparaison du nouveau Rdv avec Rdvid " 
							+ objRdvFromList.getIdRdv() + " dela liste => " + listRdvFournie.size() + " items");
					checkChevauchementDebutRdvExistant( newRdv, objRdvFromList );			
					checkChevauchementFinRdvExistant(newRdv, objRdvFromList );
					checkRdvExistantEnglobeParNouveauRdv(newRdv, objRdvFromList );
					
				}
				
			} else {
				
				logger.info("Rdvservice Log : Pas de Rdv de plannifie pour la date : " + newRdv.getDateHeureDebut() + ",");						
				logger.info("Rdvservice Log : alors le Rdv sera ajoute dans la Bdd sans tester,");
				logger.info("les differnets chevauchements possibles.");
			}
			

			
		} catch (Error msg) {
			logger.error("RdvService Log : Le rdv n est pas integrable");
			throw new RdvNonIntegrableException("RdvService Exception : Le rdv n est pas integrable");
		}
		
	}
	
	/**
	 * Vérifie si la dateDeFin du nouveau Rdv
	 * se retrouve dans l interval(plage dateDebut dateFin) du rdvBdd
	 * @param Rdv newRdv
	 * @param Rdv rdvFromBdd
	 * @throws RdvNouveauDateFinChevaucheRdvExistantDateDebutException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 */
	private void checkChevauchementDebutRdvExistant(Rdv newRdv, Rdv rdvFromBdd) throws TimestampToZoneDateTimeConvertionException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException  {
		
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
	 * Verifie si le début du  nouveau Rdv
	 * se retrouve dans la plage(dateDebut dateFin) du rdv 
	 * @param newRdv
	 * @param rdvFromBdd
	 * @throws RdvNouveauDateDebutChevaucheRdvExistantDateFinException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 */
	private void checkChevauchementFinRdvExistant(Rdv newRdv, Rdv rdvFromBdd) throws TimestampToZoneDateTimeConvertionException, RdvNouveauDateDebutChevaucheRdvExistantDateFinException {
		
		logger.info("RdvService log : Verification si le debut du  nouveau Rdv chevauche le rdvBdd");
		
		if (dateHeureFourniePlusUneMinute(tsToZdt(newRdv.getDateHeureDebut()))
				.isBefore(tsToZdt(rdvFromBdd.getDateHeureFin())) 
					& dateHeureFournieMoinUneMinute(tsToZdt(newRdv.getDateHeureDebut()))
					.isAfter(tsToZdt(rdvFromBdd.getDateHeureDebut()))) {
			
			logger.error("RdvService Exception : Le debut du nouveau Rdv chevauche la fin du Rdv dans la Bdd");
			throw new RdvNouveauDateDebutChevaucheRdvExistantDateFinException("RdvService Exception : Le debut du nouveau Rdv chevauche la fin du Rdv dans la Bdd");
		}
	
	}
	
	/**
	 * Verifie si le nouveau Rdv n englobe pas le rdv existant
	 * @param Rdv newRdv
	 * @param Rdv rdvFromBdd
	 * @throws RdvNouveauEnglobeRdvExistantException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 */
	private void checkRdvExistantEnglobeParNouveauRdv(Rdv newRdv, Rdv rdvFromBdd) throws TimestampToZoneDateTimeConvertionException, RdvNouveauEnglobeRdvExistantException  {
		
		logger.info("RdvService log : Verification si le nouveau Rdv englobe le rdv existant");
		
		if (dateHeureFourniePlusUneMinute(tsToZdt(newRdv.getDateHeureDebut()))
			.isBefore(tsToZdt(rdvFromBdd.getDateHeureDebut()))
			& dateHeureFournieMoinUneMinute(tsToZdt(newRdv.getDateHeureFin()))
					.isAfter(tsToZdt(rdvFromBdd.getDateHeureFin())) ) {
			
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
		
		return dateHeureFournie.minusSeconds(60);
	}
	
	/**
	 * Ajoute 60 secondes a l instant fourni
	 * @param dateHeureFournie
	 * @return ZonedDateTime 
	 */
	private ZonedDateTime dateHeureFourniePlusUneMinute(ZonedDateTime dateHeureFournie) {
		
		return dateHeureFournie.plusSeconds(60);
	}
	
	/**
	 * Converti un Timestamp en Instant
	 * @param Timestamp
	 * @return dateHeureConverted Instant
	 * @throws TimestampToZoneDateTimeConvertionException 
	 */
	private ZonedDateTime tsToZdt(Timestamp dateHeureToConvert) throws TimestampToZoneDateTimeConvertionException {
		
		try {
			
			ZonedDateTime tsToZdt = dateHeureToConvert.toInstant().atZone(ZoneId.of("Europe/Paris"));
			
			return tsToZdt;
			
		} catch (Error msg) {
			
			logger.error("RdvService Exception : Il y a un probleme avec la conversion du Timestamp fourni : " + dateHeureToConvert);
			throw new TimestampToZoneDateTimeConvertionException("RdvService Exception : Il y a un probleme avec la conversion du Timestamp fourni : " + dateHeureToConvert);
		
		}
	}

}
