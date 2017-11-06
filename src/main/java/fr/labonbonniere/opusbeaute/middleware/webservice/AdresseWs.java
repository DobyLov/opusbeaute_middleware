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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.Adresse;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.service.AdresseService;

@Stateless
@Path("/prestation")
public class AdresseWs {
	private static final Logger logger = LogManager.getLogger(AdresseWs.class);

	@EJB
	private AdresseService adresseservice;

	@GET
	@Path("/listeadresse")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListe() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseWs log : Demande au Service la liste des Prestations");
			final List<Adresse> listeadresse = adresseservice.recupereListeAdresse();
			logger.info("AdressenWs log : Transmission du contenu(liste Prestation).");
			builder = Response.ok(listeadresse);

		} catch (DaoException message) {
			logger.error("AdresseWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	@GET
	@Path("/{idAdresse: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(@PathParam("idAdresse") final Integer idAdresse) throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseWs log - Demande a la bdd le Adresse id :  " + idAdresse);
			Adresse adresse = adresseservice.recupererUneAdresse(idAdresse);
			logger.info("AdresseWs log - Adresse demande " + adresse.getIdAdresse() + " transmise");
			builder = Response.ok(adresse);

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseWs log - l Adresse id : " + idAdresse + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	@POST
	@Path("/addadresse")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUneadresse(Adresse adresse) throws AdresseExistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseWs log : Demande d ajout d une nouvelle Adresse dans la Bdd.");
			adresseservice.ajoutAdresse(adresse);
			logger.info("AdresseWs log : Nouvelle Adresse ajoutee, avec l id : " + adresse.getIdAdresse());
			builder = Response.ok(adresse);

		} catch (AdresseExistanteException message) {
			logger.error("AdresseWs log : Impossible de creer cette Adresse dans la Bdd.");
			throw new AdresseExistanteException(
					"PrestationWs Exception : Impossible de creer cette Prestation dans la Bdd.");
		}
		return builder.build();
	}

	@PUT
	@Path("/modifadresse")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUneadresse(Adresse adresse) throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseWs log : Demande de modification de l Adresse id : " + adresse.getIdAdresse()
					+ " dans la Bdd.");
			adresseservice.modifDeLAdresse(adresse);
			logger.info("AdresseWs log : Adresse id : " + adresse.getIdAdresse() + " a bien ete modifiee dans la Bdd.");
			String msg = "Adresse id : " + adresse.getIdAdresse() + " a bien ete modifiee dans la Bdd.";
			builder = Response.ok(msg);

		} catch (AdresseInexistanteException message) {
			logger.error(
					"AdresseWs log : l Adresse id : " + adresse.getIdAdresse() + " ne peut etre modifiee dans la Bdd.");
			builder = Response.notModified();

		}

		return builder.build();

	}

	@DELETE
	@Path("/remove/{idAdresse: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheAdresse(@PathParam("idAdresse") final Integer idAdresse)
			throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseWs log : Demande de suppression de l Adresse id : " + idAdresse + " dans la Bdd.");
			adresseservice.suppressionddUneAdresse(idAdresse);
			logger.info("AdresseWs log : Prestation id : " + idAdresse + " a bien ete modifie dans la Bdd.");
			String msg = "AdresseWs log : l adresse id : " + idAdresse + " a ien ete supprimee de la Bdd.";
			builder = Response.ok(msg);

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseWs log : l Adresse id : " + idAdresse + " ne peut etre supprimee dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}
}