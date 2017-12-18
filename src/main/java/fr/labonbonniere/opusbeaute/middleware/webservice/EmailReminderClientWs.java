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

import fr.labonbonniere.opusbeaute.middleware.service.mail.SendMailReminderClientService;

@Stateless
@Path("/email")
public class EmailReminderClientWs {

	private static final Logger logger = LogManager.getLogger(EmailReminderClientWs.class);
	
	@EJB
	private SendMailReminderClientService sendmailreminderclient;
	
	@GET
	@Path("/sendclients")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerEMailClients() {
		
		Response.ResponseBuilder builder = null;
		logger.info("EmailReminderClientWs log : Debut de Procedure envoi Email Client via TriggerWS");
		
		try {
			
			sendmailreminderclient.envoyerUnEmailRappelClientTriggerWs();
			logger.info("EmailReminderClientWs log : Fin de procedure Envoi Email Cleint via TriggerWS terminee.");
			builder = Response.ok();
			
		} catch(Exception message) {
			
			logger.error("EmailReminderClientWs Exception : Echec de la procedure d envoi Email Client via TriggerWS.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		
		}
		return builder.build();
	}
	
	@GET
	@Path("/sendpraticiens")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerEMailPraticiens() {
		
		Response.ResponseBuilder builder = null;
		logger.info("EmailReminderClientWs log : Debut de la Procedure Envoi Email Praticien via TriggerWS");
		
		try {
			
			sendmailreminderclient.envoyerUnMailRecapRdvPraticienTriggerWs();
			logger.info("EmailReminderClientWs log : Fin de procedure d Envoi Email Praticien via TriggerWS terminee.");
			builder = Response.ok();
			
		} catch(Exception message) {
			
			logger.error("EmailReminderClientWs Exception : Echec de la procedure d Envoi Email praticien via TriggerWS.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		
		}
		return builder.build();
	}
	
	
}
