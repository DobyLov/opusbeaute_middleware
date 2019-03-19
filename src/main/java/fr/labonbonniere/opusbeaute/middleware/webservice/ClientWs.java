package fr.labonbonniere.opusbeaute.middleware.webservice;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInvalideException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.client.ClientService;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTsAniversaireException;
import fr.labonbonniere.opusbeaute.middleware.service.client.PhoneMobileNotStartWith0607Exception;
import fr.labonbonniere.opusbeaute.middleware.service.client.SuscribeMailReminderException;
import fr.labonbonniere.opusbeaute.middleware.service.client.SuscribedNewsLetterException;
import fr.labonbonniere.opusbeaute.middleware.service.client.SuscribedSmsReminderException;
import fr.labonbonniere.opusbeaute.middleware.service.genre.GenreClientNullException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailNotFoundException;
import fr.labonbonniere.opusbeaute.middleware.service.rgpd.RgpdException;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.MailExistantException;

/**
 * Gere le WebService Rest Client
 * 
 * @author fred
 *
 */
@SecuApp
@Stateless
@Path("/client")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class ClientWs {

	private static final Logger logger = LogManager.getLogger(ClientWs.class.getName());

	@EJB
	private ClientService clientservice;

	/**
	 * Retourne la Liste de Client
	 * 
	 * @return Response
	 * @throws DaoException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeClient() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("ClientWs log : Demande au Service la liste des Clients");
			final List<Client> listeClient = clientservice.recupereListeClient();
			logger.info("ClientWs log : Transmission du contenu(liste Client).");
			builder = Response.ok(listeClient);

		} catch (DaoException message) {
			logger.error("ClientWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}
	/**
	 * Retourne un Client via son id
	 * 
	 * @param idClient Integer
	 * @return Response
	 * @throws ClientInexistantException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/{idClient: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theClient(@PathParam("idClient") final Integer idClient) throws ClientInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ClientWs log - Demande a la bdd le Client id :  " + idClient);
			Client client = clientservice.recupererUnClientById(idClient);
			logger.info("ClientWs log - Client demande " + client.getIdClient() + " transmis");
			builder = Response.ok(client);

		} catch (ClientInexistantException message) {
			logger.error("ClientWs log - le Client id : " + idClient + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}
	
	/**
	 * 
	 * @param adresseMailClient String
	 * @return Client client
	 * @throws ClientInexistantException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response theClientByMail(@QueryParam("adresseMailClient") String adresseMailClient) throws ClientInexistantException {
		
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("ClientWs log - Demande a la bdd le Client via Email :  " + adresseMailClient);
			Client client = clientservice.recupererUnClientParEmail(adresseMailClient);
			logger.info("ClientWs log - Client email : " + client.getAdresseMailClient() + " recupere");
			builder = Response.ok(client);
			
		} catch (ClientInexistantException message) {
			logger.error("ClientWs log - Client email : " + adresseMailClient + " non inexistant");
			throw new ClientInexistantException("ClientWs Exception - Client email : " + adresseMailClient + " non inexistant");
			
		}
		
		return builder.build();
	}

	/**
	 * Ajoute un Client 
	 *  
	 * @param client Client
	 * @return Response
	 * @throws NbNumZipcodeException Exception
	 * @throws NbNumRueException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharNomException Exception
	 * @throws NbCharTsAniversaireException Exception
	 * @throws NbCharPaysException Exception
	 * @throws NbCharTelException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws DaoException Exception
	 * @throws GenreInvalideException Exception
	 * @throws GenreClientNullException Exception
	 * @throws PhoneMobileNotStartWith0607Exception Exception
	 * @throws SuscribeMailReminderException Exception
	 * @throws SuscribedNewsLetterException Exception
	 * @throws SuscribedSmsReminderException Exception
	 * @throws MailExistantException 
	 * @throws MailNotFoundException 
	 * @throws URISyntaxException 
	 * @throws RgpdException 
	 * @throws ClientInexistantException 
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnClient(Client client)
			throws NbNumZipcodeException, NbNumRueException, NbCharRueVilleException, NbCharPrenomException,
			NbCharNomException, NbCharTsAniversaireException, NbCharPaysException, NbCharTelException, EmailFormatInvalidException,
			DaoException, GenreInvalideException, GenreClientNullException, PhoneMobileNotStartWith0607Exception, SuscribeMailReminderException, SuscribedNewsLetterException, SuscribedSmsReminderException, MailNotFoundException, MailExistantException, UnsupportedEncodingException, MalformedURLException, ClientInexistantException, RgpdException, URISyntaxException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ClientWs log : Demande d ajout d un nouveau Client dans la Bdd.");
			clientservice.ajoutClient(client);
			logger.info("ClientWs log : Nouveau Client ajoute, avec l id : " + client.getIdClient());
			builder = Response.ok(client);

		}  catch (EmailFormatInvalidException message) {
			logger.error("ClientWs log : Verifiez Client.Email.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharTelException message) {
			logger.error("ClientWs log : Verifiez Client.Telephone.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharTsAniversaireException message) {
			logger.error("ClientWs log : Verifiez Client.DateAnniversaire.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharPrenomException message) {
			logger.error("ClientWs log : Verifiez Client.Prenom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharNomException message) {
			logger.error("ClientWs log : Verifiez Client.Nom.");
			builder = Response.status(Response.Status.BAD_REQUEST);
			
		} catch (NbNumRueException message) {
			logger.error("ClientWs log : Verifiez Adresse.NumeroRue.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharPaysException message) {
			logger.error("ClientWs log : Verifiez Adresse.Pays.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbNumZipcodeException message) {
			logger.error("ClientWs log : Verifiez Adresse.Zipcode.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharRueVilleException message) {
			logger.error("ClientWs log : Verifiez Adresse.Ville.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (DaoException message) {
			logger.error("ClientWs log : Impossible de creer ce Client dans la Bdd le client est deja existant.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (GenreInvalideException message) {
			logger.error("ClientWs log : il y aun probleme avec l Id Genre, ne match pac dans la table Genre.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (GenreClientNullException message) {
			logger.error("ClientWs log : il y aun probleme avec l Id Genredeclare dans le client.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}  

		return builder.build();
	}
	/**
	 * Modifie un Client
	 * 
	 * @param client Client
	 * @return Response
	 * @throws ClientInexistantException Exception
	 * @throws AdresseInexistanteException Exception
	 * @throws NbNumRueException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharNomException Exception
	 * @throws NbCharTsAniversaireException Exception
	 * @throws NbCharTelException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbNumZipcodeException Exception
	 * @throws NbCharPaysException Exception
	 * @throws GenreInvalideException Exception
	 * @throws DaoException Exception
	 * @throws GenreClientNullException Exception
	 */
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnClient(Client client)
			throws ClientInexistantException, AdresseInexistanteException, NbNumRueException, NbCharPrenomException,
			NbCharNomException, NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException, NbCharRueVilleException,
			NbNumZipcodeException, NbCharPaysException, GenreInvalideException, DaoException, GenreClientNullException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info(
					"ClientWs log : Demande de modification du Client id : " + client.getIdClient() + " dans la Bdd.");
			clientservice.modifDuClient(client);
			logger.info("ClientWs log : Client id : " + client.getIdClient() + " a bien ete modifie dans la Bdd.");
			// String msg = "Client id : " + client.getIdClient() + " a bien ete
			// modifie dans la Bdd.";
			builder = Response.ok();

		} catch (NbNumRueException message) {
			logger.error("ClientWs log : Le num de rue dans l adresse depasee 3 caracteres.");
			builder = Response.notModified();

		} catch (EmailFormatInvalidException message) {
			logger.error("ClientWs log : L email ne respecte pas le format xyz@xyz.xyz.");
			builder = Response.notModified();

		} catch (NbCharTelException message) {
			logger.error("ClientWs log : Le Telephone du Client 10 caracteres.");
			builder = Response.notModified();

		} catch (NbCharTsAniversaireException message) {
			logger.error("ClientWs log : Le TimeStamp de la date Anniversaire du Client depasee 12 caracteres.");
			builder = Response.notModified();

		} catch (NbCharPrenomException message) {
			logger.error("ClientWs log : Le Prenom du Client depasee 30 caracteres.");
			builder = Response.notModified();

		} catch (NbCharNomException message) {
			logger.error("ClientWs log : Le Nom du Client depasee 30 caracteres.");
			builder = Response.notModified();

		} catch (NbCharPaysException message) {
			logger.error("ClientWs log : Le Pays dans  l adresse depasse 6 caracteres.");
			builder = Response.notModified();

		} catch (NbNumZipcodeException message) {
			logger.error("ClientWs log : Le Zipcode dans l adresse depasse 5 caracteres.");
			builder = Response.notModified();

		} catch (NbCharRueVilleException message) {
			logger.error("ClientWs log : Le nombre de caracteres ville OU rue dans l adresse est superieur a 30.");
			builder = Response.notModified();

		} catch (ClientInexistantException message) {
			logger.error(
					"ClientWs log : le Client id : " + client.getIdClient() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();

		}

		return builder.build();

	}

	/**
	 * Supprime un Client
	 * 
	 * @param idClient Integer
	 * @return Response
	 * @throws ClientInexistantException Exception
	 */
	@DefineUserRole({"PRATICIEN"})
	@DELETE
	@Path("/del/{idClient: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheClient(@PathParam("idClient") final Integer idClient) throws ClientInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ClientWs log : Demande de suppression de le Client id : " + idClient + " dans la Bdd.");
			clientservice.suppressionDUnClient(idClient);
			logger.info("ClientWs log : Client id : " + idClient + " a bien ete modifie dans la Bdd.");
			// String msg = "ClientWs log : Client id : " + idClient + " a ien
			// ete supprime de la Bdd.";
			builder = Response.ok();

		} catch (ClientInexistantException message) {
			logger.error("ClientWs log : Client id : " + idClient + " ne peut etre supprime dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

}
