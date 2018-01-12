package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Stateless
public class TokenValidator {

	private static final Logger logger = LogManager.getLogger(TokenValidator.class);
	// ici on va valider le token que l utilisateur à présenté
	
	public boolean validationTkn (String token) throws Exception{
		
		
		// Recupération de la clée
		String sClee = new Clee().getCleeDuToken();
		logger.info("TokenValidator log :  Recuperation de la clee secrete : " + sClee);
	
		boolean tokenIsValid = false;
		
		try {
			logger.info("TokenValidator log :  Analyse du token :");
		    logger.info("TokenValidator log :  Token : " + token);
		    
		    Jws<Claims> parsToken = Jwts.parser().setSigningKey(sClee).parseClaimsJws(token);
		    
		    logger.info("TokenValidator log :  Le Token est compatible avec la clee secrete :)");
		
		    Date expirationDateToken = parsToken.getBody().getExpiration();
		    expirationDateChecker(expirationDateToken);
		    
		    String role = (String) parsToken.getBody().get("role");
		    logger.info("TokenValidator log : JWT Role : " + role);
			// definition de la date du jour

		    //OK, we can trust this JWT
		    tokenIsValid = true;
	
		} catch (SignatureException e) {
			tokenIsValid = false;
			logger.error("TokenValidator Exception :  Le Token fourni est non valide :( ");
			throw new Exception("TokenValidator Exception :  Le Token fourni est non valide :( ");
		    //don't trust the JWT!
			
//		} catch (TokenExpiredException message) {
//			
//			//The JWT as expired!
		} catch (JwtException e) {
			logger.error("TokenValidator log : La date de validite du token a expiree");
			throw new TokenExpiredException("TokenValidator Exception :  La date de validite du token a expiree");
		}
		
		return tokenIsValid;
		
	}
	
	public boolean expirationDateChecker (Date expirationDateToken) throws TokenExpiredException {
		
		// afin de definir la TimeZone ne 
//		long nowMillis = 
				System.currentTimeMillis();
//		Date instDateTime = new Date(nowMillis);
//		instDateTime.toString();
		
		boolean isExpired = true;	
		
		Date now = new Date();
		logger.info("TokenValidator log : ExpirationToken DateNowToInstant : " + now);
		
		logger.info("TokenValidator log : ExpirationToken DateExpirationToInstant : " + expirationDateToken);
		
		if(now.toInstant().isAfter(expirationDateToken.toInstant())) {
			
			logger.error("TokenValidator log : La date de validite du token a expiree");
			isExpired = true;
			throw new TokenExpiredException("TokenValidator Exception :  La date de validite du token a expiree");
			
		} else {
			logger.info("TokenValidator log : La date de validite du token est ok");
			isExpired = false;
		}
		
		return isExpired;
	}
	
}
