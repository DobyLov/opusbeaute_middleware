package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailNotFoundException;

/**
 * Gere la persistance des Utilisateurs
 * 
 * @author fred
 *
 */
@Stateless
@Transactional
public class UtilisateurDao {

	static final Logger logger = LogManager.getLogger(UtilisateurDao.class.getSimpleName());

	@PersistenceContext(unitName = "opusBeautePU")
	private EntityManager em;

	/**
	 * Recupere la liste des Utilisateurs persistes
	 * 
	 * @return Liste d objet Utilisteurs
	 * @throws DaoException Exception
	 */
	public List<Utilisateur> obtenirListeUtilisateur() throws DaoException {

		try {
			logger.info("UtilisateurDao log : Demande a la bdd la liste des Utilisateurs");
			String requete = "SELECT U FROM Utilisateur U" + " ORDER BY idUtilisateur asc";

			TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
			List<Utilisateur> listeUtilisateurs = query.getResultList();

			logger.info("UtilisateurDao log :  Envoi de la liste de Clients");
			return listeUtilisateurs;

		} catch (Exception message) {
			logger.error("UtilisateurDao Exception : Probleme de la bdd.");
			throw new DaoException("UtilisateurDao Exception : Probleme de la bdd.");
		}

	}

	/**
	 * Recupere un Utilisateur persiste par son Id
	 * 
	 * @param idUtilisateur Integer
	 * @return utilisateur Utilisateur
	 * @throws UtilisateurInexistantException Exception
	 */
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

	/**
	 * Persiste un nouvel utilisateur
	 * 
	 * @param utilisateur Utilisateur
	 * @throws UtilisateurExistantException Exception
	 */
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

	/**
	 * Modifie un Utilisateur Persiste
	 * 
	 * @param utilisateur Utilisateur
	 * @throws UtilisateurInexistantException Exception
	 */
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

	/**
	 * Supprime un Utilisateur Persiste
	 * 
	 * @param idUtilisateur Integer
	 * @throws UtilisateurInexistantException Exception
	 */
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

	/**
	 * Retrouve un Utilisateur par son Email
	 * 
	 * 
	 * @param email String
	 * @return utilisateur Utilisateur
	 * @throws UtilisateurInexistantException Exception
	 */
	public Utilisateur utilisateurParEmail(final String email) throws UtilisateurInexistantException {
		logger.info("UtilisateurDao log : Recherche utilisateur enregistre avec email : " + email + " a la Bdd.");
		Utilisateur utilisateur = null;
		try {
			String requete = "SELECT u FROM Utilisateur u" + " WHERE adresseMailUtilisateur = '" + email + "'";
			Query query = em.createQuery(requete);
			utilisateur = (Utilisateur) query.getSingleResult();
			logger.info("UtilisateurDao log : id " + utilisateur.getIdUtilisateur());

		} catch (Exception message) {
			throw new UtilisateurInexistantException("UtilisateurDao Exception : l utilisateur est introuvable");

		}

		return utilisateur;

	}
	
	/**
	 * Cherche si le mail fourni existe dans la table Utilisateur
	 * 
	 * @param email String
	 * @return nombreUtilisateurAvecCetEmail Integer 
	 * @throws MailNotFoundException Exception
	 */
	public Integer checkMailExistInDB(String email) throws MailNotFoundException {
		

		try {
			logger.info("UtilisateurDao log : si l adresseEmail fourni existe dans la bdd.");
			String requete = "SELECT u FROM Utilisateur u" 
					+ " WHERE adresseMailUtilisateur = '" + email + "'";
			
			TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
			List<Utilisateur> ListeU = query.getResultList();
			Integer nombreUtilisateurAvecCetEmail = ListeU.size();
			
			logger.info("UtilisateurDao log :  AdresseEmail " + email + " trouvee " 
			+ nombreUtilisateurAvecCetEmail + " fois dans la bdd ");
			
			return nombreUtilisateurAvecCetEmail;
			
		} catch (Exception message) {			
			logger.info("UtilisateurDao log :  AdresseEmail " + email 
					+ " non trouvee dans la bdd ");	
			throw new MailNotFoundException("UtilisateurDao log :  AdresseEmail " + email 
					+ " non trouvee dans la bdd ");
			
		}
	
	}
	
	/**
	 * recupere le nombre total d utilisateurs
	 * 
	 * @return compteurUtilisateur Integer
	 * @throws DaoException Exception
	 */
	public Integer nombreUtilisateurs() throws DaoException {
		
		try {
			String requete = "SELECT count(u) FROM Utilisateur u";

			logger.info("UtilisateurDao log : Requete : " + requete);
			Query query = em.createQuery(requete);
			Integer compteurUtilisateur = query.getFirstResult();
			
			return compteurUtilisateur;
			
		} catch (Exception message) {
			throw new DaoException ("UtilisateurDao Exception : Il y a eu probleme lors de la recuperation du nombre d utilisateurs");
		}
		
		
	}

}
