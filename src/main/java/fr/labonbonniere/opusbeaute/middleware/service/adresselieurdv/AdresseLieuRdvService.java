package fr.labonbonniere.opusbeaute.middleware.service.adresselieurdv;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.AdresseLieuRdvDao;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresselieurdv.AdresseLieuRdv;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;


@Stateless
public class AdresseLieuRdvService {
	static final Logger logger = LogManager.getLogger(AdresseLieuRdvService.class);

	@EJB
	private AdresseLieuRdvDao adresselieurdvdao;

	public List<AdresseLieuRdv> recupereListeAdresseLieuRdv() throws DaoException {

		try {

			logger.info("AdresseLieurdv log : Demande au Dao la liste des Adresses");
			List<AdresseLieuRdv> lalisteAdresseLieuRdv = adresselieurdvdao.obtenirListeAdresseLieuRdv();
			logger.info("AdresseService - Liste des Adresses recuperee");
			return lalisteAdresseLieuRdv;

		} catch (DaoException message) {
			logger.error("AdresseLieurdv log : Probleme de la bdd.");
			throw new DaoException("AdresseLieurRdvService Exception : Probleme de la bdd.");
		}
	}

	public AdresseLieuRdv recupererUneAdresseLieuRdv(final Integer idAdresseLieuRdv) throws AdresseInexistanteException {

		try {
			logger.info("AdresseLieurdv log : Demande a la bdd le Adresse id : " + idAdresseLieuRdv);
			AdresseLieuRdv adresseLieuRdv = adresselieurdvdao.obtenirAdresseLieuRdv(idAdresseLieuRdv);
			logger.info("AdresseLieurdv log : Adresse id : " + idAdresseLieuRdv
					+ " trouve, envoie de l objet Adresse a AdresseWS");
			return adresseLieuRdv;

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseLieurdv log : L Adresse demandee est introuvable");
			throw new AdresseInexistanteException("AdresseLieurRdvService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutAdresseLieuRdv(AdresseLieuRdv adresseLieuRdv) throws AdresseExistanteException, 
	NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {

		try {
			logger.info("AdresseLieurdv log : Demande d ajout d une nouvelle Adresse dans la Bdd.");
			adresseLieuRdvValiderFormater(adresseLieuRdv);
			adresselieurdvdao.ajouterUneAdresseLieuRdv(adresseLieuRdv);
			logger.info("AdresseLieurdv log : Nouvelle Adresse ajoutee, avec l id : " + adresseLieuRdv.getIdAdresseLieuRdv());

		} catch (AdresseExistanteException message) {
			logger.error("AdresseLieurdv log : Impossible de creer cette adresse dans la Bdd.");
			throw new AdresseExistanteException(
					"AdresseLieurRdvService Exception : Impossible de creer cette Adresse dans la Bdd.");
		}
	}

	public void modifDeLAdresseLieuRdv(AdresseLieuRdv adresseLieuRdv) throws AdresseInexistanteException, 
	NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {

		try {
			logger.info("AdresseLieurdv log : Demande de modification de l Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv()
					+ " dans la Bdd.");
			adresseLieuRdvValiderFormater(adresseLieuRdv);
			adresselieurdvdao.modifieUneAdresseLieuRdv(adresseLieuRdv);
			
			logger.info("AdresseLieurdv log : Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " a ete modifie dans la Bdd.");

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseLieurdv log : Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv()
					+ " ne peut etre modifie dans la Bdd.");
			throw new AdresseInexistanteException("AdresseLieurRdvService Exception : Adresse avec l id : "
					+ adresseLieuRdv.getIdAdresseLieuRdv() + " ne peut etre modifie.");
		}
	}

	public void setToNullDeLAdresseLieuRdv(Integer idAdresseLieuRdv) throws AdresseInexistanteException {

		try {
			logger.info("AdresseLieurdv log : Demande de reinitialisation de l Adresse id : " + idAdresseLieuRdv
					+ " dans la Bdd.");
			// recup de l adresse avec l id
			AdresseLieuRdv adresseLieuRdv = adresselieurdvdao.obtenirAdresseLieuRdv(idAdresseLieuRdv);
			// reset to null de l adresse
			adresseLieuRdv.setNumero(null);
			adresseLieuRdv.setPays(null);
			adresseLieuRdv.setRue(null);
			adresseLieuRdv.setVille(null);
			adresseLieuRdv.setZipCode(null);
			// Persistance de l adresseLieuRdv
			adresselieurdvdao.reinitUneAdresseLieuRdv(adresseLieuRdv);
			logger.info("AdresseLieurdv log : Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv()
					+ " a ete reinitialisee dans la Bdd.");

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseLieurdv log : Adresse id : " + idAdresseLieuRdv + " ne peut etre reinitialisee dans la Bdd.");
			throw new AdresseInexistanteException(
					"AdresseLieurRdvService Exception : Adresse avec l id : " + idAdresseLieuRdv + " ne peut etre reinitialisee.");
		}
	}

	public void suppressionddUneAdresseLieuRdv(final Integer idAdresseLieuRdv) throws AdresseInexistanteException {

		try {
			logger.info("AdresseLieurdv log : Demande de suppression de l Adresse id : " + idAdresseLieuRdv + " dans la Bdd.");
			adresselieurdvdao.supprimeUneAdresseLieuRdv(idAdresseLieuRdv);
			logger.info("AdresseLieurdv log : Adresse id : " + idAdresseLieuRdv + " a bien ete supprime de la Bdd.");

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseLieurdv log : Adresse id : " + idAdresseLieuRdv + " ne peut etre supprime dans la Bdd.");
			throw new AdresseInexistanteException(
					"AdresseLieurRdvService Exception : Adresse id : " + idAdresseLieuRdv + " ne peut etre supprime dans la Bdd.");
		}
	}
	
	private AdresseLieuRdv adresseLieuRdvValiderFormater(AdresseLieuRdv adresseLieuRdv)
			throws NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {
		
		AdresseLieuRdv adresseFormatee = new AdresseLieuRdv();
		
		if (adresseLieuRdv.getNumero() != null && !adresseLieuRdv.getNumero().isEmpty()) {
			
			if (adresseLieuRdv.getNumero().length() > 3) {
				throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue depasse 3 caracteres");
			} else {
				adresseFormatee.setNumero(adresseLieuRdv.getNumero());
			}
		} else {
			throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue est a null");
		}

		
		if (adresseLieuRdv.getRue() != null && !adresseLieuRdv.getRue().isEmpty()) {
			if (adresseLieuRdv.getRue().length() > 30) {
				throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue a depasse 30 caracteres");
			} else {
				adresseFormatee.setRue(WordUtils.capitalizeFully(adresseLieuRdv.getRue()));
			}
		} else {
			throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue est null");
		}

		
		if (adresseLieuRdv.getVille() != null && !adresseLieuRdv.getVille().isEmpty()) {
			if (adresseLieuRdv.getVille().length() > 30) {
				throw new NbCharRueVilleException(
						"ClientService Validation Exception : Le nom de la Ville depasse 30 caracteres");
			} else {
				adresseFormatee.setVille(adresseLieuRdv.getVille().toUpperCase());
			}
		} else {
			throw new NbCharRueVilleException(
					"ClientService Validation Exception : Le nom de la Ville est null");
		}
		

		if (adresseLieuRdv.getZipCode() != null && !adresseLieuRdv.getZipCode().isEmpty()) {
			if (adresseLieuRdv.getZipCode().toString().length() > 5) {
				throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode depasse 5 caracteres");
			} else {
				adresseFormatee.setZipCode(adresseLieuRdv.getZipCode());
			}
		} else {
			throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode est null");
		}
		
		
		if (adresseLieuRdv.getPays() != null && !adresseLieuRdv.getPays().isEmpty()) {
			if (adresseLieuRdv.getPays().length() > 6) {
				throw new NbCharPaysException("ClientService Validation Exception : Le Pays depasse 6 caracteres");
			} else {
				adresseFormatee.setPays(adresseLieuRdv.getPays().toUpperCase());
			}
		} else {
			throw new NbCharPaysException("ClientService Validation Exception : Le Pays est null");
		}

		adresseLieuRdv.setNumero(adresseFormatee.getNumero());
		adresseLieuRdv.setRue(adresseFormatee.getRue());
		adresseLieuRdv.setVille(adresseFormatee.getVille());
		adresseLieuRdv.setZipCode(adresseFormatee.getZipCode());
		adresseLieuRdv.setPays(adresseFormatee.getPays());
		
		return adresseLieuRdv;

	}

}
