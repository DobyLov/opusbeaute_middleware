package fr.labonbonniere.opusbeaute.middleware.webservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.UnsuscribeMailReminderClientService;

@Stateless
@Path("/unsuscribe")
public class UnsuscribeRdvMailReminderClientWs {

	private static final Logger logger = LogManager.getLogger(UnsuscribeRdvMailReminderClientWs.class);

	@EJB
	private UnsuscribeMailReminderClientService unsuscribemailrdvreminderclientservice;

	@GET
	@Path("/rdvreminder/{adresseMailClient:}-{key:}")
//	@Path("/rdvreminder")
	@Produces(MediaType.TEXT_HTML)
	public Response envoyerUnSmsRappelRdvClient(@PathParam("adresseMailClient") final String adresseMailClient,
			@PathParam("key") final String key
			) throws ClientInexistantException, Exception {

//		String key = "M7AkuQu2hGHvzdYcDfxbPkcWaP9fe42G";
		logger.info("UnsuscribeRdvMailReminderClientWs log : Demande de desinscription au rappel de Rdv");
		Response.ResponseBuilder builder = null;

		String keyMustMatch = "M7AkuQu2hGHvzdYcDfxbPkcWaP9fe42G";
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(adresseMailClient);

		Boolean matches = matcher.matches();
		Boolean passeDeTaF = false;
		if (matches != true) {
			logger.info("UnsuscribeRdvMailReminderClientWs log : Le format email n est pas correcte :(");
			throw new Exception("UnsuscribeRdvMailReminderClientWs Exception : Le format email n est pas correcte :(");
		} if (key != keyMustMatch) {
			logger.info("UnsuscribeRdvMailReminderClientWs log : La pseudo clee de validation ne correspond pas :(");
			builder = Response.status(Response.Status.BAD_REQUEST);
			throw new Exception(
					"UnsuscribeRdvMailReminderClientWs Exception : La pseudo clee de validation ne correspond pas :(");
		}


			logger.info("UnsuscribeRdvMailReminderClientWs log : Tentative de modifier SuscribedMailRmeinder a False :).)");
			passeDeTaF = unsuscribemailrdvreminderclientservice.unscuscribeRdvMailReminderClient(adresseMailClient);
			
			logger.info("UnsuscribeMailReminderClientWs log : SuscribedMailRmeinder passe a False :)");
			logger.info("UnsuscribeMailReminderClientWs log : valeur de passeDeTaF : " + passeDeTaF);
			
			if ( passeDeTaF != false  ) {
			builder = Response.ok().entity("<html> " + "<title>" + "OpusBeaute_UnsuscribeRdvMail"
					+ "</title><body><b>Opus_Beauté vous informe que votre demande a bien étée enregistrée</b></body></html>");
			
			} if (passeDeTaF != true) {
				
				builder = Response.notModified().entity("<html> " + "<title>" + "OpusBeaute_UnsuscribeRdvMail"
						+ "</title><body><b>Opus_Beauté vous informe que votre demande n' pas été traitée</b></body></html>");
				
			}

			
		return builder.build();				


	}

}
