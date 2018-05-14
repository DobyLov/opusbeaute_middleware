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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailNotFoundException;
import fr.labonbonniere.opusbeaute.middleware.service.praticien.PraticienService;

/**
 * WebService REST Verifie si le mail 
 * n est pas deja utilise dans la Table Praticien
 * 
 * @author fred
 *
 */
@SecuApp
@Stateless
@Path("/testmailpraticien")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class CheckMailNotUsedPraticienWS {
	
	static final Logger logger = LogManager.getLogger(CheckMailNotUsedPraticienWS.class.getName());
	
	@EJB
	PraticienService praticienservcie;
	
	Response.ResponseBuilder builder = null;
	
	/**
	 * Verifie que le mail n est pas present dans la Table Pratcien
	 * 
	 * @param email String
	 * @return Response 
	 * @throws MailNotFoundException Exception
	 * @throws DaoException Exception
	 */
    @GET
    @Path("/{email}")
//    @Produces(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response verifyMailIsNotUsed(@PathParam("email") final String email) throws MailNotFoundException, DaoException  {
    	
    	
    	try {
    		
            logger.info("CheckMailNotUsedPraticienWS log : recherche de l email " + email + " dans la Bdd") ;
            
    		// verifie le format de l em mail avant de consulter le service
    		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    		Pattern pattern = Pattern.compile(regex);
    		Matcher matcher = pattern.matcher(email);

    		Boolean matches = matcher.matches();
    		
    		// verifie la strucutre email pattern, null, vide, inferieur a 40 caracteres
    		if (matches != true) {
    			logger.info("CheckMailNotUsedPraticienWS log : Le format email n est pas correcte :(");
    			throw new EmailFormatInvalidException("CheckMailNotUsedPraticienWS Exception : le format Email invalide");				
    		}
    		
            if (email.isEmpty() || email == null) {
            	logger.info("CheckMailNotUsedPraticienWS log : L email est vide ou null ");
            	throw new EmailFormatInvalidException("CheckMailNotUsedPraticienWS Exception : le mail ne peut etre null");	
            	
            } 
            
            if (email.length() > 40) {
            	logger.info("CheckMailNotUsedPraticienWS log : L email est vide ou null ");
            	throw new EmailFormatInvalidException("CheckMailNotUsedPraticienWS Exception : le format Email invalide");
            
            }	

            // verifie si le mail existe dans la table Praticien
            logger.info("CheckMailNotUsedPraticienWS log : Recherche du mail dans la table Praticien ");
            if (praticienservcie.verifieSiAdresseMailFournieExisteDansPraticien(email.toLowerCase()) == true) {
            	logger.info("CheckMailNotUsedPraticienWS log : Le mail existe dans la table Praticien ");
            	builder = Response.ok("true");
            	return builder.build();
            	
            } else {
            	
            	logger.info("CheckMailNotUsedPraticienWS log : Le mail " + email 
            			+ " n est pas trouve dans la table : Praticien ");
            	builder = Response.ok("false");
            	return builder.build();
    		}


        } catch (EmailFormatInvalidException message) {
        	logger.info("CheckMailNotUsedWS Exception : Format Email invalide");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            
        } catch (MailNotFoundException message) {
        	logger.info("CheckMailNotUsedPraticienWS log : l email fourni " + email + " n est pas present danc la Table Praticien");
        	return Response.ok("false").build();
        	
        } catch (Exception message) {
        	logger.info("CheckMailNotUsedPraticienWS log : l email fourni " + email + " n est pas present danc la Table Praticien");
        	return Response.ok("false").build();
        }
    	
    }
}

