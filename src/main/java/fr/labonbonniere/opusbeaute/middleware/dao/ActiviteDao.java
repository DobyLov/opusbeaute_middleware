package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.activite.Activite;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.activite.ActiviteInexistanteException;

/**
 * Gere la persistance des activites (prestations)
 * 
 * @author fred
 *
 */

@Stateless
@Transactional
public class ActiviteDao {

	/* Loger */
	static final Logger logger = LogManager.getLogger(ActiviteDao.class);
	
	/* Gestionnaire d entite */
	@PersistenceContext(unitName = "opusBeautePU")
	private EntityManager em;
	
	/**
	 * Retourne le nombre d entree dans la base pour la table Activite
	 * @return
	 * @throws DaoException
	 */
	public long countGenre() throws DaoException {
		
		logger.info("ActiviteDao log : Compte le nombre d Activite dans la table Activite");
		String requete = "SELECT COUNT(*) FROM Activite a";
		Query query = em.createQuery(requete);
		
		long nbActivite = (long) query.getSingleResult();
		
		return nbActivite;
		
	}
	
	/**
	 * Recupere la liste des activites
	 * @return
	 * @throws DaoException
	 */
	public List<Activite> recupereLaListeDesActivites() throws DaoException {
		
		try {
			
			logger.info("ActiviteDao log : Demande a la bdd la liste des Activites");
			String requete = "SELECT A FROM Activite A" + " ORDER BY activiteNom asc";
			
			TypedQuery<Activite> query = em.createQuery(requete, Activite.class);
			List<Activite> liste = query.getResultList();

			logger.info("ActiviteDao log :  Envoi de la liste des activites");
			
			return liste;

		} catch (Exception message) {
			
			logger.error("ActiviteDao Exception : Probleme de la bdd.");
			throw new DaoException("ActiviteDao Exception : Probleme de la bdd.");
			
		}

	}
	
	/**
	 * Retourne L Activite par son Id
	 * @param idActivite
	 * @return Activite
	 * @throws ActiviteInexistanteException
	 */
	public Activite obtenirActiviteParId(final Integer idActivite) throws ActiviteInexistanteException {

		logger.info("ActiviteDao log : Demande a la bdd l Activite id : " + idActivite);
		Activite activite = null;
		activite = em.find(Activite.class, idActivite);

		if (Objects.isNull(activite)) {
			
			logger.error("activiteDao log : L Activite : " + idActivite + " demande est introuvable");
			throw new ActiviteInexistanteException(
					"ActiviteDao Exception : L Activite Id : " + idActivite + " est introuvable dans la base");
		}

		logger.info("ActiviteDao log : Activite " + idActivite + " trouve, envoie de l activite a ActiviteService");
		
		return activite;

	}
	
	/**
	 * Ajouter une Activite 
	 * @param activite
	 * @throws DaoException
	 */
	public void ajouterUneActivite(Activite activite) throws DaoException{

		try {
			
			logger.info("ActiviteDao log : Demande d ajout d un nouveau Genre dans la Bdd.");
			em.persist(activite);
			logger.info("ActiviteDao log : Nouveau type de Genre ajoute, avec l id : " + activite.getIdActivite());

		} catch (Exception message) {
			
			logger.error("ActiviteDao log : Impossible de creer cette nouvelle Activite dans la Bdd.");
			throw new DaoException("ActiviteDao Exception : Probleme, ce Activite a l air d'être deja persistee");

		}
	}
	
	/**
	 * Modifie un activite existante
	 * @param activite
	 * @throws ActiviteInexistanteException
	 */
	public void modifieUneActivite(Activite activite) throws ActiviteInexistanteException {

		logger.info("ActiviteDao log : Demande de modification activite id : " + activite.getIdActivite() + " a la Bdd.");
		Activite activiteBdd = em.find(Activite.class, activite.getIdActivite());
		if (Objects.nonNull(activiteBdd)) {
			em.merge(activite);
			
			logger.info("ActiviteDao log : Activite id : " + activite.getIdActivite() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("ActiviteDao log : Activite id : " + activite.getIdActivite() + " ne peut etre modifie dans la Bdd.");
			throw new ActiviteInexistanteException("ActiviteDao log : Modification impossible,"
					+ "il n'y a pas d Activite à modifier pour l'id : " + activite.getIdActivite() + " demande.");
		}
	}
	
	public void supprimeUneActivite(final Integer idActivite) throws ActiviteInexistanteException  {

		logger.info("ActiviteDao log : Demande de suppression de l activite id : " + idActivite + " dans la Bdd.");
		Activite activite = null;
		activite = em.find(Activite.class, idActivite);
		if (Objects.nonNull(activite)) {
			
			em.remove(activite);
			logger.info("ActiviteDao log : Activite id : " + idActivite + " a bien ete supprimee de la Bdd.");
		
		} else {
			
			logger.error(
					"ActiviteDao log : Activite id : " + idActivite + " inexistante alors il ne peut etre supprime de la Bdd.");
			throw new ActiviteInexistanteException(
					"ActiviteDao Exception : Cette activite id : " + idActivite + " ne peut etre supprime de la Bdd.");
		}
	}
	
}
