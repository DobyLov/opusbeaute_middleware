package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Gestionnaire de token
 * Creation, verification
 *  
 * @author fred
 *
 */
@Stateless
public class TokenService {

	static final Logger logger = LogManager.getLogger(TokenService.class);

	/**
	 * Creation d un Token (JWT) 
	 * 
	 * @param idUtilisateur Integer
	 * @param prenomUtilisateur String
	 * @param emailUtilisateur String
	 * @param roleUtilisateur String
	 * @return String
	 * @throws UnsupportedEncodingException Exception
	 */
	public String CreationDuJWT(String idUtilisateur, String prenomUtilisateur, String emailUtilisateur,
			String roleUtilisateur) throws UnsupportedEncodingException {

		// recupere la clee pour la signature
		Clee clee = new Clee();
		String cleeSecrete = clee.getCleeDuToken();
		logger.info("TokenService log : Recuperation de la clee secrete.");

		// Definition du temps de validite du token
		long ttlMillis = ((60 * 1000) * 60) * 3; // 1minute = 60 000 milisecondes :}
//		long ttlMillis = ((60 * 1000) * 1); //reduction à 1 min pour les tests
		logger.info("TokenService log : Validite du token : " + ttlMillis);

		// definition de la date du jour
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		logger.info("TokenService log : Date du jour : " + now);

		// definition de l expiration 3 Heures apres creation du token
		long expMillis = nowMillis + ttlMillis;
		Date exp = new Date(expMillis);
		logger.info("TokenService log : Le token expirera a : " + exp);

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		logger.info("TokenService log : JcaName de l agorithme : " + signatureAlgorithm.getJcaName());

		// We will sign our JWT with our ApiKey secret
		 byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(cleeSecrete);
		JwtBuilder token = Jwts.builder()
				.setHeaderParam("type", "JWT")
				.claim("idUser", idUtilisateur)
				.claim("role",roleUtilisateur)
				.claim("prenom", convertCharAccentToWithoutaccent(prenomUtilisateur))
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(signatureAlgorithm, apiKeySecretBytes);

		// Builds the JWT and serializes it to a compact, URL-safe string
		logger.info("TokenService log : Ok token genere  => " + token.compact());

		return token.compact();
	}
	
	/**
	 * Verifie la validite du token fourni
	 * Verifie la signature, la date, et la recuperation du role Utilisateur
	 * @param token String
	 * @return Boolean
	 * @throws TokenInvalidException Exception
	 * @throws TokenSignatureInvalidException Exception
	 * @throws TokenExpiredException Exception
	 * @throws TokenRoleInvalidException Exception
	 */
	public Boolean checkTokenValidity(String token) throws TokenInvalidException, TokenSignatureInvalidException, TokenExpiredException, TokenRoleInvalidException {
	

		logger.info("TokenService log : Verification de la validite du Token");
		
		try {
			
			if (!this.checkTokenSignatureValidity(token)) {
				return false;
//				throw new TokenSignatureInvalidException("TokenService Exception : La Signature du token n est pas valide.");
			}
			
			if (!this.compareGivenDateIsGreaterThanJJ(this.tokenDateExtractor(token))) {
				return false;
//				throw new TokenExpiredException("TokenService Exception : La Date du token est expiree.");
			}
			
			if (this.roleExtractorFromToken(token).isEmpty()) {
				return false;
//				throw new TokenRoleInvalidException("TokenService Exception : Ce token ne dispose pas de Role");
			}
			logger.info("TokenService log : Fin de la procedure de verification du du Token");
			return true;
			
		} catch (JwtException message) {
			logger.error("TokenService log : Le token n est pas valide.");
			throw new TokenInvalidException("TokenService Exception : Le token n est pas valide.");
		}
		
	}
	
	/**
	 * Verifie si le token est signe
	 * @param token String
	 * @return Boolean
	 * @throws TokenSignatureInvalidException Exception
	 */
	public Boolean checkTokenSignatureValidity(String token) throws TokenSignatureInvalidException {
		
		logger.info("TokenService log : Check de la signature du token");
		String sClee = new Clee().getCleeDuToken();
		
		try {
			// 
			Claims claims = (Claims) Jwts.parser().setSigningKey(sClee).parse(token).getBody();
			String role = (String) claims.get("role");
			logger.info("TokenService log : La signature du token est valide lecture du role : " + role);
			
			logger.info("TokenService log : La signature du token est valide.");
			return true;
			
		} catch (JwtException message) {
			logger.error("TokenService log : Token non signe correctement.");
			throw new TokenSignatureInvalidException("TokenService Exception : Token non signe correctement");
		}		
	}
	
	/**
	 * Extraction de la Date depuis le Token
	 * @param token String
	 * @return Date
	 * @throws TokenExpiredException Exception
	 */
	public Date tokenDateExtractor(String token) throws TokenExpiredException {
		
		logger.info("TokenService log : Extraction de la date du Token");
		String sClee = new Clee().getCleeDuToken();
		
		try {
			
			Jws<Claims> parsToken = Jwts.parser().setSigningKey(sClee).parseClaimsJws(token);
			Date dateExtracted = (Date) parsToken.getBody().getExpiration();
			logger.info("TokenService log : Date du Token : " + dateExtracted.toString());
			return dateExtracted;
			
		} catch (JwtException message) {
			logger.error("TokenService log : La date du Token n est plus valide");
			throw new TokenExpiredException("TokenService Exception : La date du Token n est plus valide");
		}
		
		
	}
	
	/**
	 * Compare la date donnee si elle est superieur a la date du jour
	 * @param tokenDate Date
	 * @return boolean
	 * @throws TokenExpiredException Exception
	 */
	public Boolean compareGivenDateIsGreaterThanJJ(Date tokenDate) throws TokenExpiredException {
		
		System.currentTimeMillis();
		boolean isGreater = true;
		Date now = new Date();
		logger.info("TokenService log : ExpirationToken DateNowToInstant : " + now);
		logger.info("TokenService log : ExpirationToken DateExpirationToInstant : " + tokenDate);
		if (now.toInstant().isAfter(tokenDate.toInstant())) {
			logger.error("TokenService log : La date de validite du token a expiree");
			isGreater = false;
			throw new TokenExpiredException(
					"TokenService Exception :  La date de validite du token a expiree");

		} else {
			logger.info("TokenService log : La date de validite du token est ok");
			isGreater = true;
		}

		return isGreater;
	}
	
	/**
	 * Extraction du Role Utilisateur depuis le token fourni
	 * @param token String
	 * @return String
	 * @throws TokenInvalidException Exception
	 */
	public String roleExtractorFromToken(String token) throws TokenInvalidException {
		
		logger.info("TokenService log : Extraction du Role depuis le token.");
		String sClee = new Clee().getCleeDuToken();
		
		try {		
			
			Jws<Claims> parsToken = Jwts.parser().setSigningKey(sClee).parseClaimsJws(token);
			String roleExtracted = (String) parsToken.getBody().get("role");
			logger.info("TokenService log : Role Extrait depuis le token : " + roleExtracted);
			
			return roleExtracted;
			
		} catch (JwtException message) {
			logger.error("TokenService log : Il n y a pas role defini dans le token.");
			throw new TokenInvalidException("TokenService Exception : Il n y a pas role defini dans le token.");
		}
		
	}
	
	/**
	 * Remplace les accents aigues et graves de la String par un 'e'
	 * @param checkCahr String
	 * @return stringChar String 
	 */
	private String convertCharAccentToWithoutaccent(String checkCahr) {
		logger.error("TokenService log : Conversion si il y a des accents au prenom : " + checkCahr);
		String stringChar = checkCahr.replace("^è", "e");
		logger.error("TokenService log : Conversion si il y a des accents au prenom : " + stringChar);
		return stringChar;
	}
	
}
