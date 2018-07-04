package fr.labonbonniere.opusbeaute.middleware.service.client;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailNotFoundException;

/**
 * Gestion des regles Metier ClientService
 * 
 * @author fred
 *
 */
@Stateless
public class ClientService {
	static final Logger logger = LogManager.getLogger(ClientService.class);

	@EJB
	private ClientDao clientdao;
	@EJB
	private AdresseClientDao adressedao;
	@EJB
	private GenreDao genredao;

	/**
	 * Retourne la liste des Objets client persistes
	 * 
	 * @return List
	 * @throws DaoException Exception
	 */
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

	/**
	 * Recupere un Client par son Id 
	 * 
	 * @param idClient Integer
	 * @return Client
	 * @throws ClientInexistantException Exception
	 */
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

	/**
	 * Ajoute un nouvel objet Client et AdresseClient
	 * 
	 * @param client Cleint
	 * @throws DaoException Exception
	 * @throws NbNumRueException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbNumZipcodeException Exception
	 * @throws NbCharPaysException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharNomException Exception
	 * @throws NbCharTsAniversaireException Exception
	 * @throws NbCharTelException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws DaoException Exception
	 * @throws GenreInvalideException Exception
	 * @throws GenreClientNullException Exception
	 * @throws SuscribeMailReminderException Exception
	 * @throws SuscribedNewsLetterException Exception
	 * @throws SuscribedSmsReminderException Exception
	 * @throws PhoneMobileNotStartWith0607Exception Exception
	 */
	public void ajoutClient(Client client)
			throws DaoException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException,
			NbCharPrenomException, NbCharNomException, NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException,
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

	/**
	 * Modifie un Objet Client et AdresseClient existant 
	 * 
	 * @param client Cleint
	 * @throws ClientInexistantException Exception
	 * @throws NbNumRueException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbNumZipcodeException Exception
	 * @throws NbCharPaysException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharNomException Exception
	 * @throws NbCharTsAniversaireException Exception
	 * @throws NbCharTelException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws GenreInvalideException Exception
	 * @throws DaoException Exception
	 * @throws GenreClientNullException Exception
	 */
	public void modifDuClient(Client client)
			throws ClientInexistantException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException,
			NbCharPaysException, NbCharPrenomException, NbCharNomException, NbCharTsAniversaireException, NbCharTelException,
			EmailFormatInvalidException, GenreInvalideException, DaoException, GenreClientNullException {

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

	/**
	 * Suppression d un objet Client et de son AdresseClient
	 * 
	 * 
	 * @param idClient Integer
	 * @throws ClientInexistantException Exception
	 */
	public void suppressionDUnClient(final Integer idClient) throws ClientInexistantException {

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
	
	/**
	 * Recuperer un Client par sonEmail
	 * 
	 * @param email String
	 * @return client Client
	 * @throws ClientInexistantException	Si pas de Praticien 
	 */
	public Client recupererUnClientParEmail(final String email) throws ClientInexistantException {

		try {
			logger.info("ClientService log : Demande a la bdd le Client par email : " + email);
			Client client = clientdao.retrouveUnClientViaEmail(email);
			logger.info("ClientService log : Client email : " + client.getAdresseMailClient()
					+ " trouve, envoie de l objet Client a ClientWS");
			return client;

		} catch (ClientInexistantException message) {
			logger.error("ClientService log : Le Client demande est introuvable");
			throw new ClientInexistantException(
					"ClientService Exception : l' email est introuvable dans la base");
		}
	}
	
	/**
	 * Cherche si le mail fourni existe dans la table Client
	 * 
	 * @param email String
	 * @return Boolean
	 * @throws MailNotFoundException Exception
	 */
	public boolean verifieSiAdresseMailFournieExisteDansClient(String email) throws MailNotFoundException {	
		
		
		try {
			logger.info("ClientService log : Demande si le mail " + email + " existe dans la table client");
			
//			clientdao.checkMailExistInDB(email.toLowerCase());
			if ( clientdao.checkMailExistInDB(email.toLowerCase()) >=1) {
			logger.info("ClientService log : Le mail " + email + " existe dans la table client");
 			
			return true; 
			
			} else {				
				logger.info("ClientService log : Le mail " + email + " n existe pas dans la table client");
				return false;
				
			}
			
		} catch (MailNotFoundException message) {
			logger.info("ClientService log : Lee mail " + email + " n existe pas dans la table client");
			return false;
//		} catch (DaoException message) {
//			logger.info("ClientService log : Lee mail " + email + " n existe pas dans la table client");
//			return false;
		} catch (Exception message) {
			logger.info("ClientService log : Lee mail " + email + " n existe pas dans la table client");
			return false;
		}
		
	}
	
	
	/**
	 * Validation des champs de l objet AdresseClient
	 * 
	 * @param client Client
	 * @return Client
	 * @throws NbNumRueException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbNumZipcodeException Exception
	 * @throws NbCharPaysException Exception
	 */
	private Client adresseValiderFormater(Client client)
			throws NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException {

		AdresseClient adresseFormatee = new AdresseClient();
		
		if (client.getAdresse().getNumero() != null && !client.getAdresse().getNumero().isEmpty()) {
			
			if (client.getAdresse().getNumero().length() > 3) {
				throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue depasse 3 caracteres");
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = client.getAdresse().getNumero();
				String StringWithoutSpaceAndCharspec =	strUniquemtNumero(checkSpaceAtStrBeginAndCharacSpec);
				adresseFormatee.setNumero(StringWithoutSpaceAndCharspec);
			}
		} else {
			throw new NbNumRueException("ClientService Validation Exception : Le numero de Rue est a null");
		}

		
		if (client.getAdresse().getRue() != null && !client.getAdresse().getRue().isEmpty()) {
			if (client.getAdresse().getRue().length() > 30) {
				throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue a depasse 30 caracteres");
			
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = client.getAdresse().getRue();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);	
				adresseFormatee.setRue(WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec));
			}
			
		} else {
			throw new NbCharRueVilleException("ClientService Exception : Le nom de la Rue est null");
		}

		
		if (client.getAdresse().getVille() != null && !client.getAdresse().getVille().isEmpty()) {
			if (client.getAdresse().getVille().length() > 30) {
				throw new NbCharRueVilleException(
						"ClientService Validation Exception : Le nom de la Ville depasse 30 caracteres");
			} else {
				
				String checkSpaceAtStrBeginAndCharacSpec = client.getAdresse().getVille();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);	
				adresseFormatee.setVille(StringWithoutSpaceAndCharspec.toUpperCase());
				
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
				String checkSpaceAtStrBeginAndCharacSpec = client.getAdresse().getPays();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				adresseFormatee.setPays(StringWithoutSpaceAndCharspec);
			}
		} else {
			throw new NbCharPaysException("ClientService Validation Exception : Le Pays est null");
		}

		
		client.setAdresse(adresseFormatee);

		return client;

	}

	/**
	 * Supprime les champs d un objet Adresse
	 * 
	 * @param client Client
	 * @return Client
	 */
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

	/**
	 * Validation de champs d un Client
	 * 
	 * @param client Client
	 * @return Client
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharNomException Exception
	 * @throws NbCharTsAniversaireException Exception
	 * @throws NbCharTelException Exception
	 * @throws EmailFormatInvalidException Exception
	 */
	private Client clientValiderFormater(Client client) throws NbCharPrenomException, NbCharNomException,
			NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException {

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
				
				String checkSpaceAtStrBeginAndCharacSpec = client.getPrenomClient();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				client.setPrenomClient(WordUtils.capitalizeFully(StringWithoutSpaceAndCharspec, '-'));				
				logger.info("ClientService log : Client.Prenom formate en Xxxxx.");
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
				String checkSpaceAtStrBeginAndCharacSpec = client.getNomClient();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				client.setNomClient(StringWithoutSpaceAndCharspec.toUpperCase());
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
				throw new NbCharTsAniversaireException(
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
			
			String checkSpaceAtStrBeginAndCharacSpec = client.getAdresseMailClient();
			String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpecMail(checkSpaceAtStrBeginAndCharacSpec);
			String StringWithoutSpaceAndCharspecToLowC = StringWithoutSpaceAndCharspec.toLowerCase();
			client.setAdresseMailClient(StringWithoutSpaceAndCharspecToLowC);
			
			if (client.getAdresseMailClient().length() > 50) {
				logger.info("ClientService log : Client.AdresseMail format non valide depasse 50 caracteres.");
				client.setSuscribedMailReminder("F");
				logger.info("ClientService log : SuscribedMailRemider force a FALSE");
				client.setSuscribedNewsLetter("F");
				logger.info("ClientService log : SuscribedNewsLetter force a FALSE");
				client.setSuscribedCommercials("F");
				logger.info("ClientService log : SuscribedCommercials force a FALSE");
				throw new EmailFormatInvalidException("ClientService Validation Exception : Client.Mail non valide");

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
					throw new EmailFormatInvalidException("ClientService Validation Exception : Client.Mail non valide");

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
		
		
		if (client.getRgpdDateClientvalidation() != null) {
			
			if (client.getRgpdInfoClientValidation() != null && !client.getRgpdInfoClientValidation().isEmpty()) {
				
				if (client.getRgpdInfoClientValidation().contains("f")) {
					client.setRgpdInfoClientValidation("F");
					
				} else if (client.getRgpdInfoClientValidation().contains("t")) {
					client.setRgpdInfoClientValidation("T");
		
				}
				
			} else {
				client.setRgpdInfoClientValidation("F");
			}
			
		} else {
			
			Timestamp tsjj = Timestamp.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris")).toInstant());
			client.setRgpdDateClientvalidation(tsjj);

		}
		


		return client;

	}

	/**
	 * Validation du champs AdresseMail
	 * 
	 * @param emailFormatvalidation String
	 * @return boolean
	 */
	public boolean isValidEmailAddress(String emailFormatvalidation) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(emailFormatvalidation);
		logger.info("ClientService log : Check mail : " + m.matches());
		return m.matches();
	}

	/**
	 * Verifie que la bdd possede bien des Genres
	 * 
	 * @param client Client
	 * @throws GenreInvalideException Exception
	 * @throws DaoException Exception
	 * @throws GenreClientNullException Exception
	 */
	public void checkNbEntreeGenre(Client client)
			throws GenreInvalideException, DaoException, GenreClientNullException {

		Integer NbRowGenreFromBdd = (int) genredao.CountGenre();
		logger.info("ClientService log : Nombre d id Genre BDD : " + NbRowGenreFromBdd);

		try {

			logger.info("ClientService log : id Genre Client : " + client.getGenreClient().getIdGenre());		

			if (client.getGenreClient().getIdGenre() <= 0) {
				logger.error("ClientService log : Il y a un probleme sur L id Genre.");
				throw new GenreInvalideException(
						"ClientService Validation Exception : Il y a un probleme sur L id Genre < 0");
			}


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
	
	/**
	 * Verifie si le champs possede un espace au debut
	 * ou des caracteres speciaux
	 * Si oui les suppression de ceux-ci
	 * 
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec String
	 * @return String
	 */
	
	// Verifier la String Si elle commence par un espace ou possede des carcteres speciaux
	// Si c est le cas ne clash pas l appli mais reformate la String sans l espace en debut et sans les carac Spec.
	public String checkSpaceAtStrBeginAndCharacSpec(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");
		
		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _" + checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^a-zA-Z^-]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBeginCheckedCSpec);
		
		} else {
			
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^a-zA-Z^-]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}
		
		return strWithoutSpaceAtBegin;
	}
	
	/**
	 * suppression de l espace au debut
	 * ou de caracteres speciaux
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec String
	 * @return String
	 */
	public String strUniquemtNumero(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");
		
		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _" + checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^0-9^-]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.error("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBeginCheckedCSpec);
		
		} else {
			
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^0-9^-]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}
		
		return strWithoutSpaceAtBegin;
	}


	public String checkSpaceAtStrBeginAndCharacSpecMail(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");
		
		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _" + checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^.^@^a-zA-Z^-]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBeginCheckedCSpec);
		
		} else {
			
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^.^@^a-zA-Z^-]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}
		
		return strWithoutSpaceAtBegin;
	}

	/**
	 * Verifie si le Client est abonne aux
	 * 
	 * Publicites
	 * Rappels de Rdv par Email
	 * News letter
	 * Rappels de Rdv par Sms
	 * 
	 * @param client Cleint
	 */
	public void checkPreRequisSouscriptions(Client client) {
		/* 
		 * 
		 * (Si la SOUSCRIPTION AUX RAPPELS n est pas NULLE et non VIDE)
		 * 	Alors {
		 * 		(Si la SOUSCRIPTION AUX RAPPELS est egalle a "T" ou "t")
		 * 			Alors {
		 * 				(Si l ADRESSE EMAIL n est pas NULLE et non VIDE)
		 * 					Alors {
		 * 						mettre a "T" la SOUSCRIPTION AUX RAPPELS PAR EMAIL}
		 * 				} Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR EMAIL}
		 * 		} Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS}
		 *	Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS}
		 *				
		 */
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
		
		/*
		 * (Si la SOUSCRIPTION AUX RAPPELS PAR MAIL n est pas NULLE et non VIDE)
		 * 	Alors {
		 * 		(Si la SOUSCRIPTION AUX RAPPELS PAR MAIL est egalle a "T" ou "t")
		 * 			Alors {
		 * 				(Si la SOUSCRIPTION l adresse MAIL n est pas NULLE et non VIDE)
		 * 					Alors {
		 * 						mettre a "T" la SOUSCRIPTION AUX RAPPELS PAR MAIL}
		 * 				} Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR MAIL}
		 * 		} Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR MAIL}
		 *	Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR MAIL}
		 *			
		 */
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
		
		/* 
		 * (Si la SOUSCRIPTION A LA NEWSLETTER n est pas NULLE et non VIDE)
		 * 	Alors {
		 * 		(Si la SOUSCRIPTION AUX RAPPELS PAR MAIL est egalle a "T" ou "t")
		 * 			Alors {
		 * 				(Si l ADRESSE EMAIL n est pas NULLE et non VIDE)
		 * 					Alors {
		 * 						mettre a "T" la SOUSCRIPTION AUX RAPPELS PAR EMAIL}
		 * 				} Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR EMAIL}
		 * 		} Sinon {mettre a "F" la SOUSCRIPTION A LA NEWSLETTER}
		 *	Sinon {mettre a "F" la SOUSCRIPTION A LA NEWSLETTER}
		 *				
		 */
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
		
		/*
		 * (Si la SOUSCRIPTION AUX RAPPELS PAR SMS n est pas NULLE et non VIDE)
		 * 	Alors {
		 * 		(Si la SOUSCRIPTION AUX RAPPELS PAR SMS est egalle a "T" ou "t")
		 * 			Alors {
		 * 				(Si le NUMERO DE PORTABLE n est pas NUL et non VIDE)
		 * 					Alors {
		 * 						mettre a "T" la SOUSCRIPTION AUX RAPPELS PAR SMS}
		 * 				} Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR SMS}
		 * 		} Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR SMS}
		 *	Sinon {mettre a "F" la SOUSCRIPTION AUX RAPPELS PAR SMS}
		 *				
		 */
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
