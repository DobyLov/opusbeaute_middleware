package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvInexistantException;


@Stateless
public class RdvService {
	static final Logger logger = LogManager.getLogger(RdvService.class);

	@EJB
	private RdvDao rdvdao;
	
	
	public List<Rdv> recupereListeRdv() throws DaoException {		
		
		try {	
			
			logger.info("RdvService log : Demande au Dao la liste des Rdv's");
			List<Rdv> lalisterdv = rdvdao.obtenirListeRdv();
			logger.info("RdvService - Liste des Rdv's recuperee");			
			return lalisterdv;			
		
		} catch ( DaoException message ) {			
			logger.error("RdvService log : Probleme de la bdd.");
			throw new DaoException("RdvService Exception : Probleme de la bdd.");	
		}				
	}
	
	
	public Rdv recupererUnRdv(final Integer idRdv) throws RdvInexistantException {
		
		try {			
			logger.info("RdvService log : Demande a la bdd le Rdv id : " + idRdv);
			Rdv rdv = rdvdao.obtenirRdv(idRdv);
			logger.info("RdvService log : Rdv id : " + idRdv + " trouve, envoie de l objet Rdv a RdvWS");
			return rdv;
			
		} catch (RdvInexistantException message) {
			logger.error("RdvService log : Le rdv demande est introuvable");
			throw new RdvInexistantException("RdvService Exception : l' Id est introuvable dans la base");
		}
	}
	
	
	public List<Rdv> recupereListeRdvParDate(final String listeRdvDateDuJour) throws DaoException{
		
		try {
			logger.info("Rdvservice log : Demande au Dao la liste des Rdv's par date selectionnee.");
			List<Rdv> lalisterdvpardate = rdvdao.obtenirListeRdvParDate(listeRdvDateDuJour);
			logger.info("Rdvservice log : Envoie a RdvWs de la liste des Rdv's par date selectionnee.");
			return lalisterdvpardate;
			
		} catch (DaoException message) {			
			logger.error("Rdvservice Exception : probleme sur le format de la date.");
			throw new DaoException("RdvService Exception : probleme sur le format de la date.");
		}
	}
			
		
	public List<Rdv> recupereListeRdvViaPlageDate(String RdvPlageJourA, String RdvPlageJourB)  throws DaoException {
		
		try {
			logger.info("RdvService log : Demande au RdvDao la liste des Rdv's par plage de dates selectionnees.");
			List<Rdv> lalisterdvplagedate = rdvdao.obtenirListeRdvViaPlageDate(RdvPlageJourA, RdvPlageJourB);
			logger.info("RdvService log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			return lalisterdvplagedate;
			
		} catch (Exception message) {
			logger.error("RdvWs log : probleme sur le format de la/des date(s).");
			throw new DaoException("RdvWs Exception : probleme sur le format de la/des date(s).");
		}		
	}
	
	
	public void ajoutRdv(Rdv rdv) throws RdvExistantException {
		
		try {
			logger.info("RdvService log : Demande d ajout d un nouveau Rdv dans la Bdd.");
			rdvdao.ajouterUnRdv(rdv);
			logger.info("RdvService log : Nouveau Rdv ajoute, avec l id : " + rdv.getIdRdv());
			
		} catch (RdvExistantException message) {
			logger.error("RdvService log : Impossible de creer ce rdv dans la Bdd.");
			throw new RdvExistantException("RdvService Exception : Impossible de creer ce rdv dans la Bdd.");
		}		
	}
	
	
	 public void  modifduRdv(Rdv rdv) throws RdvInexistantException {
		 
		 try {
			logger.info("RdvService log : Demande de modification du Rdv id : " + rdv.getIdRdv() + " dans la Bdd.");
			rdvdao.modifieUnRdv(rdv);
			logger.info("RdvService log : Rdv id : " + rdv.getIdRdv() + " a ete modifie dans la Bdd.");
			
		 } catch (RdvInexistantException message) {
			logger.error("RdvService log : Rdv id : " + rdv.getIdRdv() + " ne peut etre modifie dans la Bdd.");
			throw new RdvInexistantException("RdvService Exception : Rdv avec l Id : " + rdv.getIdRdv() + " ne peut etre modifie.");
		 }				
	}
	
	 
	public void suppressiondunrdv (final Integer idRdv) throws RdvInexistantException {
		
		try {
			logger.info("RdvService log : Demande de suppression du Rdv id : " + idRdv + " dans la Bdd.");
			rdvdao.supprimeunRdv(idRdv);
			logger.info("RdvService log : Rdv id : " + idRdv + " a bien ete supprime de la Bdd.");	
			
		} catch (RdvInexistantException message) {
			logger.error("RdvService log : Rdv id : " + idRdv + " ne peut etre supprime dans la Bdd.");	
			throw new RdvInexistantException("RdvService Exception : Rdv id : " + idRdv + " ne peut etre supprime dans la Bdd.");
		}
	}
	
	
}


