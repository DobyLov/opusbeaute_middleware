package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

@Stateless
@Transactional

public class RdvDao {
	

	@PersistenceContext(unitName="dobyPUtest")
	private EntityManager em;
	
	// ne fonctionne po :(
	// Censé renvoyer la liste des rdv's
//	@SuppressWarnings("unchecked")
	public List<Rdv> obtenirListeRdv() {
		String requete = "SELECT c FROM Rdv c";
		TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
		List<Rdv> rdv = query.getResultList();
		return rdv;
		
	}
	
	// ajoute un rdv tout neuf
	public void ajouterUnRdv(Rdv rdv) {
		
		em.persist(rdv);
		
	}
	
	// Modifie un rdv
	public void modifieUnRdv (Rdv rdv) {
		
		// Rdv rdv = em.getReference(Rdv.class, idRdv);
		em.merge(rdv);
		
	}
	
	public void supprimeunRdv (final Integer idRdv ) {
		// Rdv rdv = null;
		Rdv rdv = em.getReference(Rdv.class, idRdv);
		if (rdv!= null){
		em.remove(rdv);
		}
	}
	
	
	// renvoie le Rdv via L id
	public Rdv obtenirRdv(final Integer idRdv) {
		
		Rdv rdv = null;
		rdv = em.find(Rdv.class, idRdv);
		
		return rdv;
	}
	
}


