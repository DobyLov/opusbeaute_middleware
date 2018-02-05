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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.userRoles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.mail.UnsuscribeMailReminderClientService;

@Stateless
@Path("/unsuscribe")
@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class UnsuscribeRdvMailReminderClientWs {

	private static final Logger logger = LogManager.getLogger(UnsuscribeRdvMailReminderClientWs.class);

	@EJB
	private UnsuscribeMailReminderClientService unsuscribemailrdvreminderclientservice;

	@GET
	@Path("/rdvreminder/{adresseMailClient}/{key}")
	// @Path("/rdvreminder")
	@Produces(MediaType.TEXT_HTML)
	public Response envoyerUnSmsRappelRdvClient(@PathParam("adresseMailClient") final String adresseMailClient,
			@PathParam("key") final String key) throws ClientInexistantException, Exception {

		// String key = "M7AkuQu2hGHvzdYcDfxbPkcWaP9fe42G";
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
		}
		if (key.contentEquals(keyMustMatch) == false) {
			logger.info("UnsuscribeRdvMailReminderClientWs log : La clee de validation ne correspond pas :(");
			builder = Response.status(Response.Status.BAD_REQUEST);
			throw new Exception(
					"UnsuscribeRdvMailReminderClientWs Exception : La clee de validation ne correspond pas :(");
		}

		logger.info("UnsuscribeRdvMailReminderClientWs log : Tentative de modifier SuscribedMailRmeinder a False :).)");
		passeDeTaF = unsuscribemailrdvreminderclientservice.unscuscribeRdvMailReminderClient(adresseMailClient);

		logger.info("UnsuscribeMailReminderClientWs log : SuscribedMailRmeinder passe a False :)");
		logger.info("UnsuscribeMailReminderClientWs log : valeur de passeDeTaF : " + passeDeTaF);

		if (passeDeTaF != false) {
			logger.info("UnsuscribeMailReminderClientWs log : Desinscription de l adresse: " + adresseMailClient);

			String messageUnsusbcribe = "<p>&nbsp;</p>"
					+ "<p><span style=\"font-family: helvetica; font-size: medium;\">Votre demande de d&eacute;sinscritpion au rappel automatique est prise en compte.&nbsp;</span></p>"
					+ "<p><span style=\"font-family: helvetica;\"><span style=\"font-size: medium;\">Cordialement,</span></span></p>"
					+ "<p><span style=\"font-family: helvetica; font-size: medium;\">La_bonbonn&egrave;re_d'audrey</span></p>";

			builder = Response.ok().entity(messageUnsusbcribe);

		}
		if (passeDeTaF != true) {
			logger.info("UnsuscribeMailReminderClientWs log : Echec de la desinscription de l adresse: "
					+ adresseMailClient);
			builder = Response.notModified();

		}

		return builder.build();

	}

}
