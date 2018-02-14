package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.utilisateur.UtilisateurService;

/**
 * Gestion du Login Verifie les credentiel de l utilisateur Genere un Token
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
	TokenGenService tokengen;

	@EJB
	PasswordHandlerService passwordHandler;

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
				logger.info("LoginService log : L utilisateur : " + loginexist.getPrenomUtilisateur()
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
	 * @return boolean
	 * @throws Exception Exception
	 */
	private boolean checkCredentiels(Utilisateur utilisateurReconnu, String pwd) throws Exception {

		boolean credentialsMatch = false;
		logger.info("LoginService log : Verification du mot de passe :");

		boolean pwdAndHashMatch = false;
		try {
			// check si le mot de passe match
			// logger.info("LoginService log : Le mot de passe saisi par l
			// utilisateur : " + pwd);
			logger.info("LoginService log : Le mot de passe Hash dans la BDD : " + utilisateurReconnu.getMotDePasse());

			pwdAndHashMatch = passwordHandler.ashVerifier(pwd, utilisateurReconnu.getMotDePasse());
			utilisateurReconnu.setMotDePasse(pwd);
			utilisateurService.modifierUnUtilisateur(utilisateurReconnu);

			logger.info("LoginService log : Nouveau Hash persiste.");

			if (pwdAndHashMatch == false) {
				logger.error("LoginService log : Le mot de passe ne correspond pas");
				throw new UtilisateurInexistantException();

			} else {

				logger.info("LoginService log : Mot de passe ok");
				credentialsMatch = true;
			}

		} catch (Exception e) {
			logger.error("LoginService Exception : Le password ne match pas");
			throw new Exception("LoginService Exception : Le mot de passe ne correspond pas");
		}

		return credentialsMatch;

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

		String idUtilisateur = utilisateurReconnu.getIdUtilisateur().toString();
		String prenomUtilisateur = utilisateurReconnu.getPrenomUtilisateur();
		String emailUtilisateur = utilisateurReconnu.getAdresseMailUtilisateur();
		String roleUtilisateur = utilisateurReconnu.getRoles().getRoles();

		try {

			// The issued token must be associated to a user
			// Return the issued token
			logger.error("LoginService log : Tentive de creation du JWT");
			token = tokengen.CreationDuJWT(idUtilisateur, prenomUtilisateur, emailUtilisateur, roleUtilisateur);
			// logger.info("LoginService log : Token genere =>" +
			// token.toString());

		} catch (Exception e) {
			logger.info("LoginService log : Le token n a pas ete genere");
		}
		return token;
	}

}
