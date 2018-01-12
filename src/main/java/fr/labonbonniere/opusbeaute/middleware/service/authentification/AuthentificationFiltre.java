package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.SignatureException;

@SecuApp
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthentificationFiltre implements ContainerRequestFilter {
	
	@EJB
	TokenValidator tokenValidator;
	
	private static final Logger logger = LogManager.getLogger(AuthentificationFiltre.class);
	//public class AuthenticationFilter implements ContainerRequestFilter {

	    private static final String REALM = "example";
	    private static final String AUTHENTICATION_SCHEME = "Bearer";

	    @Override
	    public void filter(ContainerRequestContext requestContext) throws IOException {
	    	
	    	logger.info("AuthentificationFiltre log : Filtrage du header pour extraction du token");

	        // Get the Authorization header from the request
	        String authorizationHeader =
	                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

	        // Validate the Authorization header
	        if (!isTokenBasedAuthentication(authorizationHeader)) {
	            abortWithUnauthorized(requestContext);
	            return;
	        }

	        // Extract the token from the Authorization header
	        String token = authorizationHeader
	                            .substring(AUTHENTICATION_SCHEME.length()).trim();

	        try {

	            // Validate the token
	            validateToken(token);

	        } catch (Exception e) {
	            abortWithUnauthorized(requestContext);
	        }
	    }

	    
	    private boolean isTokenBasedAuthentication(String authorizationHeader) {

	        // Check if the Authorization header is valid
	        // It must not be null and must be prefixed with "Bearer" plus a whitespace
	        // The authentication scheme comparison must be case-insensitive
	        return authorizationHeader != null && authorizationHeader.toLowerCase()
	                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	    }


	    private void validateToken(String token) throws Exception {
	    	
	    	logger.info("AuthentificationFiltre log :  Tentative de validation du token");
	        // Check if the token was issued by the server and if it's not expired
	        // Throw an Exception if the token is invalid
	    	try {
	    		
	    		tokenValidator.validationTkn(token);
	    		
	    	} catch (SignatureException message) {
	    		logger.error("AuthentificationFiltre Exception : La Signature du token Nok.");
	    		throw new SignatureException("AuthentificationFiltre Exception : La Signature du token est Nok.");
	    	
	    	} catch (TokenExpiredException message) {
	    		logger.error("AuthentificationFiltre Exception : Date Token expiree.");
	    		throw new SignatureException("AuthentificationFiltre Exception : La Signature du token est Nok.");
	    	}
	    }
	    
	    
	    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

	        // Abort the filter chain with a 401 status code response
	        // The WWW-Authenticate header is sent along with the response
	        requestContext.abortWith(
	                Response.status(Response.Status.UNAUTHORIZED)
	                        .header(HttpHeaders.WWW_AUTHENTICATE, 
	                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
	                        .build());
	    }
}

