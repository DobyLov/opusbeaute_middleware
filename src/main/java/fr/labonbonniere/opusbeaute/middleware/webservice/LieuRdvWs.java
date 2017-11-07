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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvInexistantException;

import fr.labonbonniere.opusbeaute.middleware.service.LieuRdvService;

@Stateless
@Path("/lieurdv")
public class LieuRdvWs {
	private static final Logger logger = LogManager.getLogger(LieuRdvWs.class);

	@EJB
	private LieuRdvService lieurdvservice;

	@GET
	@Path("/listelieurdv")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeLieuRdv() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("LieuRdvWs log : Demande au Service la liste des LieuRdv");
			final List<LieuRdv> listeLieuRdv = lieurdvservice.recupereListeLieuRdv();
			logger.info("LieuRdvWs log : Transmission du contenu(liste LieuRdv).");
			builder = Response.ok(listeLieuRdv);

		} catch (DaoException message) {
			logger.error("AdresseWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	@GET
	@Path("/{idLieuRdv: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(@PathParam("idLieuRdv") final Integer idLieuRdv) throws LieuRdvInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("LieuRdvWs log - Demande a la bdd le LieuRdv id :  " + idLieuRdv);
			LieuRdv lieurdv = lieurdvservice.recupererUnLieuRdv(idLieuRdv);
			logger.info("LieuRdvWs log - LieuRdv demande " + lieurdv.getIdLieuRdv() + " transmi");
			builder = Response.ok(lieurdv);

		} catch (LieuRdvInexistantException message) {
			logger.error("LieuRdvWs log - LieuRdv id : " + idLieuRdv + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	@POST
	@Path("/addlieurdv")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnLieuRdv(LieuRdv lieurdv) throws LieuRdvExistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("LieuRdvWs log : Demande d ajout d un nouveau LieuRdv dans la Bdd.");
			lieurdvservice.ajoutLieuRdv(lieurdv);
			logger.info("LieuRdvWs log : nouveau LieuRdv ajoute, avec l id : " + lieurdv.getIdLieuRdv());
			builder = Response.ok(lieurdv);

		} catch (LieuRdvExistantException message) {
			logger.error("LieuRdvWs log : Impossible de creer ceLieuRdv dans la Bdd.");
			throw new LieuRdvExistantException("LieuRdvWs Exception : Impossible de creer ce LieuRdv dans la Bdd.");
		}
		return builder.build();
	}

	@PUT
	@Path("/modiflieurdv")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnLieuRdv(LieuRdv lieurdv) throws LieuRdvInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("LieuRdvWs log : Demande de modification du LieuRdv id : " + lieurdv.getIdLieuRdv()
					+ " dans la Bdd.");
			lieurdvservice.modifDeLLieuRdv(lieurdv);
			logger.info("LieuRdvWs log : LieuRdv id : " + lieurdv.getIdLieuRdv() + " a bien ete modifie dans la Bdd.");
			String msg = "LieuRdv id : " + lieurdv.getIdLieuRdv() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);

		} catch (LieuRdvInexistantException message) {
			logger.error(
					"LieuRdvWs log : LieuRdv id : " + lieurdv.getIdLieuRdv() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();

		}

		return builder.build();

	}

	@DELETE
	@Path("/remove/{idLieuRdv: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheAdresse(@PathParam("idLieuRdv") final Integer idLieuRdv)
			throws LieuRdvInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("LieuRdvWs log : Demande de suppression du LieuRdv id : " + idLieuRdv + " dans la Bdd.");
			lieurdvservice.suppressionddUnLieuRdv(idLieuRdv);
			logger.info("LieuRdvWs log : LieuRdv id : " + idLieuRdv + " a bien ete modifie dans la Bdd.");
			String msg = "LieuRdvWs log : l LieuRdv id : " + idLieuRdv + " a ien ete supprimee de la Bdd.";
			builder = Response.ok(msg);

		} catch (LieuRdvInexistantException message) {
			logger.error("LieuRdvWs log : LieuRdv id : " + idLieuRdv + " ne peut etre supprime dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

}
