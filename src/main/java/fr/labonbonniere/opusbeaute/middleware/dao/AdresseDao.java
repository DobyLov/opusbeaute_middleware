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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.Adresse;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseInexistanteException;

@Stateless
@Transactional
public class AdresseDao {
	static final Logger logger = LogManager.getLogger(PrestationDao.class);

	@PersistenceContext(unitName="dobyPUtest")
	private EntityManager em;
	
	
	public List<Adresse> obtenirListeAdresse() throws DaoException {
		
		
		try {
			logger.info("AdresseDao log : Demande a la bdd la liste des Adresses");
			String requete =  "SELECT A FROM Adresse A"
							+ " ORDER BY idAdresse asc";;
					
			TypedQuery<Adresse> query = em.createQuery(requete,Adresse.class);
			List<Adresse> listeAdresses = query.getResultList();
			
			logger.info("AdresseDao log :  Envoi de la liste des Adresses");
			return listeAdresses;
			
		} catch (Exception message) {
			logger.error("AdresseDAO Exception : Probleme de la bdd.");
			throw new DaoException("AdresseDao Exception : Probleme de la bdd.");
		}

		
	}
	
	public Adresse obtenirAdresse(final Integer idAdresse) throws AdresseInexistanteException {
		
		logger.info("AdresseDao log : Demande a la bdd l adresse id : " + idAdresse);
		Adresse adresse = null;
		adresse = em.find(Adresse.class, idAdresse);
		
		if (Objects.isNull(adresse)) {
			logger.error("AdresseDao log : L adresse id : " + idAdresse + " demande est introuvable");
			throw new AdresseInexistanteException ("AdresseDao Exception : L' Id : " + idAdresse+ " est introuvable dans la base");
		}
		
		logger.info("AdresseDao log : Adresse " + idAdresse + " trouve, envoie de l adresse a AdresseService");
		return adresse;			

	}
	
	public void ajouterUneAdresse (Adresse adresse)  throws AdresseExistanteException {
		
		try {
			logger.info("AdresseDao log : Demande d ajout d une nouvelle Adresse dans la Bdd.");
			em.persist(adresse);
			logger.info("AdresseDao log : Nouvelle Adresse ajoutee, avec l id : " + adresse.getIdAdresse());
		
		}catch (EntityExistsException message) { 
			logger.error("GenreDao log : Impossible d ajouter cette Adresse dans la Bdd.");
			throw new AdresseExistanteException("AdresseDao Exception : Probleme, cette Adresse a l air d'être deja persistee");
			
		}
	}
	
	 public void modifieUneAdresse(Adresse adresse) throws AdresseInexistanteException {
		 
			logger.info("AdresseDao log : Demande de modification de l adresse id : " + adresse.getIdAdresse() + " a la Bdd.");
			Adresse adresseBdd = em.find(Adresse.class, adresse.getIdAdresse());		
			if ( Objects.nonNull(adresseBdd) ) {
				em.merge(adresse);			
				logger.info("AdresseDao log : Adresse id : " + adresse.getIdAdresse() + " a ete modifie dans la Bdd.");
			} else {
				logger.error("AdresseDao log : Adresse id : " + adresse.getIdAdresse() + " ne peut etre modifie dans la Bdd.");
				throw new AdresseInexistanteException("AdresseDao log : Modification impossible," 				
						+ "il n'y a pas d adresse à modifier pour l'id : " 
						+ adresse.getIdAdresse() + " demande.");
			}			
	}
		
	
	public void supprimeUneAdresse(final Integer idAdresse) throws AdresseInexistanteException {
			
			
			logger.info("AdresseDao log : Demande de suppression de l Adresse id : " + idAdresse + " dans la Bdd.");
				Adresse adresse = null;
				adresse = em.find(Adresse.class, idAdresse);
				if (Objects.nonNull(adresse)) {
				em.remove(adresse);
				logger.info("AdresseDao log : Adresse id : " + idAdresse + " a bien ete supprime de la Bdd.");
				} else {
				logger.error("AdresseDao log : Adresse id : " + idAdresse + " inexistante alors elle ne peut etre supprimee de la Bdd.");
					throw new AdresseInexistanteException("AdresseDao Exception : Adresse id : " + idAdresse + " ne peut etre supprimee de la Bdd.");
				}
		}	
	
	
	

}
