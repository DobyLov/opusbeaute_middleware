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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.activite.Activite;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.activite.ActiviteInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.activite.ActiviteInvalideException;
import fr.labonbonniere.opusbeaute.middleware.service.activite.ActiviteService;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;

/**
 * WebService REST Activite
 * Gere les ressources Activite
 * 
 * @author fred
 *
 */
@SecuApp
@Stateless
@Path("/activite")
//@DefineUserRole({"ANONYMOUS"})
@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class ActiviteWs {
	
	static final Logger logger = LogManager.getLogger(ActiviteWs.class);
	
	@EJB
	private ActiviteService activiteservice;
	
	
	/**
	 * Recupere la liste des activites
	 * @return
	 * @throws DaoException
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeDesActivites() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("ActiviteWs log : Demande au Service la liste des Activites");
			final List<Activite> list = activiteservice.retourneLaListeActivite();
			logger.info("ActiviteWs log : Transmission du contenu(liste Activite).");
			builder = Response.ok(list);

		} catch (DaoException message) {
			logger.error("ActiviteWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}
	
	/**
	 * Recupere une activite par son id
	 * @param idActivite
	 * @return
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/{idActivite: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRole(@PathParam("idActivite") final Integer idActivite) {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ActiviteWs log - Demande a la bdd l activite id :  " + idActivite);
			Activite activite = activiteservice.retourneLActiviteDepuisLId(idActivite);
			logger.info("ActiviteWs log - Activite demande " + activite.getIdActivite() 
				+ " transmis");
			builder = Response.ok(activite);

		} catch (ActiviteInexistanteException message) {
			logger.error("ActiviteWs log - L activite id : " + idActivite + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}
	
	/**
	 * Ajouter une activite
	 * @param activite
	 * @return
	 * @throws ActiviteInexistanteException
	 * @throws ActiviteInvalideException
	 */
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnRole(Activite activite) throws ActiviteInexistanteException, ActiviteInvalideException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ActiviteWs log : Demande d ajout d une nouvelle Activite dans la Bdd.");
			activiteservice.ajouteUneactivite(activite);
			logger.info("ActiviteWs log : nouvelle activite ajoutee, avec l id : " + activite.getIdActivite());
			builder = Response.ok(activite);

		} catch (DaoException message) {
			logger.error("ActiviteWs log : Impossible de creer cette Activite dans la Bdd.");
			throw new ActiviteInvalideException("ActiviteWs Exception : Impossible de creer cette activite dans la Bdd.");

		}
		return builder.build();
	}

	/**
	 * Modifier une activite
	 * @param activite
	 * @return
	 */
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnRole(Activite activite) {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ActiviteWs log : Demande de modification de l activite id : " + activite.getIdActivite() + " dans la Bdd.");
			activiteservice.modifieUneActivite(activite);
			logger.info("ActiviteWs log : Activite id : " + activite.getIdActivite() + " a bien ete modifie dans la Bdd.");
			String msg = "Acitivite id : " + activite.getIdActivite() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);

		} catch (ActiviteInexistanteException message) {
			logger.error("ActiviteWs log : L activite id : " + activite.getIdActivite() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();

		} catch (ActiviteInvalideException message) {
			logger.error("ActiviteWs log : Verifiez l Activite");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}
	
	/**
	 * Supprimer une activite
	 * @param idActivite
	 * @return
	 */
	@DefineUserRole({"PRATICIEN"})
	@DELETE
	@Path("/del/{idActivite: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response supprimerUnRole(@PathParam("idActivite") final Integer idActivite) {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("ActiviteWs log : Demande de suppression de l activite id : " + idActivite + " dans la Bdd.");
			activiteservice.supprimeUneActivite(idActivite);
			logger.info("ActiviteWs log : Activite id : " + idActivite + " a bien ete modifie dans la Bdd.");
			builder = Response.ok();

		} catch (ActiviteInexistanteException message) {
			logger.error("ActiviteWs log : Activite id : " + idActivite+ " ne peut etre supprime dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

}
