package fr.labonbonniere.opusbeaute.middleware.service.rgpd;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.Clee;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Stateless
public class TokenGenClientForRgpgAction {
	
	static final Logger logger = LogManager.getLogger(TokenGenClientForRgpgAction.class.getSimpleName());


	public String tokenGenForclientRgpdAction(Client client) throws UnsupportedEncodingException {

		// recupere la clee pour la signature
		Clee clee = new Clee();
		String cleeSecrete = clee.getCleeDuToken();
		logger.info("TokenGenClientForRgpgAction log : Recuperation de la clee secrete.");

		// Definition du temps de validite du token
//		long ttlMillis = ((60 * 1000) * 60) * 3; // 1minute = 60 000 milisecondes 
		long ttlMillis = ((60 * 1000) * 20); //reduction à 1 min pour les tests
		logger.info("TokenGenClientForRgpgAction log : Validite du token : " + ttlMillis);

		// definition de la date du jour
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		logger.info("TokenGenClientForRgpgAction log : Date du jour : " + now);

		// definition de l expiration 3 Heures apres creation du token
		long expMillis = nowMillis + ttlMillis;
		Date exp = new Date(expMillis);
		logger.info("TokenGenClientForRgpgAction log : Le token expirera a : " + exp);

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		logger.info("TokenGenClientForRgpgAction log : JcaName de l agorithme : " + signatureAlgorithm.getJcaName());

		// We will sign our JWT with our ApiKey secret
		 byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(cleeSecrete);		 
		
		JwtBuilder token = Jwts.builder()
				.setHeaderParam("type", "JWT")
				.claim("idClient", client.getIdClient())
				.claim("prenom", client.getPrenomClient())
				.claim("email", client.getAdresseMailClient())
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(signatureAlgorithm, apiKeySecretBytes);

		// Builds the JWT and serializes it to a compact, URL-safe string
		logger.info("TokenGenClientForRgpgAction log : Ok token genere  => " + token.compact());

		return token.compact();
	}

}



