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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresselieurdv.AdresseLieuRdv;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;
import fr.labonbonniere.opusbeaute.middleware.service.adresselieurdv.AdresseLieuRdvService;

@Stateless
@Path("/adresselieurdv")
public class AdresseLieuRdvWs {
	private static final Logger logger = LogManager.getLogger(AdresseLieuRdvWs.class);

	@EJB
	private AdresseLieuRdvService adresselieurdvservice;

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeLieuRdv() throws DaoException {

		Response.ResponseBuilder builder = null;

		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseLieuRdvWs log : Demande au Service la liste des Prestations");
			final List<AdresseLieuRdv> listeadresseLieuRdv = adresselieurdvservice.recupereListeAdresseLieuRdv();
			logger.info("AdresseLieuRdvWs log : Transmission du contenu(liste Prestation).");
			builder = Response.ok(listeadresseLieuRdv);

		} catch (DaoException message) {
			logger.error("AdresseLieuRdvWs log : Il doit y avoir un probleme avec la BDD.");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

	@GET
	@Path("/{idAdresseLieuRdv: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(@PathParam("idAdresseLieuRdv") final Integer idAdresseLieuRdv) throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseWs log - Demande a la bdd le Adresse id :  " + idAdresseLieuRdv);
			AdresseLieuRdv adresse = adresselieurdvservice.recupererUneAdresseLieuRdv(idAdresseLieuRdv);
			logger.info("AdresseWs log - Adresse demande " + adresse.getIdAdresseLieuRdv() + " transmise");
			builder = Response.ok(adresse);

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseWs log - l Adresse id : " + idAdresseLieuRdv + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
		}

		return builder.build();
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUneadresseLieuRdv(AdresseLieuRdv adresseLieuRdv) throws AdresseExistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseLieuRdvWs log : Demande d ajout d une nouvelle Adresse dans la Bdd.");
			adresselieurdvservice.ajoutAdresseLieuRdv(adresseLieuRdv);
			logger.info("AdresseLieuRdvWs log : Nouvelle Adresse ajoutee, avec l id : " + adresseLieuRdv.getIdAdresseLieuRdv());
			builder = Response.ok(adresseLieuRdv);

		} catch (AdresseExistanteException message) {
			logger.error("AdresseLieuRdvWs log : Impossible de creer cette AdresselieuRdv dans la Bdd.");
			throw new AdresseExistanteException(
					"PrestationWs Exception : Impossible de creer cette AdresseLieuRdv dans la Bdd.");
		} catch (NbNumRueException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieuRdv.NumeroRue.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharPaysException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieuRdv.Pays.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbNumZipcodeException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieurdv.Zipcode.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharRueVilleException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieuRdv.Ville ou AdresseLieuRdv.Rue");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}
		
		return builder.build();
	}

	@PUT
	@Path("/mod")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUneadresseLieuRdv(AdresseLieuRdv adresseLieuRdv) throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseLieuRdvWs log : Demande de modification de l Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv()
					+ " dans la Bdd.");
			adresselieurdvservice.modifDeLAdresseLieuRdv(adresseLieuRdv);
			logger.info("AdresseLieuRdvWs log : Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " a bien ete modifiee dans la Bdd.");
//			String msg = "Adresse id : " + adresse.getIdAdresse() + " a bien ete modifiee dans la Bdd.";
			builder = Response.ok(adresseLieuRdv);

		} catch (AdresseInexistanteException message) {
			logger.error(
					"AdresseLieuRdvWs log : l Adresse id : " + adresseLieuRdv.getIdAdresseLieuRdv() + " ne peut etre modifiee dans la Bdd.");
			builder = Response.notModified();

		}catch (NbNumRueException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieuRdv.NumeroRue.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharPaysException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieuRdv.Pays.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbNumZipcodeException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieuRdv.Zipcode.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		} catch (NbCharRueVilleException message) {
			logger.error("AdresseWS log : Verifiez AdresseLieuRdv.Ville.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}

	
	@PUT
	@Path("/settonull/{idAdresse: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sesToNullUneadresseLieuRdv(@PathParam("idAdresseLieuRdv") final Integer idAdresseLieuRdv) throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseLieuRdvWs log : Demande de renitialisation de l Adresse id : " + idAdresseLieuRdv
					+ " dans la Bdd.");
			adresselieurdvservice.setToNullDeLAdresseLieuRdv(idAdresseLieuRdv);;
			logger.info("AdresseLieuRdvWs log : AdresseLieuRdv id : " + idAdresseLieuRdv + " a bien ete reinitialisee dans la Bdd.");
//			String msg = "Adresse id : " + adresse.getIdAdresse() + " a bien ete modifiee dans la Bdd.";
			builder = Response.ok();

		} catch (AdresseInexistanteException message) {
			logger.error(
					"AdresseLieuRdvWs log : l AdresseLieuRdv id : " + idAdresseLieuRdv + " ne peut etre reinitialisee dans la Bdd.");
			builder = Response.notModified();

		}

		return builder.build();

	}
	
	
	@DELETE
	@Path("/del/{idAdresse: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheAdresseLieuRdv(@PathParam("idAdresseLieuRdv") final Integer idAdresseLieuRdv)
			throws AdresseInexistanteException {

		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("AdresseLieuRdvWs log : Demande de suppression de l Adresse id : " + idAdresseLieuRdv + " dans la Bdd.");
			adresselieurdvservice.suppressionddUneAdresseLieuRdv(idAdresseLieuRdv);
			logger.info("AdresseLieuRdvWs log : Prestation id : " + idAdresseLieuRdv + " a bien ete modifie dans la Bdd.");
//			String msg = "AdresseLieuRdvWs log : l adresse id : " + idAdresse + " a ien ete supprimee de la Bdd.";
			builder = Response.ok();

		} catch (AdresseInexistanteException message) {
			logger.error("AdresseLieuRdvWs log : l Adresse id : " + idAdresseLieuRdv + " ne peut etre supprimee dans la Bdd.");
			builder = Response.status(Response.Status.BAD_REQUEST);

		}

		return builder.build();

	}
}