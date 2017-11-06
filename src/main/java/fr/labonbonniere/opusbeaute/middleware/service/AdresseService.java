package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.AdresseDao;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.Adresse;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseInexistanteException;

@Stateless
public class AdresseService {
	static final Logger logger = LogManager.getLogger(AdresseService.class);

	@EJB
	private AdresseDao adressedao;

	public List<Adresse> recupereListeAdresse() throws DaoException {

		try {

			logger.info("AdresseService log : Demande au Dao la liste des Adresses");
			List<Adresse> lalisteadresse = adressedao.obtenirListeAdresse();
			logger.info("AdresseService - Liste des Adresses recuperee");
			return lalisteadresse;

		} catch (DaoException message) {
			logger.error("AdresseService log : Probleme de la bdd.");
			throw new DaoException("AdresseService Exception : Probleme de la bdd.");
		}
	}

	public Adresse recupererUneAdresse(final Integer idAdresse) throws AdresseInexistanteException {

		try {
			logger.info("AdresseService log : Demande a la bdd le Adresse id : " + idAdresse);
			Adresse adresse = adressedao.obtenirAdresse(idAdresse);
			logger.info("AdresseService log : Adresse id : " + idAdresse
					+ " trouve, envoie de l objet Adresse a AdresseWS");
			return adresse;

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseService log : L Adresse demandee est introuvable");
			throw new AdresseInexistanteException("AdresseService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutAdresse(Adresse adresse) throws AdresseExistanteException {

		try {
			logger.info("AdresseService log : Demande d ajout d une nouvelle Adresse dans la Bdd.");
			adressedao.ajouterUneAdresse(adresse);
			logger.info("AdresseService log : Nouvelle Adresse ajoutee, avec l id : " + adresse.getIdAdresse());

		} catch (AdresseExistanteException message) {
			logger.error("AdresseService log : Impossible de creer cette adresse dans la Bdd.");
			throw new AdresseExistanteException(
					"AdresseService Exception : Impossible de creer cette Adresse dans la Bdd.");
		}
	}

	public void modifDeLAdresse(Adresse adresse) throws AdresseInexistanteException {

		try {
			logger.info("AdresseService log : Demande de modification de l Adresse id : " + adresse.getIdAdresse()
					+ " dans la Bdd.");
			adressedao.modifieUneAdresse(adresse);
			;
			logger.info("AdresseService log : Adresse id : " + adresse.getIdAdresse() + " a ete modifie dans la Bdd.");

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseService log : Adresse id : " + adresse.getIdAdresse()
					+ " ne peut etre modifie dans la Bdd.");
			throw new AdresseInexistanteException("AdresseService Exception : Adresse avec l id : "
					+ adresse.getIdAdresse() + " ne peut etre modifie.");
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

}
