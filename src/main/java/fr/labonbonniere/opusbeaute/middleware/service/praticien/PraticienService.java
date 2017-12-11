package fr.labonbonniere.opusbeaute.middleware.service.praticien;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.PraticienDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.Praticien;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.PraticienExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.PraticienInexistantException;
import fr.labonbonniere.opusbeaute.middleware.service.client.EmailFormatInvalid;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;

@Stateless
public class PraticienService {
	static final Logger logger = LogManager.getLogger(PraticienService.class);

	@EJB
	private PraticienDao praticiendao;

	public List<Praticien> recupereListePraticien() throws DaoException {

		try {

			logger.info("PraticienService log : Demande au Dao la liste des Praticiens");
			List<Praticien> lalistePraticien = praticiendao.obtenirListePraticien();
			logger.info("PraticienService - Liste des Praticiens recuperee");
			return lalistePraticien;

		} catch (DaoException message) {
			logger.error("PraticienService log : Probleme de la bdd.");
			throw new DaoException("PraticienService Exception : Probleme de la bdd.");
		}
	}

	public Praticien recupererUnPraticien(final Integer idPraticien) throws PraticienInexistantException {

		try {
			logger.info("PraticienService log : Demande a la bdd le Praticien id : " + idPraticien);
			Praticien praticien = praticiendao.obtenirPraticien(idPraticien);
			logger.info("PraticienService log : Praticien id : " + idPraticien
					+ " trouve, envoie de l objet Praticien a PraticiensWS");
			return praticien;

		} catch (PraticienInexistantException message) {
			logger.error("PraticienService log : L Praticien demande est introuvable");
			throw new PraticienInexistantException(
					"PraticienService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutUnPraticien(Praticien praticien) throws PraticienExistantException, EmailFormatInvalid, NbCharNomException, NbCharPrenomException, NbCharTelException {

		try {
			logger.info("PraticienService log : Demande d ajout d un nouvel Praticien dans la Bdd.");
			validationformat(praticien);
			praticiendao.ajouterUnPraticien(praticien);
			logger.info("PraticienService log : Nouvelle Praticien ajoutee, avec l id : " + praticien.getIdPraticien());

		} catch (PraticienExistantException message) {
			logger.error("PraticienService log : Impossible de creer cet Praticien dans la Bdd.");
			throw new PraticienExistantException(
					"PraticienService Exception : Impossible de creer cet Praticien dans la Bdd.");
		}
	}

	public void modifierUnPraticien(Praticien praticien) throws PraticienInexistantException, 
	EmailFormatInvalid, NbCharNomException, NbCharPrenomException, NbCharTelException {

		try {
			logger.info("PraticienService log : Demande de modification du Praticien id : "
					+ praticien.getIdPraticien() + " dans la Bdd.");
			validationformat(praticien);			
			praticiendao.modifieUnPraticien(praticien);
			logger.info("PraticienService log : Praticien id : " + praticien.getIdPraticien()
					+ " a ete modifie dans la Bdd.");

		} catch (PraticienInexistantException message) {
			logger.error("PraticienService log : Praticien id : " + praticien.getIdPraticien()
					+ " ne peut etre modifie dans la Bdd.");
			throw new PraticienInexistantException("PraticienService Exception : Praticien avec l id : "
					+ praticien.getIdPraticien() + " ne peut etre modifie.");
		}
	}

	public void suppressionDUnPraticien(final Integer idPraticien) throws PraticienInexistantException {

		try {
			logger.info(
					"PraticienService log : Demande de suppression de l Praticien id : " + idPraticien + " dans la Bdd.");
			praticiendao.supprimeUnPraticien(idPraticien);
			logger.info("PraticienService log : Praticien id : " + idPraticien + " a bien ete supprime de la Bdd.");

		} catch (PraticienInexistantException message) {
			logger.error(
					"PraticienService log : Praticien id : " + idPraticien + " ne peut etre supprime dans la Bdd.");
			throw new PraticienInexistantException("PraticienService Exception : Praticien id : " + idPraticien
					+ " ne peut etre supprime dans la Bdd.");
		}
	}
	
	private Praticien validationformat(Praticien praticien) throws EmailFormatInvalid, NbCharNomException, 
				NbCharPrenomException, NbCharTelException {
		
		// ok
		// Check Prenom est vide / depasse 30 caracteres.
		logger.info("PraticienService log : test Praticien.Prenom.");
		if (praticien.getPrenomPraticien() != null && !praticien.getPrenomPraticien().isEmpty()) {
			logger.info("PraticienService log : Praticien.Prenom n est pas null.");
			if (praticien.getPrenomPraticien().length() > 30) {
				logger.error("PraticienService log : Praticien.Prenom depasse 30 caracteres");
				praticien.setPrenomPraticien(null);
				throw new NbCharPrenomException(
						"PraticienService Validation Exception : Praticien.Prenom est null ou depasse 30 caracteres");
			} else {
				logger.info("PraticienService log : Praticien.Prenom formate en Xxxxx.");
				praticien.setPrenomPraticien(WordUtils.capitalizeFully(praticien.getPrenomPraticien(), '-'));

			}

		} else {
			logger.error("PraticienService log : Praticien.Prenom est null.");
			praticien.setPrenomPraticien(null);
			throw new NbCharNomException("PraticienService Validation Exception : Client.Prenom est Null");
		}

		// ok
		// Check Nom est vide / depasse 30 caracteres
		logger.info("PraticienService log : test Praticien.Nom.");
		if (praticien.getNomPraticien() != null && !praticien.getNomPraticien().isEmpty()) {
			logger.info("PraticienService log : Praticien.Nom n est pas null.");
			if (praticien.getNomPraticien().length() > 30) {
				logger.error("PraticienService log : Praticien.Nom depasse 30 caracteres");
				praticien.setNomPraticien(null);
				throw new NbCharNomException("PraticienService Validation Exception : Utilisteur.Nom depasse 30 caracteres.");

			} else {
				logger.info("PraticienService log : Praticien.Nom Nom formate en MAJ.");
				praticien.setNomPraticien(praticien.getNomPraticien().toUpperCase());
			}

		} else {
			logger.error("PraticienService log : Praticien.Nom est null.");
			praticien.setNomPraticien(null);
			throw new NbCharNomException("PraticienService Validation Exception : Praticien.Nom est Null");
		}

		
		// OK ---------------------------------------------------
		// Check Présence Numéro de portable égale à 10, commençant par 06 ou
		// 07.
		logger.info("PraticienService log : test Praticien.TelMobile si null/ autre que 06/07.");

		if (praticien.getTeleMobilePraticien() != null && !praticien.getTeleMobilePraticien().isEmpty()) {
			logger.info("PraticienService log : Praticien.TelMobile n est pas null :)");

			if (praticien.getTeleMobilePraticien().length() == 10) {

				if (praticien.getTeleMobilePraticien().substring(0, 2).equalsIgnoreCase("06") == true
						|| praticien.getTeleMobilePraticien().substring(0, 2).equalsIgnoreCase("07") == true) {

					logger.info("PraticienService log : Praticien.TelMobile commence par : "
							+ praticien.getTeleMobilePraticien().substring(0, 2));
					logger.info("PraticienService log : SuscribedSmsReminder True est envisageable");

				} else {
					logger.error("PraticienService log : SuscribedSmsReminder TRUE n est pas envisageable "
							+ "car Praticien.TelMobile commence par : " + praticien.getTeleMobilePraticien().substring(0, 2));
							praticien.setSuscribedSmsReminder("F");
					logger.info("PraticienService log : SuscribedSmsReminder force a etre FALSE");
				}

			} else {
				logger.info("PraticienService log : Praticien.TelMobile comporte : " + praticien.getTeleMobilePraticien().length()
						+ " alors que 10 sont attendus.");
				praticien.setSuscribedSmsReminder("F");
				logger.info("PraticienService log : SuscribedSmsReminder force a etre FALSE");
				praticien.setTeleMobilePraticien(null);
				throw new NbCharTelException(
						"PraticienService Validation Exception : Praticien.TeleMobile numero <10< à caracteres");
			}

		} else {
			logger.info("PraticienService log : TelMobileClient est null ou vide.)");
			praticien.setSuscribedSmsReminder("F");
			logger.info("PraticienService log : SuscribedSmsReminder force a etre FALSE");
			praticien.setTeleMobilePraticien(null);

		}

		// ok ------------------------------------------------------------
		// Check AdresseMail si nul si supperieur a 50 caracteres
		logger.info("PraticienService log : test Client.AdresseMail.");

		if (praticien.getAdresseMailPraticien() != null && !praticien.getAdresseMailPraticien().isEmpty()) {

			logger.info("PraticienService log : test Praticien.AdresseMail non null.");
			if (praticien.getAdresseMailPraticien().length() > 50) {
				logger.info("PraticienService log : Praticien.AdresseMail format non valide depasse 50 caracteres.");
				praticien.setSuscribedMailReminder("F");
				logger.info("PraticienService log : SuscribedMailRemider force a FALSE");
				throw new EmailFormatInvalid("PraticienService Validation Exception : Praticien.Mail non valide");

			} else {
				Boolean emailFormatvalidation = isValidEmailAddress(praticien.getAdresseMailPraticien());

				if (emailFormatvalidation == false) {
					logger.info("PraticienService log : Praticien.AdresseMail Format non valide : " + emailFormatvalidation);
					praticien.setSuscribedMailReminder("F");
					logger.info("PraticienService log : SuscribedMailRemider force a FALSE");
					throw new EmailFormatInvalid("PraticienService Validation Exception : Praticien.Mail non valide");

				}
			}

		} else {
			logger.info("PraticienService log : Client.AdresseMail null ou vide.");
			praticien.setSuscribedMailReminder("F");
			logger.info("PraticienService log : SuscribedMailRemider force a False");
			praticien.setAdresseMailPraticien(null);
		}

		return praticien;

	}

	public boolean isValidEmailAddress(String emailFormatvalidation) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(emailFormatvalidation);
		logger.info("PraticienService log : Check mail : " + m.matches());
		return m.matches();
	}


}