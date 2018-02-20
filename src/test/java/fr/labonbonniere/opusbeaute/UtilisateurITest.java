package fr.labonbonniere.opusbeaute;

import static org.junit.Assert.*;

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
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import fr.labonbonniere.opusbeaute.middleware.dao.UtilisateurDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.client.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
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
//@RunWith(Arquillian.class)
public class UtilisateurITest {
	
	static final Logger logger = LogManager.getLogger(UtilisateurITest.class.getSimpleName());


    /**
     * Utilise le Gestionnaire de Persistance dedie au tests
     * cf: persistence.xml
     */
	@PersistenceContext(unitName = "opusBeautePUTest")
	private EntityManager em;

	/**
	 * Injection comme EJB des Dao et Service
	 */
	@Inject 
	private UtilisateurDao utilisateurDao;
	
	@Inject
	private UtilisateurService utilisateurService;
		
	/**
	 * Creation d une archive Utilisateur dediee dedie au test.
	 * 
	 * @return ShrinkWrap
	 */
//    @Deployment
    public static WebArchive createDeployment() {
    	 
    	logger.info("Package : " + Utilisateur.class.getPackage().getName().trim());
    	WebArchive jarchive = ShrinkWrap
				        		.create(WebArchive.class, "UtilisateurTest.war")
				        		.addPackage(Utilisateur.class.getPackage())
				        		.addPackage(UtilisateurDao.class.getPackage())
				        		.addPackage(UtilisateurService.class.getPackage())
				        		.addClasses(Utilisateur.class, UtilisateurDao.class, UtilisateurService.class)
				        		.addAsResource("META-INF/persistence.xml", "test-persistence.xml")
//				        		.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    							// Enable CDI
				        		.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    	
    	logger.info("Utilisateurtest log : jarchive : " + jarchive.toString().trim());
        
        return jarchive;
        
	}
	
    /**
     * Creation d un entite Utilisateur
     */
	
//	@Test
	public void valideNouvelUtilisateurTest() throws UtilisateurExistantException, 
					EmailFormatInvalidException, NbCharNomException, NbCharPrenomException, NbCharTelException {
		logger.info("UtilisateurTest log : Creation entite:");
		Utilisateur nouvelUtilisateurTest = new Utilisateur();
		
		nouvelUtilisateurTest.setIdUtilisateur(1);
		nouvelUtilisateurTest.setPrenomUtilisateur("FredTesT");
		nouvelUtilisateurTest.setNomUtilisateur("BEAUDEAUTest");
		nouvelUtilisateurTest.setMotDePasse("bonjour");
		nouvelUtilisateurTest.setAdresseMailUtilisateur("Test@tesT.cOm");
		nouvelUtilisateurTest.setSuscribedMailReminder("t");
		nouvelUtilisateurTest.setSuscribedSmsReminder("t");
		nouvelUtilisateurTest.setTeleMobileUtilisateur("0620790050");
		logger.info("UtilisateurTest log : Objet Utilisateur avant Formatage Utilisateur");
		logger.info("UtilisateurTest log : Entite : " + nouvelUtilisateurTest.toString().trim());
		
		// assert avant Formatage
		assertNotNull("L utilisateur ne peut etre null", nouvelUtilisateurTest.getIdUtilisateur());
		assertEquals("Le format du Prenom ne correspond pas a ce que l on attend !", "FredTesT", 
				nouvelUtilisateurTest.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "BEAUDEAUTest", 
				nouvelUtilisateurTest.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "Test@tesT.cOm", 
				nouvelUtilisateurTest.getAdresseMailUtilisateur());
		assertEquals("La souscription au mailReminder ne correspond pas a ce que l on attend !", "t", 
				nouvelUtilisateurTest.getSuscribedMailReminder());
		assertEquals("La souscription au smsReminder ne correspond pas a ce que l on attend !", "t", 
				nouvelUtilisateurTest.getSuscribedSmsReminder());
		assertEquals("Le numero de telephone Portable ne correspond pas a ce que l on attend !", "0620790050", 
				nouvelUtilisateurTest.getTeleMobileUtilisateur());
		
		logger.info("UtilisateurTest log : Formatage Utilisateur");
		Utilisateur nouvUtilFormat = utilisateurService.validationformat(nouvelUtilisateurTest);
		
		// assert apres formatage
		assertNotNull("L utilisateur ne peut etre null", nouvelUtilisateurTest.getIdUtilisateur());
		assertEquals("Le format du Prenom ne correspond pas a ce que l on attend !", "Fredtest", 
				nouvelUtilisateurTest.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "BEAUDEAUTEST", 
				nouvelUtilisateurTest.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "test@test.com", 
				nouvelUtilisateurTest.getAdresseMailUtilisateur());
		assertEquals("La souscription au mailReminder ne correspond pas a ce que l on attend !", "T", 
				nouvelUtilisateurTest.getSuscribedMailReminder());
		assertEquals("La souscription au smsReminder ne correspond pas a ce que l on attend !", "T", 
				nouvelUtilisateurTest.getSuscribedSmsReminder());
		assertEquals("Le numero de telephone Portable ne correspond pas a ce que l on attend !", "0620790050", 
				nouvelUtilisateurTest.getTeleMobileUtilisateur());
			
		
		logger.info("UtilisateurTest log : Validation des donnees a ete realisee comme prevue");

		
	}
	
	
	/**
	 * Persistance de l entite cree
	 * @param nouvelUtilisateurTest
	 */
//	@Test
	public void persisteUtilisateurTest(Utilisateur nouvUtilFormat) {
		
		logger.info("UtilisateurTest log : Tentative de persistance Entite: " + nouvUtilFormat.toString().trim());
		
		em.persist(nouvUtilFormat);
		
		logger.info("UtilisateurTest log : Entite Persistee :)");
		
	}
	
	/**
	 * Recuperation de la liste des utilisateurs persistes
	 */
//	@Test
	public void listUtilisateursBdd() {
		
		logger.info("UtilisateurTest log : Consultation de la bdd : liste utilisateurs : " );
		
		String requete = "SELECT U FROM Utilisateur U" + " ORDER BY idUtilisateur asc";
		TypedQuery<Utilisateur> query = em.createQuery(requete, Utilisateur.class);
		List<Utilisateur> listeUtilisateurs = query.getResultList();
		
		assertNotNull("La liste recuperer ne doit pas etre null", listeUtilisateurs);
		logger.info("UtilisateurTest log : liste recuperee : " + listeUtilisateurs.toString().length());
		
	}
	
	/**
	 * Retrouve l entite qui a etee persistee
	 * @throws UtilisateurInexistantException 
	 * 
	 */
//	@Test
	public void retrouveUtilisateurTestParMail() throws UtilisateurInexistantException {
		
		String email = "test@test.com";
		
		logger.info("UtilisateurTest log : Consultation de la bdd : Retrouve via son son Email un utilisateur persiste :" );

		Utilisateur utilRetrouveParMail = utilisateurDao.utilisateurParEmail(email);
		
		logger.info("UtilisateurTest log : Consultation de la bdd : Retrouve via son son Email un utilisateur persiste :" );
		
		// assert apres formatage
		assertNotNull("L utilisateur ne peut etre null", utilRetrouveParMail.getIdUtilisateur());
		assertEquals("Le format du Prenom ne correspond pas a ce que l on attend !", "Fredtest", 
				utilRetrouveParMail.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "BEAUDEAUTEST", 
				utilRetrouveParMail.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "test@test.com", 
				utilRetrouveParMail.getAdresseMailUtilisateur());
		assertEquals("La souscription au mailReminder ne correspond pas a ce que l on attend !", "T", 
				utilRetrouveParMail.getSuscribedMailReminder());
		assertEquals("La souscription au smsReminder ne correspond pas a ce que l on attend !", "T", 
				utilRetrouveParMail.getSuscribedSmsReminder());
		assertEquals("Le numero de telephone Portable ne correspond pas a ce que l on attend !", "0620790050", 
				utilRetrouveParMail.getTeleMobileUtilisateur());
		
		logger.info("UtilisateurTest log : Consultation de la bdd : utilisateur retrouve :" );
		logger.info("UtilisateurTest log : Consultation de la bdd : " + utilRetrouveParMail);
	
	}

}
