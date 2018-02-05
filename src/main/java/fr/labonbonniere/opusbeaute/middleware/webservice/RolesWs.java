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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.Roles;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.RolesInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.userRoles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.roles.RoleInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.roles.RolesService;
import fr.labonbonniere.opusbeaute.middleware.service.roles.RolesUtilisateurNullException;

@Stateless
@Path("/roles")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class RolesWs {

	private static final Logger logger = LogManager.getLogger(RolesWs.class);
	
	@EJB
	RolesService rolesService;
	
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeDesRoles() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("RolesWs log : Demande au Service la liste des Roles");
			final List<Roles> listeDesRoles = rolesService.recupereListeRoles();
			logger.info("RolesWs log : Transmission du contenu(liste Roles).");
			builder = Response.ok(listeDesRoles);

		} catch (DaoException message) {
			logger.error("RolesWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/{idRole: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theGenre(@PathParam("idRole") final Integer idRole) throws RolesInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RolesWs log - Demande a la bdd le Role id :  " + idRole);
			Roles role = rolesService.recupererUnRole(idRole);
			logger.info("RolesWs log - Role demande " + role.getIdRoles() + " transmis");
			builder = Response.ok(role);

		} catch (RoleInexistantException message) {
			logger.error("RolesWs log - le Role id : " + idRole + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnRole(Roles role) throws DaoException, RolesUtilisateurNullException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RolesWs log : Demande d ajout d un nouveau Role dans la Bdd.");
			rolesService.ajoutRole(role);
			logger.info("RolesWs log : Nouveau Role ajoute, avec l id : " + role.getIdRoles());
			builder = Response.ok(role);

		} catch (DaoException message) {
			logger.error("RolesWs log : Impossible de creer ce Client dans la Bdd.");
			throw new DaoException("RolesWs Exception : Impossible de creer ce Role dans la Bdd.");

		} catch (RolesUtilisateurNullException message) {
			logger.error("RolesWs log : Verifiez le Role");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}
		return builder.build();
	}

	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnRole(Roles role) throws RoleInexistantException, RolesUtilisateurNullException, RolesInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RolesWs log : Demande de modification du Role id : " + role.getIdRoles() + " dans la Bdd.");
			rolesService.modifDuRole(role);
			logger.info("RolesWs log : Role id : " + role.getIdRoles() + " a bien ete modifie dans la Bdd.");
			String msg = "Role id : " + role.getIdRoles() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);

		} catch (RoleInexistantException message) {
			logger.error("RolesWs log : le Role id : " + role.getIdRoles() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();

		} catch (RolesUtilisateurNullException message) {
			logger.error("RolesWs log : Verifiez le Role");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

	@DefineUserRole({"PRATICIEN"})
	@DELETE
	@Path("/del/{idRole: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheRole(@PathParam("idRole") final Integer idRole) throws RolesInexistantException, RoleInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RolesWs log : Demande de suppression de le Role id : " + idRole + " dans la Bdd.");
			rolesService.suppressionddUnRole(idRole);
			logger.info("RolesWs log : Role id : " + idRole + " a bien ete modifie dans la Bdd.");
			builder = Response.ok();

		} catch (RolesInexistantException message) {
			logger.error("RolesWs log : Role id : " + idRole + " ne peut etre supprime dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}
	
}
