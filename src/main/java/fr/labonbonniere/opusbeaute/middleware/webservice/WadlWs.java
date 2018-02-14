package fr.labonbonniere.opusbeaute.middleware.webservice;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.wadl.WadlFileReaderService;

/**
 * Gere la Production du WADL (Web Application Document Language)
 * Pour qu une application tiers puisse recuperer
 * la liste des Ressources Web REST 
 * du middleware OpusBeaute
 * 
 * @author fred
 *
 */

@Stateless
@Path("/")
@DefineUserRole({"ANONYMOUS"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR","PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"PRATICIEN","STAGIAIRE"})
//@DefineUserRole({"ALLOWALL"})
//@DefineUserRole({"DENYALL"})
//@DefineUserRole({"ROOT","ADMINISTRATEUR"})
public class WadlWs {

	private static final Logger logger = LogManager.getLogger(WadlWs.class.getName());
	
	@EJB
	WadlFileReaderService wadlFileReaderService;
	
	/**
	 * Retourne le fichier WADL au format XML.
	 * 
	 * @return Response
	 */
	@GET
	@Path("/wadl")
	@Produces(MediaType.APPLICATION_XML)
//	@Produces(MediaType.TEXT_PLAIN)	
//	@Produces({"text/plain", "text/html" , "application/json"})
//	@Produces(MediaType.APPLICATION_JSON)
	public Response produceWadl() {

		Response.ResponseBuilder builder = null;
		
		try {
			logger.info("-----------------------------------------------------");
			logger.info("WadlWs log : Demande de generation du Wadl");
			String xmlWadl = wadlFileReaderService.WadlFileReader();
			builder = Response.ok(xmlWadl);
//			logger.info("WadlWs log : Fichier Wadl : " + xmlWadl);
			logger.info("WadlWs log : Wadl genere :)");
			
		} catch (Exception message) {
			logger.info("WadlWs log : Wadl non genere");
			builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return builder.build();
		
	}

}
