package fr.labonbonniere.opusbeaute;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import fr.labonbonniere.opusbeaute.middleware.dao.UtilisateurDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;

/**
 * Test Unitaire entite Utilisateur
 * 
 * Test de Creation d un Utilisateur
 * Test de listing des Utilisateurs Persistes
 * Test de Retrouve l Utilisateur persiste
 * Test de Suppression d un Utilisateur Persiste
 * Test de recherche d un Utilisateur supprime
 * 
 * Creation d un environement pour tester de facon unitaire
 * la creation d un utilisateur avec un environement restreint
 * mais proche de la realite.
 * 
 * Creation Utilisateur
 * 
 * @author fred
 *
 */
@Stateless
@Transactional
@RunWith(Arquillian.class)
public class UtilisateurTest {
	
	static final Logger logger = LogManager.getLogger(UtilisateurTest.class.getSimpleName());


    /**
     * Utilise le Gestionnaire de Persistance 
     */
	@PersistenceContext(unitName = "opusBeautePUTest")
	private EntityManager em;

	/**
	 * Creation d une archive Utilisateur dediee dedie au test.
	 * 
	 * @return ShrinkWrap
	 */
    @Deployment
//    public static WebArchive createDeployment() {
  public static Archive<?> createDeployment() {
    	
    	logger.info("Package : ");
//    	logger.info("Package : " + Utilisateur.class.getPackage().getName().trim());
    	WebArchive jarchive = ShrinkWrap
				        		.create(WebArchive.class, "UtilisateurTest.war")
//				        		.addPackage(UtilisateurTest.class.getPackage())
				        		.addClass(Utilisateur.class)
				        		.addClass(UtilisateurDao.class)
				        		.addClass(UtilisateurService.class)
				        		.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				        		.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    	
    	logger.info("Utilisateurtest log : jarchive : " + jarchive);
        
        return jarchive;
        
	}
	
    /**
     * Creation d un entite Utilisateur
     */
	@Test
	public void creationUtilisateurTest() {
		
		logger.info("UtilisateurTest log : Creation entite:");
		Utilisateur nouvelUtilisateurTest = new Utilisateur();
		nouvelUtilisateurTest.setIdUtilisateur(1);
		nouvelUtilisateurTest.setPrenomUtilisateur("FredTest");
		nouvelUtilisateurTest.setNomUtilisateur("BEAUDEAUTest");
//		nouvelUtilisateurTest.setRoles();
		nouvelUtilisateurTest.setMotDePasse("bonjour");
		nouvelUtilisateurTest.setAdresseMailUtilisateur("test@test.com");
		nouvelUtilisateurTest.setSuscribedMailReminder("T");
		nouvelUtilisateurTest.setSuscribedSmsReminder("T");
		nouvelUtilisateurTest.setTeleMobileUtilisateur("0620790050");
		logger.info("UtilisateurTest log : Entite : " + nouvelUtilisateurTest.toString().trim());

		persisteUtilisateurTest(nouvelUtilisateurTest);
	
	}
	
	/**
	 * Persistance de l entite cree
	 * @param nouvelUtilisateurTest
	 */
	public void persisteUtilisateurTest(Utilisateur nouvelUtilisateurTest) {
		
		logger.info("UtilisateurTest log : Tentative de persistance Entite: " + nouvelUtilisateurTest.toString().trim());
//		em.persist(nouvelUtilisateurTest);
		logger.info("UtilisateurTest log : Entite Persistee :)");
		
		
	}
	
	
	/**
	 * Recuperation de la liste des utilisateurs persistes
	 */
//	@Test
	public void listUtilisateursBdd() {
		
		String requete = "SELECT U FROM Utilisateur U" + " ORDER BY idUtilisateur asc";
		TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
		List<Utilisateur> listeUtilisateurs = query.getResultList();
		logger.info("liste utilisateurs : " + listeUtilisateurs.toString().length());
		
	}
	
	/**
	 * Retrouve l entite qui a etee persistee
	 * 
	 */
//	@Test
	public void retrouveUtilisateurTest() {
//		Integer idAttendu = 1;
		String prenomAttendu = "fredTest";
		String nomAttendu = "BEAUDEAUTest";
		String pwdAttendu = "bonjour";
		String mailAttendu = "test@test.com";
		
		try {
			
			logger.info("UtilisateurTest log : Tentative de recuperation de l entite");
//			Utilisateur utilisateurTest = em.find(Utilisateur, primaryKey)
//			logger.info("UtilisateurTest log : Entitee retrouvee" + utilisateurTest);
			
		} catch (Exception e) {
			logger.info("UtilisateurTest log : L entite n pas etee retrouvee :(");
		}
		
		
	/**
	 * Supprime l entite
	 */
		
		
	/**
	 * Cherche l entit supprimee
	 */
	
	}

}
