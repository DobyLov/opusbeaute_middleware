package fr.labonbonniere.opusbeaute.middleware.intercepteur;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;


@Provider
public class CORSFilter implements ContainerResponseFilter {

	   @Override
	   public void filter(final ContainerRequestContext requete,
	                      final ContainerResponseContext reponse) throws IOException {
	      reponse.getHeaders().add("Access-Control-Allow-Origin", "*");
	      reponse.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
	      reponse.getHeaders().add("Access-Control-Allow-Credentials", "true");
	      reponse.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
	   }

	}