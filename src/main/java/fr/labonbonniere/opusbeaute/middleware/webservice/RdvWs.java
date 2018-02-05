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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.userRoles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.NoRdvException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvDebutChevauchementException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvEgaliteChevauchementException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvEnglobantException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvFinChevauchementException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvService;


@SecuApp
@Stateless
@Path("/rdv")
//@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class RdvWs {
	
	// logger
	private static final Logger logger = LogManager.getLogger(RdvWs.class);

	@EJB
	private RdvService rdvService;

	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/list")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListe() throws DaoException {
		
		Response.ResponseBuilder builder = null;	
		
		try { 
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au Service la liste des Rdv's");
			final List<Rdv> liste = rdvService.recupereListeRdv();
			logger.info("RdvWs log : Transmission du contenu(liste Rdv).");
			builder = Response.ok(liste);	
			
		} catch ( DaoException message ) {
				logger.error("RdvWs log : Il doit y avoir un probleme avec la BDD." );
				builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
			
		return builder.build(); 
	}
	
	
	//	http://localhost:8080/opusbeaute-0/obws/rdv/$idRdv
	@DefineUserRole({"ALLOWALL"})
	@GET
    @Path("/{idRdv: \\d+}") // fonctionne bien 11/07
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(
			@PathParam("idRdv") final Integer idRdv) throws RdvInexistantException  {

		Response.ResponseBuilder builder = null;
		try {
		logger.info("-----------------------------------------------------");			
		logger.info("WebService log - Demande a la bdd le Rdv id :  " + idRdv);
		Rdv rdv = rdvService.recupererUnRdv(idRdv);
		logger.info("WebService log - Le rdv demande " + rdv.getIdRdv()  + " transmis");
		builder = Response.ok(rdv);		

		
		} catch (RdvInexistantException message) {
			logger.error("WebService log - Le rdv id : " + idRdv + " demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);

			
		}		
		return builder.build();

	}

	
	// regex qui gere le format yyyy-mm-dd numeric separe par "-" 
	// le regex va un petit peu plus loin :
	// yyyy => 2010 a 2029
	// mm => 01 à 12
	// dd => 01 à 31
	// !!! le regex ne gere pas le 30 fevrier ! a completer dans la partie service / metiers
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/pardate/{listeRdvDateDuJour: (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParDate (
			@PathParam("listeRdvDateDuJour") final String listeRdvDateDuJour ) throws DaoException {		
		
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par date selectionnee.");
			final List<Rdv> listerdvpardate = rdvService.recupereListeRdvParDate(listeRdvDateDuJour);
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par date selectionnee.");
			builder = Response.ok(listerdvpardate);

		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la date.");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		
		} 		
		return builder.build();

	}
	
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/plagedate/{RdvPlageJourA:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{RdvPlageJourB:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParPlageDate (
			@PathParam("RdvPlageJourA") final String RdvPlageJourA, 
			@PathParam("RdvPlageJourB") final String RdvPlageJourB) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par plage de dates selectionnees.");
			final List<Rdv> listerdvpardate = rdvService.recupereListeRdvViaPlageDate(RdvPlageJourA, RdvPlageJourB );
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			builder = Response.ok(listerdvpardate);
		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la/des date(s).");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		}
		return builder.build();
	}
	
	
	//	http://localhost:8080/opusbeaute-0/obws/rdv/addrdv
	// ne pas mettre l idRdv, ajouter a l' objet et les parametres RDV	
	// POSTMAN
	//		POST	BODY	raw		JSON
	
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@POST
	@Path("/add")	// fonctionne bien 11/07
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnRdv(Rdv rdv) throws RdvExistantException, DaoException, 
											RdvEgaliteChevauchementException, NoRdvException, RdvDebutChevauchementException, 
											RdvFinChevauchementException, RdvEnglobantException {
		logger.info("-----------------------------------------------------");
		logger.info("RdvWs log : Demande d ajout d un nouveau Rdv dans la Bdd.");
		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande d ajout d un nouveau Rdv dans la Bdd.");
			rdvService.ajoutRdv(rdv);
			logger.info("RdvWs log : Nouveau Rdv ajoute, avec l id : " + rdv.getIdRdv());
			builder = Response.ok(rdv);
			
		} catch (RdvExistantException message) {
			logger.error("RdvWs log : Impossible de creer ce rdv dans la Bdd.");
			throw new RdvExistantException("RdvWs Exception : Impossible de creer ce rdv dans la Bdd.");
		}
		return builder.build();
	}
		
	
	//	http://localhost:8080/opusbeaute-0/obws/rdv/modifrdv
	// Mettre l idRdv de l objet et les parametres a modifier rdv
	// POSTMAN
	//		PUT
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@PUT
	@Path("/mod") // fonctionne bien 11/07
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnRdv (Rdv rdv) throws RdvInexistantException {
		
		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande de modification du Rdv id : " + rdv.getIdRdv() + " dans la Bdd.");
			rdvService.modifduRdv(rdv);
			logger.info("RdvWs log : Rdv id : " + rdv.getIdRdv() + " a bien ete modifie dans la Bdd.");
			String msg = "Rdv id : " + rdv.getIdRdv() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);
			
		} catch (RdvInexistantException message) {
			logger.error("RdvWs log : Rdv id : " + rdv.getIdRdv() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();
			
		}
		
		return builder.build();
		
	}
	

	
	// http://localhost:8080/opusbeaute-0/obws/rdv/remove/$idRdv
	// Mettre l id du rdv
	// POSTMAN
	// 		DELETE	Authorisation	Body	Json	
	@DefineUserRole({"PRATICIEN"})
	@DELETE
    @Path("/del/{idRdv: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheRdv(
			@PathParam("idRdv") final Integer idRdv) throws RdvInexistantException {

		Response.ResponseBuilder builder = null;		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande de suppression du Rdv id : " + idRdv + " dans la Bdd.");
			rdvService.suppressiondunrdv(idRdv);
			logger.info("RdvWs log : Rdv id : " + idRdv + " a bien ete modifie dans la Bdd.");	
			String msg = "RdvWs log : le Rdv id : " + idRdv + " a ien ete supprime de la Bdd.";
			builder = Response.ok(msg);

		} catch (RdvInexistantException message ) {
			logger.error("RdvWs log : Rdv id : " + idRdv + " ne peut etre supprime dans la Bdd.");	
			builder = Response.status(Response.Status.BAD_REQUEST);
			
		}
		
		return builder.build();
		
		}
		
}



	


