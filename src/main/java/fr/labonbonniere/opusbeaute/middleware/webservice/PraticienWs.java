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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.Praticien;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.PraticienExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.PraticienInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.praticien.PraticienService;

/**
 * WebService REST
 * Gere l acces a la ressource Praticien
 * 
 * @author fred
 *
 */
@SecuApp
@Stateless
@Path("/praticien")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class PraticienWs {
	private static final Logger logger = LogManager.getLogger(PraticienWs.class.getName());

	@EJB
	private PraticienService praticienservice;

	/**
	 * Retourne la Liste de Praticien
	 * 
	 * @return Response
	 * @throws DaoException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/liste")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListePraticien() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("PraticienWs log : Demande au Service la liste des Praticien");
			final List<Praticien> listePraticien = praticienservice.recupereListePraticien();
			logger.info("PraticienWs log : Transmission du contenu(liste Praticien).");
			builder = Response.ok(listePraticien);

		} catch (DaoException message) {
			logger.error("Praticiens log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	/**
	 * Retourne un Praticien par son Id 
	 * 
	 * @param idPraticien Integer
	 * @return Response
	 * @throws PraticienInexistantException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/{idPraticien: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response thePraticien(@PathParam("idPraticien") final Integer idPraticien)
			throws PraticienInexistantException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PraticienWs log - Demande a la bdd le Praticien id :  " + idPraticien);
			Praticien praticien = praticienservice.recupererUnPraticien(idPraticien);
			logger.info("PraticienWs log - Praticien demande " + praticien.getIdPraticien() + " transmis");
			builder = Response.ok(praticien);

		} catch (PraticienInexistantException message) {
			logger.error("PraticienWs log - le Praticien id : " + idPraticien + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	/**
	 * Creation d un nouveau Praticien
	 * 
	 * @param praticien Praticien
	 * @return Response
	 * @throws PraticienExistantException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws NbCharNomException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharTelException Exception
	 */
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnPraticien(Praticien praticien) throws PraticienExistantException, EmailFormatInvalidException,
			NbCharNomException, NbCharPrenomException, NbCharTelException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PraticienWs log : Demande d ajout d une nouveau Praticien dans la Bdd.");
			praticienservice.ajoutUnPraticien(praticien);
			logger.info("PraticienWs log : Nouveau Praticien ajoute, avec l id : " + praticien.getIdPraticien());
			builder = Response.ok(praticien);

		} catch (PraticienExistantException message) {
			logger.error("PraticienWs log : Impossible de creer ce Praticien dans la Bdd.");
			throw new PraticienExistantException(
					"PraticienWs Exception : Impossible de creer ce Praticien dans la Bdd.");
		} catch (NbCharPrenomException message) {
			logger.error("PraticienWS log : Verifiez Client.Prenom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharNomException message) {
			logger.error("PraticienWS log : Verifiez Client.Nom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (EmailFormatInvalidException message) {
			logger.error("PraticienWS log : Verifiez Client.Email.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharTelException message) {
			logger.error("PraticienWS log : Verifiez Client.Telephone.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}
		return builder.build();
	}
	/**
	 * Modifie un Praticien
	 * 
	 * @param praticien Praticien
	 * @return Repsonse
	 * @throws PraticienInexistantException Response
	 * @throws EmailFormatInvalidException Response
	 * @throws NbCharNomException Response
	 * @throws NbCharPrenomException Response
	 * @throws NbCharTelException Response
	 */
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnPraticien(Praticien praticien) throws PraticienInexistantException,
			EmailFormatInvalidException, NbCharNomException, NbCharPrenomException, NbCharTelException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PraticienWs log : Demande de modification du Praticien id : "
					+ praticien.getIdPraticien() + " dans la Bdd.");
			praticienservice.modifierUnPraticien(praticien);
			logger.info("PraticienWs log : Praticien id : " + praticien.getIdPraticien()
					+ " a bien ete modifie dans la Bdd.");
			String msg = "Praticien id : " + praticien.getIdPraticien() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);

		} catch (PraticienInexistantException message) {
			logger.error("PraticienWs log : Le Praticien id : " + praticien.getIdPraticien()
					+ " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();

		} catch (NbCharPrenomException message) {
			logger.error("PraticienWs log : Verifiez Praticien.Prenom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharNomException message) {
			logger.error("PraticienWs log : Verifiez Praticien.Nom.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (EmailFormatInvalidException message) {
			logger.error("PraticienWs log : Verifiez Praticien.Email.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharTelException message) {
			logger.error("PraticienWs log : Verifiez Praticien.Telephone.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}
	/**
	 * Supprime un Praticien
	 * 
	 * @param idPraticien Integer
	 * @return Response
	 * @throws AdresseInexistanteException Exception
	 */
	@DefineUserRole({"PRATICIEN"})
	@DELETE
	@Path("/del/{idPraticien: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response supprimeUnPraticien(@PathParam("idPraticien") final Integer idPraticien)
			throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info(
					"PraticienWs log : Demande de suppression de l Adresse id : " + idPraticien + " dans la Bdd.");
			praticienservice.suppressionDUnPraticien(idPraticien);
			logger.info("PraticienWs log : Praticien id : " + idPraticien + " a bien ete modifie dans la Bdd.");
			String msg = "PraticienWs log : Le Praticien id : " + idPraticien + " a ien ete supprimee de la Bdd.";
			builder = Response.ok(msg);

		} catch (PraticienInexistantException message) {
			logger.error(
					"PraticienWs log : Le Praticien id : " + idPraticien + " ne peut etre supprime dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();
	}
	
	@DefineUserRole({"PRATICIEN"})
	@GET
//	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response praticienParmail(@QueryParam("email") final String email) throws PraticienInexistantException {
		
		logger.info("PraticienWs log : Recuperation du praticien par son email");
		
		ResponseBuilder builder = null;
		try {
			
			Praticien praticien = praticienservice.recupererUnPraticienParEmail(email);
			builder = Response.ok(praticien);
			
		} catch (PraticienInexistantException message) {
			logger.error("PraticienWs log : Il y a eu un probleme lors de la recuperation du praticien par son email");
			builder = Response.status(Response.Status.BAD_REQUEST);
		}
		
		return builder.build();
		
		
	}
}
