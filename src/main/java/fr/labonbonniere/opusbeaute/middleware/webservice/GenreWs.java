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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.userRoles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.genre.GenreClientNullException;
import fr.labonbonniere.opusbeaute.middleware.service.genre.GenreService;

@Stateless
@Path("/genre")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class GenreWs {
	private static final Logger logger = LogManager.getLogger(GenreWs.class);

	@EJB
	private GenreService genreservice;

	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeGenre() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("GenreWs log : Demande au Service la liste des Genres");
			final List<Genre> listeGenre = genreservice.recupereListeGenre();
			logger.info("GenreWs log : Transmission du contenu(liste Genre).");
			builder = Response.ok(listeGenre);

		} catch (DaoException message) {
			logger.error("GenreWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/{idGenre: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theGenre(@PathParam("idGenre") final Integer idGenre) throws GenreInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("GenreWs log - Demande a la bdd le Client id :  " + idGenre);
			Genre genre = genreservice.recupererUnGenre(idGenre);
			logger.info("GenreWs log - Genre demande " + idGenre + " transmis");
			builder = Response.ok(genre);

		} catch (GenreInexistantException message) {
			logger.error("GenreWs log - le Genre id : " + idGenre + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnClient(Genre genre) throws DaoException, GenreClientNullException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("GenreWs log : Demande d ajout d un nouveau Genre dans la Bdd.");
			genreservice.ajoutGenre(genre);
			logger.info("GenreWs log : Nouveau Client ajoute, avec l id : " + genre.getIdGenre());
			builder = Response.ok(genre);

		} catch (DaoException message) {
			logger.error("GenreWs log : Impossible de creer ce Genre dans la Bdd.");
			throw new DaoException("GenreWs Exception : Impossible de creer ce Genre dans la Bdd.");

		} catch (GenreClientNullException message) {
			logger.error("GenreWs log : Verifiez Genre.Genrehum.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}
		return builder.build();
	}

	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnGenre(Genre genre) throws GenreInexistantException, GenreClientNullException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("GenreWs log : Demande de modification du Genre id : " + genre.getIdGenre() + " dans la Bdd.");
			genreservice.modifDeLGenre(genre);
			logger.info("GenreWs log : Genre id : " + genre.getIdGenre() + " a bien ete modifie dans la Bdd.");
			String msg = "Genre id : " + genre.getIdGenre() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);

		} catch (GenreInexistantException message) {
			logger.error("GenreWs log : le Genre id : " + genre.getIdGenre() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();

		} catch (GenreClientNullException message) {
			logger.error("GenreWs log : Verifiez Genre.Genrehum.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

	@DefineUserRole({"PRATICIEN"})
	@DELETE
	@Path("/del/{idGenre: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheClient(@PathParam("idGenre") final Integer idGenre) throws GenreInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("GenreWs log : Demande de suppression de le Genre id : " + idGenre + " dans la Bdd.");
			genreservice.suppressionddUnGenre(idGenre);
			logger.info("GenreWs log : Genre id : " + idGenre + " a bien ete modifie dans la Bdd.");
			builder = Response.ok();

		} catch (GenreInexistantException message) {
			logger.error("GenreWs log : Genre id : " + idGenre + " ne peut etre supprime dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

}
