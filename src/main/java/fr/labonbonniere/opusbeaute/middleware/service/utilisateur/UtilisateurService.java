package fr.labonbonniere.opusbeaute.middleware.service.utilisateur;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.UtilisateurDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.GeneratePwdNewUtilisateurService;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.PasswordHandlerService;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailNotFoundException;

/**
 * Gere les objets Utilisateurs
 * 
 * @author fred
 *
 */
@Stateless
public class UtilisateurService {
	static final Logger logger = LogManager.getLogger(UtilisateurService.class);

	@EJB
	private UtilisateurDao utilisateurdao;

	@EJB
	private PasswordHandlerService passwordHandler;

	@EJB
	private GeneratePwdNewUtilisateurService generatepwdnewutilisateurservice;

	/**
	 * Recupere une Liste d Utilisateur
	 * 
	 * @return List
	 * @throws DaoException
	 *             Si pb Bdd
	 */
	public List<Utilisateur> recupereListeUtilisateur() throws DaoException {

		try {

			logger.info("UtilisateurService log : Demande au Dao la liste des Utilisateurs");
			List<Utilisateur> lalisteUtilisateur = utilisateurdao.obtenirListeUtilisateur();
			logger.info("UtilisateurService - Liste des Utilisateurs recuperee");
			return lalisteUtilisateur;

		} catch (DaoException message) {
			logger.error("UtilistaeurService log : Probleme de la bdd.");
			throw new DaoException("UtilistaeurService Exception : Probleme de la bdd.");
		}
	}

	/**
	 * Recupere un Utilisateur via son Id
	 * 
	 * @param idUtilisateur
	 *            Integer
	 * @return Utilisateur
	 * @throws UtilisateurInexistantException
	 *             Si Utilisiteur inexistant
	 */
	public Utilisateur recupererUnUtilisateur(final Integer idUtilisateur) throws UtilisateurInexistantException {

		try {
			logger.info("UtilisateurService log : Demande a la bdd le Utilisateur id : " + idUtilisateur);
			Utilisateur utilisateur = utilisateurdao.obtenirUtilisateur(idUtilisateur);
			logger.info("UtilisateurService log : Utilisateur id : " + idUtilisateur
					+ " trouve, envoie de l objet Utilisateur a UtilisateursWS");
			return utilisateur;

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilisateurService log : L Utilisateur demande est introuvable");
			throw new UtilisateurInexistantException(
					"UtilisateurService Exception : l' Id est introuvable dans la base");
		}
	}

	/**
	 * Ajoute un nouvel Utilisateur
	 * 
	 * @param utilisateur
	 *            Utilisateur
	 * @throws UtilisateurExistantException
	 *             Si Utilisateur inexistant
	 * @throws EmailFormatInvalidException
	 *             si Format Email ne correspond pas
	 * @throws NbCharNomException
	 *             Si le nombre de caracteres ne correspond pas
	 * @throws NbCharPrenomException
	 *             Si le nombre de caracteres du prenom ne correspond pas
	 * @throws NbCharTelException
	 *             si le nombre de caracteres du numero de telephone ne
	 *             correspond pas
	 * @throws DaoException
	 *             si il y a un probleme avec la bdd
	 * @throws UtilisateurInexistantException
	 *             Exception
	 */
	public void ajoutUnUtilisateur(Utilisateur utilisateur)
			throws UtilisateurExistantException, EmailFormatInvalidException, NbCharNomException, NbCharPrenomException,
			NbCharTelException, UtilisateurInexistantException, DaoException {

		try {
			logger.info("UtilisateurService log : Demande d ajout d un nouvel Utilisateur dans la Bdd.");
			validationformat(utilisateur);
			// setInitialPwdExpirationDateTime(utilisateur);
			utilisateurdao.ajouterUnutilisateur(utilisateur);
			// envoyer un mail ave cles credentiels temporaires.
			envoyerUnMessageAvecCredetielsAuNouvelUtilisateur(utilisateur);
			logger.info("UtilisateurService log : Nouvelle Utilisateur ajoutee, avec l id : "
					+ utilisateur.getIdUtilisateur());

		} catch (UtilisateurExistantException message) {
			logger.error("UtilisateurService log : Impossible de creer cet Utilisateur dans la Bdd.");
			throw new UtilisateurExistantException(
					"UtilisateurService Exception : Impossible de creer cet Utilisateur dans la Bdd.");
		}
	}

	/**
	 * modifie un Utilisateur
	 * 
	 * @param utilisateur
	 *            Utilisateur
	 * @throws UtilisateurInexistantException
	 *             si utilisateur inexistant
	 * @throws EmailFormatInvalidException
	 *             Si Le format Email ne correspond pas
	 * @throws NbCharNomException
	 *             Si le nombre de caractere du Nom ne correspond pas
	 * @throws NbCharPrenomException
	 *             Si le nombre de caractere du Prenom ne correspond pas
	 * @throws NbCharTelException
	 *             Si le nombre de caractere ne correspond pas
	 */
	public void modifierUnUtilisateur(Utilisateur utilisateur) throws UtilisateurInexistantException,
			EmailFormatInvalidException, NbCharNomException, NbCharPrenomException, NbCharTelException {

		try {
			logger.info("UtilisateurService log : Demande de modification du Utilisateur id : "
					+ utilisateur.getIdUtilisateur() + " dans la Bdd.");
			validationformat(utilisateur);
			utilisateurdao.modifieUnUtilisateur(utilisateur);
			logger.info("UtilisateurService log : Utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " a ete modifie dans la Bdd.");

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilisateurService log : Utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " ne peut etre modifie dans la Bdd.");
			throw new UtilisateurInexistantException("UtilisateurService Exception : Utilisateur avec l id : "
					+ utilisateur.getIdUtilisateur() + " ne peut etre modifie.");
		}
	}

	/**
	 * Supprime un Utilisateur par son Id
	 * 
	 * @param idUtilisateur
	 * @throws UtilisateurInexistantException
	 */
	public void suppressionDUnUtilisateur(final Integer idUtilisateur) throws UtilisateurIneffacableException {

		logger.info("UtilisateurService log :Procedure pour effacer l'utilisateur " + idUtilisateur);

		try {

			this.effaceUtilisateur(idUtilisateur);
			logger.info("UtilisateurService log  : Utilisateur avec Id : " + idUtilisateur + " efface de la Bdd");

		} catch (UtilisateurInexistantException message) {

			logger.error("UtilisateurService log  : Utilisateur avec Id : " + idUtilisateur + " ineffacable");
			throw new UtilisateurIneffacableException(
					"UtilisateurService log  : Utilisateur avec Id : " + idUtilisateur + " ineffacable");
		}

	}

	/**
	 * Calcul la date du jour J en fonction de la Zone Europe/Paris et ajoute 90
	 * Jours
	 *
	 * @param utilisateur
	 *            Utilisateur
	 * @return utilisateur Utilisateur
	 */
	public Utilisateur setInitialPwdExpirationDateTime(Utilisateur utilisateur) {

		// definition de la date du jourJ selon la zone de Paris et ajoute 90Jrs
		Timestamp tsZDTP90J = Timestamp
				.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris")).plusMinutes(60).toInstant());

		utilisateur.setPwdExpirationDateTime(tsZDTP90J);

		return utilisateur;
	}

	/**
	 * Genere un mot de passe aleatoire Envoyer un mail avec un mot de passe
	 * temporaire
	 * 
	 * @param nouvelUtilisateur
	 *            Utilisateur
	 * @throws UtilisateurInexistantException
	 *             Exception
	 * @throws DaoException
	 *             Exception
	 */
	public void envoyerUnMessageAvecCredetielsAuNouvelUtilisateur(Utilisateur nouvelUtilisateur)
			throws UtilisateurInexistantException, DaoException {

		generatepwdnewutilisateurservice.genereatePwd(nouvelUtilisateur.getAdresseMailUtilisateur());
	}

	/**
	 * Valide les champs d un Utilisateur
	 * 
	 * @param utilisateur
	 *            Utilisateur
	 * @return Utilisateur
	 * @throws EmailFormatInvalidException
	 *             Exception
	 * @throws NbCharNomException
	 *             Exception
	 * @throws NbCharPrenomException
	 *             Exception
	 * @throws NbCharTelException
	 *             Exception
	 */
	public Utilisateur validationformat(Utilisateur utilisateur)
			throws EmailFormatInvalidException, NbCharNomException, NbCharPrenomException, NbCharTelException {

		// Password validation et encryption Bcrypt
		logger.info("UtilisateurService log : Test Utilisateur");
		if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty()) {
			logger.info("UtilisateurService log : Utilisateur.MotDePasse n est pas null.");
			if (utilisateur.getMotDePasse().length() < 1) {
				logger.error("UtilisateurService log : Utilisateur.MotDePasse est inferieur a 7 caracteres");
				logger.error("UtilisateurService log : Utilisateur.MotDePasse est rempli de force");
				utilisateur.setMotDePasse("ilfautdescaracteres");
				throw new EmailFormatInvalidException(
						"UtilisateurService Validation Exception : Utilisateur.MotDePasse est null ou depasse 30 caracteres");
			} else {
				logger.info("UtilisateurService log : Utilisateur.MotdePasse vas etre encrypte.");
				String pwdEncryptedTobddStorage = passwordHandler.hashPwd(utilisateur.getMotDePasse());
				utilisateur.setMotDePasse(pwdEncryptedTobddStorage);

			}

		} else {
			logger.error("UtilisateurService log : Utilisateur.MotDePasse est null.");
			logger.error("UtilisateurService log : Utilisateur.MotDePasse est rempli de force");
			utilisateur.setMotDePasse("ilfautdescaracteres");
			// throw new NbCharNomException("Utilisateur Service Validation
			// Exception : Client.MotDePass est Null");
		}

		// ---------------------------------------------------------------------------------------------------------
		// ok
		// Check Prenom est vide / depasse 30 caracteres.
		logger.info("UtilisateurService log : test Utilisateur.Prenom.");
		if (utilisateur.getPrenomUtilisateur() != null && !utilisateur.getPrenomUtilisateur().isEmpty()) {
			logger.info("UtilisateurService log : Utilisateur.Prenom n est pas null.");
			if (utilisateur.getPrenomUtilisateur().length() > 30) {
				logger.error("UtilisateurService log : Utilisateur.Prenom depasse 30 caracteres");
				utilisateur.setPrenomUtilisateur(null);
				throw new NbCharPrenomException(
						"Utilisateur Service Validation Exception : Utilisateur.Prenom est null ou depasse 30 caracteres");
			} else {
				logger.info("UtilisateurService log : Utilisateur.Prenom formate en Xxxxx.");
				String checkSpaceAtStrBeginAndCharacSpec = utilisateur.getPrenomUtilisateur();
				String StringWithoutSpaceAndCharspec = checkSpaceAtStrBeginAndCharacSpec(
						checkSpaceAtStrBeginAndCharacSpec);
				utilisateur.setPrenomUtilisateur(WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec, '-'));

			}

		} else {
			logger.error("UtilisateurService log : Utilisateur.Prenom est null.");
			utilisateur.setPrenomUtilisateur(null);
			throw new NbCharNomException("Utilisateur Service Validation Exception : Client.Prenom est Null");
		}

		// ok
		// Check Nom est vide / depasse 30 caracteres
		logger.info("Utilisateur Service log : test Utilisateur.Nom.");
		if (utilisateur.getNomUtilisateur() != null && !utilisateur.getNomUtilisateur().isEmpty()) {
			logger.info("Utilisateur Service log : Utilisateur.Nom n est pas null.");
			if (utilisateur.getNomUtilisateur().length() > 30) {
				logger.error("Utilisateur Service log : Utilisateur.Nom depasse 30 caracteres");
				utilisateur.setNomUtilisateur(null);
				throw new NbCharNomException(
						"Utilisateur Service Validation Exception : Utilisteur.Nom depasse 30 caracteres.");

			} else {
				logger.info("Utilisateur Service log : Utilisateur.Nom Nom formate en MAJ.");
				String checkSpaceAtStrBeginAndCharacSpec = utilisateur.getNomUtilisateur();
				String StringWithoutSpaceAndCharspec = checkSpaceAtStrBeginAndCharacSpec(
						checkSpaceAtStrBeginAndCharacSpec);
				utilisateur.setNomUtilisateur(StringWithoutSpaceAndCharspec.toUpperCase());
			}

		} else {
			logger.error("Utilisateur Service log : Utilisateur.Nom est null.");
			utilisateur.setNomUtilisateur(null);
			throw new NbCharNomException("Utilisateur Service Validation Exception : Utilisateur.Nom est Null");
		}

		// OK ---------------------------------------------------
		// Check Présence Numéro de portable égale à 10, commençant par 06 ou
		// 07.
		logger.info("Utilisateur Service log : test Utilisateur.TelMobile si null/ autre que 06/07.");

		if (utilisateur.getTeleMobileUtilisateur() != null && !utilisateur.getTeleMobileUtilisateur().isEmpty()) {
			logger.info("Utilisateur Service log : Utilisateur.TelMobile n est pas null :)");
			String checkSpaceAtStrBeginAndCharacSpec = utilisateur.getTeleMobileUtilisateur();
			String StringWithoutSpaceAndCharspec = strUniquemtNumero(checkSpaceAtStrBeginAndCharacSpec);
			utilisateur.setTeleMobileUtilisateur(StringWithoutSpaceAndCharspec);
			if (utilisateur.getTeleMobileUtilisateur().length() == 10) {

				if (utilisateur.getTeleMobileUtilisateur().substring(0, 2).equalsIgnoreCase("06") == true
						|| utilisateur.getTeleMobileUtilisateur().substring(0, 2).equalsIgnoreCase("07") == true) {

					logger.info("Utilisateur Service log : Utilisateur.TelMobile commence par : "
							+ utilisateur.getTeleMobileUtilisateur().substring(0, 2));
					logger.info("Utilisateur Service log : SuscribedSmsReminder True est envisageable");

				} else {
					logger.error("Utilisateur Service log : SuscribedSmsReminder TRUE n est pas envisageable "
							+ "car Utilisateur.TelMobile commence par : "
							+ utilisateur.getTeleMobileUtilisateur().substring(0, 2));
//					utilisateur.setSuscribedSmsReminder("F"); // Correction de type
					utilisateur.setSuscribedSmsReminder(false);
					logger.info("Utilisateur Service log : SuscribedSmsReminder force a etre FALSE");
				}

			} else {
				logger.info("Utilisateur Service log : Utilisateur.TelMobile comporte : "
						+ utilisateur.getTeleMobileUtilisateur().length() + " alors que 10 sont attendus.");
//				utilisateur.setSuscribedSmsReminder("F"); // Correction de Type
				utilisateur.setSuscribedSmsReminder(false);
				logger.info("Utilisateur Service log : SuscribedSmsReminder force a etre FALSE");
				utilisateur.setTeleMobileUtilisateur(null);
				throw new NbCharTelException(
						"Utilisateur Service Validation Exception : Utilisateur.TeleMobile numero <10< à caracteres");
			}

		} else {
			logger.info("Utilisateur Service log : TelMobileClient est null ou vide.)");
//			utilisateur.setSuscribedSmsReminder("F"); // Correction de Type
			utilisateur.setSuscribedSmsReminder(false); 
			logger.info("Utilisateur Service log : SuscribedSmsReminder force a etre FALSE");
			utilisateur.setTeleMobileUtilisateur(null);

		}

		// ok ------------------------------------------------------------
		// Check AdresseMail si nul si supperieur a 50 caracteres
		logger.info("Utilisateur Service log : test Client.AdresseMail.");

		if (utilisateur.getAdresseMailUtilisateur() != null && !utilisateur.getAdresseMailUtilisateur().isEmpty()) {

			logger.info("Utilisateur Service log : test Utilisateur.AdresseMail non null.");
			String checkSpaceAtStrBeginAndCharacSpec = utilisateur.getAdresseMailUtilisateur();
			String StringWithoutSpaceAndCharspec = checkSpaceAtStrBeginAndCharacSpecMail(
					checkSpaceAtStrBeginAndCharacSpec);
			utilisateur.setAdresseMailUtilisateur(StringWithoutSpaceAndCharspec);

			if (utilisateur.getAdresseMailUtilisateur().length() > 50) {
				logger.info(
						"Utilisateur Service log : Utilisateur.AdresseMail format non valide depasse 50 caracteres.");
//				utilisateur.setSuscribedMailReminder("F");// Correction de Type
				utilisateur.setSuscribedMailReminder(false);
				logger.info("Utilisateur Service log : SuscribedMailRemider force a FALSE");
				throw new EmailFormatInvalidException(
						"UtilisateurService Validation Exception : Utilisateur.Mail non valide");

			} else {
				Boolean emailFormatvalidation = isValidEmailAddress(utilisateur.getAdresseMailUtilisateur());

				if (emailFormatvalidation == false) {
					logger.info("Utilisateur Service log : Utilisateur.AdresseMail Format non valide : "
							+ emailFormatvalidation);
//					utilisateur.setSuscribedMailReminder("F"); // Correction de Type
					utilisateur.setSuscribedMailReminder(false); 
					logger.info("Utilisateur Service log : SuscribedMailRemider force a FALSE");
					throw new EmailFormatInvalidException(
							"UtilisateurService Validation Exception : Utilisateur.Mail non valide");

				}
			}

		} else {
			logger.info("Utilisateur Service log : Client.AdresseMail null ou vide.");
//			utilisateur.setSuscribedMailReminder("F");// Correction de Type
			utilisateur.setSuscribedMailReminder(false);
			logger.info("Utilisateur Service log : SuscribedMailRemider force a False");
			utilisateur.setAdresseMailUtilisateur(null);
		}

		return utilisateur;

	}

	/**
	 * Valide le format de l adresse Email
	 * 
	 * @param emailFormatvalidation
	 *            String
	 * @return boolean
	 */
	public boolean isValidEmailAddress(String emailFormatvalidation) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(emailFormatvalidation);
		logger.info("Utilisateur Service log : Check mail : " + m.matches());
		return m.matches();
	}

	/**
	 * Verifie si le champs ne commence pas par un espace et si possede des
	 * caracteres speciaux Si oui suppression de ceux ci
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec
	 *            String
	 * @return String
	 */
	// Verifier la String Si elle commence par un espace ou possede des
	// carcteres speciaux
	// Si c est le cas ne clash pas l appli mais reformate la String sans l
	// espace en debut et sans les carac Spec.
	public String checkSpaceAtStrBeginAndCharacSpec(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");

		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _"
					+ checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^a-zA-Z^-^é^è]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _"
					+ strWithoutSpaceAtBeginCheckedCSpec);

		} else {

			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^a-zA-Z^-^é^è]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}

		return strWithoutSpaceAtBegin;
	}

	/**
	 * Verifie si la String commence par un espace Verifie si la string comporte
	 * des caracteres speciaux
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec
	 *            String
	 * @return String
	 */
	public String strUniquemtNumero(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");

		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _"
					+ checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^0-9^-]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.error("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _"
					+ strWithoutSpaceAtBeginCheckedCSpec);

		} else {

			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^0-9^-]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}

		return strWithoutSpaceAtBegin;
	}

	/**
	 * Verifie si la String commence par un espace Verifie si la string comporte
	 * des caracteres speciaux
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec
	 *            String
	 * @return String
	 */
	public String checkSpaceAtStrBeginAndCharacSpecMail(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");

		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _"
					+ checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^.^@^a-zA-Z^-^é^è]", "");
			// strWithoutSpaceAtBeginCheckedCSpec =
			// strWithoutSpaceAtBegin.replaceAll("[^-^@^a-zA-Z^é^è]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _"
					+ strWithoutSpaceAtBeginCheckedCSpec);

		} else {

			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^.^@^a-zA-Z^-^é^è]", "");
			// strWithoutSpaceAtBegin =
			// checkSpaceAtBeginAndCharacSpec.replaceAll("[^-^@^a-zA-Z^é^è]",
			// "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}

		return strWithoutSpaceAtBegin.toLowerCase();
	}

	/**
	 * Retrouve un utilisateur par son email
	 * 
	 * @param email
	 *            String
	 * @return Utilisateur
	 * @throws UtilisateurInexistantException
	 *             Exception
	 */
	public Utilisateur recupererUnUtilisateurViaeMail(final String email) throws UtilisateurInexistantException {

		try {

			logger.info("UtilisateurService log : Demande a la bdd le Utilisateur email : " + email);

			Utilisateur utilisateur = utilisateurdao.utilisateurParEmail(email.toLowerCase());
			logger.info("UtilisateurService log : Utilisateur email : " + utilisateur.getAdresseMailUtilisateur()
					+ " trouve, envoie de l objet Utilisateur");

			return utilisateur;

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilisateurService log : L Utilisateur demande est introuvable");
			throw new UtilisateurInexistantException(
					"UtilisateurService Exception : Pas d utilisateur est trouve dans la base avec l email : " + email);
		}
	}

	/**
	 * Cherche si le mail fourni existe dans la table Utilisateur
	 * 
	 * @param email
	 *            String
	 * @return boolean
	 * @throws MailNotFoundException
	 *             Exception
	 */
	public boolean verifieSiAdresseMailFournieExisteDansUtilisateur(String email) throws MailNotFoundException {

		try {

			logger.info("UtilisateurService log : Demande si le mail " + email + " existe dans la table Utilisateur");
			if (utilisateurdao.checkMailExistInDB(email.toLowerCase()) >= 1) {
				logger.info("UtilisateurService log : Le mail " + email + " existe dans la table Utilisateur");

				return true;
			} else {
				logger.info("UtilisateurService log : Le mail " + email + " n existe pas dans la table Utilisateur");

				return false;
			}

		} catch (MailNotFoundException message) {
			logger.info("UtilisateurService log : Lee mail " + email + " n existe pas dans la table Utilisateur");
			return false;

		} catch (Exception message) {
			logger.error(
					"UtilisateurService Exception : Lee mail " + email + " n existe pas dans la table Utilisateur");
			return false;
		}

	}

	/**
	 * Effacer l'utilisateur par son Id en vérifiant que l utilisateur existe,
	 * que l utilisateur a le status Effacable puis l efface
	 * 
	 * @param idUtilisateurToSwipe
	 * @return Boolean
	 * @throws UtilisateurIneffacableException
	 * @throws UtilisateurInexistantException
	 * @throws UtilisateurInexistantException
	 */
	private Boolean effaceUtilisateur(Integer idUtilisateurToSwipe)
			throws UtilisateurIneffacableException, UtilisateurInexistantException {

		logger.info("UtilisateurService log : Verification de la possibilite de supprimer l utilisateur id : "
				+ idUtilisateurToSwipe);

		try {
			// Recuperer lutilisateur via son ID
			logger.info("UtilisateurService log : recherche Utilisateur id : " + idUtilisateurToSwipe);
			Utilisateur utilisateur = this.recupererUnUtilisateur(idUtilisateurToSwipe);
			// si l'utilisateur existe verifier qui a le status effacable
			this.checkIfUserIsErasable(utilisateur);
			// si le compte a le status effacable l'effacer
			this.effacerUtilisateurFourniParSonId(idUtilisateurToSwipe);

		} catch (UtilisateurIneffacableException message) {
			logger.error("UtilisateurService log  : Utilisateur avec Id : " + idUtilisateurToSwipe + " ineffacable");
			throw new UtilisateurIneffacableException(
					"UtilisateurService Exception  : Utilisateur avec Id : " + idUtilisateurToSwipe + " ineffacable");
		}

		return true;

	}

	/**
	 * Verifie le status EFFACABLEde l'utilisateur avent de l effacer
	 * 
	 * @param utilisateur
	 *            Utilisateur
	 * @return Boolean
	 */
	private Boolean checkIfUserIsErasable(Utilisateur utilisateur) throws UtilisateurIneffacableException {

		logger.info("UtilisateurService log : Verification si l utilisateur a le status Effacable");

		if (utilisateur.getCompteEffacable().booleanValue() == false) {

			logger.error("UtilisateurService log : Cet utilisateur ne peut etre efface a cause de son status");

			throw new UtilisateurIneffacableException(
					"UtilisateurService Exception : Le status de l'utilisateur ne permet pas de leffacer");

		}

		logger.info("UtilisateurService log : Cet utilisateur peut etre efface");
		return true;

	}

	/**
	 * Effacer L'utilisateur par son Id
	 * 
	 * @param Integer idUtilisateur
	 * @throws UtilisateurInexistantException
	 */
	private boolean effacerUtilisateurFourniParSonId(Integer idUtilisateur) throws UtilisateurInexistantException {

		try {

			logger.info("UtilisateurService log : Demande de suppression de l Utilisateur id : " + idUtilisateur
					+ " dans la Bdd.");
			utilisateurdao.supprimeUnUtilisateur(idUtilisateur);
			logger.info(
					"UtilisateurService log : Utilisateur id : " + idUtilisateur + " a bien ete supprime de la Bdd.");

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilisateurService log : Utilisateur id : " + idUtilisateur
					+ " ne peut etre supprime dans la Bdd.");
			throw new UtilisateurInexistantException("UtilisateurService Exception : Utilisateur id : " + idUtilisateur
					+ " ne peut etre supprime dans la Bdd.");
		}

		return true;

	}

}
