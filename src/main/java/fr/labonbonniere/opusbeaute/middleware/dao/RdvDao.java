package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityExistsException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;



import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvInexistantException;


@Stateless
@Transactional
public class RdvDao {

	static final Logger logger = LogManager.getLogger(RdvDao.class);

	@PersistenceContext(unitName="dobyPUtest")
	private EntityManager em;
	

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
	
	
	public Rdv obtenirRdv(final Integer idRdv) throws RdvInexistantException {
		
		logger.info("RdvDao log : Demande a la bdd le Rdv id : " + idRdv);
		Rdv rdv = null;
		rdv = em.find(Rdv.class, idRdv);
		
		if (Objects.isNull(rdv)) {
			logger.error("RdvDao log : Le rdv : " + idRdv + " demande est introuvable");
			throw new RdvInexistantException ("RdvDao Exception : L' Id : " + idRdv + " est introuvable dans la base");
		}
		
		logger.info("RdvDao log : Rdv " + idRdv + " trouve, envoie de l objet Rdv a RdvService");
		return rdv;			

	}
	
	
	public List<Rdv> obtenirListeRdvViaPlageDate(String RdvPlageJourA, String RdvPlageJourB) throws DaoException {
		
		try {
			logger.info("RdvDao log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees.");
			String requete =  "SELECT c FROM Rdv c"
							+ " WHERE rdv_dhdebut >= '" + RdvPlageJourA + " 00:00:00'"
							+ " AND rdv_dhdebut <= '" + RdvPlageJourB + " 23:59:00'"
							+ " ORDER BY rdv_dhdebut";		
			TypedQuery<Rdv> query = em.createQuery(requete,Rdv.class);
			List<Rdv> rdv = query.getResultList();
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			return rdv;
			
		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la/des date(s).");
			throw new DaoException("RdvDao Exception : probleme sur le format de la/des date(s).");
		}
	}
	
	
	public List<Rdv> obtenirListeRdvParDate(String listeRdvDateDuJour) throws DaoException {
		
		try {
			logger.info("RdvDao log : Demande a la Bdd de la liste des Rdv's par date selectionnee.");
			String requete =  "SELECT c FROM Rdv c"
							+ " WHERE rdv_dhdebut >= '" + listeRdvDateDuJour + " 00:00:00'"
							+ " AND rdv_dhdebut <= '" + listeRdvDateDuJour + " 23:59:00'"
							+ " ORDER BY rdv_dhdebut";				
			TypedQuery<Rdv> query = em.createQuery(requete,Rdv.class);
			List<Rdv> rdv = query.getResultList();	
			logger.info("RdvDao log : Transmission de la Liste des Rdv's par date selectionnee.");
			return rdv;	
				
		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la date.");
			throw new DaoException("RdvDao Exception : Date non existante dans calendrier.");
		}
	}
				

	public void ajouterUnRdv(Rdv rdv)  throws RdvExistantException {
		
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
	

	 public void modifieUnRdv (Rdv rdv) throws RdvInexistantException {
		 
			logger.info("RdvDao log : Demande de modification du Rdv id : " + rdv.getIdRdv() + " a la Bdd.");
			Rdv rdvbdd = em.find(Rdv.class, rdv.getIdRdv());		
			if ( Objects.nonNull(rdvbdd) ) {
				em.merge(rdv);			
				logger.info("RdvDao log : Rdv id : " + rdv.getIdRdv() + " a ete modifie dans la Bdd.");
			} else {
				logger.error("RdvDao log : Rdv id : " + rdv.getIdRdv() + " ne peut etre modifie de la Bdd.");
				throw new RdvInexistantException ("RdvDao log : Modification impossible," 				
						+ "il n'y a pas de Rdv à modifier pour l'id : " 
						+ rdv.getIdRdv() + " demande.");
			}
	}
	 
	
	public void supprimeunRdv (final Integer idRdv ) throws RdvInexistantException {
		
		
			logger.info("RdvDao log : Demande de suppression du Rdv id : " + idRdv + " a la Bdd.");
			Rdv rdv = null;
			rdv = em.find(Rdv.class, idRdv);
			if (Objects.nonNull(rdv)) {
			em.remove(rdv);
			logger.info("RdvDao log : Rdv id : " + idRdv + " a bien ete supprime de la Bdd.");
			} else {
				logger.error("RdvDao log : Rdv id : " + idRdv + " inexistant alors il ne peut etre supprime de la Bdd.");
				throw new RdvInexistantException("RdvDao Exception : Rdv id : " + idRdv + " ne peut etre supprime de la Bdd.");
			}
	}
	
	
	public Integer renvoyerLeNbDeRdvDuJour(String rdvDateDuJour) throws DaoException {
		
		try {
			logger.info("RdvDao log : Demande a la Bdd le nombre de Rdv's a la date selectionnee.");
			logger.info("RdvDao log : date a traiter : " + rdvDateDuJour);
			
			String requete =  "SELECT count(c) FROM Rdv c"
							+ " WHERE rdv_dhdebut >= '" + rdvDateDuJour + " 00:00:00'"
							+ " AND rdv_dhdebut <= '" + rdvDateDuJour + " 23:59:00'";
			
//			String requete =  "SELECT count(*) FROM T_RDV";
//					+ " WHERE rdv_dhdebut >= '" + rdvDateDuJour + " 00:00:00'"
//					+ " AND rdv_dhdebut <= '" + rdvDateDuJour + " 23:59:00'";			
			
			logger.info("RdvDao log : Requete : " + requete);
			Query query = em.createQuery(requete);
			Integer compteurRdv = query.getFirstResult();
			
			
			logger.info("RdvDao log : Transmission du nombre de Rdv's par date selectionnee.");

			return compteurRdv;	
				
		} catch (Exception message) {
			message.printStackTrace();
			logger.error("RdvDao log : Date non existante dans calendrier.");
			throw new DaoException("RdvDao Exception : Date non existante dans calendrier.");
		}
	}
	
	public List<Rdv> obtenirListeRdvViaDateAndMailSuscribedTrue(String rdvDateDuJourFormate) throws DaoException {
			
			try {
				logger.info("RdvDao log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees et abonne au MailReminder.");
				String requete =  "SELECT c FROM Rdv c"
						+ " WHERE rdv_dhdebut >= '" + rdvDateDuJourFormate + " 00:00:00'"
						+ " AND rdv_dhdebut <= '" + rdvDateDuJourFormate + " 23:59:00'";	
				TypedQuery<Rdv> query = em.createQuery(requete,Rdv.class);
				List<Rdv> rdv = query.getResultList();
				logger.info("RdvDao log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
				return rdv;
				
			} catch (Exception message) {
				logger.error("RdvDao log : probleme sur le format de la/des date(s).");
				throw new DaoException("RdvDao Exception : probleme sur le format de la/des date(s).");
			}
	}
	
	public List<Rdv> obtenirListeRdvViaDatePraticienSuscribedMailReminder(String rdvDateDuJourFormate) throws DaoException {
		
		try {
			logger.info("RdvDao log : Demande au RdvDao la liste des Rdv's du jour demande.");
			
			String requete =  "SELECT c FROM Rdv c"
					+ " WHERE rdv_dhdebut >= '" + rdvDateDuJourFormate + " 00:00:00'"
					+ " AND rdv_dhdebut <= '" + rdvDateDuJourFormate + " 23:59:00'";
//					+ " AND suscribedMailReminder = 'T' ";
			
			TypedQuery<Rdv> query = em.createQuery(requete,Rdv.class);
			List<Rdv> rdv = query.getResultList();
			
			logger.info("RdvDao log : Transmission de la Liste des Rdv's du jour selectionnes.");
			return rdv;
			
		} catch (Exception message) {
			logger.error("RdvDao log : probleme sur le format de la date.");
			throw new DaoException("RdvDao Exception : probleme sur le format de la date.");
		}
}
	
	
}
	



