package fr.labonbonniere.opusbeaute.middleware.webservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.PraticienInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailNotFoundException;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;

/**
 * WebService REST Verifie si lemail 
 * n est pas deja utilise dans la base 
 * @author fred
 *
 */
@Stateless
@Path("/testmailutilisateur")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class CheckMailNotUsedUtilisateurWS {
	
	static final Logger logger = LogManager.getLogger(CheckMailNotUsedUtilisateurWS.class.getName());

	@EJB
	UtilisateurService utilisateurservice;
	
	Response.ResponseBuilder builder = null;
	
	/**
	 * Verifie que le mail n est pas utilise
	 * dans la table Utilisateur
	 * 
	 * @param email String
	 * @return Response 
	 * @throws ClientInexistantException 
	 * @throws PraticienInexistantException 
	 * @throws UtilisateurInexistantException 
	 * @throws MailNotFoundException 
	 * @throws DaoException 
	 * @throws Exception Exception
	 */
    @GET
    @Path("/{email}")
//    @Produces(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response verifyMailIsNotUsed(@PathParam("email") final String email) throws MailNotFoundException, DaoException  {
    	
    	
    	try {
    		
            logger.info("CheckMailNotUsedUtilisateurWS log : recherche de l email " + email + " dans la Bdd") ;
            
    		// verifie le format de l em mail avant de consulter le service
    		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    		Pattern pattern = Pattern.compile(regex);
    		Matcher matcher = pattern.matcher(email);

    		Boolean matches = matcher.matches();
    		
    		// verifie la strucutre email pattern, null, vide, inferieur a 40 caracteres
    		if (matches != true) {
    			logger.info("CheckMailNotUsedUtilisateurWS log : Le format email n est pas correcte :(");
    			throw new EmailFormatInvalidException("CheckMailNotUsedUtilisateurWS Exception : le format Email invalide");				
    		}
    		
            if (email.isEmpty() || email == null) {
            	logger.info("CheckMailNotUsedUtilisateurWS log : L email est vide ou null ");
            	throw new EmailFormatInvalidException("CheckMailNotUsedUtilisateurWS Exception : le mail ne peut etre null");	
            	
            } 
            
            if (email.length() > 40) {
            	logger.info("CheckMailNotUsedUtilisateurWS log : L email est vide ou null ");
            	throw new EmailFormatInvalidException("CheckMailNotUsedUtilisateurWS Exception : le format Email invalide");
            
            }	

            // verifie si le mail existe dans la table Utilisateur
            logger.info("CheckMailNotUsedUtilisateurWS log : Recherche du mail dans la table Utilisateur ");
            if (utilisateurservice.verifieSiAdresseMailFournieExisteDansUtilisateur(email.toLowerCase()) == true) {
            	logger.info("CheckMailNotUsedUtilisateurWS log : Le mail " + email + " existe dans la table Utilisateur ");
            	builder = Response.ok("true");
            	return builder.build();
            	
    		} else {
    			
            	logger.info("CheckMailNotUsedUtilisateurWS log : Le mail " + email 
            			+ " n est pas trouve dans la table : Utilisateur ");
            	builder = Response.ok("false");
            	return builder.build();
    		}


        } catch (EmailFormatInvalidException message) {
        	logger.info("CheckMailNotUsedUtilisateurWS Exception : Format Email invalide");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            
        } catch (MailNotFoundException message) {
        	logger.info("CheckMailNotUsedUtilisateurWS log : l email fourni " + email + " n est pas present danc la Table Utilisateur");
        	return Response.ok("false").build();
        } catch (Exception message) {
        	logger.info("CheckMailNotUsedUtilisateurWS log : l email fourni " + email + " n est pas present danc la Table Utilisateur");
        	return Response.ok("false").build();
        }
    	
    }
}

