package fr.labonbonniere.opusbeaute.middleware.service.rgpd;

import java.io.UnsupportedEncodingException;
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
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.ClientDao;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInvalideException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharPaysException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbCharRueVilleException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumRueException;
import fr.labonbonniere.opusbeaute.middleware.service.adresse.NbNumZipcodeException;
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
public class SendMailClientRgpdService {
	static final Logger logger = LogManager.getLogger(SendMailClientRgpdService.class.getSimpleName());

	@EJB
	private ClientDao clientdao;
	
	@EJB
	private ClientService clientservice;

	@EJB
	private TokenGenClientForRgpgAction tokengenclientforrgpdactionservice;

	@EJB
	private MailEngine mailengine;

	String urlIhm = "http://192.168.1.100:4200/rgpd/";
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
	 */
	@Schedule(dayOfWeek = "*", hour = "20", minute = "00")
	public void rgpdClientFormValidationResponse()
			throws DaoException, UnsupportedEncodingException, 
						ClientInexistantException, NbNumRueException, NbCharRueVilleException, 
						NbNumZipcodeException, NbCharPaysException, NbCharPrenomException, NbCharNomException, 
						NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException, 
						GenreInvalideException, GenreClientNullException, EmptyListException {
		try {
			
			List<Client> listCliF = this.getListClientRgpdValidationFixedToFFromBdd();
			if (listCliF.size() != 0) {
				ArrayList<Client> newListFiltred = this.createNewClientListWithDateAfterJJ(listCliF);
				if (newListFiltred.size() != 0) {
					this.scanNvelleListeCliToSendUrl(newListFiltred);
					
				} else {
					logger.info("RgpdTimerService log : Il semble que la nouvelle liste soit vide");
					
				}

			} else {
				logger.info("RgpdTimerService log : Il semble que la Liste de Client a F soit vide");
			}

		} catch (EmptyListException message) {
			throw new EmptyListException("RgpdTimerService Exception : Liste vide");
		}
	}

	/**
	 * Recuprer la liste des clients ou le rgpd status a F
	 *
	 * @return List
	 * @throws DaoException
	 *             Exception
	 */
	public List<Client> getListClientRgpdValidationFixedToFFromBdd() throws DaoException {
		logger.info("RgpdTimerService log : Extraction depuis la BDD  de la lisClient a F");
		
//		List<Client> listCliRgpdValF = null;
		try {

			logger.info("RgpdTimerService log : Tentative de recuperation de la Liste de Clients depuis la Bdd");
			List<Client> listCliRgpdValF = clientdao.obtenirListeClientRgpdValidation("F");
			logger.info("RgpdTimerService log : Liste de Clients recuperee => (" + listCliRgpdValF.size() + ") Objet(s)" );
			
			if (listCliRgpdValF.size() != 0) {				
				Integer nbOfcli = listCliRgpdValF.size();
				logger.info("RgpdTimerService log : Extraction Ok il y a au moins un Client (" + nbOfcli + ")");
				return listCliRgpdValF;
				
			} else {				
				logger.info("RgpdTimerService log : Extraction n a retournee aucun Object Client a F");
				logger.info("RgpdTimerService log : Pas de mail RGPPD a envoyer :)");
				return listCliRgpdValF;
				
			}

		} catch (DaoException message) {			
			throw new DaoException("RgpdTimerService Exception : La recuperation de la liste de Client n a pa etee efectuee");
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
		logger.info("RgpdTimerService log : Creation de la nouvelle Liste de Clients a F");
		logger.info("RgpdTimerService log : nb d objet avant traitement ");
		logger.info("RgpdTimerService log : nb d objet avant traitement :" + listCliFromDao.size());

		ArrayList<Client> nvelleArrayListeCli = new ArrayList<>();

		Iterator<Client> iter = listCliFromDao.iterator();
		logger.info("RgpdTimerService log : avant While");
		while (iter.hasNext()) {
			logger.info("RgpdTimerService log : dans le While");
			Client client = iter.next();
			if (client.getRgpdDateClientvalidation() == null) {
				logger.info("RgpdTimerService log : la date est null alors fixe a JJ");
				client.setRgpdDateClientvalidation(tsjj);
			}
			
			if (client.getRgpdDateClientvalidation().before(tsjj)) {
				logger.info("RgpdTimerService log : ObjetClient no : " + client.getIdClient() 
				+ " Date de traitement : " + client.getRgpdDateClientvalidation());
				nvelleArrayListeCli.add(client);
				
			}

		}
		logger.info("RgpdTimerService log : Nouvelle Liste ArrayList cree avec " + nvelleArrayListeCli.size() + " Objets Client");
		logger.info("RgpdTimerService log : Il y aura "+ nvelleArrayListeCli.size() + " mails a envoyer");
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
	 */
	private void scanNvelleListeCliToSendUrl(ArrayList<Client> nvelArrayList) throws UnsupportedEncodingException, 
				ClientInexistantException, NbNumRueException, NbCharRueVilleException, NbNumZipcodeException, 
				NbCharPaysException, NbCharPrenomException, NbCharNomException, NbCharTsAniversaireException, 
				NbCharTelException, EmailFormatInvalidException, GenreInvalideException , DaoException, GenreClientNullException {

		logger.info("RgpdTimerService log : Iteration de la nouvelle Liste de Clients");
		Iterator<Client> iter = nvelArrayList.iterator();
		logger.info("RgpdTimerService log : Il y aura " + nvelArrayList.size() + " mail(s) a envoyer");
		while (iter.hasNext()) {

			Client client = iter.next();
			String rgpdUrl = this.urlConstructor(this.urlIhm, this.getFreshToken(client));
			logger.info("RgpdTimerService log : Mail envoye a l adresse suivante : " + client.getAdresseMailClient());
			sendRgpdUrlToClient(client, tsjj, rgpdUrl);
			this.addSixDaysToJJ(client);
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

		String tokenRgpd = tokengenclientforrgpdactionservice.tokenGenForclientRgpdAction(client);

		return tokenRgpd;

	}

	/**
	 * Construction de l URL qui pointe sur L ihm chargee de d afficher le
	 * formulaire RGPD
	 * 
	 * @param urlSite String
	 * @param token String
	 * @return String
	 */
	private String urlConstructor(String urlSite, String token) {
		
		UriBuilder url = UriBuilder.fromUri(urlSite + token);
		
		return url.build().toString();

	}

	/**
	 * Remplissage dynamique d l email qui sera envoye au cleint
	 * 
	 * @param client Client
	 * @param tsjj Timestamp
	 * @param urlRgpdLaBonbonniereDAudrey String
	 */
	private void sendRgpdUrlToClient(Client client, Timestamp tsjj, String urlRgpdLaBonbonniereDAudrey) {
		
		logger.info("RgpdTimerService log : Scan de la nouvelle liste pour envoyer les Mails aux clients");
		
		String acteurOuActrice;
		if (client.getGenreClient().getGenreHum() == "Femme") {
			acteurOuActrice = "actrice";
		} else {
			acteurOuActrice = "acteur";
		}
		
		String customMailSubject = "La Bonbonnière d'Audrey : RGPD, vous informer sur la gestion de vos données personnelles.";

		String customMessageDynamic = "<br><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
				+ "Bonjour " + client.getPrenomClient() + ",</span></p>"
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "La Bonbonnière d'Audrey réalise sa tranformation digitale, <br>"
				+ "Cette démarche inclue d'informer sa clientèle sur la gestion / utilisation de vos donnees personnelles.<br>"
				+ "La " + rgpdWikiUrl + " (wikipedia) est une description des points à mêtre en oeuvre "
				+ "afin de vous informer sur l'usages de celles-ci.</span></p>"
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "La Bonbonnière d'Audrey vous propose d'être "
				+ acteurOuActrice
				+ " le temps d'1 minute,<br>"
				+ "en choisissant les options d'usage de vos informations.<br>"
				+ "Lien : "
				+ "<a href="
				+ '"'
				+ urlRgpdLaBonbonniereDAudrey 
				+ '"' 
				+ ">La Bonbonnière d'Audrey RGPD</a></p>"
				+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "La Bonbonniè;re d'Audrey</span></p><p>&nbsp;</p>";

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
		logger.info("RgpdTimerService log : Modification du jour d envoie du mail");
		
		logger.info("RgpdTimerService log : tsjj : " + tsjj);
		Timestamp tsJJPlusSixDays = Timestamp.valueOf((tsjj.toLocalDateTime().plusDays(addDays)));
		
		logger.info("RgpdTimerService log : tsPlus6Days to Ts : " + tsJJPlusSixDays);		
		try {
			client.setRgpdDateClientvalidation(tsJJPlusSixDays);
			clientservice.modifDuClient(client);	

			
		} catch (ClientInexistantException message) {
			throw new ClientInexistantException("RgpdTimerService Exception : Le Client est introuvable");
			
			
		}
		
	}
	

}
