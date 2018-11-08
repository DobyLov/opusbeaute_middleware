package fr.labonbonniere.opusbeaute.middleware.webservice;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.mail.SendMailReminderClientService;
import fr.labonbonniere.opusbeaute.middleware.service.mail.SendMailReminderPraticienService;
import fr.labonbonniere.opusbeaute.middleware.service.rgpd.RgpdService;

/**
 * Gere l envoi d email de rappel 
 * au Client et Praticien
 * 
 * @author fred
 *
 */
@Stateless
@Path("/email")
@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class EmailReminderWs {

	private static final Logger logger = LogManager.getLogger(EmailReminderWs.class.getName());
	
	@EJB
	private SendMailReminderClientService sendmailreminderclient;
	
	@EJB
	private SendMailReminderPraticienService sendmailreminderpraticien;
	
	@EJB
	private RgpdService rgpdservice;
	
	/**
	 * Envoi d un Email rappel de Rdv
	 * aux Clients
	 * 
	 * @return Response
	 */
	@GET
	@Path("/sendclients")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerEMailClients() {
		
		Response.ResponseBuilder builder = null;
		logger.info("EmailReminderClientWs log : Debut de Procedure envoi Email Client via TriggerWS");
		
		try {
			
			sendmailreminderclient.envoyerEmailRappelRdvClientScheduled();;
			logger.info("EmailReminderClientWs log : Fin de procedure Envoi Email Cleint via TriggerWS terminee.");
			builder = Response.ok();
			
		} catch(Exception message) {
			
			logger.error("EmailReminderClientWs Exception : Echec de la procedure d envoi Email Client via TriggerWS.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		
		}
		return builder.build();
	}
	
	/**
	 * Envoi un email de Rappel de Rdv
	 * aux Praticiens
	 * 
	 * @return  response
	 */
	@GET
	@Path("/sendpraticiens")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerEMailPraticiens() {
		
		Response.ResponseBuilder builder = null;
		logger.info("EmailReminderClientWs log : Debut de la Procedure Envoi Email Praticien via TriggerWS");
		
		try {
			
			sendmailreminderpraticien.envoyerUnMailRecapRdvPraticienScheduled();
			logger.info("EmailReminderClientWs log : Fin de procedure d Envoi Email Praticien via TriggerWS terminee.");
			builder = Response.ok();
			
		} catch(Exception message) {
			
			logger.error("EmailReminderClientWs Exception : Echec de la procedure d Envoi Email praticien via TriggerWS.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		
		}
		return builder.build();
	}
	
	/**
	 * envoie d email avec une URL
	 * pour acceder au formulaire RGPD
	 * 
	 * @return Response
	 */
	@GET
	@Path("/sendclientsrgpd")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerEMailClientFormValidation() {
		
		Response.ResponseBuilder builder = null;
		logger.info("EmailReminderClientWs log : Debut de la Procedure Envoi Email Client RGPD Formulaire via TriggerWS");
		
		try {
			
			rgpdservice.rgpdClientFormValidationResponse();
			logger.info("EmailReminderClientWs log : Fin de procedure d Envoi Email Client RGPD Formulaire via TriggerWS terminee.");
			builder = Response.ok();
			
		} catch(Exception message) {
			
			logger.error("EmailReminderClientWs Exception : Echec de la procedure d Envoi Email Client RGPD via TriggerWS.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		
		}
		return builder.build();
	}
	
	
}
