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
import fr.labonbonniere.opusbeaute.middleware.service.sms.SendSmsReminderClientService;
import fr.labonbonniere.opusbeaute.middleware.service.sms.SendSmsReminderPraticienService;

/**
 * WebService REST SMS
 * Gere l envoi de SMS de rappel de Rdv au Client
 * 
 * @author fred
 *
 */
@Stateless
@Path("/sms")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN"})
public class SmsReminderWs {

	private static final Logger logger = LogManager.getLogger(SmsReminderWs.class.getName());
	
	@EJB
	private SendSmsReminderClientService sendsmsreminderclientservice;
	
	@EJB
	private SendSmsReminderPraticienService sendsmsreminderpraticienservice;
	
	/**
	 * Envoie un Sms de rappel de Rdv au Client
	 * 
	 * @return Response
	 * @throws Exception Exception
	 */
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

	/**
	 * Envoi un Sms de rappel de Rdv au Praticien
	 * 
	 * @return Response
	 * @throws Exception Exception
	 */
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
