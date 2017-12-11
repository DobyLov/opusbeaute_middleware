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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.client.EmailFormatInvalid;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;

@Stateless
@Path("/utilisateur")
public class UtilisateurWs {
	private static final Logger logger = LogManager.getLogger(UtilisateurWs.class);

	@EJB
	private UtilisateurService utilisateurservice;

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeUtilisateur() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("UtilisateurWs log : Demande au Service la liste des Utilisateurs");
			final List<Utilisateur> listeUtilisateur = utilisateurservice.recupereListeUtilisateur();
			logger.info("UtilisateurWs log : Transmission du contenu(liste Utilisateur).");
			builder = Response.ok(listeUtilisateur);

		} catch (DaoException message) {
			logger.error("AdresseWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	@GET
	@Path("/{idUtilisateur: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(@PathParam("idUtilisateur") final Integer idUtilisateur)
			throws UtilisateurInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("UtilisateurWs log - Demande a la bdd le Utilisateur id :  " + idUtilisateur);
			Utilisateur utilisateur = utilisateurservice.recupererUnUtilisateur(idUtilisateur);
			logger.info("UtilisateurWs log - Utilisateur demande " + utilisateur.getIdUtilisateur() + " transmis");
			builder = Response.ok(utilisateur);

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilisateurWs log - l Utilisateur id : " + idUtilisateur + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnUtilisateur(Utilisateur utilisateur) throws UtilisateurExistantException, EmailFormatInvalid,
			NbCharNomException, NbCharPrenomException, NbCharTelException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("UtilisateurWs log : Demande d ajout d une nouvel Utilisateur dans la Bdd.");
			utilisateurservice.ajoutUnUtilisateur(utilisateur);
			logger.info("UtilisateurWs log : Nouvel Utilisateur ajoute, avec l id : " + utilisateur.getIdUtilisateur());
			builder = Response.ok(utilisateur);

		} catch (UtilisateurExistantException message) {
			logger.error("UtilisateurWs log : Impossible de creer cet Utilisateur dans la Bdd.");
			throw new UtilisateurExistantException(
					"UtilisateurWs Exception : Impossible de creer cet utilisateur dans la Bdd.");
		} catch (NbCharPrenomException message) {
			logger.error("UtilisateurWs log : Verifiez Utilisateur.Prenom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharNomException message) {
			logger.error("UtilisateurWs log : Verifiez Utilisateur.Nom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (EmailFormatInvalid message) {
			logger.error("UtilisateurWs log : Verifiez Utilisateur.Email.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharTelException message) {
			logger.error("UtilisateurWs log : Verifiez Utilisateur.Telephone.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}
		return builder.build();
	}

	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnUtilisateur(Utilisateur utilisateur) throws UtilisateurInexistantException,
			EmailFormatInvalid, NbCharNomException, NbCharPrenomException, NbCharTelException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("UtilisateurWs log : Demande de modification de l Adresse id : "
					+ utilisateur.getIdUtilisateur() + " dans la Bdd.");
			utilisateurservice.modifierUnUtilisateur(utilisateur);
			logger.info("UtilisateurWs log : Adresse id : " + utilisateur.getIdUtilisateur()
					+ " a bien ete modifiee dans la Bdd.");
			String msg = "Adresse id : " + utilisateur.getIdUtilisateur() + " a bien ete modifiee dans la Bdd.";
			builder = Response.ok(msg);

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilisateurWs log : l Adresse id : " + utilisateur.getIdUtilisateur()
					+ " ne peut etre modifiee dans la Bdd.");
			builder = Response.notModified();

		} catch (NbCharPrenomException message) {
			logger.error("UtilisateurWs log : Verifiez Client.Prenom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharNomException message) {
			logger.error("UtilisateurWs log : Verifiez Client.Nom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (EmailFormatInvalid message) {
			logger.error("UtilisateurWs log : Verifiez Client.Email.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharTelException message) {
			logger.error("UtilisateurWs log : Verifiez Client.Telephone.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

	@DELETE
	@Path("/del/{idUtilisateur: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheUtilisateur(@PathParam("idUtilisateur") final Integer idUtilisateur)
			throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info(
					"UtilisateurWs log : Demande de suppression de l Adresse id : " + idUtilisateur + " dans la Bdd.");
			utilisateurservice.suppressionDUnUtilisateur(idUtilisateur);
			logger.info("UtilisateurWs log : Prestation id : " + idUtilisateur + " a bien ete modifie dans la Bdd.");
			String msg = "UtilisateurWs log : l adresse id : " + idUtilisateur + " a ien ete supprimee de la Bdd.";
			builder = Response.ok(msg);

		} catch (UtilisateurInexistantException message) {
			logger.error(
					"UtilisateurWs log : l Adresse id : " + idUtilisateur + " ne peut etre supprimee dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}
}
