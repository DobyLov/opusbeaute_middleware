package fr.labonbonniere.opusbeaute.middleware.service.adresse;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.AdresseClientDao;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseClient;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;


@Stateless
public class AdresseClientService {
	static final Logger logger = LogManager.getLogger(AdresseClientService.class);

	@EJB
	private AdresseClientDao adressedao;

	public List<AdresseClient> recupereListeAdresse() throws DaoException {

		try {

			logger.info("AdresseService log : Demande au Dao la liste des Adresses");
			List<AdresseClient> lalisteadresse = adressedao.obtenirListeAdresse();
			logger.info("AdresseService - Liste des Adresses recuperee");
			return lalisteadresse;

		} catch (DaoException message) {
			logger.error("AdresseService log : Probleme de la bdd.");
			throw new DaoException("AdresseService Exception : Probleme de la bdd.");
		}
	}

	public AdresseClient recupererUneAdresse(final Integer idAdresse) throws AdresseInexistanteException {

		try {
			logger.info("AdresseService log : Demande a la bdd le Adresse id : " + idAdresse);
			AdresseClient adresse = adressedao.obtenirAdresse(idAdresse);
			logger.info("AdresseService log : Adresse id : " + idAdresse
					+ " trouve, envoie de l objet Adresse a AdresseWS");
			return adresse;

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseService log : L Adresse demandee est introuvable");
			throw new AdresseInexistanteException("AdresseService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutAdresse(AdresseClient adresse) throws AdresseExistanteException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {

		try {
			logger.info("AdresseService log : Demande d ajout d une nouvelle Adresse dans la Bdd.");
			adresseValiderFormater(adresse);
			adressedao.ajouterUneAdresse(adresse);
			logger.info("AdresseService log : Nouvelle Adresse ajoutee, avec l id : " + adresse.getIdAdresse());

		} catch (AdresseExistanteException message) {
			logger.error("AdresseService log : Impossible de creer cette adresse dans la Bdd.");
			throw new AdresseExistanteException(
					"AdresseService Exception : Impossible de creer cette Adresse dans la Bdd.");
		}
	}

	public void modifDeLAdresse(AdresseClient adresse) throws AdresseInexistanteException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {

		try {
			logger.info("AdresseService log : Demande de modification de l Adresse id : " + adresse.getIdAdresse()
					+ " dans la Bdd.");
			adresseValiderFormater(adresse);
			adressedao.modifieUneAdresse(adresse);
			
			logger.info("AdresseService log : Adresse id : " + adresse.getIdAdresse() + " a ete modifie dans la Bdd.");

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseService log : Adresse id : " + adresse.getIdAdresse()
					+ " ne peut etre modifie dans la Bdd.");
			throw new AdresseInexistanteException("AdresseService Exception : Adresse avec l id : "
					+ adresse.getIdAdresse() + " ne peut etre modifie.");
		}
	}

	public void setToNullDeLAdresse(Integer idAdresse) throws AdresseInexistanteException {

		try {
			logger.info("AdresseService log : Demande de reinitialisation de l Adresse id : " + idAdresse
					+ " dans la Bdd.");
			// recup de l adresse avec l id
			AdresseClient adresse = adressedao.obtenirAdresse(idAdresse);
			// reset to null de l adresse
			adresse.setNumero(null);
			adresse.setPays(null);
			adresse.setRue(null);
			adresse.setVille(null);
			adresse.setZipCode(null);
			// Persistance de l adresse
			adressedao.reinitUneAdresse(adresse);
			logger.info("AdresseService log : Adresse id : " + adresse.getIdAdresse()
					+ " a ete reinitialisee dans la Bdd.");

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseService log : Adresse id : " + idAdresse + " ne peut etre reinitialisee dans la Bdd.");
			throw new AdresseInexistanteException(
					"AdresseService Exception : Adresse avec l id : " + idAdresse + " ne peut etre reinitialisee.");
		}
	}

	public void suppressionddUneAdresse(final Integer idAdresse) throws AdresseInexistanteException {

		try {
			logger.info("AdresseService log : Demande de suppression de l Adresse id : " + idAdresse + " dans la Bdd.");
			adressedao.supprimeUneAdresse(idAdresse);
			logger.info("AdresseService log : Adresse id : " + idAdresse + " a bien ete supprime de la Bdd.");

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseService log : Adresse id : " + idAdresse + " ne peut etre supprime dans la Bdd.");
			throw new AdresseInexistanteException(
					"AdresseService Exception : Adresse id : " + idAdresse + " ne peut etre supprime dans la Bdd.");
		}
	}
	
	private AdresseClient adresseValiderFormater(AdresseClient adresse)
			throws NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {
		
		AdresseClient adresseFormatee = new AdresseClient();
		
		if (adresse.getNumero() != null && !adresse.getNumero().isEmpty()) {
			
			if (adresse.getNumero().length() > 3) {
				throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue depasse 3 caracteres");
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = adresse.getNumero();
				String StringWithoutSpaceAndCharspec =	strUniquemtNumero(checkSpaceAtStrBeginAndCharacSpec);
				adresseFormatee.setNumero(StringWithoutSpaceAndCharspec);
			}
		} else {
			throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue est a null");
		}

		
		if (adresse.getRue() != null && !adresse.getRue().isEmpty()) {
			if (adresse.getRue().length() > 30) {
				throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue a depasse 30 caracteres");
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = adresse.getRue();
				String StringWithoutSpaceAndCharspec =	strUniquemtNumero(checkSpaceAtStrBeginAndCharacSpec);
				adresseFormatee.setRue(WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec));
			}
		} else {
			throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue est null");
		}

		
		if (adresse.getVille() != null && !adresse.getVille().isEmpty()) {
			if (adresse.getVille().length() > 30) {
				throw new NbCharRueVilleException(
						"ClientService Validation Exception : Le nom de la Ville depasse 30 caracteres");
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = adresse.getVille();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				adresseFormatee.setVille(StringWithoutSpaceAndCharspec.toUpperCase());
			}
		} else {
			throw new NbCharRueVilleException(
					"ClientService Validation Exception : Le nom de la Ville est null");
		}
		

		if (adresse.getZipCode() != null && !adresse.getZipCode().isEmpty()) {
			if (adresse.getZipCode().toString().length() > 5) {
				throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode depasse 5 caracteres");
			} else {
				adresseFormatee.setZipCode(adresse.getZipCode());
			}
		} else {
			throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode est null");
		}
		
		
		if (adresse.getPays() != null && !adresse.getPays().isEmpty()) {
			if (adresse.getPays().length() > 6) {
				throw new NbCharPaysException("ClientService Validation Exception : Le Pays depasse 6 caracteres");
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = adresse.getPays();
				String StringWithoutSpaceAndCharspec =	strUniquemtNumero(checkSpaceAtStrBeginAndCharacSpec);
				adresseFormatee.setPays(StringWithoutSpaceAndCharspec.toUpperCase());
			}
		} else {
			throw new NbCharPaysException("ClientService Validation Exception : Le Pays est null");
		}

		adresse.setNumero(adresseFormatee.getNumero());
		adresse.setRue(adresseFormatee.getRue());
		adresse.setVille(adresseFormatee.getVille());
		adresse.setZipCode(adresseFormatee.getZipCode());
		adresse.setPays(adresseFormatee.getPays());
		
		return adresseFormatee;

	}
	
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
