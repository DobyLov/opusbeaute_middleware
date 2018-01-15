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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.Roles;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.RolesInexistantException;


@Stateless
@Transactional
public class RolesDao {

	static final Logger logger = LogManager.getLogger(Roles.class);
	
	@PersistenceContext(unitName = "dobyPUtest")
	private EntityManager em;
	
	public long CountRoles() throws DaoException {
		logger.info("RolesDao log : Compte le nombre de Role dans la table Role");
		String requete = "SELECT COUNT(G) FROM Roles g";
//		String requete = "SELECT COUNT(*) FROM T_CLIENT";
		Query query = em.createQuery(requete);
		long nbRolesRowsinBdd = (long) query.getSingleResult();
		return nbRolesRowsinBdd;
	}

	public List<Roles> obtenirListeRoles() throws DaoException {

		try {
			logger.info("RolesDao log : Demande a la bdd la liste des Roles");
			String requete = "SELECT G FROM Roles G" + " ORDER BY idRoles asc";
			;

			TypedQuery<Roles> query = em.createQuery(requete, Roles.class);
			List<Roles> listeRoles = query.getResultList();

			logger.info("RolesDao log :  Envoi de la liste de Roles");
			return listeRoles;

		} catch (Exception message) {
			logger.error("RolesDao Exception : Probleme de la bdd.");
			throw new DaoException("RolesDao Exception : Probleme de la bdd.");
		}

	}

	public Roles obtenirRole(final Integer idRole) throws RolesInexistantException {

		logger.info("RolesDao log : Demande a la bdd le Prestations id : " + idRole);
		Roles role = null;
		role = em.find(Roles.class, idRole);

		if (Objects.isNull(role)) {
			logger.error("RolesDao log : Le role : " + idRole + " demande est introuvable");
			throw new RolesInexistantException(
					"RolesDao Exception : L' Id : " + idRole + " est introuvable dans la base");
		}

		logger.info("RolesDao log : Prestations " + idRole + " trouve, envoie du Role a PrestationsService");
		return role;

	}

	public void ajouterUnRoles(Roles role) throws DaoException{

		try {
			logger.info("RolesDao log : Demande d ajout d un nouveau Role dans la Bdd.");
			em.persist(role);
			logger.info("RolesDao log : Nouveau type de Role ajoute, avec l id : " + role.getIdRoles());

		} catch (Exception message) {
			logger.error("RolesDao log : Impossible de creer ce nouveau Role dans la Bdd.");
			throw new DaoException("RolesDao Exception : Probleme, ce Role a l air d'être deja persistee");

		}
	}

	public void modifieUnRoles(Roles role) throws RolesInexistantException {

		logger.info("RolesDao log : Demande de modification role id : " + role.getIdRoles() + " a la Bdd.");
		Roles roleBdd = em.find(Roles.class, role.getIdRoles());
		if (Objects.nonNull(roleBdd)) {
			em.merge(role);
			logger.info("RolesDao log : Rdv id : " + role.getIdRoles() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("RolesDao log : Rdv id : " + role.getIdRoles() + " ne peut etre modifie dans la Bdd.");
			throw new RolesInexistantException("RolesDao log : Modification impossible,"
					+ "il n'y a pas de Client à modifier pour l'id : " + role.getIdRoles() + " demande.");
		}
	}

	public void supprimeUnRoles(final Integer idRoles) throws RolesInexistantException {

		logger.info("RolesDao log : Demande de suppression du Role id : " + idRoles + " dans la Bdd.");
		Roles genre = null;
		genre = em.find(Roles.class, idRoles);
		if (Objects.nonNull(genre)) {
			em.remove(genre);
			logger.info("RolesDao log : Type de Role id : " + idRoles + " a bien ete supprime de la Bdd.");
		} else {
			logger.error(
					"RolesDao log : Role id : " + idRoles + " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new RolesInexistantException(
					"RolesDao Exception : Ce Role id : " + idRoles + " ne peut etre supprime de la Bdd.");
		}
	}
	
	
	
}
