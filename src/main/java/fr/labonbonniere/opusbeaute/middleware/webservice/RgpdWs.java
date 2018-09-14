package fr.labonbonniere.opusbeaute.middleware.webservice;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
//import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInvalideException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rgpd.Rgpd;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
//import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
//import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
//import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
//import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
//import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
//import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
//import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
//import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTsAniversaireException;
//import fr.labonbonniere.opusbeaute.middleware.service.genre.GenreClientNullException;
//import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.rgpd.RgpdException;
import fr.labonbonniere.opusbeaute.middleware.service.rgpd.RgpdService;

/**
 * Web Service REST
 * Gere la modification des informations
 * RGPD du Client par le Client lui meme
 * @author fred
 *
 */

@Stateless
@Path("/rgpd")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
//@DefineUserRole({"RGPDCLIENT"})
public class RgpdWs {
	
	private static final Logger logger = LogManager.getLogger(RgpdWs.class.getSimpleName());

	
	@EJB
	private RgpdService rgpdservice;
	
	/**
	 * Demmande la creation d un nouveau
	 * Token pour modifier les otions Rgpd
	 * 
	 * @param rgpdIdClient Integer
	 * @param rgpdPrenomClient String
	 * @param rgpdEmailClient String
	 * @return response Response
	 * @throws Exception Exception
	 */
	@SecuApp
	@DefineUserRole({"ANONYMOUS"})
	@GET
	@Path("/askvalidtoken")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_FORM_URLENCODED)
	public Response askForANewRgpdClientToken(@QueryParam("rgpdIdClient") final Integer rgpdIdClient,
										@QueryParam("rgpdPrenomClient") String rgpdPrenomClient,
										@QueryParam("rgpdEmailClient") final String rgpdEmailClient) throws Exception {
		
		logger.info("RgpdWs log : Demande une nouvelle URL avec token pour modif Rgpd settings.");
		Response.ResponseBuilder builder = null;
		
		String decode = URLDecoder.decode(rgpdPrenomClient, "UTF-8");
		logger.info("Rgpd log : String codee : " + rgpdPrenomClient);
		logger.info("Rgpd log : String decodee : " + decode);
		
		
		if (this.checkIfIntgerIsNotMoreBiggerThan4Digit(rgpdIdClient) == true) {
			
			builder = Response.status(400); // 400 bad Request
		}
		
		if (this.checkIfMailFormatIsCorrect(rgpdEmailClient) != true) {
		
			builder = Response.status(400); // 400 bad Request
		};
		
		if (this.checkIfStringIsNotMoreBiggerThan50Digits(rgpdEmailClient) != true) {
			
			builder = Response.status(400); // 400 bad Request
		}
		
		if (this.checkIfStringIsNotMoreBiggerThan50Digits(rgpdPrenomClient) != true) {
			
			builder = Response.status(400); // 400 bad Request
		}
		
		try {
			
			rgpdservice.rgpdClientAskForANewValidToken(rgpdIdClient, rgpdEmailClient, rgpdPrenomClient);
			builder = Response.status(200);
			logger.info("RgpdWs log : le mail avec l URL et le token sont envoyes");
			
		} catch (RgpdException message){
			logger.error("RgpdWs Exception : la procedure de demande de mail avec URL et Token a echouee");
			builder = Response.status(400);
			
		} catch (ClientInexistantException message) {
			logger.error("RgpdWs Exception : la procedure de demande de mail avec URL et Token a echouee");
			builder = Response.status(400);
		}
		
		// envoyer les infos au RgpdService
											
		
		return builder.build();		
		
	}
	
	/**
	 * Recupere les informations rgpd
	 * 	
	 * @param rgpdEmailClient String
	 * @return rgpd Rgpd
	 * @throws RgpdException Exception
	 * @throws Exception Exception
	 */
	@SecuApp
	@DefineUserRole({"RGPDCLIENT"})
	@GET	
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getRgpdClientSettings(@QueryParam("rgpdEmailClient") final String rgpdEmailClient) throws RgpdException, Exception {
//		check email format
//		Retourne Rgdpsetting objet
		logger.info("RgpdWs log : Demande le Rgpd settings de lutilisateur");
		Response.ResponseBuilder builder = null;
		
		if (!this.checkIfMailFormatIsCorrect(rgpdEmailClient)){
			logger.info("RgpdWs log : il y a un probleme avec le format de l email du client");
			builder = Response.status(400);			
			throw new RgpdException("RgpdWs log: Il y a un probleme avec le format de l email du Client");
		}
		
		try {
			
			Rgpd rgpd = rgpdservice.getRgpdClientSettings(rgpdEmailClient);
			logger.info("RgpdWs log : Les reglages Rgpd du client sont recuperes");
			builder = Response.ok(rgpd);
			
		} catch (RgpdException message) {			
			logger.error("RgpdWs Exception : il y a un probleme avec le format de l email du client");
			builder = Response.status(400);
		}
		
		return builder.build();
		
	}
	
	/**
	 * Met a jour les parametre Rgpd (Object Rgpd)
	 * 
	 * @param rgpd Rgpd
	 * @return Response
	 * @throws Exception  Exception
	 */
	@SecuApp
	@DefineUserRole({"RGPDCLIENT"})
	@PUT
	@Path("/updatesettings/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRgpdClientSettings(Rgpd rgpd) throws Exception {
		logger.info("RgpdWs log : Modifications des Reglages Rgpd");
		logger.info("RgpdWs log : Modifications des Reglages Rgpd" 
				+ rgpd.toString().trim());
		Response.ResponseBuilder builder = null;
		
		try {
			
			rgpdservice.setRgpdClientSettings(rgpd);
			logger.info("RgpdWs log : Les nouveaux reglages sont sauvegardes");
			builder = Response.ok(rgpd);
		} catch (RgpdException message) {
			logger.error("RgpdWs log : il y a eu un probleme lors de la sauvegrade des Rgpd Settings");
			builder = Response.serverError();
			throw new RgpdException("RgpdWs Exception : Probleme de modifications des RGPD settings");
		}
		
		return builder.build();
		
	}
	
	/**
	 * Verifier le format de l email
	 * 
	 * @param email String
	 * @return Boolean
	 * @throws Exception Exception
	 */
	private Boolean checkIfMailFormatIsCorrect(String email) throws Exception {
		
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);

		Boolean matches = matcher.matches();
		
		if (matches != true) {
			logger.info("RgpdWs log : Le format email n est pas correcte :( : " + email);
			throw new RgpdException("RgpdWs Exception : Le format email n est pas correcte :( : " + email);
		}

		return matches;
		
	}
	
	/**
	 * Verifie que l Integer
	 * ne depasse pas le 4 digits
	 * 
	 * @param rgpdIdClient Integer
	 * @return Boolean
	 */
	private Boolean checkIfIntgerIsNotMoreBiggerThan4Digit(Integer rgpdIdClient) {
		
		if (rgpdIdClient.toString().length() > 4) {			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 *  Verifie que la chaine de caracteres
	 *  ne depasse pas 30 characteres
	 * 
	 * @param rgpdPrenomClient String
	 * @return Boolean
	 */
	private Boolean checkIfStringIsNotMoreBiggerThan50Digits(String rgpdPrenomClient) {
		
		if (rgpdPrenomClient.length() > 50 ) {
			return true;
		} else {
			return false;
		}
		
	}
}
