package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;

/**
 * Gestion du Login Verifie les credentiels de l utilisateur Genere un Token
 * 
 * @author fred
 *
 */
@Stateless
public class LoginService {

	static final Logger logger = LogManager.getLogger(LoginService.class);

	@EJB
	UtilisateurService utilisateurService;

	@EJB
	TokenService tokenservice;

	@EJB
	PasswordHandlerService passwordHandlerService;

	/**
	 * Genere un Token Utilisateur
	 * 
	 * @param email String
	 * @param pwd String
	 * @return String
	 * @throws Exception Exception
	 * @throws UtilisateurInexistantException Exception
	 */
	public String tokenGenAtLogin(String email, String pwd) throws Exception, UtilisateurInexistantException {

		String token = null;

		try {

			Utilisateur utilisateurReconnu = chercheUtilisateur(email);

			boolean credentialsMatching = checkCredentiels(utilisateurReconnu, pwd);

			if (credentialsMatching == true) {

				token = generationDuToken(utilisateurReconnu);

			}

		} catch (Exception e) {
			throw new Exception("LoginService Exception : Le JWT ne sera cree.");
		}

		return token;

	}

	/**
	 * Recherche un Utilisateur Par son Email
	 * 
	 * @param email String
	 * @return loginExist 
	 * @throws Exception Exception
	 * @throws UtilisateurInexistantException Exception
	 */
	private Utilisateur chercheUtilisateur(String email) throws Exception, UtilisateurInexistantException {

		logger.error("LoginService log : Recherche de l email : " + email + " dans la base.");
		Utilisateur loginexist = null;

		try {

			loginexist = utilisateurService.recupererUnUtilisateurViaeMail(email);

			if (loginexist != null) {
				logger.info("LoginService log : L utilisateur a ce prenom : " + loginexist.getPrenomUtilisateur()
						+ " a ete trouve avec le mail : " + loginexist.getAdresseMailUtilisateur());
			}

		} catch (Exception e) {
			logger.error("LoginService log : L email : " + email + " n est associe a aucun utilisateur dans la base");
			throw new Exception("LoginService Exception : Le mail n est associe a aucun utilisateur");
		}

		return loginexist;
	}

	/**
	 * Verification des credentiels
	 * Si utilisateur trouve dans la bdd
	 * 
	 * @param utilisateurReconnu Utilisateur
	 * @param pwd String
	 * @return boolean credentialsMatchDateValid
	 * @throws Exception Exception
	 */
	private boolean checkCredentiels(Utilisateur utilisateurReconnu, String pwd) throws Exception {

		boolean credentialsMatchDateValid = false;
		logger.info("LoginService log : Verification du mot de passe :");

		
		try {
			// check si le mot de passe match
			// logger.info("LoginService log : Le mot de passe saisi par l
			// utilisateur : " + pwd);
			logger.info("LoginService log : Le mot de passe Hash dans la BDD : " + utilisateurReconnu.getMotDePasse());

			boolean pwdAndHashMatch = passwordHandlerService.ashVerifier(pwd, utilisateurReconnu.getMotDePasse());
			boolean isPwdIsExpired = isPwdValidityExpired(utilisateurReconnu);
			
			if ( pwdAndHashMatch == true ) {
				
				if (isPwdIsExpired == true) { 
					credentialsMatchDateValid = false;
					logger.error("LoginService log : la date de validite du mot de passe dans la BDD est expiree");
					throw new PwdExpirationDateException("LoginService Exception : le mot de passe est expire");
				} else {
					credentialsMatchDateValid = true;
					logger.info("LoginService log : la date du mot de passe BDD est valide");
				}
				
				utilisateurReconnu.setMotDePasse(pwd);
				utilisateurService.modifierUnUtilisateur(utilisateurReconnu);
				logger.info("LoginService log : Mot de passe ok");
				credentialsMatchDateValid = true;
				logger.info("LoginService log : Nouveau Hash persiste.");
			
			} else {
				logger.error("LoginService log : pwdAndHashMatch : " + pwdAndHashMatch);
				logger.error("LoginService log : Le mot de passe ne correspond pas");
				throw new UtilisateurInexistantException();
				
			}				

		} catch (Exception e) {
			logger.error("LoginService Exception : Le password ne match pas");
			throw new Exception("LoginService Exception : Le mot de passe ne correspond pas");
		}

		return credentialsMatchDateValid;

	}

	/**
	 * Creation d un Token JWT
	 *  
	 * Id utilisateur, Prenom utilisateur, Email Utilisateur, Role utilisateur
	 * 
	 * @param utilisateurReconnu Utilisateur
	 * @return String
	 */
	private String generationDuToken(Utilisateur utilisateurReconnu) {
		// Issue a token (can be a random String persisted to a database or a
		// JWT token)
		logger.info("LoginService log : Generation du token :");
		String token = null;

		try {

			// The issued token must be associated to a user
			// Return the issued token
			logger.info("LoginService log : Tentive de creation du JWT");
			token = tokenservice.CreationDuJWT(utilisateurReconnu.getIdUtilisateur().toString(), utilisateurReconnu.getPrenomUtilisateur(), utilisateurReconnu.getAdresseMailUtilisateur(), utilisateurReconnu.getRoles().getRoles());
			// logger.info("LoginService log : Token genere =>" +
			// token.toString());

		} catch (Exception e) {
			logger.info("LoginService log : Le token n a pas ete genere");
		}
		return token;
	}
	
	/**
	 * Verifie si la date du mot de passe ne depasse pas JJ
	 * 
	 * @param utilisateur Utilisateur 
	 * @return boolean
	 */
	private boolean isPwdValidityExpired(Utilisateur utilisateur) {
		
		boolean isPwdExpired = true;
		// definition de la date du jourJ selon la zone de Paris
		Timestamp tsjj = Timestamp.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris")).toInstant());
		// extrait la date d expiration du mot de passe de l utilisateur
		Timestamp userPwdDate = utilisateur.getPwdExpirationDateTime();
		
		// verifie si lutilisateur est root
		// si c est le cas ne pas verifier la date d expiration.
		if (utilisateur.getPrenomUtilisateur().toLowerCase().matches("root") 
				&& utilisateur.getNomUtilisateur().toLowerCase().matches("root")) {
			
			logger.info("LoginService log : L'utilisateur " + utilisateur.getPrenomUtilisateur().toString()
					+"/" + utilisateur.getNomUtilisateur().toString()
					+ " n est pas concerne par le check de validite du mot de passe");
			isPwdExpired = false;
			
		} else if (userPwdDate.after(tsjj)) {
			
			logger.info("LoginService log : le mot de passe dans la BDD n est pas expire");
			isPwdExpired = false;
			
		} else {
			
			logger.info("LoginService log : le mot de passe dans la BDD est expire");
			isPwdExpired = true;
			
		}
		// verifie si la validite de la date du mot de passe est expiree		
		
		
		return isPwdExpired;
		
	}

}
