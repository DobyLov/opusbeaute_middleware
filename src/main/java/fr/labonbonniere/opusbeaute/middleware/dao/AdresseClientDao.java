package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseClient;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;

/**
 * Gere la persistance des AdresseClient
 * 
 * @author fred
 *
 */
@Stateless
@Transactional
public class AdresseClientDao {
	static final Logger logger = LogManager.getLogger(AdresseClientDao.class);

	@PersistenceContext(unitName = "opusBeautePU")
	private EntityManager em;

	/**
	 * Retourne la liste des Adresse Client
	 * 
	 * @return La liste des adresses Client
	 * @throws DaoException Exception
	 */
	public List<AdresseClient> obtenirListeAdresse() throws DaoException {

		try {
			logger.info("AdresseDao log : Demande a la bdd la liste des Adresses");
			String requete = "SELECT A FROM Adresse A" + " ORDER BY idAdresse asc";
			;

			TypedQuery<AdresseClient> query = em.createQuery(requete, AdresseClient.class);
			List<AdresseClient> listeAdresses = query.getResultList();

			logger.info("AdresseDao log :  Envoi de la liste des Adresses");
			return listeAdresses;

		} catch (Exception message) {
			logger.error("AdresseDAO Exception : Probleme de la bdd.");
			throw new DaoException("AdresseDao Exception : Probleme de la bdd.");
		}

	}

	/**
	 * Retourne l adresse d un Client via son Id
	 * 
	 * @param idAdresse Integer
	 * @return AdresseClient
	 * @throws AdresseInexistanteException Exception
	 */
	public AdresseClient obtenirAdresse(final Integer idAdresse) throws AdresseInexistanteException {

		logger.info("AdresseDao log : Demande a la bdd l adresse id : " + idAdresse);
		AdresseClient adresse = null;
		adresse = em.find(AdresseClient.class, idAdresse);

		if (Objects.isNull(adresse)) {
			logger.error("AdresseDao log : L adresse id : " + idAdresse + " demande est introuvable");
			throw new AdresseInexistanteException(
					"AdresseDao Exception : L' Id : " + idAdresse + " est introuvable dans la base");
		}

		logger.info("AdresseDao log : Adresse " + idAdresse + " trouve, envoie de l adresse a AdresseService");
		return adresse;

	}

	/**
	 * Persiste une nouvelle Adresseclient
	 * 
	 * @param adresse AdresseClient
	 * @throws AdresseExistanteException Exception
	 */
	public void ajouterUneAdresse(AdresseClient adresse) throws AdresseExistanteException {

		try {
			logger.info("AdresseDao log : Demande d ajout d une nouvelle Adresse dans la Bdd.");
			em.persist(adresse);
			em.flush();
			logger.info("AdresseDao log : Nouvelle Adresse ajoutee, avec l id : " + adresse.getIdAdresse());

		} catch (EntityExistsException message) {
			logger.error("GenreDao log : Impossible d ajouter cette Adresse dans la Bdd.");
			throw new AdresseExistanteException(
					"AdresseDao Exception : Probleme, cette Adresse a l air d'être deja persistee");

		}
	}

	/**
	 * Modifie l AdresseClient Persistee
	 * 
	 * @param adresse AdresseClient
	 * @throws AdresseInexistanteException Exception
	 */
	public void modifieUneAdresse(AdresseClient adresse) throws AdresseInexistanteException {

		logger.info(
				"AdresseDao log : Demande de modification de l adresse id : " + adresse.getIdAdresse() + " a la Bdd.");
		AdresseClient adresseBdd = em.find(AdresseClient.class, adresse.getIdAdresse());
		if (Objects.nonNull(adresseBdd)) {
			em.merge(adresse);
			em.flush();
			logger.info("AdresseDao log : Adresse id : " + adresse.getIdAdresse() + " a ete modifie dans la Bdd.");
		} else {
			logger.error(
					"AdresseDao log : Adresse id : " + adresse.getIdAdresse() + " ne peut etre modifie dans la Bdd.");
			throw new AdresseInexistanteException("AdresseDao log : Modification impossible,"
					+ "il n'y a pas d adresse à modifier pour l'id : " + adresse.getIdAdresse() + " demande.");
		}
	}

	/**
	 * Remise a Zero d une AdresseClient
	 * 
	 * @param adresse AdresseClient
	 * @throws AdresseInexistanteException Exception
	 */
	public void reinitUneAdresse(AdresseClient adresse) throws AdresseInexistanteException {

		logger.info("AdresseDao log : Demande de reinitialisation de l adresse id : " + adresse.getIdAdresse()
				+ " a la Bdd.");
		AdresseClient adresseBdd = em.find(AdresseClient.class, adresse.getIdAdresse());
		if (Objects.nonNull(adresseBdd)) {
			em.merge(adresse);
			em.flush();
			logger.info("AdresseDao log : Adresse id : " + adresse.getIdAdresse() + " a ete reinitialise dans la Bdd.");
		} else {
			logger.error("AdresseDao log : Adresse id : " + adresse.getIdAdresse()
					+ " ne peut etre reinitialisee dans la Bdd.");
			throw new AdresseInexistanteException("AdresseDao log : Reinitialisation impossible,"
					+ "il n'y a pas d adresse à reinitialiser pour l'id : " + adresse.getIdAdresse() + " demande.");
		}
	}

	/**
	 * Sumpression d une AdresseClient persistee
	 * 
	 * @param idAdresse Integer
	 * @throws AdresseInexistanteException Exception
	 */
	public void supprimeUneAdresse(final Integer idAdresse) throws AdresseInexistanteException {

		logger.info("AdresseDao log : Demande de suppression de l Adresse id : " + idAdresse + " dans la Bdd.");
		AdresseClient adresse = null;
		adresse = em.find(AdresseClient.class, idAdresse);
		if (Objects.nonNull(adresse)) {
			em.remove(adresse);
			em.flush();
			logger.info("AdresseDao log : Adresse id : " + idAdresse + " a bien ete supprime de la Bdd.");
		} else {
			logger.error("AdresseDao log : Adresse id : " + idAdresse
					+ " inexistante alors elle ne peut etre supprimee de la Bdd.");
			throw new AdresseInexistanteException(
					"AdresseDao Exception : Adresse id : " + idAdresse + " ne peut etre supprimee de la Bdd.");
		}
	}

}
