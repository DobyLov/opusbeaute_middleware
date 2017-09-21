package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import fr.labonbonniere.opusbeaute.middleware.dao.RdvDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

@Stateless
public class RdvService {

	@EJB
	private RdvDao rdvdao;
	
	public Rdv recupererUnRdv(final Integer idRdv) {

		Rdv rdv = rdvdao.obtenirRdv(idRdv); 
		return rdv;
	}
	
	public List<Rdv> recupereListeRdv() {
		
		List<Rdv> lalisterdv = rdvdao.obtenirListeRdv();
		return lalisterdv;
	}
	
	public void ajoutRdv(Rdv rdv) {
		
		rdvdao.ajouterUnRdv(rdv);
		
	}
	
	public void  modifduRdv(Rdv rdv) {
		
		rdvdao.modifieUnRdv(rdv);
				
	}
	
	public void suppressiondunrdv (final Integer idRdv) {
		
		rdvdao.supprimeunRdv(idRdv);
	}
		
	
}


//	public static Rdv recupererUnRdv (Integer idRdv,String nom, String prenom) 
//			throws RdvInexistantException,RdvInvalideException {
//				
//		if (idRdv == null) {
//			throw new RdvInvalideException("L'Id du Rdv ne peut etre null");
//
//		}
//		try {
//			Rdv rdv = rdvdao.obtenirRdv(idRdv);
//			return rdv;
//		}
//		catch (EntityExistsException e){
//			throw new RdvInvalideException("probleme de bdd");
//		}
//
//			
//	 }

