package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.Date;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class TestFiltreAlege implements ContainerRequestFilter {

	// logger
	private static final Logger logger = LogManager.getLogger(TestFiltreAlege.class);

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		logger.info("RolesAuthorisationFilter log : Filtrage Role Utilisateur : ");

		try {
			tokenIsValid(requestContext);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String classe = resourceInfo.getResourceClass().getSimpleName().trim();
		Method method = resourceInfo.getResourceMethod();
		logger.info("RolesAuthorisationFilter log : Classe.Methode : " + classe.toUpperCase() +"." 
															+  method.getName().trim().toUpperCase());

		
		// @DenyAll on the method takes precedence over @RolesAllowed and
		// @PermitAll
		logger.info("RolesAuthorisationFilter log : Detection @nnotation DenyAll");
		if (method.isAnnotationPresent(DenyAll.class)) {
			logger.info("RolesAuthorisationFilter log : Etat Annotation : " + method.isAnnotationPresent(DenyAll.class));
			refuseRequest(requestContext);
		} else {
			logger.info("RolesAuthorisationFilter log : L @nnotation DenyAll non trouvee");
		}

	
		// @RolesAllowed on the method takes precedence over @PermitAll
		logger.info("RolesAuthorisationFilter log : Detection @nnotation RolesAllowed sur Methode");
		RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
		if (rolesAllowed != null) {
			logger.info("RolesAuthorisationFilter log : rolesAllowed." + rolesAllowed.toString().trim());
			try {
				performAuthorization(rolesAllowed.value(), requestContext);
				logger.info("RolesAuthorisationFilter log : revenu ds la methode ");
				logger.info("RolesAuthorisationFilter log : is Secure : " + requestContext.getSecurityContext().isSecure());
				logger.info("RolesAuthorisationFilter log : " + requestContext.getSecurityContext().isUserInRole("PRATICIEN"));
			return;
			
			} catch (Exception message) {
				logger.info("RolesAuthorisationFilter log : Detection @nnotation RolesAllowed non trouvee ou invalide");
//				e.printStackTrace();
			}
			return;
		}

		// @PermitAll on the method takes precedence over @RolesAllowed on the
		// class
		logger.info("RolesAuthorisationFilter log : Detection @nnotation PermitAll sur Methode");
		if (method.isAnnotationPresent(PermitAll.class)) {
			logger.info("RolesAuthorisationFilter log : @nnotation PermitAll trouve");
			// Do nothing
			return;

		} else {
			logger.info("RolesAuthorisationFilter log : @nnotation DenyAll non trouvee");
		}

		
		// @DenyAll can't be attached to classes
		// @RolesAllowed on the class takes precedence over @PermitAll on the ** precedence = priorite
		// class
		logger.info("RolesAuthorisationFilter log : Detection @nnotation RolesAllowed su Classe");
		rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
		if (rolesAllowed != null) {
			logger.info("RolesAuthorisationFilter log : @nnotation RolesAllowed trouvee");
			try {
				logger.info("RolesAuthorisationFilter log : Test authoristion:");
				performAuthorization(rolesAllowed.value(), requestContext);
				logger.info("RolesAuthorisationFilter log : Authorisations accordee");
			} catch (Exception e) {
				logger.info("RolesAuthorisationFilter log : Acces non authorise");
//				e.printStackTrace();
			}
		} else {
			logger.info("RolesAuthorisationFilter log : @nnotation RolesAllowed non trouvee");
		}

		
		// @PermitAll on the class
		logger.info("RolesAuthorisationFilter log : Detection @nnotation PermitAll sur Classe");
		if (resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
			logger.info("RolesAuthorisationFilter log : @nnotation PermitAll trouve");
			// Do nothing
			return;
		} else {
			logger.info("RolesAuthorisationFilter log : @nnotation PermitAll non trouvee");
		}

		
		// Authentication is required for non-annotated methods
		try {
			if (!isAuthenticated(requestContext)) {
				logger.info("RolesAuthorisationFilter log : Authentification NOK");
				refuseRequest(requestContext);
			} else {
				logger.info("RolesAuthorisationFilter log : Authentification OK");
			}
		} catch (Exception e) {
			logger.info("RolesAuthorisationFilter Exception : Authentification OK");
//			e.printStackTrace();
		}

	}

	private void performAuthorization(String[] rolesAllowed, ContainerRequestContext requestContext) throws Exception {

		logger.info("RolesAuthorisationFilter log : Performing Authorisation");

		final String AUTHENTICATION_SCHEME = "Bearer";
		String sClee = new Clee().getCleeDuToken();
		logger.info("RolesAuthorisationFilter log : Filtrage du Token :");

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
//		logger.info("RolesAuthorisationFilter log : Token Brut : " + token);
		Jws<Claims> parsToken = Jwts.parser().setSigningKey(sClee).parseClaimsJws(token);
		String userRole = (String) parsToken.getBody().get("role");
		logger.info("RolesAuthorisationFilter log : Token userRole : " + userRole);
		logger.info("RolesAuthorisationFilter log : Role defini sur la Methode : " + rolesAllowed[0]);
		String rolesAllowedZeroPoz = rolesAllowed[0].toString();
		if (!rolesAllowedZeroPoz.contentEquals(userRole)) {
			logger.info("RolesAuthorisationFilter log : probleme de role");
			refuseRequest(requestContext);

		} else {
			logger.info("RolesAuthorisationFilter log : Roles Matches");			
			return;
		}
		logger.info("RolesAuthorisationFilter log : retour");
	}

	private boolean isAuthenticated(final ContainerRequestContext requestContext) throws Exception {

		boolean tokIsValid = false;		
		tokenIsValid(requestContext);
		tokIsValid = true;
		logger.info("RolesAuthorisationFilter log : token is valid : " + tokIsValid);
		
		return tokIsValid;
	}

	private void refuseRequest(ContainerRequestContext requestContext) throws AccessDeniedException {
		requestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN).build());
		throw new AccessDeniedException(
				"RolesAuthorisationFilter log : You don't have permissions to perform this action.");
	}



	private boolean tokenIsValid(ContainerRequestContext requestContext) throws TokenExpiredException, TokenInvalideException {

		String sClee = new Clee().getCleeDuToken();
		final String AUTHENTICATION_SCHEME = "Bearer";

		logger.info("RolesAuthorisationFilter log : Validation du Token :");
		try {
			
			String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
			Jws<Claims> parsToken = Jwts.parser().setSigningKey(sClee).parseClaimsJws(token);
			logger.info("RolesAuthorisationFilter log : Etape 1/2 Signature Token Valide");
			
		    Date expirationDateToken = parsToken.getBody().getExpiration();
		    boolean tknDateIsValid = tknExpirationDateChecker(expirationDateToken);
		    
		    if (!tknDateIsValid == false) {
		    	
		    	logger.error("RolesAuthorisationFilter log : Token InValide :(" + tknDateIsValid);
		    	throw new TokenInvalideException("RolesAuthorisationFilter Exception :Token Invalide :( ");
		    }
		    
		    logger.info("RolesAuthorisationFilter log : Etape 2/2 Date Token Valide");
			return true;
			
		}catch (Exception e) { 
			logger.error("RolesAuthorisationFilter log : Token InValide :(");
			throw new TokenInvalideException("RolesAuthorisationFilter Exception :Token Invalide :( ");
		}
	}
	
	public boolean tknExpirationDateChecker (Date expirationDateToken) throws TokenExpiredException {
		
		// afin de definir la TimeZone ne 
//		long nowMillis = 
		System.currentTimeMillis();
//		Date instDateTime = new Date(nowMillis);
//		instDateTime.toString();
		
		boolean isExpired = true;	
		
		Date now = new Date();
		logger.info("RolesAuthorisationFilter log : ExpirationToken DateNowToInstant : " + now);
		
		logger.info("RolesAuthorisationFilter log : ExpirationToken DateExpirationToInstant : " + expirationDateToken);
		
		if(now.toInstant().isAfter(expirationDateToken.toInstant())) {
			
			logger.error("RolesAuthorisationFilter log : La date de validite du token a expiree");
			isExpired = true;
			throw new TokenExpiredException("RolesAuthorisationFilter Exception :  La date de validite du token a expiree");
			
		} else {
			logger.info("RolesAuthorisationFilter log : La date de validite du token est ok");
			isExpired = false;
		}
		
		return isExpired;
	}

}
