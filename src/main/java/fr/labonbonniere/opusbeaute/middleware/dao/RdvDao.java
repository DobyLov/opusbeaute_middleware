package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityExistsException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvInexistantException;

/**
 * Gere la persistance des Rdv
 * 
 * @author fred
 *
 */
@Stateless
@Transactional
public class RdvDao {

	static final Logger logger = LogManager.getLogger(RdvDao.class);

	@PersistenceContext(unitName = "opusBeautePU")
	private EntityManager em;

	/**
	 * Recupere la liste de Rdv persistes
	 * 
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdv() throws DaoException {

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Rdv> reqCriteria = cb.createQuery(Rdv.class);
		reqCriteria.from(Rdv.class);
		final TypedQuery<Rdv> requete = em.createQuery(reqCriteria);
		List<Rdv> listeRdvs;

		try {
			logger.info("RdvDao log : Demande a la bdd la liste des Rdv");
			listeRdvs = requete.getResultList();

		} catch (Exception message) {
			logger.error("RdvDao Exception : Probleme de la bdd.");
			throw new DaoException("RdvDao Exception : Probleme de la bdd.");
		}
		logger.info("RdvDao log :  Envoi de la liste de Rdv");
		return listeRdvs;

	}
	
	/**
	 * Recupere la liste de rdv par client
	 * 
	 * @param idClient Integer
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdvParClient(final Integer idClient) throws DaoException{
		try {
			logger.info("RdvDao log : Demande a la Bdd de la liste des Rdv's selon les criteres de date selectionnee et par praticien");
			String requete = "SELECT c FROM Rdv c" + " WHERE client_idClient = " + idClient;
			
			TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
			List<Rdv> listeRdvClient = query.getResultList();
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par date selectionnee et par praticien.");
			return listeRdvClient;

		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la date ou id_Praticien.");
			throw new DaoException("RdvDao Exception : Date non existante dans calendrier.");
		}
	}
	
	/**
	 * Recupere la liste de rdv par Date et
	 * Par Praticien
	 * 
	 * @param dateJJ String
	 * @param IdPraticien Integer
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdvDuJJParPraticien(final String dateJJ, final Integer IdPraticien) throws DaoException {
		try {
			logger.info("RdvDao log : Demande a la Bdd de la liste des Rdv's selon les criteres de date selectionnee et par praticien");
			String requete = "SELECT c FROM Rdv c" + " WHERE rdv_dhdebut >= '" + dateJJ + " 00:00:00'"
					+ " AND rdv_dhdebut <= '" + dateJJ + " 23:59:00'" 
					+ " AND RDV_IDPRATICIEN_fk = " + IdPraticien
					+ " ORDER BY rdv_dhdebut";
			
			TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
			List<Rdv> listeRdvDateJJPraticien = query.getResultList();
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par date selectionnee et par praticien.");
			return listeRdvDateJJPraticien;

		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la date ou id_Praticien.");
			throw new DaoException("RdvDao Exception : Date non existante dans calendrier.");
		}
	}
	
	/**
	 * Recupere la liste de Rdv par plage de date 
	 * et par Praticien
	 * 
	 * @param dateA String
	 * @param dateB String
	 * @param IdPraticien Integer
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdvParPlageDeDateParPraticien(final String dateA, final String dateB, final Integer IdPraticien) throws DaoException {
		
		try {
			
			logger.info("RdvDao log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees et par praticien");
			String requete = "SELECT c FROM Rdv c" + " WHERE rdv_dhdebut >= '" + dateA + " 00:00:00'"
					+ " AND rdv_dhdebut <= '" + dateB + " 23:59:00'" 
					+ " AND RDV_IDPRATICIEN_fk = " + IdPraticien 
					+ " ORDER BY rdv_dhdebut";
			
			TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
			List<Rdv> rdvList = query.getResultList();
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par plage de dates selectionnees et par praticien.");
			return rdvList;

		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la/des date(s).");
			throw new DaoException("RdvDao Exception : probleme sur le format de la/des date(s).");
		}
	
	}
	
	/**
	 * Recupere un Rdv par son Id
	 * 
	 * @param idRdv Integer
	 * @return rdv Rdv
	 * @throws RdvInexistantException Exception
	 */
	public Rdv obtenirRdv(final Integer idRdv) throws RdvInexistantException {

		logger.info("RdvDao log : Demande a la bdd le Rdv id : " + idRdv);
		Rdv rdv = null;
		rdv = em.find(Rdv.class, idRdv);

		if (Objects.isNull(rdv)) {
			logger.error("RdvDao log : Le rdv : " + idRdv + " demande est introuvable");
			throw new RdvInexistantException("RdvDao Exception : L' Id : " + idRdv + " est introuvable dans la base");
		}

		logger.info("RdvDao log : Rdv " + idRdv + " trouve, envoie de l objet Rdv a RdvService");
		return rdv;

	}

	/**
	 * Recupere la liste de Rdv par plage de date
	 * 
	 * @param dateA String
	 * @param dateB string
	 * @return rdv Rdv
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdvViaPlageDate(String dateA, String dateB) throws DaoException {

		try {
			logger.info("RdvDao log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees.");
			String requete = "SELECT c FROM Rdv c" + " WHERE rdv_dhdebut >= '" + dateA + " 00:00:00'"
					+ " AND rdv_dhdebut <= '" + dateB + " 23:59:00'" + " ORDER BY rdv_dhdebut";
			TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
			List<Rdv> rdv = query.getResultList();
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			return rdv;

		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la/des date(s).");
			throw new DaoException("RdvDao Exception : probleme sur le format de la/des date(s).");
		}
	}

	/**
	 * Recupere la liste des Rdv (persistes) du Jour specifie
	 * 
	 * @param listeRdvDateDuJour String
	 * @return rdv Rdv
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdvParDate(String listeRdvDateDuJour) throws DaoException {

		try {
			logger.info("RdvDao log : Demande a la Bdd de la liste des Rdv's par date selectionnee.");
			String requete = "SELECT c FROM Rdv c" + " WHERE rdv_dhdebut >= '" + listeRdvDateDuJour + " 00:00:00'"
					+ " AND rdv_dhdebut <= '" + listeRdvDateDuJour + " 23:59:00'" + " ORDER BY rdv_dhdebut";
			TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
			List<Rdv> rdv = query.getResultList();
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par date selectionnee.");
			return rdv;

		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la date.");
			throw new DaoException("RdvDao Exception : Date non existante dans calendrier.");
		}
	}

	/**
	 * persiste un nouveau Rdv
	 * 
	 * @param rdv Rdv
	 * @throws RdvExistantException Exception
	 */
	public void ajouterUnRdv(Rdv rdv) throws RdvExistantException {

		try {
			logger.info("RdvDao log : Demande d ajout d un nouveau Rdv dans la Bdd.");
			em.persist(rdv);
			logger.info("RdvDao log : Nouveau Rdv ajoute, avec l id : " + rdv.getIdRdv());
		}

		catch (EntityExistsException message) {
			logger.error("RdvDao log : Impossible de creer ce rdv dans la Bdd.");
			throw new RdvExistantException("RdvDao Exception : Probleme, ce rdv a l air d'être deja persisté");

		}
	}

	/**
	 * Modifie un Rdv persiste
	 * 
	 * @param rdv Rdv
	 * @throws RdvInexistantException Exception
	 */
	public void modifieUnRdv(Rdv rdv) throws RdvInexistantException {

		logger.info("RdvDao log : Demande de modification du Rdv id : " + rdv.getIdRdv() + " a la Bdd.");
		Rdv rdvbdd = em.find(Rdv.class, rdv.getIdRdv());
		if (Objects.nonNull(rdvbdd)) {
			em.merge(rdv);
			logger.info("RdvDao log : Rdv id : " + rdv.getIdRdv() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("RdvDao log : Rdv id : " + rdv.getIdRdv() + " ne peut etre modifie de la Bdd.");
			throw new RdvInexistantException("RdvDao log : Modification impossible,"
					+ "il n'y a pas de Rdv à modifier pour l'id : " + rdv.getIdRdv() + " demande.");
		}
	}

	/**
	 * Suprime un Rdv persiste
	 * 
	 * @param idRdv Integer
	 * @throws RdvInexistantException Exception
	 */
	public void supprimeunRdv(final Integer idRdv) throws RdvInexistantException {

		logger.info("RdvDao log : Demande de suppression du Rdv id : " + idRdv + " a la Bdd.");
		Rdv rdv = null;
		rdv = em.find(Rdv.class, idRdv);
		if (Objects.nonNull(rdv)) {
			em.remove(rdv);
			logger.info("RdvDao log : Rdv id : " + idRdv + " a bien ete supprime de la Bdd.");
		} else {
			logger.error("RdvDao log : Rdv id : " + idRdv + " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new RdvInexistantException(
					"RdvDao Exception : Rdv id : " + idRdv + " ne peut etre supprime de la Bdd.");
		}
	}

	/**
	 * Retourne le nombre de Rdv plannifies selon la date
	 * 
	 * @param rdvDateDuJour String
	 * @return Integer
	 * @throws DaoException Exception
	 */
	public Integer renvoyerLeNbDeRdvDuJour(String rdvDateDuJour) throws DaoException {

		try {
			logger.info("RdvDao log : Demande a la Bdd le nombre de Rdv's a la date selectionnee.");
			logger.info("RdvDao log : Date a traiter : " + rdvDateDuJour);
			logger.info("RdvDao log : toto : ");
			String requete = "SELECT COUNT(*) FROM Rdv"
					+ " WHERE rdv_dhdebut > '" + rdvDateDuJour + " 00:00:00'"
					+ " AND rdv_dhdebut < '" + rdvDateDuJour + " 23:59:59'";
			
			logger.info("RdvDao log : Requete : " + requete);
			TypedQuery<Long> query = em.createQuery(requete, Long.class);
			Integer compteurRdv = query.getSingleResult().intValue();
			logger.info("RdvDao log : il y a : " + compteurRdv + " pour la date du : " + rdvDateDuJour);
			logger.info("RdvDao log : Transmission du nombre de Rdv's par date selectionnee.");

			return compteurRdv;

		} catch (Exception message) {
			message.printStackTrace();
			logger.error("RdvDao log : Date non existante dans calendrier.");
			throw new DaoException("RdvDao Exception : Date non existante dans calendrier.");
		}
	}

	/**
	 * Modifier dans RdvService la methode Pour aller chercher le resultat de la
	 * date du jour
	 * 
	 * @param rdvDateDuJourFormate String
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdvViaDateAndMailSuscribedTrue(String rdvDateDuJourFormate) throws DaoException {

		try {
			logger.info(
					"RdvDao log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees et abonne au MailReminder.");
			String requete = "SELECT c FROM Rdv c" + " WHERE rdv_dhdebut >= '" + rdvDateDuJourFormate + " 00:00:00'"
					+ " AND rdv_dhdebut <= '" + rdvDateDuJourFormate + " 23:59:00'";
			TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
			List<Rdv> rdv = query.getResultList();
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			return rdv;

		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la/des date(s).");
			throw new DaoException("RdvDao Exception : probleme sur le format de la/des date(s).");
		}
	}

	/**
	 * Modifier dans RdvService la methode Pour aller chercher le resultat de la
	 * date du jour
	 * 
	 * @param rdvDateDuJourFormate String
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Rdv> obtenirListeRdvViaDatePraticienSuscribedMailReminder(String rdvDateDuJourFormate)
			throws DaoException {

		try {
			logger.info("RdvDao log : Demande a la Bdd la liste des Rdv's du jour demande.");

			String requete = "SELECT c FROM Rdv c" + " WHERE rdv_dhdebut >= '" + rdvDateDuJourFormate + " 00:00:00'"
					+ " AND rdv_dhdebut <= '" + rdvDateDuJourFormate + " 23:59:00'";
			// + " AND suscribedMailReminder = 'T' ";

			TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
			List<Rdv> rdvList = query.getResultList();

			logger.info("RdvDao log : Transmission de la Liste des Rdv's du jour selectionnes.");
			return rdvList;

		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la date.");
			throw new DaoException("RdvDao Exception : probleme sur le format de la date.");
		}
	}
}
