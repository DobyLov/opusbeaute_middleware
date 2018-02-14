package fr.labonbonniere.opusbeaute.middleware.service.lieurdv;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;

import fr.labonbonniere.opusbeaute.middleware.dao.LieuRdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresselieurdv.AdresseLieuRdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvInvalideException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;

/**
 * Gere les objets LieuRdv 
 * 
 * @author fred
 *
 */
@Stateless
public class LieuRdvService {
	static final Logger logger = LogManager.getLogger(LieuRdvService.class);

	@EJB
	private LieuRdvDao lieurdvdao;
	
	/**
	 * Recupere la liste d objet LieuRdv
	 * 
	 * @return lalistelieurdv
	 * @throws DaoException ExceptionDAO
	 */
	public List<LieuRdv> recupereListeLieuRdv() throws DaoException {

		try {

			logger.info("LieuRdvService log : Demande au Dao la liste des Genres");
			List<LieuRdv> lalistelieurdv = lieurdvdao.obtenirListeLieuRdv();
			logger.info("LieuRdvService - Liste des Genre recuperee");
			return lalistelieurdv;

		} catch (DaoException message) {
			logger.error("LieuRdvService log : Probleme de la bdd.");
			throw new DaoException("LieuRdvService Exception : Probleme de la bdd.");
		}
	}

	/**
	 * Recupere un LieuRdv par son Id
	 * 
	 * @param idLieuRdv Integer
	 * @return LieuRdv
	 * @throws LieuRdvInexistantException Si n existe pas 
	 */
	public LieuRdv recupererUnLieuRdv(final Integer idLieuRdv) throws LieuRdvInexistantException {

		try {
			logger.info("LieuRdvService log : Demande a la bdd le Genre id : " + idLieuRdv);
			LieuRdv lieurdv = lieurdvdao.obtenirLieuRdv(idLieuRdv);
			logger.info("LieuRdvService log : Genre id : " + idLieuRdv + " trouve, envoie de l objet Genre a GenreWS");
			return lieurdv;

		} catch (LieuRdvInexistantException message) {
			logger.error("LieuRdvService log : Le Genre demandee est introuvable");
			throw new LieuRdvInexistantException("LieuRdvService Exception : l' Id est introuvable dans la base");
		}
	}

	/**
	 * ajoute un nouveau LieuRdv a persister
	 * 
	 * @param lieuRdv Objet LieuRdv
	 * @throws LieuRdvExistantException Si n existe pas
	 * @throws LieuRdvInvalideException Si non valide
	 * @throws NbCharPaysException Si le nombre de caractere ne correspond pas
	 * @throws NbNumRueException Les numeros de rue ne correspond pas
	 * @throws NbCharRueVilleException Si le nombre de caracteres du champs ville ne correspond pas
	 * @throws NbNumZipcodeException Si le nombre de numero du code postale ne correspond pas
	 */
	public void ajoutLieuRdv(LieuRdv lieuRdv) throws LieuRdvExistantException, LieuRdvInvalideException, NbCharPaysException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException {

		try {
			logger.info("LieuRdvService log : Demande d ajout d un nouveu Genre dans la Bdd.");
			validationFormatageLieuRdv(lieuRdv);			
			lieurdvdao.ajouterUnLieuRdv(lieuRdv);
			logger.info("LieuRdvService log : Nouveau Genre ajoute, avec l id : " + lieuRdv);

		} catch (LieuRdvExistantException message) {
			logger.error("LieuRdvService log : Impossible de creer ce Genre dans la Bdd.");
			throw new LieuRdvExistantException("LieuRdvService Exception : Impossible de creer ce Genre dans la Bdd.");
		}
	}

	/**
	 * Modifie un LieuRdv persiste
	 * 
	 * @param lieuRdv Integer
	 * @throws LieuRdvInexistantException Si n existe pas
	 * @throws LieuRdvInvalideException Si n est pas valide
	 * @throws NbCharPaysException Si le nombre de caracetres ne correspond pas
	 * @throws NbNumRueException Si le nombre du numero de rue ne corresppond pas
	 * @throws NbCharRueVilleException Si le nombre de caracteres ne correspond pas
	 * @throws NbNumZipcodeException Si le Zip code ne correspond pas
	 */
	public void modifDuLieuRdv(LieuRdv lieuRdv) throws LieuRdvInexistantException, LieuRdvInvalideException, NbCharPaysException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException {

		try {
			logger.info("LieuRdvService log : Demande de modification du Genre id : " + lieuRdv.getIdLieuRdv()
					+ " dans la Bdd.");
			validationFormatageLieuRdv(lieuRdv);
			lieurdvdao.modifieUnLieuRdv(lieuRdv);
			logger.info("LieuRdvService log : Genre id : " + lieuRdv.getIdLieuRdv() + " a ete modifie dans la Bdd.");

		} catch (LieuRdvInexistantException message) {
			logger.error(
					"LieuRdvService log : Genre id : " + lieuRdv.getIdLieuRdv() + " ne peut etre modifie dans la Bdd.");
			throw new LieuRdvInexistantException("LieuRdvService Exception : Genre avec l id : "
					+ lieuRdv.getIdLieuRdv() + " ne peut etre modifie.");
		}
		
	}

	/**
	 * Supprime un lieuRdv
	 * 
	 * @param idLieuRdv Integer
	 * @throws LieuRdvInexistantException si n existe pas
	 */
	public void suppressionddUnLieuRdv(final Integer idLieuRdv) throws LieuRdvInexistantException {

		try {
			logger.info("LieuRdvService log : Demande de suppression de Genre id : " + idLieuRdv + " dans la Bdd.");
			lieurdvdao.supprimeUnLieuRdv(idLieuRdv);
			logger.info("LieuRdvService log : Genre id : " + idLieuRdv + " a bien ete supprime de la Bdd.");

		} catch (LieuRdvInexistantException message) {
			logger.error("LieuRdvService log : Genre id : " + idLieuRdv + " ne peut etre supprime dans la Bdd.");
			throw new LieuRdvInexistantException(
					"LieuRdvService Exception : Genre id : " + idLieuRdv + " ne peut etre supprime dans la Bdd.");
		}
	}
	
	/**
	 * Valide les champs de l objet LieuRdv
	 * 
	 * @param lieuRdv Integer
	 * @return l objet lieuRdv formate
	 * @throws LieuRdvInvalideException si n est pas valide
	 * @throws NbCharPaysException Si le nombre de caracterse ne corresponde pas
	 * @throws NbNumRueException Si le nombre du numero de rue ne correspond pas
	 * @throws NbCharRueVilleException Si le nombre de caracteres de la rue ne correspond pas
	 * @throws NbNumZipcodeException  si le nombre du numero du code postale ne correspond pas
	 */
	private LieuRdv validationFormatageLieuRdv(LieuRdv lieuRdv) throws LieuRdvInvalideException, NbCharPaysException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException {
//		AdresseLieuRdv adresseFormatee = new AdresseLieuRdv();
		AdresseLieuRdv adresseLieuRdvFormatee = new AdresseLieuRdv();
		
		if (lieuRdv.getLieuRdv() != null && !lieuRdv.getLieuRdv().isEmpty()) {
			
			if (lieuRdv.getLieuRdv().length() > 30) {
				throw new LieuRdvInvalideException(
						"LieuRdvService Exception : LieuRdv.LieuRdv superieur a 30 caracteres.");
				
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = lieuRdv.getLieuRdv();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				String lieurdvStringyfy = WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec);
				lieuRdv.setLieuRdv(lieurdvStringyfy);
			}
			
			// INSERTION ADRESSELIEUR FORMATAGE		
			if (lieuRdv.getAdresseLieuRdv().getNumero() != null && !lieuRdv.getAdresseLieuRdv().getNumero().isEmpty()) {
				
				if (lieuRdv.getAdresseLieuRdv().getNumero().length() > 3) {
					throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue depasse 3 caracteres");
				} else {
					String checkSpaceAtStrBeginAndCharacSpec = lieuRdv.getAdresseLieuRdv().getNumero();
					String StringWithoutSpaceAndCharspec =	strUniquemtNumero(checkSpaceAtStrBeginAndCharacSpec);
					adresseLieuRdvFormatee.setNumero(StringWithoutSpaceAndCharspec);
				}
			} else {
				throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue est a null");
			}
	
			
			if (lieuRdv.getAdresseLieuRdv().getRue() != null && !lieuRdv.getAdresseLieuRdv().getRue().isEmpty()) {
				if (lieuRdv.getAdresseLieuRdv().getRue().length() > 30) {
					throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue a depasse 30 caracteres");
				} else {
					String checkSpaceAtStrBeginAndCharacSpec = lieuRdv.getAdresseLieuRdv().getRue();
					String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
					adresseLieuRdvFormatee.setRue(WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec));
				}
			} else {
				throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue est null");
			}
	
			
			if (lieuRdv.getAdresseLieuRdv().getVille() != null && !lieuRdv.getAdresseLieuRdv().getVille().isEmpty()) {
				if (lieuRdv.getAdresseLieuRdv().getVille().length() > 30) {
					throw new NbCharRueVilleException(
							"ClientService Validation Exception : Le nom de la Ville depasse 30 caracteres");
				} else {
					String checkSpaceAtStrBeginAndCharacSpec = lieuRdv.getAdresseLieuRdv().getVille();
					String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
					adresseLieuRdvFormatee.setVille(StringWithoutSpaceAndCharspec.toUpperCase());
				}
			} else {
				throw new NbCharRueVilleException(
						"ClientService Validation Exception : Le nom de la Ville est null");
			}
			
	
			if (lieuRdv.getAdresseLieuRdv().getZipCode() != null && !lieuRdv.getAdresseLieuRdv().getZipCode().isEmpty()) {
				if (lieuRdv.getAdresseLieuRdv().getZipCode().toString().length() > 5) {
					throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode depasse 5 caracteres");
				} else {
					adresseLieuRdvFormatee.setZipCode(lieuRdv.getAdresseLieuRdv().getZipCode());
				}
			} else {
				throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode est null");
			}
			
			
			if (lieuRdv.getAdresseLieuRdv().getPays() != null && !lieuRdv.getAdresseLieuRdv().getPays().isEmpty()) {
				if (lieuRdv.getAdresseLieuRdv().getPays().length() > 6) {
					throw new NbCharPaysException("ClientService Validation Exception : Le Pays depasse 6 caracteres");
				} else {
					adresseLieuRdvFormatee.setPays(lieuRdv.getAdresseLieuRdv().getPays().toUpperCase());
				}
			} else {
				throw new NbCharPaysException("ClientService Validation Exception : Le Pays est null");
			}
	
			adresseLieuRdvFormatee.setNumero(adresseLieuRdvFormatee.getNumero());
			adresseLieuRdvFormatee.setRue(adresseLieuRdvFormatee.getRue());
			adresseLieuRdvFormatee.setVille(adresseLieuRdvFormatee.getVille());
			adresseLieuRdvFormatee.setZipCode(adresseLieuRdvFormatee.getZipCode());
			adresseLieuRdvFormatee.setPays(adresseLieuRdvFormatee.getPays());
			
			lieuRdv.setAdresseLieuRdv(adresseLieuRdvFormatee);
			
		} else {
			throw new LieuRdvInvalideException(
					"LieuRdvService Exception : Genre.genreHum est null :/");
		}

		
		return lieuRdv;
	}

	/**
	 * Verifie si la chaine du champs commence par un espace
	 * et detecte la presence de caracteres speciaux
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec Verifie si la String possede un espace au debut et de caracteres speciaux
	 * @return la String modifiee
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
	
	/**
	 * Verifie si la chaine du champs commence par un espace
	 * et detecte la presence de caracteres speciaux
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec Verifie si la String possede un espace au debut et de caracteres speciaux
	 * @return la String modifiee
	 */
	public String strUniquemtNumero(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");
		
		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _" + checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^0-9^-]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.error("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBeginCheckedCSpec);
		
		} else {
			
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^0-9^-]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}
		
		return strWithoutSpaceAtBegin;
	}	
}
