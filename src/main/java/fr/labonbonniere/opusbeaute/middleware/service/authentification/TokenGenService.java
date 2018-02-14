package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Gestionnaire de token
 * Creation, verification
 * 
 * 
 * @author fred
 *
 */
@Stateless
public class TokenGenService {

	static final Logger logger = LogManager.getLogger(TokenGenService.class);

	// Sample method to construct a JWT
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
		logger.info("TokenGen log : Recuperation de la clee secrete.");

		// Definition du temps de validite du token
		long ttlMillis = ((60 * 1000) * 60) * 3; // 1minute = 60 000 milisecondes :}
//		long ttlMillis = ((60 * 1000) * 1); reduction à 1 min pour les tests
		logger.info("TokenGen log : Validite du token : " + ttlMillis);

		// definition de la date du jour
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		logger.info("TokenGen log : Date du jour : " + now);

		// definition de l expiration 3 Heures apres creation du token
		long expMillis = nowMillis + ttlMillis;
		Date exp = new Date(expMillis);
		logger.info("TokenGen log : Le token expirera a : " + exp);

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		logger.info("TokenGen log : JcaName de l agorithme : " + signatureAlgorithm.getJcaName());
//		logger.info("TokenGen log : SignatureKey de la signature : " + cleeSecrete);

		// We will sign our JWT with our ApiKey secret
		 byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(cleeSecrete);
//		 logger.info("TokenGen log : Conversion en byte de la cleesecrete : " + apiKeySecretBytes.toString());
		 
//		 Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//		 logger.info("TokenGen log : Generation de la signature : " + signingKey);

		// Let's set the JWT Claims
		 
		
		JwtBuilder token = Jwts.builder()
				.setHeaderParam("type", "JWT")
				.claim("idUser", idUtilisateur)
				.claim("role",roleUtilisateur)
				.claim("prenom", prenomUtilisateur)
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(signatureAlgorithm, apiKeySecretBytes);

		// Builds the JWT and serializes it to a compact, URL-safe string
		logger.info("TokenGen log : Ok token genere  => " + token.compact());

		return token.compact();
	}
}
