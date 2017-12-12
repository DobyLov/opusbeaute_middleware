
package fr.labonbonniere.opusbeaute.middleware.service.rdv;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;

import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvInexistantException;

@Stateless
public class RdvService {
	static final Logger logger = LogManager.getLogger(RdvService.class);

	@EJB
	private RdvDao rdvdao;

	public List<Rdv> recupereListeRdv() throws DaoException {

		try {

			logger.info("RdvService log : Demande au Dao la liste des Rdv's");
			List<Rdv> lalisterdv = rdvdao.obtenirListeRdv();
			logger.info("RdvService - Liste des Rdv's recuperee");
			return lalisterdv;

		} catch (DaoException message) {
			logger.error("RdvService log : Probleme de la bdd.");
			throw new DaoException("RdvService Exception : Probleme de la bdd.");
		}
	}

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

	public List<Rdv> recupereListeRdvParDate(final String listeRdvDateDuJour) throws DaoException {

		try {
			logger.info("Rdvservice log : Demande au Dao la liste des Rdv's par date selectionnee.");
			List<Rdv> lalisterdvpardate = rdvdao.obtenirListeRdvParDate(listeRdvDateDuJour);
			logger.info("Rdvservice log : Envoie a RdvWs de la liste des Rdv's par date selectionnee.");
			return lalisterdvpardate;

		} catch (DaoException message) {
			logger.error("Rdvservice Exception : probleme sur le format de la date.");
			throw new DaoException("RdvService Exception : probleme sur le format de la date.");
		}
	}

	public List<Rdv> recupereListeRdvViaPlageDate(String RdvPlageJourA, String RdvPlageJourB) throws DaoException {

		try {
			logger.info("RdvService log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees.");
			List<Rdv> lalisterdvplagedate = rdvdao.obtenirListeRdvViaPlageDate(RdvPlageJourA, RdvPlageJourB);
			logger.info("RdvService log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			return lalisterdvplagedate;

		} catch (Exception message) {
			logger.error("RdvWs log : probleme sur le format de la/des date(s).");
			throw new DaoException("RdvWs Exception : probleme sur le format de la/des date(s).");
		}
	}

	public void ajoutRdv(Rdv rdv) throws RdvExistantException, DaoException, RdvEgaliteChevauchementException,
			NoRdvException, RdvDebutChevauchementException, RdvFinChevauchementException, RdvEnglobantException {
//		rdvdao.ajouterUnRdv(rdv);

		logger.info(
				"Rdvservice log : Debut traitement metier pour ajouter Du Rdv.");
		logger.info(
				"Rdvservice log : Requeter la bdd avec la date du Rdv afin de recuperer la liste des Rdv du jour demande.");

		// Recuperation des ts debut et fin du rdv proposé
		Timestamp tsRdvDebut = rdv.getDateHeureDebut();
		long longTsRdvDebutMilliSec = tsRdvDebut.getTime();
		logger.info("Rdvservice log : Recuperation du TimeStamp tsRdvDebut : " + tsRdvDebut);

		Timestamp tsRdvFin = rdv.getDateHeureFin();
		long longTsRdvFinMilliSec = tsRdvFin.getTime();
		logger.info("Rdvservice log : Recuperation du TimeStamp tsRdvDebut : " + tsRdvFin);

		// Conversion du TS date début prestation en String date yyyy-MM-dd
		logger.info("Rdvservice log : Conversion du tsRdvDebut en STR pour requeter la bdd.");
		// traitement du TS récupéré
		String convTsToStr = Long.toString(longTsRdvDebutMilliSec) + "000";
		logger.info("Rdvservice log : Conversion du TS en STR plus 000 : " + convTsToStr);
		Date tsToDate = new Date(Long.parseLong(convTsToStr));
		logger.info("Rdvservice log : Conversion de la STR en Date : " + tsToDate);
		String dateformatpattern = "yyyy-MM-dd";
//		String dateformatpattern = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateformatpattern);
		logger.info("Rdvservice log : Formatage de la date avec le pattern yyyy-MM-dd.");
		String rdvDateDuJour = (String) sdf.format(tsToDate);
		System.out.println("date formatee : " + rdvDateDuJour);
		logger.info("Rdvservice log : Date prete a envoyer a la Bdd pour requeter.");

		try {
			// Recupération du nombre de rdv programmés le jourj
			logger.info(
					"Rdvservice log : Demande a la Bdd le nb de Rdv a la date du : " + rdvDateDuJour);
			Integer compteurRdvDuJour = rdvdao.renvoyerLeNbDeRdvDuJour(rdvDateDuJour);
			logger.info(
					"Rdvservice log : Il y a : " + compteurRdvDuJour + " Rdv a la date du : " + rdvDateDuJour);

			// recuperation des la dates de debut et fin du rdv a plannifier
			// Compteur de rdv ds la bdd, si different de Zero rdv poursuivre l
			// enregistrement du rdv
			if (compteurRdvDuJour > 0 ) {
				// recuperer la liste des rdv selon la date du rdv au format
				// Json et la gere en JSONARRAY
				List<Rdv> listeRdvJson = rdvdao.obtenirListeRdvParDate(rdvDateDuJour);
				// JSONParser listeRdvJsonPars = new JSONParser();
				org.json.simple.JSONArray jsonobj = (JSONArray) listeRdvJson;

				// Recupere le nombre d objet json pour les intervals
				Integer nombreObjJson = jsonobj.size();
				if (nombreObjJson == 0) {
					throw new NoRdvException("probleme pour les intervals Il n y a pas de rdv");
				}

				// Genereateur d intermediare de Rdv pour check le chevauchement
				long tsResultSoustraction = longTsRdvFinMilliSec - longTsRdvDebutMilliSec;
				long divisionResultatSoustraction = tsResultSoustraction / 10;
				long incremRdvDateDebut = longTsRdvDebutMilliSec + divisionResultatSoustraction;

				for (int i = 0; i < nombreObjJson; i++) {

					JSONObject obj = (JSONObject) jsonobj.get(i);
					Long rdvBddDateDebut = (Long) obj.get("dateHeureDebut");
					Long rdvBddDateFin = (Long) obj.get("dateHeureFin");
					Boolean valbou = testInterval(incremRdvDateDebut, rdvBddDateDebut, rdvBddDateFin);

					if (rdvBddDateDebut < longTsRdvDebutMilliSec && longTsRdvDebutMilliSec < rdvBddDateFin) {

						logger.info(
								"Rdvservice log : Probleme avec la Date de DEBUT du Rdv et le Rdv de la Bdd id : " + i);
						logger.info("Rdvservice log : objet : " + obj);
						throw new RdvDebutChevauchementException(
								"Probleme de chevauchement sur le debut du Rdv Propose");

					} else if (rdvBddDateDebut < longTsRdvFinMilliSec && longTsRdvFinMilliSec < rdvBddDateFin) {

						logger.info(
								"Rdvservice log : Probleme avec la date de FIN du Rdv et le Rdv de la Bdd id : " + i);
						logger.info("Rdvservice log : objet : " + obj);
						throw new RdvFinChevauchementException("Probleme de chevauchement sur la Fin du Rdv Propose");

					} else if (rdvBddDateDebut == longTsRdvDebutMilliSec && longTsRdvFinMilliSec == rdvBddDateFin) {

						logger.info(
								"Rdvservice log : Probleme ce craineau est deja occupe par un rdv de la Bdd id : " + i);
						logger.info("Rdvservice log : objet : " + obj);
						throw new RdvEgaliteChevauchementException(
								"Probleme de chevauchement sur le debut et la Fin du Rdv Propose");

					} else if (valbou == true) {

						logger.info(
								"Rdvservice log : Probleme ce craineau est deja occupe par un rdv de la Bdd id : " + i);
						logger.info("Rdvservice log : objet : " + obj);
						System.out.println("entree dans la boucle test englobage");
						throw new RdvEnglobantException(
								"Probleme de chevauchement, ce rdv englobe un ou plusieur Rdv de la Bdd");
					}
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

	public void suppressiondunrdv(final Integer idRdv) throws RdvInexistantException {

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

	public static boolean testInterval(long incremRdvDateDebut, long rdvBddDateDebut, long rdvBddDateFin) {

		if (rdvBddDateDebut < incremRdvDateDebut && incremRdvDateDebut < rdvBddDateFin)
			return true;
		else
			return false;
	}

}
