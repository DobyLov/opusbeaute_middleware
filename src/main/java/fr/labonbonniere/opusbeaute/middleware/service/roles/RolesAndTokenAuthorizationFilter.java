package fr.labonbonniere.opusbeaute.middleware.service.roles;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.userRoles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.Clee;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenExpiredException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenInvalideException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@SecuApp
@Provider
//@Priority(value = 1)
public class RolesAndTokenAuthorizationFilter implements ContainerRequestFilter {

	// logger
	private static final Logger logger = LogManager.getLogger(RolesAndTokenAuthorizationFilter.class);

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		logger.info("RolesAuthorizationFilter log : Check des Roles Utilisateur sur la classe et Methode");
		try {
			checkAnnotation(requestContext);
		} catch (TokenInvalideException e) {
			logger.info("RolesAuthorizationFilter Exception : Probleme de token");
		} catch (TokenExpiredException e) {
			logger.info("RolesAuthorizationFilter Exception : Token Expire");
		}
	}

	private void checkAnnotation(ContainerRequestContext requestContext)
			throws TokenInvalideException, TokenExpiredException {

		Class<?> methodOnClass = resourceInfo.getResourceClass();
		Method method = resourceInfo.getResourceMethod();

		DefineUserRole defineUserRoleOnClass = methodOnClass.getAnnotation(DefineUserRole.class);
		DefineUserRole defineUserRoleOnMethod = method.getAnnotation(DefineUserRole.class);

		logger.info("RolesAuthorizationFilter log : WebService appelant Classe.Methode : "
				+ resourceInfo.getResourceClass().getSimpleName().trim().toUpperCase() + "."
				+ method.getName().trim().toUpperCase());

		try {

			logger.info("RolesAuthorizationFilter log : Sequence de detection de Roles.");
			if (defineUserRoleOnClass != null) {
				logger.info("RolesAuthorizationFilter log : Roles sur Classes detectes .");
				performClassAuthorization(defineUserRoleOnClass.value(), requestContext);

			} else {

				logger.info("RolesAuthorizationFilter log : Roles sur Classes Non detectes .");
				if (defineUserRoleOnMethod != null) {
					performMethodAuthorization(defineUserRoleOnMethod.value(), requestContext);

				} else {
					logger.info("RolesAuthorizationFilter log : Roles sur Methode Non detectes .");
					refuseRequest(requestContext);
				}
			}
		} catch (Exception message) {
			logger.error("RolesAuthorizationFilter Exception : Methode CheckAnnotation  refuseRequest(): ");

			refuseRequest(requestContext);
		}
	}

	private void performClassAuthorization(String[] defineUserRoleOnClass, ContainerRequestContext requestContext)
			throws Exception {

		Method method = resourceInfo.getResourceMethod();
		DefineUserRole defineUserRoleOnMethod = method.getAnnotation(DefineUserRole.class);

		logger.info("RolesAuthorizationFilter log : Methode  : performClassAuthorization:");

		if (defineUserRoleOnClass != null) {

			Integer numbersOfRolesOnClass = defineUserRoleOnClass.length;

			if (numbersOfRolesOnClass >= 0) {

				logger.info("RolesAuthorizationFilter log : Nombre de role definis sur la Classe : "
						+ numbersOfRolesOnClass);
				for (Integer i = 0; i < numbersOfRolesOnClass; i++) {

					if (defineUserRoleOnClass[i].trim().matches("ANONYMOUS")) {

						logger.info("RolesAuthorizationFilter log : Classe @nnotee par le role ANONYMOUS : ");
						return;
					}
				}

			}

			if (numbersOfRolesOnClass >= 0) {

				tokenIsValid(requestContext);
				logger.info("RolesAuthorizationFilter log : Performing Authorisation on Class");

				for (Integer j = 0; j < numbersOfRolesOnClass; j++) {

					if (defineUserRoleOnClass[j].trim().matches("ALLOWALL")) {
						logger.info("RolesAuthorizationFilter log : ALLOWALL @nnotee sur la Classe.");
						return;
					}

					if (defineUserRoleOnClass[j].trim().matches("DENYALL")) {
						logger.info("RolesAuthorizationFilter log : DENYALL @nnote sur la Classe.");
						refuseRequest(requestContext);
					}

				}

				String userRole = gettingUserroleInToken(requestContext);
				for (Integer k = 0; k < numbersOfRolesOnClass; k++) {

					if (defineUserRoleOnClass[k].trim().matches(userRole)) {
						logger.info("RolesAuthorizationFilter log : Role sur Classe detecte : " + defineUserRoleOnClass[k]);
						logger.info("RolesAuthorizationFilter log : Token UserRole : " 
															+ userRole 
															+ " & Classe @nnotation Role : " 
															+ defineUserRoleOnClass[k].trim().toString()
															+ " Match :) ");
						return;

					}
				}

			}
		}

		if (defineUserRoleOnMethod.value().length >= 0) {
			logger.info("RolesAuthorizationFilter log : L@nnotation de la classe ne match pas avec l UserRole :) ");
			performMethodAuthorization(defineUserRoleOnMethod.value(), requestContext);

		} else {

			refuseRequest(requestContext);
		}
	}

	private void performMethodAuthorization(String[] defineUserRoleOnMethod, ContainerRequestContext requestContext)
			throws Exception {

		Integer numbersOfRolesOnMethod = defineUserRoleOnMethod.length;
		logger.info("RolesAuthorizationFilter log : Nombre de role definis sur la Methode : " + numbersOfRolesOnMethod);

		if (numbersOfRolesOnMethod >= 0) {
			logger.info("RolesAuthorizationFilter log : roles detectes sur la methode");
			for (Integer i = 0; i < numbersOfRolesOnMethod; i++) {

				if (defineUserRoleOnMethod[i].trim().matches("ANONYMOUS")) {
					logger.info("RolesAuthorizationFilter log : ANONYMOUS @nnotee sur la Methode .");
					return;
				}
			}
		}

		if (numbersOfRolesOnMethod >= 0) {
			
			logger.info("RolesAuthorizationFilter log : Performing Authorisation on Method");			
			tokenIsValid(requestContext);
			
			for (Integer i = 0; i < numbersOfRolesOnMethod; i++) {

				if (defineUserRoleOnMethod[i].trim().matches("ALLOWALL")) {
					logger.info("RolesAuthorizationFilter log : ALLOWALL @nnotee sur la Classe.");
					return;
				}

				if (defineUserRoleOnMethod[i].trim().matches("DENYALL")) {
					logger.info("RolesAuthorizationFilter log : DENYALL @nnote sur la Classe.");
					refuseRequest(requestContext);
				}

			}
			logger.info("RolesAuthorizationFilter log : dernier boucle4");
			String userRole = gettingUserroleInToken(requestContext);
			for (Integer j = 0; j < numbersOfRolesOnMethod; j++) {
				if (defineUserRoleOnMethod[j].trim().matches(userRole)) {
					logger.info("RolesAuthorizationFilter log : Role sur Methode detecte : " + defineUserRoleOnMethod[j]);
					logger.info("RolesAuthorizationFilter log : Token UserRole : " 
							+ userRole 
							+ " & Methode @nnotation Role : " 
							+ defineUserRoleOnMethod[j].trim().toString()
							+ " Match :) ");
					return;
				}
			}
			
		}
		refuseRequest(requestContext);
	}

	private void refuseRequest(ContainerRequestContext requestContext) {
		logger.error("RolesAuthorisationFilter log : Acces refuse !!!!");
		requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
	}

	private String gettingUserroleInToken(ContainerRequestContext requestContext) {

		logger.info("RolesAuthorizationFilter log : Filtrage du Token :");

		final String AUTHENTICATION_SCHEME = "Bearer";
		String sClee = new Clee().getCleeDuToken();

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
		Jws<Claims> parsToken = Jwts.parser().setSigningKey(sClee).parseClaimsJws(token);
		String userRole = (String) parsToken.getBody().get("role");

		if (userRole.isEmpty()) {
			logger.info("RolesAuthorizationFilter log : Probleme de Token userRole non defini :( .");
			refuseRequest(requestContext);
		} else {
			logger.info("RolesAuthorizationFilter log : Recuperation Token userRole : " + userRole);
		}

		return userRole;
	}

	private boolean tokenIsValid(ContainerRequestContext requestContext)
			throws TokenExpiredException, TokenInvalideException {

		String sClee = new Clee().getCleeDuToken();
		final String AUTHENTICATION_SCHEME = "Bearer";

		logger.info("RolesAuthorizationFilter log : Validation du Token :");
		try {

			String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
			Jws<Claims> parsToken = Jwts.parser().setSigningKey(sClee).parseClaimsJws(token);
			logger.info("RolesAuthorizationFilter log : Etape 1/2 Signature Token Valide");

			Date expirationDateToken = parsToken.getBody().getExpiration();
			boolean tknDateIsValid = tknExpirationDateChecker(expirationDateToken);
			
			String userRole = gettingUserroleInToken(requestContext);

			if (!tknDateIsValid == false) {

				logger.error("RolesAuthorizationFilter log : Token InValide :(" + tknDateIsValid);
				refuseRequest(requestContext);
				throw new TokenInvalideException("RolesAuthorizationFilter Exception :Token Invalide :( ");
			}
			
			if (userRole.isEmpty()) {
				logger.error("RolesAuthorizationFilter log : Token InValide L userRole n est pas defini ." + tknDateIsValid);
				refuseRequest(requestContext);
				throw new TokenInvalideException("RolesAuthorizationFilter Exception :Token InValide L userRole n est pas defini :( ");
			}

			logger.info("RolesAuthorizationFilter log : Etape 2/2 Date Token Valide");
			return true;

		} catch (Exception e) {
			logger.error("RolesAuthorizationFilter log : Token InValide :(");
			refuseRequest(requestContext);
			throw new TokenInvalideException("RolesAuthorizationFilter Exception : Token Invalide :( ");
		}
	}

	private boolean tknExpirationDateChecker(Date expirationDateToken) throws TokenExpiredException {

		System.currentTimeMillis();
		boolean isExpired = true;
		Date now = new Date();
		logger.info("RolesAuthorizationFilter log : ExpirationToken DateNowToInstant : " + now);
		logger.info("RolesAuthorizationFilter log : ExpirationToken DateExpirationToInstant : " + expirationDateToken);
		if (now.toInstant().isAfter(expirationDateToken.toInstant())) {
			logger.error("RolesAuthorizationFilter log : La date de validite du token a expiree");
			isExpired = true;
			throw new TokenExpiredException(
					"RolesAuthorizationFilter Exception :  La date de validite du token a expiree");

		} else {
			logger.info("RolesAuthorizationFilter log : La date de validite du token est ok");
			isExpired = false;
		}

		return isExpired;

	}

}
