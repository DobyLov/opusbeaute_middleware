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
	
	public List<Rdv> recupereListeRdvParDate(final String listeRdvDateDuJour) {
		
		List<Rdv> lalisterdvpardate = rdvdao.obtenirListeRdvParDate(listeRdvDateDuJour);
		return lalisterdvpardate;
		
	};
	
	public List<Rdv> recupereListeRdvViaPlageDate(String RdvPlageJourA, String RdvPlageJourB) {
		
		List<Rdv> lalisterdvplagedate = rdvdao.obtenirListeRdvViaPlageDate(RdvPlageJourA, RdvPlageJourB);
		return lalisterdvplagedate;
		
	};
	
	
	public void ajoutRdv(Rdv rdv) {
		
		rdvdao.ajouterUnRdv(rdv);
		
	}
	
	 public void  modifduRdv(Rdv rdv) {
	//public Rdv  modifduRdv(Integer idRdv, Rdv rdv) { // modif du 22/09
		rdvdao.modifieUnRdv(rdv);
		//return rdv; // modif du 22/09 il n y a vait pas de return
				
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

