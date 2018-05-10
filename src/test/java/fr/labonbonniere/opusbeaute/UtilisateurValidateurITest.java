package fr.labonbonniere.opusbeaute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.core.api.annotation.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.labonbonniere.opusbeaute.middleware.dao.UtilisateurDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurExistantException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;


@Stateless
//@Transactional
@RunWith(Arquillian.class)
public class UtilisateurValidateurITest {

	static final Logger logger = LogManager.getLogger(UtilisateurValidateurITest.class.getSimpleName());
	
	@Inject
	private UtilisateurService utilisateurService;

  @Deployment
  public static Archive<?> createDeployment() {
  	 
  	return ShrinkWrap.create(JavaArchive.class, "UtilisateurUTest.war")
				        .addPackage(Utilisateur.class.getPackage())
				        .addPackage(UtilisateurService.class.getPackage())
				        .addClasses(Utilisateur.class, UtilisateurDao.class, UtilisateurService.class)
				        .addAsResource(EmptyAsset.INSTANCE, "beans.xml");
      
	}
  
	@Test
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
		assertNotNull("L utilisateur ne peut etre null", nouvUtilFormat.getIdUtilisateur());
		assertEquals("Le format du Prenom ne correspond pas a ce que l on attend !", "Fredtest", 
				nouvUtilFormat.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "BEAUDEAUTEST", 
				nouvUtilFormat.getNomUtilisateur());
		assertEquals("Le format du Nom ne correspond pas a ce que l on attend !", "test@test.com", 
				nouvUtilFormat.getAdresseMailUtilisateur());
		assertEquals("La souscription au mailReminder ne correspond pas a ce que l on attend !", "T", 
				nouvUtilFormat.getSuscribedMailReminder());
		assertEquals("La souscription au smsReminder ne correspond pas a ce que l on attend !", "T", 
				nouvUtilFormat.getSuscribedSmsReminder());
		assertEquals("Le numero de telephone Portable ne correspond pas a ce que l on attend !", "0620790050", 
				nouvUtilFormat.getTeleMobileUtilisateur());
			
		
		logger.info("UtilisateurTest log : Validation des donnees a ete realisee comme prevue");

		
	}
  
  

}
