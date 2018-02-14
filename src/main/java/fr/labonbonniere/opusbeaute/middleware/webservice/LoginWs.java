package fr.labonbonniere.opusbeaute.middleware.webservice;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.LoginService;

/**
 * WebService REST LoginWs
 * Gere l acces a la ressource Login
 *	
 * @author fred
 *
 */
@Stateless
@Path("/login")
@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class LoginWs {
	
	static final Logger logger = LogManager.getLogger(LoginWs.class.getName());
	
	
	@EJB
	LoginService loginservice;
	

	/**
	 * Verifie les credentiels fournipar l utilisateur 
	 * pour acceder aux ressource 
	 * 
	 * @param email String
	 * @param pwd String
	 * @return Response
	 * @throws Exception Exception
	 */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginVerificationCredentiels(@FormParam("email") String email, 
    										@FormParam("pwd") String pwd) throws Exception {
    

    	try {
    		
            if (email.isEmpty() || email == null) {
            	logger.info("LoginWs Exception : L email est vide ou null ");
            	throw new Exception();
            	
            } if (email.length() > 40) {
            	logger.info("LoginWs Exception : L email est vide ou null ");
            	throw new Exception();
            	
            } if (pwd.isEmpty() || pwd.length() > 40 ) {
            	logger.info("LoginWs Exception : Le mot de passe est vide ou depasse 30");
            	throw new Exception();
            }
    		
        	logger.info("LoginWs log : Tentative d authentification ");
        	// procedure de generation du token
        	String token = loginservice.tokenGenAtLogin(email, pwd);
            
        	// Return the token on the response
        	logger.info("LoginWs log : Authentification Ok du login : " + email);
            return Response.ok(token).build();

        } catch (Exception message) {
        	logger.info("LoginWs Exception : Authentification Nok du login : " + email);
            return Response.status(Response.Status.UNAUTHORIZED).build();
            
        }      
    }
}


