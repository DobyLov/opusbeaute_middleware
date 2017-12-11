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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresselieurdv.AdresseLieuRdv;

@Stateless
@Transactional
public class AdresseLieuRdvDao {
	static final Logger logger = LogManager.getLogger(AdresseLieuRdvDao.class);

	@PersistenceContext(unitName="dobyPUtest")
	private EntityManager em;
	
	
	public List<AdresseLieuRdv> obtenirListeAdresseLieuRdv() throws DaoException {
		
		
		try {
			logger.info("AdresseLieuRdv Dao log : Demande a la bdd la liste des AdressesLieuRdv");
			String requete =  "SELECT a FROM AdresseLieuRdv a"
							+ " ORDER BY idAdresseLieuRdv asc";
					
			TypedQuery<AdresseLieuRdv> query = em.createQuery(requete,AdresseLieuRdv.class);
			List<AdresseLieuRdv> listeAdressesLieurRdv = query.getResultList();
			
			logger.info("AdresseLieuRdv Dao log :  Envoi de la liste des AdresseLieuRdv a AdresseLieuRdv");
			return listeAdressesLieurRdv;
			
		} catch (Exception message) {
			logger.error("AdresseLieuRdv DAO Exception : Probleme de la bdd.");
			throw new DaoException("AdresseLieuRdv Dao Exception : Probleme de la bdd.");
		}

		
	}
	
	public AdresseLieuRdv obtenirAdresseLieuRdv(final Integer idAdresseLieuRdv) throws AdresseInexistanteException {
		
		logger.info("AdresseLieuRdv Dao log : Demande a la bdd l adresse id : " + idAdresseLieuRdv);
		AdresseLieuRdv adresseLieuRdv = null;
		adresseLieuRdv = em.find(AdresseLieuRdv.class, idAdresseLieuRdv);
		
		if (Objects.isNull(adresseLieuRdv)) {
			logger.error("AdresseLieuRdv Dao log : L adresseLieuRdv id : " + idAdresseLieuRdv + " demande est introuvable");
			throw new AdresseInexistanteException ("AdresseLieuRdv Dao Exception : L' Id : " + idAdresseLieuRdv+ " est introuvable dans la base");
		}
		
		logger.info("AdresseLieuRdv Dao log : Adresse " + idAdresseLieuRdv + " trouve, envoie de l AdresseLieuRdv a AdresseLieuRdvService");
		return adresseLieuRdv;			

	}
	
	public void ajouterUneAdresseLieuRdv (AdresseLieuRdv adresseLieuRdv)  throws AdresseExistanteException {
		
		try {
			logger.info("AdresseLieuRdv Dao log : Demande d ajout d une nouvelle AdresselieuRdv dans la Bdd.");
			em.persist(adresseLieuRdv);
			logger.info("AdresseLieuRdv Dao log : Nouvelle AdresseLieuRdv ajoutee, avec l id : " + adresseLieuRdv.getIdAdresseLieuRdv());
		
		}catch (EntityExistsException message) { 
			logger.error("AdresseLieuRdv Dao log : Impossible d ajouter cette AdresseLieurdv dans la Bdd.");
			throw new AdresseExistanteException("AdresseLieuRdvDao Exception : Probleme, cette AdresseLieuRdv a l air d'être deja persistee");
			
		}
	}
	
	 public void modifieUneAdresseLieuRdv(AdresseLieuRdv adresseLieuRdv) throws AdresseInexistanteException {
		 
			logger.info("AdresseLieuRdv Dao log : Demande de modification de l AdresseLieuRdv id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " a la Bdd.");
			AdresseLieuRdv adresseBdd = em.find(AdresseLieuRdv.class, adresseLieuRdv.getIdAdresseLieuRdv());		
			if ( Objects.nonNull(adresseBdd) ) {
				em.merge(adresseLieuRdv);			
				logger.info("AdresseLieuRdv Dao log : Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " a ete modifie dans la Bdd.");
			} else {
				logger.error("AdresseLieuRdv Dao log : Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " ne peut etre modifie dans la Bdd.");
				throw new AdresseInexistanteException("AdresseLieuRdv Dao log : Modification impossible," 				
						+ "il n'y a pas d adresse à modifier pour l'id : " 
						+ adresseLieuRdv.getIdAdresseLieuRdv() + " demande.");
			}			
	}
	 
	 public void reinitUneAdresseLieuRdv(AdresseLieuRdv adresseLieuRdv) throws AdresseInexistanteException {
		 
			logger.info("AdresseLieuRdv Dao log : Demande de reinitialisation de l AdresseLieuRdv id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " a la Bdd.");
			AdresseLieuRdv adresseBdd = em.find(AdresseLieuRdv.class, adresseLieuRdv.getIdAdresseLieuRdv());		
			if ( Objects.nonNull(adresseBdd) ) {
				em.merge(adresseLieuRdv);			
				logger.info("AdresseLieuRdv Dao log : Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " a ete reinitialise dans la Bdd.");
			} else {
				logger.error("AdresseLieuRdv Dao log : AdresseLieuRdv id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " ne peut etre reinitialisee dans la Bdd.");
				throw new AdresseInexistanteException("AdresseLieuRdv Dao log : Reinitialisation impossible," 				
						+ "il n'y a pas d adresse à reinitialiser pour l'id : " 
						+ adresseLieuRdv.getIdAdresseLieuRdv() + " demande.");
			}			
	}
		
	
	public void supprimeUneAdresseLieuRdv(final Integer idAdresseLieuRdv) throws AdresseInexistanteException {
			
			
			logger.info("AdresseLieuRdv Dao log : Demande de suppression de l AdresselieuRdv id : " + idAdresseLieuRdv + " dans la Bdd.");
				AdresseLieuRdv adresseLieuRdv = null;
				adresseLieuRdv = em.find(AdresseLieuRdv.class, idAdresseLieuRdv);
				if (Objects.nonNull(adresseLieuRdv)) {
				em.remove(adresseLieuRdv);
				logger.info("AdresseLieuRdv Dao log : AdresseLieuRdv id : " + idAdresseLieuRdv + " a bien ete supprime de la Bdd.");
				} else {
				logger.error("AdresseLieuRdv Dao log : AdresseLieuRdv id : " + idAdresseLieuRdv + " inexistante alors elle ne peut etre supprimee de la Bdd.");
					throw new AdresseInexistanteException("AdresseLieuRdv Dao Exception : AdresseLieuRdv id : " + idAdresseLieuRdv + " ne peut etre supprimee de la Bdd.");
				}
		}	
	
	
	

}
