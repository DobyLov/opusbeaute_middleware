package fr.labonbonniere.opusbeaute.middleware.webservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.renewpwd.RenewPwdUtilisateurService;

/**
 * WebService Rest changement d emot de passe
 * 
 * @author fred
 *
 */
@Stateless
@Path("/renewpwd/")
@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class RenewPwdWs {

	private static final Logger logger = LogManager.getLogger(RenewPwdWs.class.getSimpleName());

	@EJB
	RenewPwdUtilisateurService renewpwdutilisateurservice;
	
	/**
	 * Verifie le format de l adresse Email
	 * 
	 * 
	 * @param adresseMailUtilisateur String
	 * @return renewPwdUtilisateur Response 
	 * @throws UtilisateurInexistantException Exception
	 * @throws DaoException Exception
	 * 
	 */
	@POST
	@Path("/{adresseMailUtilisateur}")
	@Produces(MediaType.TEXT_HTML)
	public Response renewPwdUtilisateur(@PathParam("adresseMailUtilisateur") final String adresseMailUtilisateur ) 
			throws UtilisateurInexistantException, DaoException {


		logger.info("RenewPwdUtilisateurWs log : Demande de nouveau mot d epasse");
		Response.ResponseBuilder builder = null;
		
		// verifie le format de l em mail avant de consulter le service
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(adresseMailUtilisateur);

		Boolean matches = matcher.matches();
		
		// bollean si la procedure foncitonne
		Boolean pwdchanged = false;
		
		if (matches != true) {
			logger.info("RenewPwdUtilisateurWs log : Le format email n est pas correcte :(");
			builder = Response.status(400);					
		}
		

		try {
			
			pwdchanged = renewpwdutilisateurservice.changePwd(adresseMailUtilisateur);
			
		} catch (UtilisateurInexistantException message) {
			builder = Response.notModified();
			//throw new UtilisateurInexistantException("RenewPwdUtilisateurWs log : La procedure n a pas fonctionnee.");
			}
		
		
		if (pwdchanged != false) {
			logger.info("RenewPwdUtilisateurWs log : Format adresse valide " + adresseMailUtilisateur);
			builder = Response.ok();
		}
		
		if (pwdchanged != true) {
			logger.info("RenewPwdUtilisateurWs log : Echec de la procedure d envoi de mot de passe par Email "
					+ adresseMailUtilisateur);
			builder = Response.notModified();

		}

		return builder.build();

	}	
	
	
	
}
