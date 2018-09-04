package fr.labonbonniere.opusbeaute.middleware.service.rgpd;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.ClientDao;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInvalideException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.rgpd.Rgpd;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;
import fr.labonbonniere.opusbeaute.middleware.service.authentification.TokenService;
import fr.labonbonniere.opusbeaute.middleware.service.client.ClientService;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharNomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharPrenomException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTelException;
import fr.labonbonniere.opusbeaute.middleware.service.client.NbCharTsAniversaireException;
import fr.labonbonniere.opusbeaute.middleware.service.genre.GenreClientNullException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.EmailFormatInvalidException;
import fr.labonbonniere.opusbeaute.middleware.service.mail.MailEngine;

/**
 * Scan les clients n ayant repondu au formulaire RGPD envoie un email avec un
 * lien pour acceder Informations sur la gestion des donnees ainsi qu aux
 * regalges
 * 
 * @author fred
 *
 */
@Stateless
public class RgpdService {
	static final Logger logger = LogManager.getLogger(RgpdService.class.getSimpleName());

	@EJB
	private TokenService tokengenservice;
	
	@EJB
	private ClientDao clientdao;
	
	@EJB
	private ClientService clientservice;

	@EJB
	private TokenClientForRgpgActionService tokengenclientforrgpdactionservice;

	@EJB
	private MailEngine mailengine;

	Integer port = 4200;
	String schema = "http", host = "192.168.1.100", path = "rgpd";
	
	String rgpdWikiUrl = "<a href=" 
							+ '"' 
							+ "https://fr.wikipedia.org/wiki/R%C3%A8glement_g%C3%A9n%C3%A9ral_sur_la_prote"
							+ "ction_des_donn%C3%A9es" 
							+ '"' 
							+ ">RGPD</a>";

	Timestamp tsjj = Timestamp.from(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris")).toInstant());

	/**
	 * Manange les methodes suivantes Methode qui recupere la liste de Clients a
	 * F (pour le status RgpdStatus Validation) Methode qui convertie la
	 * ListClient en ArrayList Methode qui envoie un mail au Cleints qui n on
	 * pas repondu au formulaire *
	 * 
	 * @throws DaoException Exception
	 * @throws EmptyListException  Excpetion
	 * @throws UnsupportedEncodingException Exception
	 * @throws GenreClientNullException Exception
	 * @throws GenreInvalideException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws NbCharTelException Exception
	 * @throws NbCharTsAniversaireException Exception 
	 * @throws NbCharNomException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharPaysException Exception
	 * @throws NbNumZipcodeException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbNumRueException Exception
	 * @throws ClientInexistantException Exception
	 * @throws MalformedURLException  Exception
	 * @throws URISyntaxException Exception
	 */
	@Schedule(dayOfWeek = "*", hour = "20", minute = "00")
	public void rgpdClientFormValidationResponse()
			throws DaoException, UnsupportedEncodingException, 
						ClientInexistantException, NbNumRueException, NbCharRueVilleException, 
						NbNumZipcodeException, NbCharPaysException, NbCharPrenomException, NbCharNomException, 
						NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException, 
						GenreInvalideException, GenreClientNullException, EmptyListException, MalformedURLException, URISyntaxException {
		try {
			
			List<Client> listCliF = this.getListClientRgpdValidationFixedToFFromBdd();
			if (listCliF.size() != 0) {
				ArrayList<Client> newListFiltred = this.createNewClientListWithDateAfterJJ(listCliF);
				if (newListFiltred.size() != 0) {
					this.scanNvelleListeCliToSendUrl(newListFiltred);
					
				} else {
					logger.info("RgpdService log : Il semble que la nouvelle liste soit vide");
					
				}

			} else {
				logger.info("RgpdService log : Il semble que la Liste de Client a F soit vide");
			}

		} catch (EmptyListException message) {
			throw new EmptyListException("RgpdService Exception : Liste vide");
		}
	}

	/**
	 * Recuprer la liste des clients ou le rgpd status a F
	 *
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Client> getListClientRgpdValidationFixedToFFromBdd() throws DaoException {
		logger.info("RgpdService log : Extraction depuis la BDD  de la lisClient a F");
		
//		List<Client> listCliRgpdValF = null;
		try {

			logger.info("RgpdService log : Tentative de recuperation de la Liste de Clients depuis la Bdd");
			List<Client> listCliRgpdValF = clientdao.obtenirListeClientRgpdValidation("F");
			logger.info("RgpdService log : Liste de Clients recuperee => (" + listCliRgpdValF.size() + ") Objet(s)" );
			
			if (listCliRgpdValF.size() != 0) {				
				Integer nbOfcli = listCliRgpdValF.size();
				logger.info("RgpdService log : Extraction Ok il y a au moins un Client (" + nbOfcli + ")");
				return listCliRgpdValF;
				
			} else {				
				logger.info("RgpdService log : Extraction n a retournee aucun Object Client a F");
				logger.info("RgpdService log : Pas de mail RGPPD a envoyer :)");
				return listCliRgpdValF;
				
			}

		} catch (DaoException message) {			
			throw new DaoException("RgpdService Exception : La recuperation de la liste de Client n a pa etee efectuee");
		}

	}

	/**
	 * Extraction des Dates Inferieures ou egales a la date du jour pour les
	 * stoquer dans une nouvelle Liste ArrayList
	 * 
	 * @param listCliFromDao List
	 * @return ArrayList
	 * @throws DaoException Excpetion
	 * @throws EmptyListException Exception
	 */
	private ArrayList<Client> createNewClientListWithDateAfterJJ(List<Client> listCliFromDao)
			throws DaoException, EmptyListException {
		logger.info("RgpdService log : Creation de la nouvelle Liste de Clients a F");
		logger.info("RgpdService log : nb d objet avant traitement ");
		logger.info("RgpdService log : nb d objet avant traitement :" + listCliFromDao.size());

		ArrayList<Client> nvelleArrayListeCli = new ArrayList<>();

		Iterator<Client> iter = listCliFromDao.iterator();
		logger.info("RgpdService log : avant While");
		while (iter.hasNext()) {
			logger.info("RgpdService log : dans le While");
			Client client = iter.next();
			if (client.getRgpdDateClientvalidation() == null) {
				logger.info("RgpdService log : la date est null alors fixe a JJ");
				client.setRgpdDateClientvalidation(tsjj);
			}
			
			if (client.getRgpdDateClientvalidation().before(tsjj)) {
				logger.info("RgpdService log : ObjetClient no : " + client.getIdClient() 
				+ " Date de traitement : " + client.getRgpdDateClientvalidation());
				nvelleArrayListeCli.add(client);
				
			}

		}
		logger.info("RgpdService log : Nouvelle Liste ArrayList cree avec " + nvelleArrayListeCli.size() + " Objets Client");
		logger.info("RgpdService log : Il y aura "+ nvelleArrayListeCli.size() + " mails a envoyer");
		return nvelleArrayListeCli;

	}

	/**
	 * Scan la Nouvelle ArrayList de Cleints et Envoie un email au client avec
	 * le lien cliquable
	 * 
	 * @param nvelArrayList ArrayList
	 * @throws UnsupportedEncodingException Exception
	 * @throws GenreClientNullException Exception
	 * @throws DaoException Exception
	 * @throws GenreInvalideException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws NbCharTelException Exception
	 * @throws NbCharTsAniversaireException Exception 
	 * @throws NbCharNomException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharPaysException Exception
	 * @throws NbNumZipcodeException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbNumRueException Exception
	 * @throws ClientInexistantException Exception
	 * @throws MalformedURLException  Exception
	 * @throws URISyntaxException Exception
	 */
	private void scanNvelleListeCliToSendUrl(ArrayList<Client> nvelArrayList) throws UnsupportedEncodingException, 
				ClientInexistantException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, 
				NbCharPaysException, NbCharPrenomException, NbCharNomException, NbCharTsAniversaireException, 
				NbCharTelException, EmailFormatInvalidException, GenreInvalideException , DaoException, GenreClientNullException, MalformedURLException, URISyntaxException {

		logger.info("RgpdService log : Iteration de la nouvelle Liste de Clients");
		Iterator<Client> iter = nvelArrayList.iterator();
		logger.info("RgpdService log : Il y aura " + nvelArrayList.size() + " mail(s) a envoyer");
		while (iter.hasNext()) {

			Client client = iter.next();
			String rgpdUrl = this.urlConstructor(this.getFreshToken(client));
			logger.info("RgpdService log : Mail envoye a l adresse suivante : " + client.getAdresseMailClient());
			sendRgpdUrlToClient(client, tsjj, rgpdUrl);
			this.addSixDaysToJJ(client);
		}
	}
	
	/**
	 * Demande un nouveau lien web avec le token
	 * pour avoir acces a la modification des options Rgpd du client
	 * 
	 * @param rgpdIdClient Integer
	 * @param rgpdEmailClient String
	 * @param rgpdPrenomClient String
	 * @throws ClientInexistantException Exception
	 * @throws RgpdException Exception
	 * @throws UnsupportedEncodingException Exception
	 * @throws MalformedURLException Exception
	 * @throws URISyntaxException Exception
	 */
	public void rgpdClientAskForANewValidToken(Integer rgpdIdClient, String rgpdEmailClient, String rgpdPrenomClient) throws ClientInexistantException, RgpdException, UnsupportedEncodingException, MalformedURLException, URISyntaxException {
		logger.info("RgpdService log : Demande d un nouveau lien Rgpd pour le Clien");
		try {
			
			// recupere le client par son email
			Client client = clientdao.retrouveUnClientViaEmail(rgpdEmailClient);
			
			// verifier que les champs prenom et Id sont Ok
			if (!this.checkIfIDMatch(rgpdIdClient, client))  {
				throw new RgpdException("RgpdService Exception : l Id ne match pas");
			}
			
			if (!this.checkIfPrenomMatch(rgpdPrenomClient, client)) {
				throw new RgpdException("RgpdService Exception : le prenom ne match pas");
			}
			// genere le token
			String token = this.getFreshToken(client);
			// genere le lien avec le token
			String rgpdUrl = this.urlConstructor(token);
			// envoie le mail au client
			this.sendRgpdUrlToClient(client, tsjj, rgpdUrl);
			
		} catch (RgpdException message) {
			throw new RgpdException("RgpdService Exception : Les informations du Client via le token ne match pas");
		} 
	}
	
	/**
	 * Recupere les donnees Rgpd du client
	 * 
	 * @param rgpdEmailClient String
	 * @return Rgpd
	 * @throws RgpdException Exception
	 * @throws ClientInexistantException Exception
	 */
	public Rgpd getRgpdClientSettings(String rgpdEmailClient) throws RgpdException, ClientInexistantException {
		logger.info("RgpdService log : Recuperation des Reglages Rgpd du Client");
		try {
			
			Client client = clientservice.recupererUnClientParEmail(rgpdEmailClient);
			Rgpd rgpd = this.clientBddRgpdSettingToRgpdObject(client);
			
			return rgpd;
			
		} catch (RgpdException message) {
			throw new RgpdException("RgpdService Exception : Il y a eu un probleme avec la recuperation des Rgps du Client");
		}
	}
	
	/**
	 * Defini les parametres Rgpd du client
	 * 
	 * @param rgpd Rgpd
	 * @return rgpd Rgpd
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
	 * @throws RgpdException Exception
	 */
	public Rgpd setRgpdClientSettings(Rgpd rgpd) throws ClientInexistantException, NbNumRueException, 
	NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException, NbCharPrenomException, 
	NbCharNomException, NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException, 
	GenreInvalideException, DaoException, GenreClientNullException, RgpdException {
		
		logger.info("RgpdService log : modification de reglages Rgpd du client");
		try {
			
			Client client = clientservice.recupererUnClientParEmail(rgpd.getRgpdCliEmail());
			client = this.rgpdObjectToClientBddSettings(client, rgpd);
			clientservice.modifDuClient(client);
			logger.info("RgpdService log : modification des reglage Rgpd Ok");
			return rgpd;
		} catch (ClientInexistantException message) {
			logger.error("RgpdService log : Les reglages Rgpd du client n ont pas etes persistes");
			throw new RgpdException("RgpdService Exception : Les reglages Rgpd du client n ont pas etes persistes");
		}
 	}
	
	/**
	 * Converti les informations le l objet Rgpd
	 * pour les injecter dans l objet Client
	 * 
	 * @param client Client
	 * @param rgpd Rgpd
	 * @return Client
	 */
	private Client rgpdObjectToClientBddSettings(Client client, Rgpd rgpd) {
		logger.info("RgpdService log : map Rgpd RgpdClient to Client");
		client.setRgpdClientCanModifyRgpdSettings(rgpd.getRgpdCliCanModifyRgpdSettings());
		client.setRgpdDateClientvalidation(tsjj);
		client.setRgpdInfoClientValidation("T");
		client.setSuscribedCommercials(rgpd.getRgpdSubsComm());
		client.setSuscribedMailReminder(rgpd.getRgpdSubsMailRem());
		client.setSuscribedNewsLetter(rgpd.getRgpdSubsNLetter());
		client.setSuscribedSmsReminder(rgpd.getRgpdSubsSmsRem());
		
		return client;
		
	}
	
	/**
	 * converti les informations rgpd du client
	 * et les injectes dans l objet Rgpd
	 * 
	 * @param client Client
	 * @return Rgpd
	 * @throws RgpdException Exception
	 */
	private Rgpd clientBddRgpdSettingToRgpdObject(Client client) throws RgpdException {
		// genere une nouvelle instance de Rgpd
		logger.info("RgpdService log : map RgpdClient to rgpdobject");
		Rgpd rgpd = new Rgpd();
		
		// Map les informations entre le client et l objet insancie
		rgpd.setRgpdCliCanModifyRgpdSettings(client.getRgpdClientCanModifyRgpdSettings());
		rgpd.setRgpdCliEmail(client.getAdresseMailClient());
		rgpd.setRgpdCliId(client.getIdClient());
		rgpd.setRgpdCliPrenom(client.getPrenomClient());
		rgpd.setRgpdCliToken("");
		rgpd.setRgpdDateCliVal(client.getRgpdDateClientvalidation());
		rgpd.setRgpdInfoCliVal(client.getRgpdInfoClientValidation());
		rgpd.setRgpdSubsComm(client.getSuscribedCommercials());
		rgpd.setRgpdSubsMailRem(client.getSuscribedMailReminder());
		rgpd.setRgpdSubsNLetter(client.getSuscribedNewsLetter());
		rgpd.setRgpdSubsSmsRem(client.getSuscribedSmsReminder());
		
		return rgpd;
		
	}
	
	/**
	 * Verifie si le champs prenom fourni dans l objet 
	 * correspond avec le prenom du Client
	 * 
	 * @param rgpdPrenomClient Client
	 * @param client Client
	 * @return Boolean
	 */
	private Boolean checkIfPrenomMatch(String rgpdPrenomClient, Client client) {		
		if (client.getPrenomClient().contentEquals(rgpdPrenomClient)) {
			
			logger.info("RgpdService log : le prenom Match");
			return true;
		} else {
			logger.info("RgpdService log : Client URL prenom : " + rgpdPrenomClient);
			logger.info("RgpdService log : Client prenom : " + client.getPrenomClient());
			logger.info("RgpdService log : le prenom match pas");
			return false;
		}		
	}
	
	/**
	 * Verifie que l id fourni 
	 * correspond avec l id de l objet Client
	 * 
	 * @param rgpdIdClient Integer
	 * @param client Client
	 * @return Booleans
	 */
	private Boolean checkIfIDMatch(Integer rgpdIdClient, Client client) {		
		if (client.getIdClient().intValue() == rgpdIdClient) {
			logger.info("RgpdService log : l Id Match");
			return true;
		} else {
			logger.info("RgpdService log : Client Id : " + client.getIdClient());
			logger.info("RgpdService log : URL Id : " + rgpdIdClient);
			logger.info("RgpdService log : l Id ne match pas");
			return false;
		}		
	}

	/**
	 * Creation d un token avec une validite
	 * 
	 * @param client Client
	 * @return String
	 * @throws UnsupportedEncodingException Exception
	 */
	private String getFreshToken(Client client) throws UnsupportedEncodingException {
		logger.info("RgpdService log : Recuperation d un token pour le lien Rgpd");
		String tokenRgpd = tokengenclientforrgpdactionservice.tokenGenForclientRgpdAction(client);

		return tokenRgpd;

	}

	/**
	 * Construction de l URL qui pointe sur L ihm chargee de d afficher le
	 * formulaire RGPD
	 * 
	 * @param token String
	 * @return String
	 * @throws MalformedURLException Eception
	 * @throws UnsupportedEncodingException Exception
	 * @throws URISyntaxException Exception 
	 */
	private String urlConstructor(String token) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
		logger.info("RgpdService log : construction de l url Rgpd avec le token"); 
		
		URIBuilder ub = new URIBuilder().setScheme(schema).setHost(host).setPort(port).setPath(path).addParameter("tkn", token);
		String urlToString = ub.build().toString();
		logger.info("RgpdService log : UrlEncode :" + urlToString );
		
		return urlToString;

	}

	/**
	 * Remplissage dynamique d l email qui sera envoye au cleint
	 * 
	 * @param client Client
	 * @param tsjj Timestamp
	 * @param urlRgpdLaBonbonniereDAudrey String
	 */
	private void sendRgpdUrlToClient(Client client, Timestamp tsjj, String urlRgpdLaBonbonniereDAudrey) {
		
		logger.info("RgpdService log : Envoi du Mail au client");
		
		String customMailSubject = "La Bonbonnière d'Audrey : RGPD, vous informer sur la gestion de vos données personnelles.";

		String customMessageDynamic = ""
				+ "<span align=" + '"' + "right" + '"' + "style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
				+ "<font color=gray><h3>Mail à conserver</h3></font></span><hr>"
				
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"				
				+ "Bonjour " + client.getPrenomClient() + ",</span></p>"
				
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "La Bonbonnière d'Audrey réalise sa tranformation digitale, <br>"
				+ "cette démarche inclue d'informer sa clientèle sur la gestion / utilisation de vos donnees personnelles.<br>"
				+ "La " + rgpdWikiUrl + " (wikipedia) est une description des actions à mêtre en oeuvre "
				+ "pour garantir la sécurité et informer sur type d'usage de vos données.</span></p>"
				
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "Afin d'être au plus proche de vos souhaits, la Bonbonnière d'Audrey vous propose de choisir<br>"
				+ " les options d'usage de vos informations.<br>"
				+ "Lien : "
				+ "<a href="
				+ '"'
				+ urlRgpdLaBonbonniereDAudrey 
				+ '"' 
				+ ">La Bonbonnière d'Audrey RGPD</a></span></p>"
				
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "Cordialement,<br>"
				+ "La Bonbonnière d'Audrey</span></p>"
				
				+ "<hr><span align=" + '"' + "right" + '"' + "style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
				+ "<font color=gray><h3>Mail à conserver<h3></font>"
				+ "</span>";

		mailengine.moteurEmailEvoyer(customMessageDynamic, client.getAdresseMailClient(), customMailSubject);

	}

	/**
	 * Ajoute 6 jours au jourJ 
	 * 
	 * @param client Client
	 * @throws ClientInexistantException Exception
	 * @throws NbNumRueException Exception
	 * @throws NbCharRueVilleException Exception
	 * @throws NbNumZipcodeException Exception
	 * @throws NbCharPaysException Exception
	 * @throws NbCharPrenomException Exception
	 * @throws NbCharNomException v
	 * @throws NbCharTsAniversaireException Exception
	 * @throws NbCharTelException Exception
	 * @throws EmailFormatInvalidException Exception
	 * @throws GenreInvalideException Exception
	 * @throws DaoException Exception
	 * @throws GenreClientNullException Exception
	 */
	private void addSixDaysToJJ(Client client) throws ClientInexistantException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, NbCharPaysException, NbCharPrenomException, NbCharNomException, NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException, GenreInvalideException, DaoException, GenreClientNullException {
		
		Integer addDays = 6;
		logger.info("RgpdService log : Modification du jour d envoie du mail");
		
		logger.info("RgpdService log : tsjj : " + tsjj);
		Timestamp tsJJPlusSixDays = Timestamp.valueOf((tsjj.toLocalDateTime().plusDays(addDays)));
		
		logger.info("RgpdService log : tsPlus6Days to Ts : " + tsJJPlusSixDays);		
		try {
			client.setRgpdDateClientvalidation(tsJJPlusSixDays);
			clientservice.modifDuClient(client);	

			
		} catch (ClientInexistantException message) {
			throw new ClientInexistantException("RgpdService Exception : Le Client est introuvable");
			
			
		}
		
	}
	

}
