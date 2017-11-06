package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.hibernate.criterion.Order;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.Prestation;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationExistanteException;

@Stateless
@Transactional
public class PrestationDao {

	static final Logger logger = LogManager.getLogger(PrestationDao.class);

	@PersistenceContext(unitName = "dobyPUtest")
	private EntityManager em;

	public List<Prestation> obtenirListePrestations() throws DaoException {

		try {
			logger.info("PrestationsDao log : Demande a la bdd la liste des Prestations");
			String requete = "SELECT P FROM Prestation P" + " ORDER BY idPrestation asc";
			;

			TypedQuery<Prestation> query = em.createQuery(requete, Prestation.class);
			List<Prestation> listePrestations = query.getResultList();

			logger.info("PrestationsDao log :  Envoi de la liste de Prestations");
			return listePrestations;

		} catch (Exception message) {
			logger.error("PrestationsDao Exception : Probleme de la bdd.");
			throw new DaoException("RdvDao Exception : Probleme de la bdd.");
		}

	}

	public Prestation obtenirPrestation(final Integer idPrestation) throws PrestationInexistanteException {

		logger.info("PrestationsDao log : Demande a la bdd le Prestations id : " + idPrestation);
		Prestation prestation = null;
		prestation = em.find(Prestation.class, idPrestation);

		if (Objects.isNull(prestation)) {
			logger.error("PrestationsDao log : La Prestation : " + idPrestation + " demande est introuvable");
			throw new PrestationInexistanteException(
					"PrestationsDao Exception : L' Id : " + idPrestation + " est introuvable dans la base");
		}

		logger.info("PrestationsDao log : Prestations " + idPrestation
				+ " trouve, envoie de l objet Prestations a PrestationsService");
		return prestation;

	}

	public List<Prestation> obtenirListPrestationsHomme() throws DaoException {

		try {
			logger.info("PrestationsDao log : Demande la liste des Prestations par homme.");
			String requete = "SELECT P FROM Prestation P" + " WHERE PRESTATION_GENRE = 'homme' ";

			TypedQuery<Prestation> query = em.createQuery(requete, Prestation.class);
			List<Prestation> prestationH = query.getResultList();
			logger.info("PrestationsDao log : Transmission de la Liste des Prestations homme.");
			return prestationH;

		} catch (Exception message) {
			logger.error("PrestationsDao log : probleme sur le format.");
			throw new DaoException("PrestationsDao Exception : probleme sur le format.");
		}
	}

	public List<Prestation> obtenirListPrestationsFemme() throws DaoException {

		try {
			logger.info("PrestationsDao log : Demande la liste des Prestations par genre femme.");
			String requete = "SELECT P FROM Prestation P" + " WHERE PRESTATION_GENRE = 'femme' ";

			TypedQuery<Prestation> query = em.createQuery(requete, Prestation.class);
			List<Prestation> prestationF = query.getResultList();
			logger.info("PrestationsDao log : Transmission de la Liste des Prestations femme.");
			return prestationF;

		} catch (Exception message) {
			logger.error("PrestationsDao log : probleme sur le format de la/des date(s).");
			throw new DaoException("PrestationsDao Exception : probleme sur le format de la/des date(s).");
		}
	}

	public List<Prestation> obtenirListPrestationsCriteresGenreActivite(String genre, String activite)
			throws DaoException {

		try {
			logger.info("PrestationsDao log : Demande la liste des Prestations selon les criteres GenreActivieSoin.");
			String requete = "SELECT P FROM Prestation P" + " WHERE PRESTATION_GENRE = '" + genre + "'"
					+ " AND PRESTATION_ACTIVITE = '" + activite + "'";

			TypedQuery<Prestation> query = em.createQuery(requete, Prestation.class);
			List<Prestation> prestationGA = query.getResultList();
			logger.info(
					"PrestationsDao log : Transmission de la Liste des Prestations selon les criteres GenreActivieSoin.");
			return prestationGA;

		} catch (Exception message) {
			logger.error("PrestationsDao log : probleme sur le format des criteres GenreActivieSoin.");
			throw new DaoException("PrestationsDao Exception : probleme sur le format des criteres GenreActivieSoin.");
		}
	}

	public List<Prestation> obtenirListPrestationsCriteresGenreActiviteSoins(String genre, String activite, String soin)
			throws DaoException {

		try {
			logger.info("PrestationsDao log : Demande la liste des Prestations selon les criteres GenreActivieSoin.");
			String requete = "SELECT P FROM Prestation P" + " WHERE PRESTATION_GENRE = '" + genre + "'"
					+ " AND PRESTATION_ACTIVITE = '" + activite + "'" + " AND PRESTATION_SOIN like '" + soin + "%"
					+ "'";

			TypedQuery<Prestation> query = em.createQuery(requete, Prestation.class);
			List<Prestation> prestationGAS = query.getResultList();
			logger.info("PrestationsDao log : Transmission de la Liste des Prestations les criteres GenreActivieSoin");
			return prestationGAS;

		} catch (Exception message) {
			logger.error("PrestationsDao log : probleme sur le format des criteresGenreActivieSoin.");
			throw new DaoException("PrestationsDao Exception : probleme sur le format  des criteresGenreActivieSoin.");
		}
	}

	public void ajouterUnePrestation(Prestation prestation) throws PrestationExistanteException {

		try {
			logger.info("PrestationsDao log : Demande d ajout d une nouvelle Prestations dans la Bdd.");
			em.persist(prestation);
			logger.info("PrestationsDao log : Nouveau Prestations ajoute, avec l id : " + prestation.getIdPrestation());

		} catch (EntityExistsException message) {
			logger.error("PrestationsDao log : Impossible de creer cette Prestations dans la Bdd.");
			throw new PrestationExistanteException(
					"PrestationsDao Exception : Probleme, cette Prestations a l air d'être deja persistee");

		}
	}

	public void modifieUnePrestation(Prestation prestation) throws PrestationInexistanteException {

		logger.info("PrestationsDao log : Demande de modification de la prestation id : " + prestation.getIdPrestation()
				+ " a la Bdd.");
		Prestation prestationbdd = em.find(Prestation.class, prestation.getIdPrestation());
		if (Objects.nonNull(prestationbdd)) {
			em.merge(prestation);
			logger.info("PrestationsDao log : Prestation id : " + prestation.getIdPrestation()
					+ " a ete modifie dans la Bdd.");
		} else {
			logger.error("PrestationsDao log : Prestation id : " + prestation.getIdPrestation()
					+ " ne peut etre modifie de la Bdd.");
			throw new PrestationInexistanteException("PrestationsDao log : Modification impossible,"
					+ "il n'y a pas de Prestation à modifier pour l'id : " + prestation.getIdPrestation()
					+ " demande.");
		}
	}

	public void supprimeUnePrestation(final Integer idPrestation) throws PrestationInexistanteException {

		logger.info("PrestationsDao log : Demande de suppression de la Prestation id : " + idPrestation + " a la Bdd.");
		Prestation prestation = null;
		prestation = em.find(Prestation.class, idPrestation);
		if (Objects.nonNull(prestation)) {
			em.remove(prestation);
			logger.info("PrestationsDao log : Prestation id : " + idPrestation + " a bien ete supprime de la Bdd.");
		} else {
			logger.error("PrestationsDao log : Prestation id : " + idPrestation
					+ " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new PrestationInexistanteException(
					"PrestationsDao Exception : Prestation id : " + idPrestation + " ne peut etre supprime de la Bdd.");
		}
	}

}
