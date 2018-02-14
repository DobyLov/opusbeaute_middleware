package fr.labonbonniere.opusbeaute.middleware.service.prestation;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.GenreDao;
import fr.labonbonniere.opusbeaute.middleware.dao.PrestationDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInvalideException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.Prestation;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationInvalideException;

/**
 * Gere les regles metier sur les objets prestation
 * 
 * @author fred
 *
 */
@Stateless
public class PrestationService {

	static final Logger logger = LogManager.getLogger(PrestationService.class);

	@EJB
	private PrestationDao prestationdao;
	@EJB
	private GenreDao genredao;
	
	/**
	 * retourne une liste de Prestation
	 * 
	 * @return Liste
	 * @throws DaoException Si probleme avec la Bdd
	 */
	public List<Prestation> recupereListePrestation() throws DaoException {

		try {
			logger.info("PrestationService log : Demande au Dao la liste des Prestations");
			List<Prestation> lalisteprestation = prestationdao.obtenirListePrestations();
			logger.info("PrestationService - Liste des Prestation's recuperee");
			return lalisteprestation;

		} catch (DaoException message) {
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");

		}
	}

	/**
	 * Recupere une Prestation par son Id
	 * 
	 * @param idPrestation Integer
	 * @return Objet Prestation
	 * @throws PrestationInexistanteException Si Prestation non trouvee
	 */
	public Prestation recupererUnePrestation(final Integer idPrestation) throws PrestationInexistanteException {

		try {
			logger.info("PrestationService log : Demande a la bdd la Prestation id : " + idPrestation);
			Prestation prestation = prestationdao.obtenirPrestation(idPrestation);
			logger.info("PrestationService log : Prestation id : " + idPrestation
					+ " trouve, envoie de l objet Prestation a RdvWS");
			return prestation;

		} catch (PrestationInexistanteException message) {
			logger.error("PrestationService log : La Prestation demande est introuvable");
			throw new PrestationInexistanteException(
					"PrestationService Exception : l' Id est introuvable dans la base");

		}
	}

	/**
	 * Recupere la liste des prestation homme
	 * 
	 * @return Liste Prestation
	 * @throws DaoException Si probleme avec la Bdd
	 */
	public List<Prestation> recupereListePrestationHomme() throws DaoException {

		try {
			logger.info("PrestationService log : Demande au Dao la liste des Prestations Homme");
			List<Prestation> lalisteprestationH = prestationdao.obtenirListPrestationsHomme();
			logger.info("PrestationService - Liste des Prestation Homme's recuperee");
			return lalisteprestationH;

		} catch (DaoException message) {
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");

		}
	}

	/**
	 * Recupere la liste des prestations Femme
	 * 
	 * @return Liste 
	 * @throws DaoException si probleme avec la Bdd
	 */
	public List<Prestation> recupereListePrestationFemme() throws DaoException {

		try {
			logger.info("PrestationService log : Demande au Dao la liste des Prestations Homme");
			List<Prestation> lalisteprestationF = prestationdao.obtenirListPrestationsFemme();
			logger.info("PrestationService - Liste des Prestation Homme's recuperee");
			return lalisteprestationF;

		} catch (DaoException message) {
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");

		}
	}

	/**
	 * Recupere la liste des prestation selon les criteres
	 * Genre et activite
	 * 
	 * @param genre String
	 * @param activite String
	 * @return Liste
	 * @throws DaoException Si pb avec la bdd
	 */
	public List<Prestation> recupereListePrestationCriteresGenreActivite(String genre, String activite)
			throws DaoException {

		try {
			logger.info("PrestationService log : Demande au Dao la liste des Prestations par genre et activite");
			List<Prestation> lalisteprestationGA = prestationdao.obtenirListPrestationsCriteresGenreActivite(genre,
					activite);
			logger.info("PrestationService - Liste des Prestation par genre et activite recuperee");
			return lalisteprestationGA;

		} catch (DaoException message) {
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");

		}
	}

	/**
	 * Recupere les Prestation selon les criteres
	 * Genre, Activite et Soin 
	 * 
	 * @param genre String
	 * @param activite String
	 * @param soin String
	 * @return Liste
	 * @throws DaoException Si pb avec la Bdd
	 */
	public List<Prestation> recupereListePrestationCriteresGenreActiviteSoins(String genre, String activite,
			String soin) throws DaoException {

		try {
			logger.info("PrestationService log : Demande au Dao la liste des Prestations par genre,activite et soin");
			List<Prestation> lalisteprestationGAS = prestationdao
					.obtenirListPrestationsCriteresGenreActiviteSoins(genre, activite, soin);
			logger.info("PrestationService - Liste des Prestation par genre,activite et soin recuperee");
			return lalisteprestationGAS;

		} catch (DaoException message) {
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");

		}
	}

	/**
	 * Ajouter une Nouvelle Prestation
	 * 
	 * @param prestation Objet Prestation
	 * @throws PrestationExistanteException Si Prestation existe
	 * @throws NbCharPrestationException Si le nombre de caracteres ne correspond pas
	 * @throws PrestationInvalideException Si la Prestation n est pas valide
	 * @throws GenreInvalideException Si le Genre n est pas valide
	 * @throws DaoException Si pb de Bdd
	 * @throws GenrePrestationNullException Si le Genre n existe pas ou null
	 */
	public void ajoutPrestation(Prestation prestation)
			throws PrestationExistanteException, NbCharPrestationException, PrestationInvalideException, GenreInvalideException, DaoException, GenrePrestationNullException {

		try {
			logger.info("PrestationService log : Demande d ajout d une nouvelle Prestation dans la Bdd.");
			validationFormat(prestation);
			prestationdao.ajouterUnePrestation(prestation);
			logger.info(
					"PrestationService log : Nouvelle Prestation ajoutee, avec l id : " + prestation.getIdPrestation());

		} catch (PrestationExistanteException message) {
			logger.error("RdvService log : Impossible de creer ce rdv dans la Bdd.");
			throw new PrestationExistanteException("RdvService Exception : Impossible de creer ce rdv dans la Bdd.");
		}
	}

	/**
	 * Modifie une Prestation
	 * 
	 * @param prestation Objet Prestation
	 * @throws PrestationInexistanteException Si la Prestation n existe pas
	 * @throws NbCharPrestationException Si le nombre de caracteres ne correspond pas
	 * @throws PrestationInvalideException	Si la Prestation ne st pas valide
	 * @throws GenreInvalideException	Si le genre n est pas valide
	 * @throws DaoException Si proble de bdd
	 * @throws GenrePrestationNullException Si le Genre n existe pas
	 */
	public void modifierUnePrestation(Prestation prestation)
			throws PrestationInexistanteException, NbCharPrestationException, PrestationInvalideException, GenreInvalideException, DaoException, GenrePrestationNullException {

		try {
			logger.info("PrestationService log : Demande de modification du Prestation id : "
					+ prestation.getIdPrestation() + " dans la Bdd.");
			validationFormat(prestation);
			prestationdao.modifieUnePrestation(prestation);
			logger.info("PrestationService log : Prestation id : " + prestation.getIdPrestation()
					+ " a ete modifie dans la Bdd.");

		} catch (PrestationInexistanteException message) {
			logger.error("PrestationService log : Prestation id : " + prestation.getIdPrestation()
					+ " ne peut etre modifie dans la Bdd.");
			throw new PrestationInexistanteException("PrestationService Exception : Prestation avec l Id : "
					+ prestation.getIdPrestation() + " ne peut etre modifie.");
		}
	}

	/**
	 * Supprime une Prestation
	 * 
	 * @param idPrestation Integer
	 * @throws PrestationInexistanteException Si la Prestation n existe pas
	 */
	public void suppressionUnePrestation(final Integer idPrestation) throws PrestationInexistanteException {

		try {
			logger.info("PrestationService log : Demande de suppression de la Prestation id : " + idPrestation
					+ " dans la Bdd.");
			prestationdao.supprimeUnePrestation(idPrestation);
			logger.info("PrestationService log : Prestation id : " + idPrestation + " a bien ete supprime de la Bdd.");

		} catch (PrestationInexistanteException message) {
			logger.error(
					"PrestationService log : Prestation id : " + idPrestation + " ne peut etre supprime dans la Bdd.");
			throw new PrestationInexistanteException("PrestationService Exception : Prestation id : " + idPrestation
					+ " ne peut etre supprime dans la Bdd.");
		}
	}

	/**
	 * Valide les champs d une prestation
	 * 
	 * @param prestation Objet prestation
	 * @return Objet Prestation modifie
	 * @throws NbCharPrestationException Si le nombre de caractere ne correspond pas
	 * @throws PrestationInvalideException Si la Prestation n est pas Valide
	 * @throws GenreInvalideException Si le Genre n est pas valide
	 * @throws DaoException	Si pb de Bdd
	 * @throws GenrePrestationNullException Si le Genre n existe pas 
	 */
	private Prestation validationFormat(Prestation prestation)
			throws NbCharPrestationException, PrestationInvalideException, GenreInvalideException, 
			DaoException, GenrePrestationNullException {

		// Prestation.Activite
		if (prestation.getActivite() != null && !prestation.getActivite().isEmpty()) {
			logger.error("PrestationService log : Prestation.Action n est pas nul :)");
			if (prestation.getActivite().length() > 50) {
				logger.error("PrestationService log : Prestation.Action depasse 50 caracteres");
				throw new NbCharPrestationException(
						"PrestationService log : Prestation.Activite depasse 50 caracteres");
			} else {
				logger.info("PrestationService log : Prestation.Action est valide");
				
				String checkSpaceAtStrBeginAndCharacSpec = prestation.getActivite();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				String activiteStringyfy = WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec);
				prestation.setActivite(activiteStringyfy);
			}
		} else {
			logger.error("PrestationService log : Prestation.Action est vide ou null :/");
			throw new PrestationInvalideException("PrestationService log : Prestation.Action est vide ou null :/");
		}

		// Prestation.Soin
		if (prestation.getSoin() != null && !prestation.getSoin().isEmpty()) {
			logger.error("PrestationService log : Prestation.Soin n est pas nul :)");
			if (prestation.getSoin().length() > 50) {
				logger.error("PrestationService log : Prestation.Soin depasse 50 caracteres");
				throw new NbCharPrestationException("PrestationService log : Prestation.Soin depasse 50 caracteres");
			} else {
				logger.info("PrestationService log : Prestation.Action est valide");
				String checkSpaceAtStrBeginAndCharacSpec = prestation.getSoin();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				String soinStringyfy = WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec);
				prestation.setActivite(soinStringyfy);
			}
		} else {
			logger.error("PrestationService log : Prestation.Soin est vide ou null :/");
			throw new PrestationInvalideException("PrestationService log : Prestation.Soin est vide ou null :/");
		}

		// Prestation.Description
		if (prestation.getDescription() != null && !prestation.getDescription().isEmpty()) {
			logger.error("PrestationService log : Prestation.Description n est pas nul :)");
			if (prestation.getDescription().length() > 500) {
				logger.error("PrestationService log : Prestation.Soin depasse 50 caracteres");
				throw new NbCharPrestationException(
						"PrestationService log : Prestation.Description depasse 500 caracteres");
			} else {
				logger.info("PrestationService log : Prestation.Description est valide");
				String checkSpaceAtStrBeginAndCharacSpec = prestation.getDescription();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				String descriptionstringyfy = StringWithoutSpaceAndCharspec.substring(0, 1).toUpperCase()
						+ prestation.getDescription().substring(1).toLowerCase();
				prestation.setDescription(descriptionstringyfy);
			}
		} else {
			logger.error("PrestationService log : Prestation.Description est vide ou null :/");
			throw new PrestationInvalideException("PrestationService log : Prestation.Description est vide ou null :/");
		}

		if (prestation.getForfait() != null && !prestation.getForfait().isEmpty()) {

			logger.info("PrestationService log : Prestation.Forfait n est pas nul :)");
			if (prestation.getNbSeance() <= 0) {
				logger.info("PrestationService log : Prestation.NbSeance <=0 ne valide pas Prestation.Forfait)");
				logger.info("PrestationService log : Prestation.Forfait positionne a null");
				prestation.setForfait(null);
				throw new NbCharPrestationException("PrestationService log : Prestation.Forfait force a null");
			}
			if (prestation.getForfait().length() > 4) {
				logger.error("PrestationService log : Prestation.Forfait depasse 4 caracteres");
				prestation.setForfait(null);
				throw new NbCharPrestationException("PrestationService log : Prestation.Forfait force a null");
			}
			if (prestation.getNbSeance() == 1) {
				logger.info("PrestationService log : Prestation.NbSeance =1 ne valide pas valide Prestation.Forfait)");
				prestation.setForfait("F");
			} else {
				logger.info(
						"PrestationService log : Prestation.NbSeances > 1 alors positionne a Prestation.Forfait à T ");
				prestation.setForfait("T");
				String forfaitstringyfy = prestation.getForfait().toUpperCase();
				prestation.setForfait(forfaitstringyfy);
			}
		} else {
			logger.error("PrestationService log : Prestation.Forfait est vide ou null :/");
			prestation.setForfait(null);
			throw new PrestationInvalideException("PrestationService log : Prestation.Forfait est vide ou null :/");
		}

		// Prestation.NbSeance
		// Conversion du Int en str pour le test de la clause null ou vide.
		String IntToStrNbseance = Integer.toString(prestation.getNbSeance());
		if (prestation.getNbSeance() != null && !IntToStrNbseance.isEmpty()) {
			logger.info("PrestationService log : Prestation.NbSeance n est pas vide :)");
			if (prestation.getNbSeance() <= 0) {
				logger.error("PrestationService log : Prestation.nbSeance est <= 0");
				throw new PrestationInvalideException("PrestationService log : Prestation.NbSeances <= 0");
			}
			if (IntToStrNbseance.length() > 3) {
				logger.error("PrestationService log : Prestation.nbSeance depase trois chracteres");
				throw new PrestationInvalideException(
						"PrestationService log : Prestation.NbSeances depasse 3 caracteres.");
			}
			if (prestation.getNbSeance() > 1) {
				logger.info("PrestationService log : Prestation.NbSeance > 1 force forfait a T");
				prestation.setForfait("T");
			} else {
				logger.info("PrestationService log : Prestation.NbSeance <= 1 force forfait a F");
				prestation.setForfait("F");
			}

		} else {
			logger.info("PrestationService log : Prestation.NbSeance est null ou vide :)");
			throw new PrestationInvalideException("PrestationService log : Prestation.NbSeances est null ou vide");
		}

		// Prestation.Dureeseace
		// Conversion du Int en Str pour test de la clause null ou vide
		String IntToStrDureeseance = Integer.toString(prestation.getDureeSeance());
		if (prestation.getDureeSeance() != null && !IntToStrDureeseance.isEmpty()) {
			logger.info("PrestationService log : Prestation.DureeSeance n est pas null ou vide :)");
			if (IntToStrDureeseance.length() > 3) {
				logger.error("PrestationService log : Prestation.DureeSeance depasse 3 cararceteres numeriques");
				throw new PrestationInvalideException(
						"PrestationService log : Prestation.Dureeseance depasse 3 caracteres.");
			}
			if (prestation.getDureeSeance() <= 0) {
				logger.error("PrestationService log : Prestation.DureeSeance <= 0");
				throw new PrestationInvalideException("PrestationService log : Prestation.Dureeseance <= 0");
			}
			if (prestation.getDureeSeance() > 180) {
				logger.error("PrestationService log : Prestation.DureeSeance depasse 180 minutes");
				throw new PrestationInvalideException(
						"PrestationService log : Prestation.Dureeseance depasse 180 minutes.");
			} else {
				logger.info("PrestationService log : Prestation.DureeSeance est valide");
			}

		} else {
			throw new PrestationInvalideException("PrestationService log : Prestation.Dureeseance est null ou vide.");
		}
		
		// genre
		checkNbEntreeGenre(prestation);
		
		return prestation;
	}
	
	/**
	 * Verifie si le Genre est superieur a 1
	 * 
	 * @param prestation Objet Prestation
	 * @throws GenreInvalideException Si non Valide
	 * @throws DaoException Si pb bdd
	 * @throws GenrePrestationNullException Si pas Genre
	 */
	public void checkNbEntreeGenre(Prestation prestation)
			throws GenreInvalideException, DaoException, GenrePrestationNullException {

		Integer NbRowGenreFromBdd = (int) genredao.CountGenre();
		logger.info("PrestationService log : Nombre d id Genre BDD : " + NbRowGenreFromBdd);

		try {
			logger.info("PrestationService log : id Genre Client : " + prestation.getGenre().getIdGenre());
			if (prestation.getGenre().getIdGenre() <= 0) {
				logger.error("PrestationService log : Il y a un probleme sur L id Genre.");
				throw new GenreInvalideException(
						"PrestationService Validation Exception : Il y a un probleme sur L id Genre <= 0");
			}
			if (prestation.getGenre().getIdGenre() > NbRowGenreFromBdd) {
				throw new GenreInvalideException(
						"PrestationService Validation Exception : Il y a un probleme sur L id Genre :" 
								+ prestation.getGenre().getIdGenre()
								+ " depasse le nombre de genre de la table : " + NbRowGenreFromBdd);
			}
		} catch (Exception message) {
			throw new GenrePrestationNullException("PrestationService log : Le Genre dans Prestation n est pas renseigne. ");
		}
	}
	
	/**
	 * Verifie si il y a un espace au debut des champs 
	 * et la presence de caracteres speciaux
	 * Si presence les suppriment
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec Valide la String
	 * @return String validee ou modifiee
	 */
	// Verifier la String Si elle commence par un espace ou possede des carcteres speciaux
	// Si c est le cas ne clash pas l appli mais reformate la String sans l espace en debut et sans les carac Spec.
	public String checkSpaceAtStrBeginAndCharacSpec(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");
		
		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _" + checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^a-zA-Z^-]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBeginCheckedCSpec);
		
		} else {
			
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^a-zA-Z^-]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}
		
		return strWithoutSpaceAtBegin;
	}
}
