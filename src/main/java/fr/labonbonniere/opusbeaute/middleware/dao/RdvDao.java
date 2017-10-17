package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;



//import org.hibernate.query.Query;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;

@Stateless
@Transactional

public class RdvDao {
	

	@PersistenceContext(unitName="dobyPUtest")
	private EntityManager em;
	


	public List<Rdv> obtenirListeRdv() {
		
		String requete = "SELECT c FROM Rdv c";
		TypedQuery<Rdv> query = em.createQuery(requete, Rdv.class);
		query.setMaxResults(100); // Evite le cout JPA :|
		List<Rdv> rdv = query.getResultList();
		return rdv;
		
	}
	
	public List<Rdv> obtenirListeRdvParDate(String listeRdvDateDuJour) {
		// la variable de la date doit etre sous la forme yyyy-mm-dd
		// ajout en dur de l heure de debut et fin dans la requete.
		String requete =  "SELECT * FROM t_rdv"
						+ " WHERE rdv_dhdebut >= '" + listeRdvDateDuJour + " 00:00:00'"
						+ " AND rdv_dhdebut <= '" + listeRdvDateDuJour + " 23:59:00'"
						+ " ORDER BY rdv_dhdebut";
		Query query = em.createNativeQuery(requete,Rdv.class);
		List<Rdv> rdv = query.getResultList();
		
		return rdv;
		
	}
	
	public List<Rdv> obtenirListeRdvViaPlageDate(String RdvPlageJourA, String RdvPlageJourB) {

		String requete =  "SELECT * FROM t_rdv"
						+ " WHERE rdv_dhdebut >= '" + RdvPlageJourA + " 00:00:00'"
						+ " AND rdv_dhdebut <= '" + RdvPlageJourB + " 23:59:00'"
						+ " ORDER BY rdv_dhdebut";
		Query query = em.createNativeQuery(requete,Rdv.class);
		List<Rdv> rdv = query.getResultList();
		
		return rdv;
		
	}
	
	
	// ajoute un rdv tout neuf
	public void ajouterUnRdv(Rdv rdv) {
		
		em.persist(rdv);
		
	}
	
	// Modifie un rdv
	 public void modifieUnRdv (Rdv rdv) {

		em.merge(rdv);
		
	}
	
	// Modification d un rdv
	public void supprimeunRdv (final Integer idRdv ) {
		
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


