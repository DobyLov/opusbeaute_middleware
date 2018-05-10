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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.Praticien;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.PraticienExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.PraticienInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailNotFoundException;

/**
 * Gestion de la Persitence des Praticien
 * 
 * @author fred
 *
 */
@Stateless
@Transactional
public class PraticienDao {

	static final Logger logger = LogManager.getLogger(PraticienDao.class);

	@PersistenceContext(unitName = "opusBeautePU")
	private EntityManager em;

	/**
	 * Retourne la liste des Praticiens Persistes
	 * 
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Praticien> obtenirListePraticien() throws DaoException {

		try {
			logger.info("PraticienDao log : Demande a la bdd la liste des Praticien");
			String requete = "SELECT P FROM Praticien P" + " ORDER BY idPraticien asc";
			;

			TypedQuery<Praticien> query = em.createQuery(requete, Praticien.class);
			List<Praticien> listePraticiens = query.getResultList();

			logger.info("PraticienDao log :  Envoi de la liste de Praticiens");
			return listePraticiens;

		} catch (Exception message) {
			logger.error("Praticienao Exception : Probleme de la bdd.");
			throw new DaoException("PraticienDao Exception : Probleme de la bdd.");
		}

	}

	/**
	 * Retourn le Praticien Persiste via son Id
	 * 
	 * @param idPraticien Integer
	 * @return particien Praticien
	 * @throws PraticienInexistantException Exception
	 */
	public Praticien obtenirPraticien(final Integer idPraticien) throws PraticienInexistantException {

		logger.info("PraticienDao log : Demande a la bdd l Praticien id : " + idPraticien);
		Praticien praticien = null;
		praticien = em.find(Praticien.class, idPraticien);

		if (Objects.isNull(praticien)) {
			logger.error("PraticienDao log : l utilisateur : " + idPraticien + " demande est introuvable");
			throw new PraticienInexistantException(
					"PraticienDao Exception : L' Id : " + idPraticien + " est introuvable dans la base");
		}

		logger.info("PraticienDao log : le client " + idPraticien + " trouve, envoie du Praticien a PraticienService");
		return praticien;

	}

	/**
	 * Persiste un nouveau Praticien
	 * 
	 * @param praticien Praticien
	 * @throws PraticienExistantException Exception
	 */
	public void ajouterUnPraticien(Praticien praticien) throws PraticienExistantException {

		try {
			logger.info("PraticienDao log : Demande d ajout d un nouveau Praticien dans la Bdd.");
			em.persist(praticien);
			logger.info(
					"PraticienDao log : Nouveau Praticien ajoute, avec l id : " + praticien.getIdPraticien());

		} catch (EntityExistsException message) {
			logger.error("PraticienDao log : Impossible de creer ce nouveau Praticien dans la Bdd.");
			throw new PraticienExistantException(
					"PraticienDao Exception : Probleme, cet Praticien a l air d'être deja persiste");

		}
	}

	/**
	 * Modifie un praticien
	 * 
	 * @param praticien Praticien
	 * @throws PraticienInexistantException Exception
	 */
	public void modifieUnPraticien(Praticien praticien) throws PraticienInexistantException {

		logger.info("PraticienDao log : Demande de modification de l praticien id : "
				+ praticien.getIdPraticien() + " a la Bdd.");
		Praticien praticienBdd = em.find(Praticien.class, praticien.getIdPraticien());
		if (Objects.nonNull(praticienBdd)) {
			em.merge(praticien);
			logger.info("PraticienDao log : L praticien id : " + praticien.getIdPraticien()
					+ " a ete modifie dans la Bdd.");
		} else {
			logger.error("PraticienDao log :  L praticien id : " + praticien.getIdPraticien()
					+ " ne peut etre modifie dans la Bdd.");
			throw new PraticienInexistantException("PraticienDao log : Modification impossible,"
					+ "il n'y a pas d Praticien à modifier pour l'id : " + praticien.getIdPraticien()
					+ " demande.");
		}
	}

	/**
	 * Supprime un Praticien Persiste
	 * 
	 * @param idPraticien Integer
	 * @throws PraticienInexistantException Exception
	 */
	public void supprimeUnPraticien(final Integer idPraticien) throws PraticienInexistantException {

		logger.info("PraticienDao log : Demande de suppression du Client id : " + idPraticien + " dans la Bdd.");
		Praticien praticien = null;
		praticien = em.find(Praticien.class, idPraticien);
		if (Objects.nonNull(praticien)) {
			em.remove(praticien);
			logger.info(
					"PraticienDao log : L Praticien  id : " + idPraticien + " a bien ete supprime de la Bdd.");
		} else {
			logger.error("PraticienDao log : L Praticien id : " + idPraticien
					+ " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new PraticienInexistantException("PraticienDao Exception : L Praticien id : " + idPraticien
					+ " ne peut etre supprime de la Bdd.");
		}
	}
	
	/**
	 * Retrouve un Praticien par son Email
	 * 
	 * 
	 * @param email String
	 * @return praticien Praticien
	 * @throws PraticienInexistantException Exception
	 */
	public Praticien praticienParEmail(final String email) throws PraticienInexistantException {
		logger.info("praticienDao log : Recherche praticien enregistre avec email : " + email + " a la Bdd.");
		Praticien praticien = null;
		
		try {
			String requete = "SELECT p FROM Praticien p" + " WHERE adresseMailPraticien = '" + email + "'";
			Query query = em.createQuery(requete);
			praticien = (Praticien) query.getSingleResult();
			logger.info("PraticienDao log : id " + praticien.getAdresseMailPraticien());

		} catch (Exception message) {
			throw new PraticienInexistantException("UtilisateurDao Exception : l utilisateur est introuvable");

		}

		return praticien;

	}
	
	/**
	 * Cherche si le mail fourni existe dans la table Praticien
	 * 
	 * @param email String
	 * @return Boolean
	 * @throws DaoException Exception
	 * @throws MailNotFoundException Exception
	 */
	public boolean checkMailExistInDB(String email) throws DaoException, MailNotFoundException {
		
		Praticien praticien = new Praticien();
		try {
			logger.info("PraticienDao log : si l adresseEmail fourni existe dans la bdd.");
			String requeteCli = "SELECT p FROM Praticien p" 
					+ " WHERE adresseMailPraticien = '" + email + "'";
			
			TypedQuery<Praticien> query = em.createQuery(requeteCli, Praticien.class);
			praticien = query.getSingleResult(); 
			
			logger.info("PraticienDao log :  AdresseEmail trouvee dans la bdd : " 
						 + praticien.getAdresseMailPraticien());
			return true;
				
			
		} catch (Exception message) {
			
			logger.info("PraticienDao Exception :  AdresseEmail " + email + " non trouvee dans la bdd : ");
			
			return false;
		}
	
	}

	
}
