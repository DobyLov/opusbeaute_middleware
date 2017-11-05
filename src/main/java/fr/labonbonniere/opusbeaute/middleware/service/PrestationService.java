package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.PrestationDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.Prestation;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationInexistanteException;


@Stateless
public class PrestationService {
	
	static final Logger logger = LogManager.getLogger(PrestationService.class);

	@EJB
	private PrestationDao prestationdao;
	
	public List<Prestation> recupereListePrestation() throws DaoException {		
			
			try {				
				logger.info("PrestationService log : Demande au Dao la liste des Prestations");
				List<Prestation> lalisteprestation = prestationdao.obtenirListePrestations();
				logger.info("PrestationService - Liste des Prestation's recuperee");			
				return lalisteprestation;			
			
			} catch ( DaoException message ) {			
				logger.error("PrestationService log : Probleme de la bdd.");
				throw new DaoException("PrestationService Exception : Probleme de la bdd.");	
				
			}				
		}
	
	
	public Prestation recupererUnePrestation(final Integer idPrestation) throws PrestationInexistanteException {
		
		try {			
			logger.info("PrestationService log : Demande a la bdd la Prestation id : " + idPrestation);
			Prestation prestation = prestationdao.obtenirPrestation(idPrestation);
			logger.info("PrestationService log : Prestation id : " + idPrestation + " trouve, envoie de l objet Prestation a RdvWS");
			return prestation;
			
		} catch (PrestationInexistanteException message) {
			logger.error("PrestationService log : La Prestation demande est introuvable");
			throw new PrestationInexistanteException("PrestationService Exception : l' Id est introuvable dans la base");
			
		}
	}
	
	
	public List<Prestation> recupereListePrestationHomme() throws DaoException {		
		
		try {				
			logger.info("PrestationService log : Demande au Dao la liste des Prestations Homme");
			List<Prestation> lalisteprestationH = prestationdao.obtenirListPrestationsHomme();
			logger.info("PrestationService - Liste des Prestation Homme's recuperee");			
			return lalisteprestationH;			
		
		} catch ( DaoException message ) {			
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");	
			
		}				
	}
	
	
	public List<Prestation> recupereListePrestationFemme() throws DaoException {		
			
		try {				
			logger.info("PrestationService log : Demande au Dao la liste des Prestations Homme");
			List<Prestation> lalisteprestationF = prestationdao.obtenirListPrestationsFemme();
			logger.info("PrestationService - Liste des Prestation Homme's recuperee");			
			return lalisteprestationF;			
		
		} catch ( DaoException message ) {			
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");	
			
		}				
	}
	
	
	public List<Prestation> recupereListePrestationCriteresGenreActivite(String genre, String activite) throws DaoException {		
		
		try {				
			logger.info("PrestationService log : Demande au Dao la liste des Prestations par genre et activite");
			List<Prestation> lalisteprestationGA = prestationdao.obtenirListPrestationsCriteresGenreActivite(genre, activite);
			logger.info("PrestationService - Liste des Prestation par genre et activite recuperee");			
			return lalisteprestationGA;			
		
		} catch ( DaoException message ) {			
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");	
			
		}				
	}
	
	
	public List<Prestation> recupereListePrestationCriteresGenreActiviteSoins(String genre, String activite, String soin) throws DaoException {		
		
		try {				
			logger.info("PrestationService log : Demande au Dao la liste des Prestations par genre,activite et soin");
			List<Prestation> lalisteprestationGAS = prestationdao.obtenirListPrestationsCriteresGenreActiviteSoins(genre, activite, soin);
			logger.info("PrestationService - Liste des Prestation par genre,activite et soin recuperee");			
			return lalisteprestationGAS;			
		
		} catch ( DaoException message ) {			
			logger.error("PrestationService log : Probleme de la bdd.");
			throw new DaoException("PrestationService Exception : Probleme de la bdd.");	
			
		}				
	}
	
	public void ajoutPrestation(Prestation prestation) throws PrestationExistanteException{
		
		try {
			logger.info("PrestationService log : Demande d ajout d une nouvelle Prestation dans la Bdd.");
			prestationdao.ajouterUnePrestation(prestation);
			logger.info("PrestationService log : Nouvelle Prestation ajoutee, avec l id : " + prestation.getIdPrestation());
			
		} catch (PrestationExistanteException message) {
			logger.error("RdvService log : Impossible de creer ce rdv dans la Bdd.");
			throw new PrestationExistanteException("RdvService Exception : Impossible de creer ce rdv dans la Bdd.");
		}		
	}
	
	
	 public void  modifierUnePrestation(Prestation prestation) throws PrestationInexistanteException{
		 
		 try {
			logger.info("PrestationService log : Demande de modification du Prestation id : " + prestation.getIdPrestation() + " dans la Bdd.");
			prestationdao.modifieUnePrestation(prestation);
			logger.info("PrestationService log : Prestation id : " + prestation.getIdPrestation() + " a ete modifie dans la Bdd.");
			
		 } catch (PrestationInexistanteException message) {
			logger.error("PrestationService log : Rdv id : " + prestation.getIdPrestation() + " ne peut etre modifie dans la Bdd.");
			throw new PrestationInexistanteException("RdvService Exception : Rdv avec l Id : " + prestation.getIdPrestation() + " ne peut etre modifie.");
		 }				
	}
	
	 
	public void suppressionUnePrestation (final Integer idPrestation) throws PrestationInexistanteException{
		
		try {
			logger.info("PrestationService log : Demande de suppression de la Prestation id : " + idPrestation + " dans la Bdd.");
			prestationdao.supprimeUnePrestation(idPrestation);
			logger.info("PrestationService log : Prestation id : " + idPrestation + " a bien ete supprime de la Bdd.");	
			
		} catch (PrestationInexistanteException message) {
			logger.error("PrestationService log : Prestation id : " + idPrestation + " ne peut etre supprime dans la Bdd.");	
			throw new PrestationInexistanteException("PrestationService Exception : Prestation id : " + idPrestation + " ne peut etre supprime dans la Bdd.");
		}
	}
}
