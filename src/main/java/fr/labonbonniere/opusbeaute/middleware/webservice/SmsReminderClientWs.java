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


@Stateless
@Path("/sms")
public class SmsReminderClientWs {

	private static final Logger logger = LogManager.getLogger(SmsReminderClientWs.class);
	
	@EJB
	private SendSmsReminderClientService sendSmsReminderClientservice;
	
	@GET
	@Path("/sendtest")
	@Produces(MediaType.APPLICATION_JSON)
	public Response envoyerUnSmsTest() throws Exception {
		
		logger.info("SmsReminderClientWs Exception : Demande envoi Sms");
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("SMSReminderClientWs Exception : Tentative d envoie du Sms.)");	
		sendSmsReminderClientservice.sendSmsClientTest();
		logger.info("SMSReminderClientWs Exception : Sms envoye :)");
		builder = Response.ok();

		
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("SMSReminderClientWs Exception : Echec envoi Sms");
			builder = Response.status(Response.Status.BAD_REQUEST);
		}
		
		return builder.build();
	}
	
	
}
