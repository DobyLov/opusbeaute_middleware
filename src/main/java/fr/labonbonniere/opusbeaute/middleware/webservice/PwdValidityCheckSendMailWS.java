package fr.labonbonniere.opusbeaute.middleware.webservice;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.mail.SendMailExpirationPwd7DaysBeforeService;

@Stateless
@Path("/")
@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN"})
public class PwdValidityCheckSendMailWS {

	private static final Logger logger = LogManager.getLogger(PwdValidityCheckSendMailWS.class.getSimpleName());
	
	@EJB
	public SendMailExpirationPwd7DaysBeforeService sendmailexpirationpwd7daysservice;
	
	@GET
	@Path("/pwdexpiration")
//	@Produces(MediaType.APPLICATION_JSON)
	public Response checkPwdValitityWS() throws Exception {
		
		logger.info("PwdValidityCheckSendMailWS log : Procedure check Mail Validite");
		Response.ResponseBuilder builder = null;
		
		try {
			
			sendmailexpirationpwd7daysservice.envoyerMailUtilisateurPwdExpireDansXJours();		
			logger.info("SendMailExpirationPwd7DaysServicetWs log : CheckPwdValidity");
			
			builder = Response.status(200);
			
		} catch (Exception message) {
			builder = Response.noContent();
			logger.info("SendMailExpirationPwd7DaysServicetWs Exception : il y a un probleme avec la procedure CheckPwdValidity");
			throw new Exception("SendMailExpirationPwd7DaysServicetWs Exception : il y a eu un probleme "
					+ "lors de la procedure de check pwd validity.");
		}
		
		return builder.build();

		
	}
	
	
}
