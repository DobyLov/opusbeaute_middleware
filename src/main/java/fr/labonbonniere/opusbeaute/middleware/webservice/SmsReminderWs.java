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

import fr.labonbonniere.opusbeaute.middleware.service.sms.SendSmsReminderClientService;
import fr.labonbonniere.opusbeaute.middleware.service.sms.SendSmsReminderPraticienService;


@Stateless
@Path("/sms")
public class SmsReminderWs {

	private static final Logger logger = LogManager.getLogger(SmsReminderWs.class);
	
	@EJB
	private SendSmsReminderClientService sendsmsreminderclientservice;
	
	@EJB
	private SendSmsReminderPraticienService sendsmsreminderpraticienservice;
	
	@GET
	@Path("/sendclients")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerUnSmsRappelRdvClient() throws Exception {
		
		logger.info("SmsReminderClientWs Exception : Demande envoi Sms");
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("SMSReminderClientWs Exception : Tentative d envoie du Sms.)");	
			sendsmsreminderclientservice.sendSmsClientScheduled();
			logger.info("SMSReminderClientWs log : Sms envoye :)");
			builder = Response.ok();

		
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("SMSReminderClientWs Exception : Echec envoi Sms");
			builder = Response.status(Response.Status.BAD_REQUEST);
		}
		
		return builder.build();
	}

	@GET
	@Path("/sendpraticiens")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerUnSmsRappelRdvPraticiens() throws Exception {
		
		logger.info("SmsReminderClientWs Exception : Demande envoi Sms");
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("SMSReminderClientWs Exception : Tentative d envoie du Sms.)");	
			sendsmsreminderpraticienservice.sendSmsPraticiensScheduled();
			logger.info("SMSReminderClientWs log : Sms envoye :)");
			builder = Response.ok();

		
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("SMSReminderClientWs Exception : Echec envoi Sms");
			builder = Response.status(Response.Status.BAD_REQUEST);
		}
		
		return builder.build();
	}
	
}
