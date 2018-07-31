package fr.labonbonniere.opusbeaute.middleware.service.roles;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerException;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.DefineUserRole;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.SecuApp;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenExpiredException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenRoleInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenService;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenSignatureInvalidException;

/**
 * Filtre l acces aux methode
 * par verification de la validite 
 * du token fourni par l utilisateur
 * puis L acces est valide selon le Role 
 * defini dans le Token 
 * 
 * @author fred
 *
 */
@SecuApp
@Provider
@Priority(value = 1)
public class RolesAndTokenAuthorizationFilter implements ContainerRequestFilter {

	// logger
	private static final Logger logger = LogManager.getLogger(RolesAndTokenAuthorizationFilter.class);

	@Context
	private ResourceInfo resourceInfo;
	
	@EJB
	private TokenService tokenservice;

	/**
	 * Methode qui declanche 
	 * le filtrage de la requete http 
	 * 
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		logger.info("RolesAuthorizationFilter log : Check des Roles Utilisateur sur la classe et Methode");
		try {
			checkAnnotation(requestContext);
		} catch (TokenInvalidException e) {
			logger.info("RolesAuthorizationFilter Exception : Probleme de token");
		} catch (TokenExpiredException e) {
			logger.info("RolesAuthorizationFilter Exception : Token Expire");
		}
	}

	/**
	 * Scan des Annotations @DefineUserRole
	 * Positionnees sur les Classes et methodes
	 * du WebService a l origine de l appel de la ressource
	 * 
	 * @param requestContext RequestContext
	 * @throws TokenInvalidException Exception
	 * @throws TokenExpiredException Exception
	 */
	private void checkAnnotation(ContainerRequestContext requestContext)
			throws TokenInvalidException, TokenExpiredException {

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

	/**
	 * Verifie si presence sur la Classe
	 * de l annotation @DefineUserRole
	 * 
	 * et execute ou non  la requete 
	 * 
	 * @param defineUserRoleOnClass String[]
	 * @param requestContext RequeestContext
	 * @throws Exception Exception
	 */
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

				String userRole = gettingUserRoleInToken(requestContext);
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

	/**
	 * Verifie si presence sur la Methode
	 * de l annotation @DefineUserRole
	 * 
	 * et execute ou non  la requete
	 * 
	 * @param defineUserRoleOnMethod String[]
	 * @param requestContext RequestContext
	 * @throws Exception Exception
	 */
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
			String userRole = gettingUserRoleInToken(requestContext);
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

	/**
	 * Revoie un message Http
	 * d acces non autorise
	 * 
	 * @param requestContext RequestContext
	 */
	private void refuseRequest(ContainerRequestContext requestContext) {
		logger.error("RolesAuthorisationFilter log : Acces refuse !!!!");
		requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
		
	}

	/**
	 * Recupere le Role utilisateur 
	 * dans le Token JWT
	 * 
	 * @param reqCtx ContainerRequestContext
	 * @return String
	 * @throws RoleInexistantException Exception
	 * @throws TokenInvalidException Exception
	 */
	private String gettingUserRoleInToken(ContainerRequestContext reqCtx) throws RoleInexistantException, TokenInvalidException {
		
		String token = this.reqCtxTokenextractor(reqCtx);
		String userRoleExtracted = tokenservice.roleExtractorFromToken(token);
		logger.info("RolesAuthorizationFilter log : Recuperation Token userRole : " + userRoleExtracted);
		return userRoleExtracted;
		
	}
	
	/**
	 * Extrait le Token du Header de la requete
	 * @param reqCtx ContainerRequestContext
	 * @return String
	 * @throws ContainerException Exception
	 */
	private String reqCtxTokenextractor(ContainerRequestContext reqCtx) throws ContainerException {
		logger.info("RolesAuthorizationFilter log : Extraction du Token dans le Header de la requete :");
		final String AUTHENTICATION_SCHEME = "Bearer";

		try {
			logger.info("RolesAuthorizationFilter log : verification du Header");
			if (reqCtx.getHeaders().isEmpty() ) {
				logger.error("RolesAuthorizationFilter log : Header isEmpty:");
				throw new ContainerException("RolesAuthorizationFilter Exception : le container a un Header vide");
			}
			
			String authorizationHeader = reqCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
			logger.info("RolesAuthorizationFilter log : Header.Authorization : " + authorizationHeader);
			
			String tokenExtracted = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();			
			logger.info("RolesAuthorizationFilter log : Token Extrait : " + tokenExtracted);
			
			return tokenExtracted;
			
		} catch (ContainerException message) {
			throw new ContainerException("RolesAuthorizationFilter Exception : le container a un Header HS");
		}
		
	}

	/**
	 * Retourne si la Token est Valide
	 * 
	 * @param requestContext Requestcontext
	 * @return Boolean
	 * @throws TokenExpiredException Exception
	 * @throws TokenInvalidException Exception
	 * @throws TokenRoleInvalidException Exception
	 * @throws TokenSignatureInvalidException Exception
	 * @throws ContainerException Exception
	 */
	private boolean tokenIsValid(ContainerRequestContext requestContext) throws TokenExpiredException, TokenInvalidException, ContainerException, TokenSignatureInvalidException, TokenRoleInvalidException {

		logger.info("RolesAuthorizationFilter log : Procedure de Validation du Token :");
		
		try {
			
			if (!tokenservice.checkTokenValidity(this.reqCtxTokenextractor(requestContext))) {
				this.refuseRequest(requestContext);
			}
			
			return true;
			
		} catch (TokenInvalidException message) {
			logger.error("RolesAuthorizationFilter log : Token InValide :(");			
			throw new TokenInvalidException("RolesAuthorizationFilter Exception : Token Invalide :( ");
		}

	}

}
