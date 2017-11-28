package fr.labonbonniere.opusbeaute.middleware.webservice;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.ClientService;

@Stateless
@Path("/client")
public class ClientWs {

	private static final Logger logger = LogManager.getLogger(ClientWs.class);

	@EJB
	private ClientService clientservice;

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

	@GET
	@Path("/{idClient: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theClient(@PathParam("idClient") final Integer idClient) throws ClientInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ClientWs log - Demande a la bdd le Client id :  " + idClient);
			Client client = clientservice.recupererUnClient(idClient);
			logger.info("ClientWs log - Client demande " + client.getIdClient() + " transmis");
			builder = Response.ok(client);

		} catch (ClientInexistantException message) {
			logger.error("ClientWs log - le Client id : " + idClient + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnClient(Client client) throws ClientExistantException, AdresseExistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ClientWs log : Demande d ajout d un nouveau Client dans la Bdd.");
			clientservice.ajoutClient(client);
			logger.info("ClientWs log : Nouveau Client ajoute, avec l id : " + client.getIdClient());
			builder = Response.ok(client);

		} catch (ClientExistantException message) {
			logger.error("ClientWs log : Impossible de creer ce Client dans la Bdd.");
			throw new ClientExistantException("ClientWs Exception : Impossible de creer ce Client dans la Bdd.");
		}
		return builder.build();
	}

	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnClient(Client client) throws ClientInexistantException, AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info(
					"ClientWs log : Demande de modification du Client id : " + client.getIdClient() + " dans la Bdd.");
			clientservice.modifduClient(client);
			logger.info("ClientWs log : Client id : " + client.getIdClient() + " a bien ete modifie dans la Bdd.");
//			String msg = "Client id : " + client.getIdClient() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok();

		} catch (ClientInexistantException message) {
			logger.error(
					"ClientWs log : le Client id : " + client.getIdClient() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();

		}

		return builder.build();

	}

	@DELETE
	@Path("/del/{idClient: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheClient(@PathParam("idClient") final Integer idClient) throws ClientInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ClientWs log : Demande de suppression de le Client id : " + idClient + " dans la Bdd.");
			clientservice.suppressionddUnClient(idClient);
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
