package fr.labonbonniere.opusbeaute.middleware.service.utilisateur;

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
import fr.labonbonniere.opusbeaute.middleware.service.client.EmailFormatInvalid;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;

@Stateless
public class UtilisateurService {
	static final Logger logger = LogManager.getLogger(UtilisateurService.class);

	@EJB
	private UtilisateurDao utilisateurdao;

	public List<Utilisateur> recupereListeUtilisateur() throws DaoException {

		try {

			logger.info("UtilistaeurService log : Demande au Dao la liste des Utilisateurs");
			List<Utilisateur> lalisteUtilisateur = utilisateurdao.obtenirListeUtilisateur();
			logger.info("UtilistaeurService - Liste des Utilisateurs recuperee");
			return lalisteUtilisateur;

		} catch (DaoException message) {
			logger.error("UtilistaeurService log : Probleme de la bdd.");
			throw new DaoException("UtilistaeurService Exception : Probleme de la bdd.");
		}
	}

	public Utilisateur recupererUnUtilisateur(final Integer idUtilisateur) throws UtilisateurInexistantException {

		try {
			logger.info("UtilistaeurService log : Demande a la bdd le Utilisateur id : " + idUtilisateur);
			Utilisateur utilisateur = utilisateurdao.obtenirUtilisateur(idUtilisateur);
			logger.info("UtilistaeurService log : Utilisateur id : " + idUtilisateur
					+ " trouve, envoie de l objet Utilisateur a UtilisateursWS");
			return utilisateur;

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilistaeurService log : L Utilisateur demande est introuvable");
			throw new UtilisateurInexistantException(
					"UtilistaeurService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutUnUtilisateur(Utilisateur utilisateur) throws UtilisateurExistantException, EmailFormatInvalid, NbCharNomException, NbCharPrenomException, NbCharTelException {

		try {
			logger.info("UtilistaeurService log : Demande d ajout d un nouvel Utilisateur dans la Bdd.");
			validationformat(utilisateur);
			utilisateurdao.ajouterUnutilisateur(utilisateur);
			logger.info("UtilistaeurService log : Nouvelle Utilisateur ajoutee, avec l id : " + utilisateur.getIdUtilisateur());

		} catch (UtilisateurExistantException message) {
			logger.error("UtilistaeurService log : Impossible de creer cet Utilisateur dans la Bdd.");
			throw new UtilisateurExistantException(
					"UtilistaeurService Exception : Impossible de creer cet Utilisateur dans la Bdd.");
		}
	}

	public void modifierUnUtilisateur(Utilisateur utilisateur) throws UtilisateurInexistantException, EmailFormatInvalid, NbCharNomException, NbCharPrenomException, NbCharTelException {

		try {
			logger.info("UtilistaeurService log : Demande de modification du Utilisateur id : "
					+ utilisateur.getIdUtilisateur() + " dans la Bdd.");
			validationformat(utilisateur);			
			utilisateurdao.modifieUnUtilisateur(utilisateur);
			logger.info("UtilistaeurService log : Utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " a ete modifie dans la Bdd.");

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilistaeurService log : Utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " ne peut etre modifie dans la Bdd.");
			throw new UtilisateurInexistantException("UtilistaeurService Exception : Utilisateur avec l id : "
					+ utilisateur.getIdUtilisateur() + " ne peut etre modifie.");
		}
	}

	public void suppressionDUnUtilisateur(final Integer idUtilisateur) throws UtilisateurInexistantException {

		try {
			logger.info(
					"UtilistaeurService log : Demande de suppression de l Utilisateur id : " + idUtilisateur + " dans la Bdd.");
			utilisateurdao.supprimeUnUtilisateur(idUtilisateur);
			logger.info("UtilistaeurService log : Utilisateur id : " + idUtilisateur + " a bien ete supprime de la Bdd.");

		} catch (UtilisateurInexistantException message) {
			logger.error(
					"UtilistaeurService log : Utilisateur id : " + idUtilisateur + " ne peut etre supprime dans la Bdd.");
			throw new UtilisateurInexistantException("UtilistaeurService Exception : Utilisateur id : " + idUtilisateur
					+ " ne peut etre supprime dans la Bdd.");
		}
	}
	
	private Utilisateur validationformat(Utilisateur utilisateur) throws EmailFormatInvalid, NbCharNomException, NbCharPrenomException, NbCharTelException {
		
		// ok
		// Check Prenom est vide / depasse 30 caracteres.
		logger.info("UtilisateurService log : test Utilisateur.Prenom.");
		if (utilisateur.getPrenomUtilisateur() != null && !utilisateur.getPrenomUtilisateur().isEmpty()) {
			logger.info("UtilisateurService log : Utilisateur.Prenom n est pas null.");
			if (utilisateur.getPrenomUtilisateur().length() > 30) {
				logger.error("UtilisateurService log : Utilisateur.Prenom depasse 30 caracteres");
				utilisateur.setPrenomUtilisateur(null);
				throw new NbCharPrenomException(
						"UtilisateurService Validation Exception : Utilisateur.Prenom est null ou depasse 30 caracteres");
			} else {
				logger.info("UtilisateurService log : Utilisateur.Prenom formate en Xxxxx.");
				utilisateur.setPrenomUtilisateur(WordUtils.capitalizeFully(utilisateur.getPrenomUtilisateur(), '-'));

			}

		} else {
			logger.error("UtilisateurService log : Utilisateur.Prenom est null.");
			utilisateur.setPrenomUtilisateur(null);
			throw new NbCharNomException("UtilisateurService Validation Exception : Client.Prenom est Null");
		}

		// ok
		// Check Nom est vide / depasse 30 caracteres
		logger.info("UtilisateurService log : test Utilisateur.Nom.");
		if (utilisateur.getNomUtilisateur() != null && !utilisateur.getNomUtilisateur().isEmpty()) {
			logger.info("UtilisateurService log : Utilisateur.Nom n est pas null.");
			if (utilisateur.getNomUtilisateur().length() > 30) {
				logger.error("UtilisateurService log : Utilisateur.Nom depasse 30 caracteres");
				utilisateur.setNomUtilisateur(null);
				throw new NbCharNomException("UtilisateurService Validation Exception : Utilisteur.Nom depasse 30 caracteres.");

			} else {
				logger.info("UtilisateurService log : Utilisateur.Nom Nom formate en MAJ.");
				utilisateur.setNomUtilisateur(utilisateur.getNomUtilisateur().toUpperCase());
			}

		} else {
			logger.error("UtilisateurService log : Utilisateur.Nom est null.");
			utilisateur.setNomUtilisateur(null);
			throw new NbCharNomException("UtilisateurService Validation Exception : Utilisateur.Nom est Null");
		}

		
		// OK ---------------------------------------------------
		// Check Présence Numéro de portable égale à 10, commençant par 06 ou
		// 07.
		logger.info("UtilisateurService log : test Utilisateur.TelMobile si null/ autre que 06/07.");

		if (utilisateur.getTeleMobileUtilisateur() != null && !utilisateur.getTeleMobileUtilisateur().isEmpty()) {
			logger.info("UtilisateurService log : Utilisateur.TelMobile n est pas null :)");

			if (utilisateur.getTeleMobileUtilisateur().length() == 10) {

				if (utilisateur.getTeleMobileUtilisateur().substring(0, 2).equalsIgnoreCase("06") == true
						|| utilisateur.getTeleMobileUtilisateur().substring(0, 2).equalsIgnoreCase("07") == true) {

					logger.info("UtilisateurService log : Utilisateur.TelMobile commence par : "
							+ utilisateur.getTeleMobileUtilisateur().substring(0, 2));
					logger.info("UtilisateurService log : SuscribedSmsReminder True est envisageable");

				} else {
					logger.error("UtilisateurService log : SuscribedSmsReminder TRUE n est pas envisageable "
							+ "car Utilisateur.TelMobile commence par : " + utilisateur.getTeleMobileUtilisateur().substring(0, 2));
							utilisateur.setSuscribedSmsReminder("F");
					logger.info("UtilisateurService log : SuscribedSmsReminder force a etre FALSE");
				}

			} else {
				logger.info("UtilisateurService log : Utilisateur.TelMobile comporte : " + utilisateur.getTeleMobileUtilisateur().length()
						+ " alors que 10 sont attendus.");
				utilisateur.setSuscribedSmsReminder("F");
				logger.info("UtilisateurService log : SuscribedSmsReminder force a etre FALSE");
				utilisateur.setTeleMobileUtilisateur(null);
				throw new NbCharTelException(
						"UtilisateurService Validation Exception : Utilisateur.TeleMobile numero <10< à caracteres");
			}

		} else {
			logger.info("UtilisateurService log : TelMobileClient est null ou vide.)");
			utilisateur.setSuscribedSmsReminder("F");
			logger.info("UtilisateurService log : SuscribedSmsReminder force a etre FALSE");
			utilisateur.setTeleMobileUtilisateur(null);

		}

		// ok ------------------------------------------------------------
		// Check AdresseMail si nul si supperieur a 50 caracteres
		logger.info("UtilisateurService log : test Client.AdresseMail.");

		if (utilisateur.getAdresseMailUtilisateur() != null && !utilisateur.getAdresseMailUtilisateur().isEmpty()) {

			logger.info("UtilisateurService log : test Utilisateur.AdresseMail non null.");
			if (utilisateur.getAdresseMailUtilisateur().length() > 50) {
				logger.info("UtilisateurService log : Utilisateur.AdresseMail format non valide depasse 50 caracteres.");
				utilisateur.setSuscribedMailReminder("F");
				logger.info("UtilisateurService log : SuscribedMailRemider force a FALSE");
				throw new EmailFormatInvalid("UtilisateurService Validation Exception : Utilisateur.Mail non valide");

			} else {
				Boolean emailFormatvalidation = isValidEmailAddress(utilisateur.getAdresseMailUtilisateur());

				if (emailFormatvalidation == false) {
					logger.info("UtilisateurService log : Utilisateur.AdresseMail Format non valide : " + emailFormatvalidation);
					utilisateur.setSuscribedMailReminder("F");
					logger.info("UtilisateurService log : SuscribedMailRemider force a FALSE");
					throw new EmailFormatInvalid("UtilisateurService Validation Exception : Utilisateur.Mail non valide");

				}
			}

		} else {
			logger.info("UtilisateurService log : Client.AdresseMail null ou vide.");
			utilisateur.setSuscribedMailReminder("F");
			logger.info("UtilisateurService log : SuscribedMailRemider force a False");
			utilisateur.setAdresseMailUtilisateur(null);
		}

		return utilisateur;

	}

	public boolean isValidEmailAddress(String emailFormatvalidation) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(emailFormatvalidation);
		logger.info("UtilisateurService log : Check mail : " + m.matches());
		return m.matches();
	}


}