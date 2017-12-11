package fr.labonbonniere.opusbeaute.middleware.service.client;

import java.util.List;
import java.util.Objects;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.labonbonniere.opusbeaute.middleware.dao.AdresseClientDao;
import fr.labonbonniere.opusbeaute.middleware.dao.ClientDao;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.GenreDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseClient;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInvalideException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;
import fr.labonbonniere.opusbeaute.middleware.service.genre.GenreClientNullException;

@Stateless
public class ClientService {
	static final Logger logger = LogManager.getLogger(ClientService.class);

	@EJB
	private ClientDao clientdao;
	@EJB
	private AdresseClientDao adressedao;
	@EJB
	private GenreDao genredao;

	public List<Client> recupereListeClient() throws DaoException {

		try {

			logger.info("ClientService log : Demande au Dao la liste des Clients");
			List<Client> lalisteclient = clientdao.obtenirListeClient();
			logger.info("ClientService - Liste des Clients recuperee");
			return lalisteclient;

		} catch (DaoException message) {
			logger.error("ClientService log : Probleme de la bdd.");
			throw new DaoException("ClientService Exception : Probleme de la bdd.");
		}
	}

	public Client recupererUnClient(final Integer idClient) throws ClientInexistantException {

		try {
			logger.info("ClientService log : Demande a la bdd le Client id : " + idClient);
			Client client = clientdao.obtenirClient(idClient);
			logger.info("ClientService log : Rdv id : " + idClient + " trouve, envoie de l objet Client a ClientWS");
			return client;

		} catch (ClientInexistantException message) {
			logger.error("ClientService log : Le Client demande est introuvable");
			throw new ClientInexistantException("ClientService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutClient(Client client)
			throws DaoException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException,
			NbCharPrenomException, NbCharNomException, NbCharTsAniversaire, NbCharTelException, EmailFormatInvalid,
			DaoException, GenreInvalideException, GenreClientNullException, SuscribeMailReminderException,
			SuscribedNewsLetterException, SuscribedSmsReminderException, PhoneMobileNotStartWith0607Exception {

		logger.info("ClientService log : Demande d ajout d un nouveau Client dans la Bdd.");

		try {

			// Appel des methodes pour formatter l objet Client
			// CLient
			logger.info("ClientService log : Check Client.");
			clientValiderFormater(client);
			logger.info("ClientService log : L objet Client. est Valide");
			
			logger.info("ClientService log : Check Client.Genre");
			checkNbEntreeGenre(client);
			logger.info("ClientService log : L objet Client.Genre est valide");

			logger.info("ClientService log : Check Adresse.");
			AdresseClient adresseCheker = client.getAdresse();
			if (Objects.isNull(adresseCheker)) {
				logger.info("ClientService log : Adresse est null.");
				adresseSetToNull(client);
			} else {
				adresseValiderFormater(client);
				logger.info("ClientService log : Adresse est valide.");
			}
			logger.info("ClientService Log : Check Sousciptions");
			checkPreRequisSouscriptions(client);
			logger.info("ClientService Log : Sousciptions ok");

		} catch (NbNumRueException message) {
			throw new NbNumRueException();
		}

		try {
			clientdao.ajouterUnClient(client);
			logger.info("ClientService log : Nouveau Client ajoute, avec l id : " + client.getIdClient());
		} catch (DaoException message) {
			logger.error("ClientService log : Le Client demande est introuvable");
		}

	}

	public void modifduClient(Client client)
			throws ClientInexistantException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException,
			NbCharPaysException, NbCharPrenomException, NbCharNomException, NbCharTsAniversaire, NbCharTelException,
			EmailFormatInvalid, GenreInvalideException, DaoException, GenreClientNullException {

		try {
			logger.info("ClientService log : Demande de modification du Client id : " + client.getIdClient()
					+ " dans la Bdd.");
			logger.info("ClientService log : Check Client.");
			clientValiderFormater(client);
			logger.info("ClientService log : L objet Client. est Valide");
			
			logger.info("ClientService log : Check Client.Genre");
			checkNbEntreeGenre(client);
			logger.info("ClientService log : L objet Client.Genre est valide");

			logger.info("ClientService log : Check Adresse.");
			AdresseClient adresseCheker = client.getAdresse();
			if (Objects.isNull(adresseCheker)) {
				logger.info("ClientService log : Adresse est null.");
				adresseSetToNull(client);
			} else {
				adresseValiderFormater(client);
				logger.info("ClientService log : Adresse est valide.");
			}
			logger.info("ClientService Log : Check Sousciptions");
			checkPreRequisSouscriptions(client);
			logger.info("ClientService Log : Sousciptions ok");
		} catch (NbNumRueException message) {
			throw new NbNumRueException();

		}
		try {
			clientdao.modifieUnClient(client);
			logger.info("ClientService log : Client id : " + client.getIdClient() + " a ete modifie dans la Bdd.");

		} catch (ClientInexistantException message) {
			logger.error(
					"ClientService log : Client id : " + client.getIdClient() + " ne peut etre modifie dans la Bdd.");
			throw new ClientInexistantException(
					"ClientService Exception : Client avec l Id : " + client.getIdClient() + " ne peut etre modifie.");
		}
	}

	public void suppressionddUnClient(final Integer idClient) throws ClientInexistantException {

		try {
			logger.info("ClientService log : Demande de suppression du Client id : " + idClient + " dans la Bdd.");
			clientdao.supprimeUnClient(idClient);
			logger.info("ClientService log : Client id : " + idClient + " a bien ete supprime de la Bdd.");

		} catch (ClientInexistantException message) {
			logger.error("ClientService log : Client id : " + idClient + " ne peut etre supprime dans la Bdd.");
			throw new ClientInexistantException(
					"ClientService Exception : Client id : " + idClient + " ne peut etre supprime dans la Bdd.");
		}
	}

	private Client adresseValiderFormater(Client client)
			throws NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {

		AdresseClient adresseFormatee = new AdresseClient();
		
		if (client.getAdresse().getNumero() != null && !client.getAdresse().getNumero().isEmpty()) {
			
			if (client.getAdresse().getNumero().length() > 3) {
				throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue depasse 3 caracteres");
			} else {
				adresseFormatee.setNumero(client.getAdresse().getNumero());
			}
		} else {
			throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue est a null");
		}

		
		if (client.getAdresse().getRue() != null && !client.getAdresse().getRue().isEmpty()) {
			if (client.getAdresse().getRue().length() > 30) {
				throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue a depasse 30 caracteres");
			} else {
				adresseFormatee.setRue(WordUtils.capitalizeFully(client.getAdresse().getRue()));
			}
		} else {
			throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue est null");
		}

		
		if (client.getAdresse().getVille() != null && !client.getAdresse().getVille().isEmpty()) {
			if (client.getAdresse().getVille().length() > 30) {
				throw new NbCharRueVilleException(
						"ClientService Validation Exception : Le nom de la Ville depasse 30 caracteres");
			} else {
				adresseFormatee.setVille(client.getAdresse().getVille().toUpperCase());
			}
		} else {
			throw new NbCharRueVilleException(
					"ClientService Validation Exception : Le nom de la Ville est null");
		}
		

		if (client.getAdresse().getZipCode() != null && !client.getAdresse().getZipCode().isEmpty()) {
			if (client.getAdresse().getZipCode().toString().length() > 5) {
				throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode depasse 5 caracteres");
			} else {
				adresseFormatee.setZipCode(client.getAdresse().getZipCode());
			}
		} else {
			throw new NbNumZipcodeException("ClientService Validation Exception : Le ZipCode est null");
		}
		
		
		if (client.getAdresse().getPays() != null && !client.getAdresse().getPays().isEmpty()) {
			if (client.getAdresse().getPays().length() > 6) {
				throw new NbCharPaysException("ClientService Validation Exception : Le Pays depasse 6 caracteres");
			} else {
				adresseFormatee.setPays(client.getAdresse().getPays().toUpperCase());
			}
		} else {
			throw new NbCharPaysException("ClientService Validation Exception : Le Pays est null");
		}

		client.setAdresse(adresseFormatee);

		return client;

	}

	private Client adresseSetToNull(Client client) {
		logger.info("ClientService log : Attribution d une adresse nulle a l objet Client.");
		AdresseClient setToNullAdresse = new AdresseClient();
		setToNullAdresse.setNumero(null);
		setToNullAdresse.setRue(null);
		setToNullAdresse.setVille(null);
		setToNullAdresse.setZipCode(null);
		setToNullAdresse.setPays(null);
		client.setAdresse(setToNullAdresse);
		return client;
	}

	private Client clientValiderFormater(Client client) throws NbCharPrenomException, NbCharNomException,
			NbCharTsAniversaire, NbCharTelException, EmailFormatInvalid {

		logger.info("ClientService log : Validation des elements de Client avant persistance.");

		// ok
		// Check Prenom est vide / depasse 30 caracteres.
		logger.info("ClientService log : test Client.Prenom.");
		if (client.getPrenomClient() != null && !client.getPrenomClient().isEmpty()) {
			logger.info("ClientService log : Client.Prenom n est pas null.");
			if (client.getPrenomClient().length() > 30) {
				logger.error("ClientService log : Client.Prenom depasse 30 caracteres");
				client.setPrenomClient(null);
				throw new NbCharPrenomException(
						"ClientService Validation Exception : Client.Prenom est null ou depasse 30 caracteres");
			} else {
				logger.info("ClientService log : Client.Prenom formate en Xxxxx.");
				client.setPrenomClient(WordUtils.capitalizeFully(client.getPrenomClient(), '-'));

			}

		} else {
			logger.error("ClientService log : Client.Prenom est null.");
			client.setPrenomClient(null);
			throw new NbCharNomException("ClientService Validation Exception : Client.Prenom est Null");
		}

		// ok
		// Check Nom est vide / depasse 30 caracteres
		logger.info("ClientService log : test Client.Nom.");
		if (client.getNomClient() != null && !client.getNomClient().isEmpty()) {
			logger.info("ClientService log : Client.Nom n est pas null.");
			if (client.getNomClient().length() > 30) {
				logger.error("ClientService log : Client.Nom depasse 30 caracteres");
				client.setNomClient(null);
				throw new NbCharNomException("ClientService Validation Exception : Client.Nom depasse 30 caracteres.");

			} else {
				logger.info("ClientService log : Client.Nom Nom formate en MAJ.");
				client.setNomClient(client.getNomClient().toUpperCase());
			}

		} else {
			logger.error("ClientService log : Client.Nom est null.");
			client.setNomClient(null);
			throw new NbCharNomException("ClientService Validation Exception : Client.Nom est Null");
		}

		// la date anniversaire du client ne peut pas etre superieur a celle du
		// jour!!!
		logger.info("ClientService log : test Client.DateAnniversaire.");
		if (client.getDateAnniversaireClient() != null) {
			logger.info("ClientService log : Client.DateAnniversaire  n est pas null.");
			if (client.getDateAnniversaireClient().toString().length() > 21) {
				logger.error(
						"ClientService log : Client.DateAnniversaire depase 21 caracteres du format yyyy-MM-dd HH:MM:SS.m");
				client.setDateAnniversaireClient(null);
				throw new NbCharTsAniversaire(
						"ClientService Validation Exception : Client.DateAnniversaire probleme de Timestamp.");
			}

		} else {
			logger.error("ClientService log : Client.DateAnniversaire est null");
			client.setDateAnniversaireClient(null);

		}

		// OK ---------------------------------------------------
		// Check Présence Numéro téléphone égale à 10 caractères.
		logger.info("ClientService log : test Client.Telephone.");
		if (client.getTelephoneClient() != null && !client.getTelephoneClient().isEmpty()) {
			logger.info("ClientService log : Client.Telephone n est pas null :)");

			if (client.getTelephoneClient().length() != 10) {
				logger.info("ClientService log : Client.TelephoneLe numéro comporte : "
						+ client.getTelephoneClient().length() + " numeros alors que 10 sont attendus.");
				throw new NbCharTelException(
						"ClientService Validation Exception : Le Telephone du Client depasse 10 caracteres");
			}

		} else {
			logger.info("ClientService log : Le numéro de Telephone est vide ou null.");
			client.setTelephoneClient(null);
		}

		// OK ---------------------------------------------------
		// Check Présence Numéro de portable égale à 10, commençant par 06 ou
		// 07.
		logger.info("ClientService log : test Client.TelMobileClient si null/ autre que 06/07.");

		if (client.getTelMobileClient() != null && !client.getTelMobileClient().isEmpty()) {
			logger.info("ClientService log : Client.TelMobile n est pas null :)");

			if (client.getTelMobileClient().length() == 10) {

				if (client.getTelMobileClient().substring(0, 2).equalsIgnoreCase("06") == true
						|| client.getTelMobileClient().substring(0, 2).equalsIgnoreCase("07") == true) {

					logger.info("ClientService log : TelMobileClient commence par : "
							+ client.getTelMobileClient().substring(0, 2));
					logger.info("ClientService log : SuscribedSmsReminder True est envisageable");

				} else {
					logger.error("ClientService log : SuscribedSmsReminder TRUE n est pas envisageable "
							+ "car TelMobileClient commence par : " + client.getTelMobileClient().substring(0, 2)
							+ " il ne commence pas par 06 ou 07 :)");
					client.setSuscribedSmsReminder("F");
					logger.info("ClientService log : SuscribedSmsReminder force a etre FALSE");
				}

			} else {
				logger.info("ClientService log : TelMobileClient comporte : " + client.getTelMobileClient().length()
						+ " alors que 10 sont attendus.");
				client.setSuscribedSmsReminder("F");
				logger.info("ClientService log : SuscribedSmsReminder force a etre FALSE");
				client.setTelMobileClient(null);
				throw new NbCharTelException(
						"ClientService Validation Exception : Client.Telephone numero <10< à caracteres");
			}

		} else {
			logger.info("ClientService log : TelMobileClient est null ou vide.)");
			client.setSuscribedSmsReminder("F");
			logger.info("ClientService log : SuscribedSmsReminder force a etre FALSE");
			client.setTelMobileClient(null);

		}

		// ok ------------------------------------------------------------
		// Check AdresseMail si nul si supperieur a 50 caracteres
		logger.info("ClientService log : test Client.AdresseMail.");

		if (client.getAdresseMailClient() != null && !client.getAdresseMailClient().isEmpty()) {

			logger.info("ClientService log : test Client.AdresseMail non null.");
			if (client.getAdresseMailClient().length() > 50) {
				logger.info("ClientService log : Client.AdresseMail format non valide depasse 50 caracteres.");
				client.setSuscribedMailReminder("F");
				logger.info("ClientService log : SuscribedMailRemider force a FALSE");
				client.setSuscribedNewsLetter("F");
				logger.info("ClientService log : SuscribedNewsLetter force a FALSE");
				client.setSuscribedCommercials("F");
				logger.info("ClientService log : SuscribedCommercials force a FALSE");
				throw new EmailFormatInvalid("ClientService Validation Exception : Client.Mail non valide");

			} else {
				Boolean emailFormatvalidation = isValidEmailAddress(client.getAdresseMailClient());

				if (emailFormatvalidation == false) {
					logger.info("ClientService log : Client.AdresseMail Format non valide : " + emailFormatvalidation);
					client.setSuscribedMailReminder("F");
					logger.info("ClientService log : SuscribedMailRemider force a FALSE");
					client.setSuscribedNewsLetter("F");
					logger.info("ClientService log : SuscribedNewsLetter force a FALSE");
					client.setSuscribedCommercials("F");
					logger.info("ClientService log : SuscribedCommercials force a FALSE");
					throw new EmailFormatInvalid("ClientService Validation Exception : Client.Mail non valide");

				}
			}

		} else {
			logger.info("ClientService log : Client.AdresseMail null ou vide.");
			client.setSuscribedMailReminder("F");
			logger.info("ClientService log : SuscribedMailRemider force a False");
			client.setSuscribedNewsLetter("F");
			logger.info("ClientService log : SuscribedNewsLetter force a False");
			client.setSuscribedCommercials("F");
			logger.info("ClientService log : SuscribedCommercials force a False");
			client.setAdresseMailClient(null);
		}

		return client;

	}

	public boolean isValidEmailAddress(String emailFormatvalidation) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(emailFormatvalidation);
		logger.info("ClientService log : Check mail : " + m.matches());
		return m.matches();
	}

	public void checkNbEntreeGenre(Client client)
			throws GenreInvalideException, DaoException, GenreClientNullException {

		Integer NbRowGenreFromBdd = (int) genredao.CountGenre();
		logger.info("ClientService log : Nombre d id Genre BDD : " + NbRowGenreFromBdd);

		try {
			//Genre nbGenreFromClient = client.getGenreClient();
//			Integer testIdGenreDuClient = nbGenreFromClient.getIdGenre();
//			logger.info("ClientService log : id Genre Client : " + testIdGenreDuClient);
			logger.info("ClientService log : id Genre Client : " + client.getGenreClient().getIdGenre());
			
//			if (testIdGenreDuClient <= 0) {
			if (client.getGenreClient().getIdGenre() <= 0) {
				logger.error("ClientService log : Il y a un probleme sur L id Genre.");
				throw new GenreInvalideException(
						"ClientService Validation Exception : Il y a un probleme sur L id Genre < 0");
			}

//			if (testIdGenreDuClient > NbRowGenreFromBdd) {
			if (client.getGenreClient().getIdGenre() > NbRowGenreFromBdd) {
				throw new GenreInvalideException(
						"ClientService Validation Exception : Il y a un probleme sur L id Genre :" 
								+ client.getGenreClient().getIdGenre()
								+ " depasse le nombre de genre de la table : " + NbRowGenreFromBdd);
			}
		} catch (Exception message) {
			throw new GenreClientNullException("ClientService log : Le Genre dans CLient n est pas renseigne. ");
		}

	}
	
	public void checkPreRequisSouscriptions(Client client) {
		
		// COMMERCIALS
		if (client.getSuscribedCommercials() != null && !client.getSuscribedCommercials().isEmpty()) {
			if (client.getSuscribedCommercials().equalsIgnoreCase("T")) {
				if (client.getAdresseMailClient() !=null && !client.getAdresseMailClient().isEmpty()) {
					client.setSuscribedCommercials("T");
				} else {
					client.setSuscribedCommercials("F");
				}
			} else {
				client.setSuscribedCommercials("F");
			}
		} else {
			client.setSuscribedCommercials("F");
		}
		
		// MAIL Reminder
		if (client.getSuscribedMailReminder() != null && !client.getSuscribedMailReminder().isEmpty()) {
			if (client.getSuscribedMailReminder().equalsIgnoreCase("T")) {
				if (client.getAdresseMailClient() !=null && !client.getAdresseMailClient().isEmpty()) {
					client.setSuscribedMailReminder("T");
				} else {
					client.setSuscribedMailReminder("F");
				}
			} else {
				client.setSuscribedMailReminder("F");
			}
		} else {
			client.setSuscribedMailReminder("F");
		}
		
		// MAIL NewsLetter
		if (client.getSuscribedNewsLetter() != null && !client.getSuscribedNewsLetter().isEmpty()) {
			if (client.getSuscribedMailReminder().equalsIgnoreCase("T")) {
				if (client.getAdresseMailClient() !=null && !client.getAdresseMailClient().isEmpty()) {
					client.setSuscribedNewsLetter("T");
				} else {
					client.setSuscribedNewsLetter("F");
				}
			} else {
				client.setSuscribedNewsLetter("F");
			}
		} else {
			client.setSuscribedNewsLetter("F");
		}
		
		// SMSReminder
		if (client.getSuscribedSmsReminder() != null && !client.getSuscribedSmsReminder().isEmpty()) {
			if (client.getSuscribedSmsReminder().equalsIgnoreCase("T")) {
				if (client.getTelMobileClient() !=null && !client.getTelMobileClient().isEmpty()) {
					client.setSuscribedSmsReminder("T");
				} else {
					client.setSuscribedSmsReminder("F");
				}
			} else {
				client.setSuscribedSmsReminder("F");
			}
		} else {
			client.setSuscribedSmsReminder("F");
		}
		
	}

}
