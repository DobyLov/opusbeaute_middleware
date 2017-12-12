package fr.labonbonniere.opusbeaute.middleware.webservice;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	
	@POST
	@Path("/sendcli/{adresseemail}/{corpsemail}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerEMailReminderRdvClient (@PathParam("adresseemail") final String adresseemail,
												@PathParam("corpsemail") final String corpsemail) {

		Response.ResponseBuilder builder = null;
		
		logger.info("EmailReminderClientWs Exception : Demande envoi Email Adresse : " + adresseemail);
		logger.info("EmailReminderClientWs Exception : Demande envoi Email Corps : " + corpsemail);
		try {	
			
			sendmailreminderclient.envoyerUnEMail(adresseemail, corpsemail);
			logger.info("EmailReminderClientWs log : Envoi email ok.");
			builder = Response.ok();
			
		} catch (Exception message) {
			
			logger.error("EmailReminderClientWs Exception : Echec Envoi email.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		
		}
		
		return builder.build();
	}
	
	@GET
	@Path("/sendmailtestautomatic")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerEMailTestAutomatique() {
		
		Response.ResponseBuilder builder = null;
		logger.info("EmailReminderClientWs log : Demande envoi Email Test Automatique : ");
		
		try {
			
			sendmailreminderclient.envoyerUnEmailTestAutomatique();
			logger.info("EmailReminderClientWs log : Envoi Email Test  ok.");
			builder = Response.ok();
			
		} catch(Exception message) {
			
			logger.error("EmailReminderClientWs Exception : Echec Envoi email test automatique.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		
		}
		return builder.build();
	}
}
