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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInvalideException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.Prestation;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationInvalideException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.prestation.GenrePrestationNullException;
import fr.labonbonniere.opusbeaute.middleware.service.prestation.NbCharPrestationException;
import fr.labonbonniere.opusbeaute.middleware.service.prestation.PrestationService;

/**
 * WebService REST Gere la ressource Prestation
 * 
 * @author fred
 *
 */
@SecuApp
@Stateless
@Path("/prestation")
// @DefineUserRole({"ANONYMOUS"})
// @DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
// @DefineUserRole({"PRATICIEN","STAGIAIRE"})
 @DefineUserRole({"ALLOWALL"})
// @DefineUserRole({"DENYALL"})
//@DefineUserRole({ "ROOT", "ADMINISTRATEUR" })
public class PrestationWs {

	private static final Logger logger = LogManager.getLogger(PrestationWs.class.getName());

	@EJB
	private PrestationService prestationservice;

	/**
	 * recupere la liste des Prestations
	 * 
	 * @return Response
	 * @throws DaoException Exception
	 */
	@DefineUserRole({ "ALLOWALL" })
	@GET
	@Path("/listeprestations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listeDesPrestations() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande au Service la liste des Prestations");
			final List<Prestation> listeprestation = prestationservice.recupereListePrestation();
			logger.info("PrestationWs log : Transmission du contenu(liste Prestation).");
			builder = Response.ok(listeprestation);

		} catch (DaoException message) {
			logger.error("PrestationWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	/**
	 * Retourne une Prestation par son Id
	 * 
	 * @param idPrestation Integer
	 * @return Reponse
	 * @throws PrestationInexistanteException Exception
	 */
	@DefineUserRole({ "ALLOWALL" })
	@GET
	@Path("/{idPrestation: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(@PathParam("idPrestation") final Integer idPrestation)
			throws PrestationInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("WebService log - Demande a la bdd le Prestation id :  " + idPrestation);
			Prestation prestation = prestationservice.recupererUnePrestation(idPrestation);
			logger.info("WebService log - Le Prestation demande " + prestation.getIdPrestation() + " transmis");
			builder = Response.ok(prestation);

		} catch (PrestationInexistanteException message) {
			logger.error("WebService log - La Prestation id : " + idPrestation + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	/**
	 * Retourne la Liste des Prestation Homme
	 * 
	 * @return Response
	 * @throws DaoException Exception
	 */
	@DefineUserRole({ "ALLOWALL" })
	@GET
	@Path("/listph")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListePrestationHomme() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande au Service la liste des Prestations homme");
			final List<Prestation> listeprestationH = prestationservice.recupereListePrestationHomme();
			logger.info("PrestationWs log : Transmission du contenu(liste Prestation homme).");
			builder = Response.ok(listeprestationH);

		} catch (DaoException message) {
			logger.error("PrestationWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	/**
	 * Retourne la liste des Prestations Femme
	 * 
	 * @return Response
	 * @throws DaoException Exception
	 */
	@DefineUserRole({ "ALLOWALL" })
	@GET
	@Path("/listpf")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListePrestationFemme() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande au Service la liste des Prestations femme");
			final List<Prestation> listeprestationF = prestationservice.recupereListePrestationFemme();
			logger.info("PrestationWs log : Transmission du contenu(liste Prestation femme).");
			builder = Response.ok(listeprestationF);

		} catch (DaoException message) {
			logger.error("PrestationWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	@DefineUserRole({ "ALLOWALL" })
	@GET
	@Path("/searchga/{genre},{activite}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListePrestationCriteresGA(@PathParam("genre") final String genre,
			@PathParam("activite") final String activite) throws DaoException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info(
					"PrestationWs log : Demande au PrestationService la liste des Prestations avec le criteres genre, activite.");
			final List<Prestation> listeprestaGA = prestationservice.recupereListePrestationCriteresGenreActivite(genre,
					activite);
			logger.info(
					"PrestationWs log : Transmission de la Liste des Prestation'sselon les criteres genre, activite.");
			builder = Response.ok(listeprestaGA);
		} catch (Exception message) {
			logger.error("PrestationWs Exception : probleme sur le format de la requete.");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		}
		return builder.build();
	}

	/**
	 * Retourne la liste des Prestations Selon les criters Genre, Activite et
	 * Soin
	 * 
	 * @param genre Genre
	 * @param activite Activite
	 * @param soin Soin
	 * @return Response
	 * @throws DaoException Exception
	 */
	@DefineUserRole({ "ALLOWALL" })
	@GET
	@Path("searchgas/{genre},{activite},{soin}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListePrestationCriteresGAS(@PathParam("genre") final String genre,
			@PathParam("activite") final String activite, @PathParam("soin") final String soin) throws DaoException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info(
					"PrestationWs log : Demande au PrestationService la liste des Prestations avec le criteres genre, activite, soin.");
			final List<Prestation> listeprestaGAS = prestationservice
					.recupereListePrestationCriteresGenreActiviteSoins(genre, activite, soin);
			logger.info(
					"PrestationWs log : Transmission de la Liste des Prestation'sselon les criteres genre, activite et soin.");
			builder = Response.ok(listeprestaGAS);
		} catch (Exception message) {
			logger.error("PrestationWs Exception : probleme sur le format de la requete.");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		}
		return builder.build();
	}

	/**
	 * Creation d un nouvelle Prestation
	 * 
	 * @param prestation String
	 * @return response
	 * @throws PrestationExistanteException Exception
	 * @throws NbCharPrestationException Exception
	 * @throws PrestationInvalideException Exception
	 * @throws GenreInvalideException Exception
	 * @throws DaoException Exception
	 * @throws GenrePrestationNullException Exception
	 */
	@DefineUserRole({ "PRATICIEN", "STAGIAIRE" })
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnePrestation(Prestation prestation)
			throws PrestationExistanteException, NbCharPrestationException, PrestationInvalideException,
			GenreInvalideException, DaoException, GenrePrestationNullException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande d ajout d une nouvelle Prestation dans la Bdd.");
			prestationservice.ajoutPrestation(prestation);
			logger.info("PrestationWs log : Nouvelle Prestation ajoutee, avec l id : " + prestation.getIdPrestation());
			builder = Response.ok(prestation);

		} catch (PrestationExistanteException message) {
			logger.error("PrestationWs log : Impossible de creer cette Prestation dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharPrestationException message) {
			logger.error("PrestationWs log : Verifiez les informations dans Prestation.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (PrestationInvalideException message) {
			logger.error("PrestationWs log : Verifiez les informations dans Prestation.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		} catch (GenreInvalideException message) {
			logger.error("PrestationWs log : Verifiez les informations dans Prestation.Genre.");
			builder = Response.status(Response.Status.BAD_REQUEST);
		}

		return builder.build();
	}

	/**
	 * Modification d un Prestation
	 * 
	 * @param prestation String
	 * @return Response
	 * @throws PrestationInexistanteException Exception
	 * @throws NbCharPrestationException Exception
	 * @throws PrestationInvalideException Exception
	 * @throws GenreInvalideException Exception
	 * @throws DaoException Exception
	 * @throws GenrePrestationNullException Exception
	 */
	@DefineUserRole({ "PRATICIEN", "STAGIAIRE" })
	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnePrestation(Prestation prestation)
			throws PrestationInexistanteException, NbCharPrestationException, PrestationInvalideException,
			GenreInvalideException, DaoException, GenrePrestationNullException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande de modification de la Prestation id : "
					+ prestation.getIdPrestation() + " dans la Bdd.");
			prestationservice.modifierUnePrestation(prestation);
			logger.info("PrestationWs log : Prestation id : " + prestation.getIdPrestation()
					+ " a bien ete modifiee dans la Bdd.");
			String msg = "Prestation id : " + prestation.getIdPrestation() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);

		} catch (PrestationInexistanteException message) {
			logger.error("PrestationWs log : Rdv id : " + prestation.getIdPrestation()
					+ " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();
		}

		return builder.build();

	}

	/**
	 * Suppression d une Prestation
	 * 
	 * @param idPrestation Integer
	 * @return Response
	 * @throws PrestationInexistanteException Exception
	 */
	@DefineUserRole({ "PRATICIEN" })
	@DELETE
	@Path("/del/{idPrestation: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response supprimerUnePrestation(@PathParam("idPrestation") final Integer idPrestation)
			throws PrestationInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande de suppression de la Prestation id : " + idPrestation
					+ " dans la Bdd.");
			prestationservice.suppressionUnePrestation(idPrestation);
			logger.info("PrestationWs log : Prestation id : " + idPrestation + " a bien ete modifie dans la Bdd.");
			String msg = "PrestationWs log : le Rdv id : " + idPrestation + " a ien ete supprime de la Bdd.";
			builder = Response.ok(msg);

		} catch (PrestationInexistanteException message) {
			logger.error("PrestationWs log : Rdv id : " + idPrestation + " ne peut etre supprime dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();
	}
}
