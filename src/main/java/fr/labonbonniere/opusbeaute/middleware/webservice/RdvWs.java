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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.RdvInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.DateConversionException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.NoRdvException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvDateIncorrecteException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvNouveauDateFinChevaucheRdvExistantDateDebutException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvNouveauEnglobeParRdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvEgaliteChevauchementException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvIdPraticienProblemeException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvNonIntegrableException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvNouveauEnglobeRdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvNouveauDateDebutChevaucheRdvExistantDateFinException;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.RdvService;
import fr.labonbonniere.opusbeaute.middleware.service.rdv.TimestampToZoneDateTimeConvertionException;

/**
 * WebService REST Gestion des RDV's
 * Liste des rdv, cherche un rdv, ajout, modifie, supprime 
 * 
 * @author fred
 *
 */
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
	private static final Logger logger = LogManager.getLogger(RdvWs.class.getName());

	@EJB
	private RdvService rdvService;

	/**
	 * Renvoie la liste totale des RDV
	 * 
	 * @return Response List
	 * @throws DaoException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
	@Path("/liste")	
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
	
	/**
	 * renvoie la liste totale de Rdv
	 * d un client
	 * 
	 * @param idClient Integer
	 * @return Response Rdv
	 * @throws DaoException Exception
	 */
	//	http://localhost:8080/opusbeaute-0/obws/rdv/list/$idClient
	@DefineUserRole({"ALLOWALL"})
	@GET
//    @Path("/listerdvparclient/{idClient: \\d+}") // fonctionne bien 11/07
	@Path("/listerdvparclient")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParClient(
			@QueryParam("idClient") final Integer idClient ) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		
		try {
		logger.info("-----------------------------------------------------");			
		logger.info("WebService log - Demande a la bdd la list des Rdv avec IdClient :  " + idClient);
		List<Rdv> listeRdv = rdvService.recupereListRdvParClient(idClient);
		logger.info("WebService log - Le Liste rdv demande transmise");
		builder = Response.ok(listeRdv);		

		} catch (DaoException message) {
			logger.error("WebService log - La Liste de rdv demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
			
		}	
		
		return builder.build();
		
	}
	
	/**
	 * Renvoie la liste de Rdv
	 * Par jourJJ et par Client
	 * 
	 * @param date String
	 * @param idClient Integer
	 * @return Response List
	 * @throws DaoException Exception
	 */
	// regex qui gere le format yyyy-mm-dd numeric separe par "-" 
	// le regex va un petit peu plus loin :
	// yyyy => 2010 a 2029
	// mm => 01 à 12
	// dd => 01 à 31
	// !!! le regex ne gere pas le 30 fevrier ! a completer dans la partie service / metiers
	@DefineUserRole({"ALLOWALL"})
	@GET
//	@Path("/listepardateparpraticien/{dateJJ: (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{idPraticien: \\d+}")
	@Path("/listepardateparclient")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParDateJJParClient(
			@QueryParam("date") final String date,
			@QueryParam("idClient") final Integer idClient) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par date selectionnee.");
			final List<Rdv> listerdvpardate = rdvService.recupereLaListeRdvParDateJJClient(date, idClient);
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par date selectionnee.");
			builder = Response.ok(listerdvpardate);

		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la date.");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		
		} 		
		return builder.build();
	}

	/**
	 * Renvoie la liste de Rdv
	 * par plage de date 
	 * dateA et dateB par Client
	 * 
	 * @param dateA	String
	 * @param dateB String
	 * @param idClient Integer
	 * @return Response List
	 * @throws DaoException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
//	@Path("/listeparplagedateparpraticien/{dateA:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{dateB:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{idPraticien: //d+}")
	@Path("/listeparplagedateparclient")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParplageDateParClient(
					@QueryParam("dateA") String dateA,
					@QueryParam("dateB") String dateB,
					@QueryParam("idClient") Integer idClient) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par plage de dates selectionnees.");
			final List<Rdv> listerdvpardate = rdvService.recupereLaListeRdvParPlageDateClient(dateA, dateB, idClient);
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			builder = Response.ok(listerdvpardate);
		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la/des date(s).");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		}
		return builder.build();
		
	}

	
	
	/**
	 * renvoie la liste totale de Rdv
	 * d un Praticien
	 * 
	 * @param idPraticien Integer
	 * @return Response Rdv
	 * @throws DaoException Exception
	 */
	//	http://localhost:8080/opusbeaute-0/obws/rdv/list/$idClient
	@DefineUserRole({"ALLOWALL"})
	@GET
//    @Path("/listerdvparclient/{idClient: \\d+}") // fonctionne bien 11/07
	@Path("/listerdvparpraticien")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParPraticien(
			@QueryParam("idPraticien") final Integer idPraticien ) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		
		try {
		logger.info("-----------------------------------------------------");			
		logger.info("WebService log - Demande a la bdd la list des Rdv avec IdPraticien :  " + idPraticien);
		List<Rdv> listeRdv = rdvService.recupereListRdvParPraticien(idPraticien);
		logger.info("WebService log - Le Liste rdv demande transmise");
		builder = Response.ok(listeRdv);		

		} catch (DaoException message) {
			logger.error("WebService log - La Liste de rdv demande est introuvable");
			builder = Response.status(Response.Status.NOT_FOUND);
			
		}	
		
		return builder.build();
		
	}

	
	/**
	 * Renvoie la liste de Rdv
	 * Par jourJJ et par praticien
	 * 
	 * @param date String
	 * @param idPraticien Integer
	 * @return Response List
	 * @throws DaoException Exception
	 */
	// regex qui gere le format yyyy-mm-dd numeric separe par "-" 
	// le regex va un petit peu plus loin :
	// yyyy => 2010 a 2029
	// mm => 01 à 12
	// dd => 01 à 31
	// !!! le regex ne gere pas le 30 fevrier ! a completer dans la partie service / metiers
	@DefineUserRole({"ALLOWALL"})
	@GET
//	@Path("/listepardateparpraticien/{dateJJ: (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{idPraticien: \\d+}")
	@Path("/listepardateparpraticien")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParDateJJParPraticien(
			@QueryParam("date") final String date,
			@QueryParam("idPraticien") final Integer idPraticien) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par date selectionnee.");
			final List<Rdv> listerdvpardate = rdvService.recupereLaListeRdvParDateJJPraticien(date, idPraticien);
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par date selectionnee.");
			builder = Response.ok(listerdvpardate);

		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la date.");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		
		} 		
		return builder.build();
	}
	
	/**
	 * Renvoie la liste de Rdv
	 * par plage de date 
	 * dateA et dateB par Praticien
	 * 
	 * @param dateA	String
	 * @param dateB String
	 * @param idPraticien Integer
	 * @return Response List
	 * @throws DaoException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
//	@Path("/listeparplagedateparpraticien/{dateA:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{dateB:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{idPraticien: //d+}")
	@Path("/listeparplagedateparpraticien")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParplageDateParPraticien(
					@QueryParam("dateA") String dateA,
					@QueryParam("dateB") String dateB,
					@QueryParam("idPraticien") Integer idPraticien) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par plage de dates selectionnees.");
			final List<Rdv> listerdvpardate = rdvService.recupereLaListeRdvParPlageDatePraticien(dateA, dateB, idPraticien);
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			builder = Response.ok(listerdvpardate);
		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la/des date(s).");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		}
		return builder.build();
		
	}
	
	/**
	 * Retrouve un Rdv par son Id
	 * 
	 * @param idRdv Integer
	 * @return Response Rdv
	 * @throws RdvInexistantException Exception
	 */
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

	/**
	 * Retourne la liste des Rdv's du jour renseigne
	 * 
	 * @param date String
	 * @return Response List
	 * @throws DaoException Exception
	 */
	// regex qui gere le format yyyy-mm-dd numeric separe par "-" 
	// le regex va un petit peu plus loin :
	// yyyy => 2010 a 2029
	// mm => 01 à 12
	// dd => 01 à 31
	// !!! le regex ne gere pas le 30 fevrier ! a completer dans la partie service / metiers
	@DefineUserRole({"ALLOWALL"})
	@GET
//	@Path("/listepardate/{listeRdvDateDuJour: (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}")
	@Path("/listepardate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParDate (
			@QueryParam("date") final String date ) throws DaoException {		
		
		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par date selectionnee.");
			final List<Rdv> listerdvpardate = rdvService.recupereListeRdvParDate(date);
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par date selectionnee.");
			builder = Response.ok(listerdvpardate);

		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la date.");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		
		} 		
		return builder.build();

	}
	/**
	 * Retourne une liste de Rdv via une plage de jour
	 * 
	 * @param dateA String
	 * @param dateB String
	 * @return Response List
	 * @throws DaoException Exception
	 */
	@DefineUserRole({"ALLOWALL"})
	@GET
//	@Path("/listeplagedate/{RdvPlageJourA:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}-{RdvPlageJourB:  (20[1-2][0-9])-(0[1-9]|10|11|12)-(0[1-9]|1[0-9]|2[0-9]|3[0-1])}")
	@Path("/listeplagedate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParPlageDate (
			@QueryParam("dateA") final String dateA, 
			@QueryParam("dateB") final String dateB) throws DaoException {
		
		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande au RdvService la liste des Rdv's par plage de dates selectionnees.");
			final List<Rdv> listerdvpardate = rdvService.recupereListeRdvViaPlageDate( dateA, dateB );
			logger.info("RdvWs log : Transmission de la Liste des Rdv's par plage de dates selectionnees.");
			builder = Response.ok(listerdvpardate);
		} catch (Exception message) {
			logger.error("RdvWs Exception : probleme sur le format de la/des date(s).");
			builder = Response.status(Response.Status.NOT_ACCEPTABLE);
		}
		return builder.build();
	}
	
	/**
	 * Ajoute un Rdv
	 * 
	 * @param rdv Rdv
	 * @return Response
	 * @throws RdvExistantException Exception
	 * @throws DaoException Exception
	 * @throws RdvEgaliteChevauchementException Exception
	 * @throws NoRdvException Exception
	 * @throws RdvNouveauDateFinChevaucheRdvExistantDateDebutException Exception
	 * @throws RdvNouveauDateDebutChevaucheRdvExistantDateFinException Exception
	 * @throws RdvNouveauEnglobeRdvExistantException Exception
	 * @throws RdvNouveauEnglobeParRdvExistantException 
	 * @throws RdvNonIntegrableException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 * @throws RdvDateIncorrecteException 
	 * @throws DateConversionException 
	 * @throws RdvIdPraticienProblemeException 
	 */
	//	http://localhost:8080/opusbeaute-0/obws/rdv/add
	// ne pas mettre l idRdv, ajouter a l' objet et les parametres RDV	
	// POSTMAN
	//		POST	BODY	raw		JSON	
//	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@DefineUserRole({"ALLOWALL"})
	@POST
	@Path("/add")	// fonctionne bien 11/07
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnRdv(Rdv rdv) throws RdvExistantException, DaoException, 
											RdvEgaliteChevauchementException, NoRdvException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException, 
											RdvNouveauDateDebutChevaucheRdvExistantDateFinException, RdvNouveauEnglobeRdvExistantException, TimestampToZoneDateTimeConvertionException, RdvNonIntegrableException, RdvNouveauEnglobeParRdvExistantException, RdvDateIncorrecteException, DateConversionException, RdvIdPraticienProblemeException {
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
		
	/**
	 * Modifie le Rdv
	 * 
	 * @param rdv Rdv
	 * @return Response 
	 * @throws DateConversionException 
	 * @throws RdvDateIncorrecteException 
	 * @throws DaoException 
	 * @throws RdvNouveauDateDebutChevaucheRdvExistantDateFinException 
	 * @throws RdvNouveauEnglobeRdvExistantException 
	 * @throws RdvNouveauEnglobeParRdvExistantException 
	 * @throws RdvNouveauDateFinChevaucheRdvExistantDateDebutException 
	 * @throws RdvNonIntegrableException 
	 * @throws TimestampToZoneDateTimeConvertionException 
	 * @throws RdvIdPraticienProblemeException 
	 */
	//	http://localhost:8080/opusbeaute-0/obws/rdv/mod
	// Mettre l idRdv de l objet et les parametres a modifier rdv
	// POSTMAN
	//		PUT
	@DefineUserRole({"PRATICIEN","STAGIAIRE"})
	@PUT
	@Path("/mod") // fonctionne bien 11/07
//	@Produces(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnRdv (Rdv rdv) throws TimestampToZoneDateTimeConvertionException, RdvNonIntegrableException, RdvNouveauDateFinChevaucheRdvExistantDateDebutException, RdvNouveauEnglobeParRdvExistantException, RdvNouveauEnglobeRdvExistantException, RdvNouveauDateDebutChevaucheRdvExistantDateFinException, DaoException, RdvDateIncorrecteException, DateConversionException, RdvIdPraticienProblemeException {
		
		Response.ResponseBuilder builder = null;
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande de modification du Rdv id : " + rdv.getIdRdv() + " dans la Bdd.");
			rdvService.modifDuRdv(rdv);
			logger.info("RdvWs log : Rdv id : " + rdv.getIdRdv() + " a bien ete modifie dans la Bdd.");
			String msg = "Rdv id : " + rdv.getIdRdv() + " a bien ete modifie dans la Bdd.";
			builder = Response.ok(msg);
			
		} catch (RdvInexistantException message) {
			logger.error("RdvWs log : Rdv id : " + rdv.getIdRdv() + " ne peut etre modifie dans la Bdd.");
			builder = Response.notModified();
			
		}
		
		return builder.build();
		
	}
	
	/**
	 * Efface un Rdv
	 * 
	 * @param idRdv Inetger
	 * @return Response
	 * @throws RdvInexistantException Exception
	 */
	// http://localhost:8080/opusbeaute-0/obws/rdv/del/$idRdv
	// Mettre l id du rdv
	// POSTMAN
	// DELETE	Authorisation	Body	Json	
	@DefineUserRole({"PRATICIEN"})
//	@DefineUserRole({"ALLOWALL"})
	@DELETE
    @Path("/del/{idRdv: \\d+}")
    @Produces(MediaType.TEXT_PLAIN)
	public Response deleteTheRdv(
			@PathParam("idRdv") final Integer idRdv) throws RdvInexistantException {

		Response.ResponseBuilder builder = null;		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("RdvWs log : Demande de suppression du Rdv id : " + idRdv + " dans la Bdd.");
			rdvService.supprimerUnrdv(idRdv);
			logger.info("RdvWs log : Rdv id : " + idRdv + " a bien ete modifie dans la Bdd.");	
			String msg = "Rdv id: " + idRdv + " supprime de la Bdd.";
			builder = Response.ok(msg);

		} catch (RdvInexistantException message ) {
			logger.error("RdvWs log : Rdv id : " + idRdv + " ne peut etre supprime dans la Bdd.");	
			builder = Response.status(Response.Status.BAD_REQUEST);
			
		}
		
		return builder.build();
		
		}		
}