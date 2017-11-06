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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;

@Stateless
@Transactional
public class UtilisateurDao {

	static final Logger logger = LogManager.getLogger(PrestationDao.class);

	@PersistenceContext(unitName = "dobyPUtest")
	private EntityManager em;

	public List<Utilisateur> obtenirListeUtilisateur() throws DaoException {

		try {
			logger.info("UtilisateurDao log : Demande a la bdd la liste des Atilisateurs");
			String requete = "SELECT U FROM Utilisateurs U" + " ORDER BY idUtilisateurs asc";
			;

			TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
			List<Utilisateur> listeUtilisateurs = query.getResultList();

			logger.info("UtilisateurDao log :  Envoi de la liste de Clients");
			return listeUtilisateurs;

		} catch (Exception message) {
			logger.error("UtilisateurDao Exception : Probleme de la bdd.");
			throw new DaoException("UtilisateurDao Exception : Probleme de la bdd.");
		}

	}

	public Utilisateur obtenirUtilisateur(final Integer idUtilisateur) throws UtilisateurInexistantException {

		logger.info("UtilisateurDao log : Demande a la bdd l utilisateur id : " + idUtilisateur);
		Utilisateur utilisateur = null;
		utilisateur = em.find(Utilisateur.class, idUtilisateur);

		if (Objects.isNull(utilisateur)) {
			logger.error("UtilisateurDao log : l utilisateur : " + idUtilisateur + " demande est introuvable");
			throw new UtilisateurInexistantException(
					"UtilisateurDao Exception : L' Id : " + idUtilisateur + " est introuvable dans la base");
		}

		logger.info("UtilisateurDao log : le client " + idUtilisateur + " trouve, envoie du Client a ClientService");
		return utilisateur;

	}

	public void ajouterUnutilisateur(Utilisateur utilisateur) throws UtilisateurExistantException {

		try {
			logger.info("UtilisateurDao log : Demande d ajout d un nouvel Utilisateur dans la Bdd.");
			em.persist(utilisateur);
			logger.info(
					"UtilisateurDao log : Nouvel utilisateur ajoute, avec l id : " + utilisateur.getIdUtilisateur());

		} catch (EntityExistsException message) {
			logger.error("UtilisateurDao log : Impossible de creer ce nouvel utilisateur dans la Bdd.");
			throw new UtilisateurExistantException(
					"UtilisateurDao Exception : Probleme, cet Utilisateur a l air d'être deja persistee");

		}
	}

	public void modifieUnUtilisateur(Utilisateur utilisateur) throws UtilisateurInexistantException {

		logger.info("UtilisateurDao log : Demande de modification de l utilisateur id : "
				+ utilisateur.getIdUtilisateur() + " a la Bdd.");
		Utilisateur utilisateurBdd = em.find(Utilisateur.class, utilisateur.getIdUtilisateur());
		if (Objects.nonNull(utilisateurBdd)) {
			em.merge(utilisateur);
			logger.info("UtilisateurDao log : L utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " a ete modifie dans la Bdd.");
		} else {
			logger.error("UtilisateurDao log :  L utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " ne peut etre modifie dans la Bdd.");
			throw new UtilisateurInexistantException("UtilisateurDao log : Modification impossible,"
					+ "il n'y a pas d Utilisateur à modifier pour l'id : " + utilisateur.getIdUtilisateur()
					+ " demande.");
		}
	}

	public void supprimeUnUtilisateur(final Integer idUtilisateur) throws UtilisateurInexistantException {

		logger.info("UtilisateurDao log : Demande de suppression du Client id : " + idUtilisateur + " dans la Bdd.");
		Utilisateur utilisateur = null;
		utilisateur = em.find(Utilisateur.class, idUtilisateur);
		if (Objects.nonNull(utilisateur)) {
			em.remove(utilisateur);
			logger.info(
					"UtilisateurDao log : L utilisateur  id : " + idUtilisateur + " a bien ete supprime de la Bdd.");
		} else {
			logger.error("UtilisateurDao log : L utilisateur id : " + idUtilisateur
					+ " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new UtilisateurInexistantException("UtilisateurDao Exception : L utilisateur id : " + idUtilisateur
					+ " ne peut etre supprime de la Bdd.");
		}
	}

}
