package fr.labonbonniere.opusbeaute.middleware.webservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.ChangePwdService;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;


/**
 * Changer de mot de passe
 * a la demande de l utilisateur
 * l utilisateur fourni s on Email
 * son ancien mot de passe
 * et deux fois le nouveau
 * 
 * @author fred
 *
 */
@SecuApp
@Stateless
@Path("/changepwd")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class ChangePwdWs {

	static final Logger logger = LogManager.getLogger(ChangePwdWs.class.getSimpleName());
	
	@EJB
	ChangePwdService changepwdservice;
	
	/**
	 * 
	 * 
	 * @param email String
	 * @param oldPwd String
	 * @param newPwdA String
	 * @param newPwdB String 
	 * @return Response 200(Ok) ou 304(not modified)
	 * @throws Exception Exception
	 */
	@POST
	@Path("/{email},{oldPwd},{newPwdA},{newPwdB}")
    public Response renouvellementPwd(	@PathParam("email") final String email,
		    							@PathParam("oldPwd") final String oldPwd,
		    							@PathParam("newPwdA") final String newPwdA,
		    							@PathParam("newPwdB") final String newPwdB) throws Exception {	


		Response.ResponseBuilder builder = null;	
		
		// verifie le format de l em mail avant de consulter le service
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);

		Boolean matches = matcher.matches();
		
		
		if (matches != true) {
			logger.error("LoginWs Exception : log : Le format email n est pas correcte :(");
			throw new Exception();				
		}
    		
            if (email.isEmpty() || email == null) {
            	logger.error("ChangePwdWs Exception : L email est vide ou null ");
//            	throw new Exception("ChangePwdWs Exception : L email est vide ou null");
              	builder = Response.notModified();
            	
            } if (email.length() > 40) {
            	logger.error("ChangePwdWs log : L email depasse 40 caracteres ");
//            	throw new Exception("ChangePwdWs Exception : l email depasse 40 caracteres");
              	builder = Response.notModified();
            	
            } if (oldPwd.isEmpty() || oldPwd == null || oldPwd.length() < 4 ) {
            	logger.info("ChangePwdWs log :L ancien mot de passe est vide ou est inferieur a 7 caracteres");
//            	throw new Exception("ChangePwdWs Exception : l ancien Pwd est soit vide soit inferieur a 7 caracteres");
              	builder = Response.notModified();
            
	        } if (newPwdA.isEmpty() || newPwdA == null || newPwdA.length() < 7 ) {
	        	logger.error("ChangePwdWs log :Le nouveau mot de passe est vide ou est inferieur a 7 caracteres");
//	        	throw new Exception("ChangePwdWs Exception : l ancien Pwd est soit vide soit inferieur a 7 caracteres");
	          	builder = Response.notModified();
	        
	        } if (newPwdB.isEmpty() || newPwdB == null || newPwdA.length() < 7 ) {
	        	logger.error("ChangePwdWs log : Le nouveau mot de passe est vide ou est inferieur a 7 caracteres");
//	        	throw new Exception("ChangePwdWs Exception : l ancien Pwd est soit vide soit inferieur a 7 caracteres");
	          	builder = Response.notModified();
	        
	        } if ( newPwdA != newPwdB ) {
	        	logger.error("ChangePwdWs log : Le nouveau mot de passe est vide ou est inferieur a 7 caracteres");
//	        	throw new Exception("ChangePwdWs Exception : l ancien Pwd est soit vide soit inferieur a 7 caracteres");
	          	builder = Response.notModified();
	        }
	        
	        logger.info("ChangePwdWs log : Avant le Try");
	     try {	
        	
	    	logger.info("ChangePwdWs log : recours au service ");
        	changepwdservice.changeOldPwd(email, oldPwd, newPwdA, newPwdB);
        	logger.info("ChangePwdWs log : Le mot de passe a ete change");
            builder = Response.ok();

        } catch (Exception message) {
        	logger.info("ChangePwdWs Exception : il y a un probleme avec soit l email, l ancien ou le nouveau mot de passe : ");
        	builder = Response.notModified();
            
        } 
		
		
		return builder.build();
		
		
		
	}

}
