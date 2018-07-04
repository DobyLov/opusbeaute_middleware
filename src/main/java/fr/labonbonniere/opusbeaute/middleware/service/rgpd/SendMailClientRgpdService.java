package fr.labonbonniere.opusbeaute.middleware.service.rgpd;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
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
			throws DaoException, EmptyListException, UnsupportedEncodingException, 
						ClientInexistantException, NbNumRueException, NbCharRueVilleException, 
						NbNumZipcodeException, NbCharPaysException, NbCharPrenomException, NbCharNomException, 
						NbCharTsAniversaireException, NbCharTelException, EmailFormatInvalidException, 
						GenreInvalideException, GenreClientNullException {
		try {
			List<Client> listCliF = this.getListClientRgpdValidationFixedToF();
			if (!listCliF.isEmpty()) {
				ArrayList<Client> newListFiltred = this.createNewClientListWithDateAfterJJ(listCliF);
				if (!newListFiltred.isEmpty()) {
					this.scanNvelleListeCliToSendUrl(newListFiltred);
				}

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
	private List<Client> getListClientRgpdValidationFixedToF() throws DaoException {

		try {

			logger.info("RgpdTimerService log : Tentative de recuperation de la Liste de Clients");
			List<Client> listCliRgpdValF = clientdao.obtenirListeClientRgpdValidation("F");
			logger.info("RgpdTimerService log : Liste de Clients recuperee");
			if (!listCliRgpdValF.isEmpty()) {

				return listCliRgpdValF;

			} else {

				return null;

			}

		} catch (DaoException message) {
			throw new DaoException(
					"RgpdTimerService Exception : La recuperation de la liste de Client n a pa etee efectuee");
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

		ArrayList<Client> nvelleArrayListeCli = new ArrayList<>();

		Iterator<Client> iter = listCliFromDao.iterator();

		while (iter.hasNext()) {

			Client client = iter.next();

			if (client.getRgpdDateClientvalidation().after(tsjj)) {
				nvelleArrayListeCli.add(client);
				
			}

		}

		logger.info("RgpdTimerService log : Nouvelle liste cree");
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

		while (iter.hasNext()) {

			Client client = iter.next();
			String rgpdUrl = this.urlConstructor(this.urlIhm, this.getFreshToken(client));
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
	 * @param rgpdUrl String
	 */
	private void sendRgpdUrlToClient(Client client, Timestamp tsjj, String rgpdUrl) {
		String informFemOuMasc;

		if (client.getGenreClient().getGenreHum() == "FEMME") {
			informFemOuMasc = "informee";

		} else {
			informFemOuMasc = "informe";

		}

		String customMailSubject = "La Bonbonnière d'Audrey : RGPD, vous informer sur la gestion de vos données personnelles.";

		String customMessageDynamic = "<br><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: large;\">"
				+ "Bonjour " + client.getPrenomClient() + ",</span></p>"
				+ "<p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "La Bonbonnière d'Audrey réalise sa tranformation digitale et <br>"
				+ "porte un grand interet sur la transparence de vos donnees personnelles collectees. </p>"
				+ "<p>En suivant le lien ci-dessous vous serez " + informFemOuMasc
				+ " sur la gestion de vos données.</p>" + "<p>" + rgpdUrl + "</p>"
				+ "<p style=\"font-size: 14.4px;\">&nbsp;</p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "Cordialement,</span></p><p><span style=\"font-family: arial, helvetica, sans-serif; font-size: medium;\">"
				+ "La Bonbonni&egrave;re d'Audrey</span></p><p>&nbsp;</p>";

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
		// ( ( (1000*60)*3600 ) *24) *6 represente 6 jours en millisecondes
		
		Timestamp tsjjPlus6Days = Timestamp.from(Instant.from(tsjj.toLocalDateTime().plusDays(6)));
		
		try {
			client.setRgpdDateClientvalidation(tsjjPlus6Days);
			clientservice.modifDuClient(client);			
			
		} catch (ClientInexistantException message) {
			throw new ClientInexistantException("RgpdTimerService Exception : Le Client est introuvable");
			
			
		}
		
	}
	

}
