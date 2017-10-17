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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv.Rdv;
import fr.labonbonniere.opusbeaute.middleware.service.RdvService;

@Stateless
@Path("/rdv")
public class RdvWs {

	@EJB
	private RdvService rdvService;
	
	//	http://localhost:8080/opusbeaute-0/obws/rdvs
	@GET
	@Path("/rdvs")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListe() {
		
		Response.ResponseBuilder builder = null;		
		final List<Rdv> liste = rdvService.recupereListeRdv();
		builder = Response.ok(liste);
		return builder.build();
		
	}
	
	
	@GET
	@Path("/rdvspardate/{listeRdvDateDuJour}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParDate (@PathParam("listeRdvDateDuJour") final String listeRdvDateDuJour ) {
		
		Response.ResponseBuilder builder = null;
		final List<Rdv> listerdvpardate = rdvService.recupereListeRdvParDate(listeRdvDateDuJour);
		builder = Response.ok(listerdvpardate);
		return builder.build();
	}
	
	@GET
	@Path("/rdvsplagedate/{RdvPlageJourA},{RdvPlageJourB}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response laListeRdvParPlageDate (@PathParam("RdvPlageJourA") final String RdvPlageJourA, 
										@PathParam("RdvPlageJourB") final String RdvPlageJourB) {
		
		Response.ResponseBuilder builder = null;
		final List<Rdv> listerdvpardate = rdvService.recupereListeRdvViaPlageDate(RdvPlageJourA, RdvPlageJourB );
		builder = Response.ok(listerdvpardate);
		return builder.build();
	}
	
	
	
	//	http://localhost:8080/opusbeaute-0/obws/rdv/$idRdv
	@GET
    @Path("/{idRdv}") // fonctionne bien 11/07
	@Produces(MediaType.APPLICATION_JSON)
	public Response theRdv(@PathParam("idRdv") final Integer idRdv) {

		Response.ResponseBuilder builder = null;		
		Rdv rdv = rdvService.recupererUnRdv(idRdv);
		builder = Response.ok(rdv);
		return builder.build();

		}
	

	
	
	//	http://localhost:8080/opusbeaute-0/obws/rdv/modifrdv
	// Mettre l idRdv de l objet et les parametres a modifier rdv
	// POSTMAN
	//		PUT
	@PUT
	@Path("/modifrdv") // fonctionne bien 11/07
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifieUnRdv (Rdv rdv) {
		
		Response.ResponseBuilder builder = null;
		rdvService.modifduRdv(rdv);
		String msg = "{ " + '"'  + " Action réalisée :->" + '"' + " : " +  '"' + "rdv modifié" + '"' + "}";
		builder = Response.ok(msg);
		return builder.build();
		
	}
	
	//	http://localhost:8080/opusbeaute-0/obws/rdv/addrdv
	// ne pas mettre l idRdv, ajouter a l' objet et les parametres RDV	
	// POSTMAN
	//		POST	BODY	raw		JSON
	@POST
	@Path("/addrdv")	// fonctionne bien 11/07
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creerUnRdv(Rdv rdv) {
		
		Response.ResponseBuilder builder = null;
		rdvService.ajoutRdv(rdv);
		builder = Response.ok(rdv);
		return builder.build();
	}
	
	// http://localhost:8080/opusbeaute-0/obws/rdv/remove/$idRdv
	// Mettre l id du rdv
	// POSTMAN
	// 		DELETE	Authorisation	Body	Json	
	@DELETE
    @Path("/remove/{idRdv}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteTheRdv(@PathParam("idRdv") final Integer idRdv) {

		Response.ResponseBuilder builder = null;		
		rdvService.suppressiondunrdv(idRdv);
		String msg = "{ " + '"'  + " Action réalisée :->" + '"' + " : " +  '"' + "rdv supprime" + '"' + "}";
		builder = Response.ok(msg);
		return builder.build();

		}
		
}



	


