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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.Prestation;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.PrestationInexistanteException;
import fr.labonbonniere.opusbeaute.middleware.service.PrestationService;


@Stateless
@Path("/prestation")
public class PrestationWs {
	
	private static final Logger logger = LogManager.getLogger(PrestationWs.class);

	@EJB
	private PrestationService prestationservice;
	
	
	@GET
	@Path("/listeprestations")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListe() throws DaoException {
		
		Response.ResponseBuilder builder = null;	
		
		try { 
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande au Service la liste des Prestations");
			final List<Prestation> listeprestation = prestationservice.recupereListePrestation();
			logger.info("PrestationWs log : Transmission du contenu(liste Prestation).");
			builder = Response.ok(listeprestation);	
			
		} catch ( DaoException message ) {
				logger.error("PrestationWs log : Il doit y avoir un probleme avec la BDD." );
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
			
		return builder.build(); 
	}
	
	@GET
    @Path("/{idPrestation: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(
			@PathParam("idPrestation") final Integer idPrestation) throws PrestationInexistanteException  {

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
	
	@GET
	@Path("/listeprestationsH")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListePrestationHomme() throws DaoException {
		
		Response.ResponseBuilder builder = null;	
		
		try { 
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande au Service la liste des Prestations homme");
			final List<Prestation> listeprestationH = prestationservice.recupereListePrestationHomme();
			logger.info("PrestationWs log : Transmission du contenu(liste Prestation homme).");
			builder = Response.ok(listeprestationH);	
			
		} catch ( DaoException message ) {
				logger.error("PrestationWs log : Il doit y avoir un probleme avec la BDD." );
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
			
		return builder.build(); 
	}
	
	
	@GET
	@Path("/listeprestationsF")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListePrestationFemme() throws DaoException {
		
		Response.ResponseBuilder builder = null;	
		
		try { 
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande au Service la liste des Prestations femme");
			final List<Prestation> listeprestationF = prestationservice.recupereListePrestationFemme();
			logger.info("PrestationWs log : Transmission du contenu(liste Prestation femme).");
			builder = Response.ok(listeprestationF);	
			
		} catch ( DaoException message ) {
				logger.error("PrestationWs log : Il doit y avoir un probleme avec la BDD." );
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
			
		return builder.build(); 
	}
	
	
	@POST
	@Path("/addprestation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnePrestation(Prestation prestation) throws PrestationExistanteException {
		
		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande d ajout d une nouvelle Prestation dans la Bdd.");
			prestationservice.ajoutPrestation(prestation);
			logger.info("PrestationWs log : Nouvelle Prestation ajoutee, avec l id : " + prestation.getIdPrestation());
			builder = Response.ok(prestation);
			
		} catch (PrestationExistanteException message) {
			logger.error("PrestationWs log : Impossible de creer cette Prestation dans la Bdd.");
			throw new PrestationExistanteException("PrestationWs Exception : Impossible de creer cette Prestation dans la Bdd.");
		}
		return builder.build();
	}
	
	@PUT
	@Path("/modifprestation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnePrestation (Prestation prestation) throws PrestationInexistanteException {
		
		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande de modification de la Prestation id : " + prestation.getIdPrestation() + " dans la Bdd.");
			prestationservice.modifierUnePrestation(prestation);
			logger.info("PrestationWs log : Prestation id : " + prestation.getIdPrestation() + " a bien ete modifiee dans la Bdd.");
			String msg = "Prestation id : " + prestation.getIdPrestation() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);
			
		} catch (PrestationInexistanteException message) {
			logger.error("PrestationWs log : Rdv id : " + prestation.getIdPrestation() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();
			
		}
		
		return builder.build();
		
	}
	
	@DELETE
    @Path("/remove/{idPrestation: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteThePrestation(
			@PathParam("idPrestation") final Integer idPrestation) throws PrestationInexistanteException {

		Response.ResponseBuilder builder = null;		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("PrestationWs log : Demande de suppression de la Prestation id : " + idPrestation + " dans la Bdd.");
			prestationservice.suppressionUnePrestation(idPrestation);
			logger.info("PrestationWs log : Prestation id : " + idPrestation + " a bien ete modifie dans la Bdd.");	
			String msg = "PrestationWs log : le Rdv id : " + idPrestation + " a ien ete supprime de la Bdd.";
			builder = Response.ok(msg);

		} catch (PrestationInexistanteException message ) {
			logger.error("PrestationWs log : Rdv id : " + idPrestation + " ne peut etre supprime dans la Bdd.");	
			builder = Response.status(Response.Status.BAD_REQUEST);
			
		}
		
		return builder.build();
		
		}

}
