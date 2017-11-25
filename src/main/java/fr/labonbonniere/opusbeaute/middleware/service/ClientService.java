package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.AdresseDao;
import fr.labonbonniere.opusbeaute.middleware.dao.ClientDao;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.Adresse;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.AdresseExistanteException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;

@Stateless
public class ClientService {
	static final Logger logger = LogManager.getLogger(ClientService.class);

	@EJB
	private ClientDao clientdao;
	@EJB
	private AdresseDao adressedao;

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

	public void ajoutClient(Client client) throws ClientExistantException, AdresseExistanteException {

		try {
			logger.info("ClientService log : Demande d ajout d un nouveau Client dans la Bdd.");
			String formatPrenom = client.getPrenomClient().substring(0, 1).toUpperCase()
					+ client.getPrenomClient().substring(1).toLowerCase();
			logger.info("ClientService log : Formatage de la string recue prenom " + client.getPrenomClient()
					+ " formatee Xxxx  : " + formatPrenom);
			String formatNom = client.getNomClient().toUpperCase();
			logger.info("ClientService log : Formatage de la string nom recue : " + client.getNomClient()
					+ " formatee XXXX : " + formatNom);
			String formatMail = client.getAdresseMailClient().toLowerCase();
			logger.info("ClientService log : Formatage de la string mail recue : " + client.getAdresseMailClient()
					+ " formatee XXXX : " + formatMail);
			// formatage de la rue dans l adresse
			// uppercase the first letter
			Adresse adresseRueformatee = new Adresse();
			adresseRueformatee.setNumero(client.getAdresse().getNumero());
			adresseRueformatee.setPays(client.getAdresse().getPays().toUpperCase());
			adresseRueformatee.setRue(WordUtils.capitalizeFully(client.getAdresse().getRue()));
			adresseRueformatee.setVille(client.getAdresse().getVille().toUpperCase());
			adresseRueformatee.setZipCode(client.getAdresse().getZipCode());
			
			client.setAdresse(adresseRueformatee);
			logger.info("ClientService log : Post persistace du client avec Formatagae de l adresse : " + client.getAdresse());
			client.setPrenomClient(formatPrenom);
			client.setNomClient(formatNom);
			client.setAdresseMailClient(formatMail);
			logger.info("ClientService log : Persistance du client avec les nom et prenom formates : " + formatPrenom
					+ " " + formatNom);
			logger.info("ClientService log : extraction adresse : " + client.getAdresse());
			
			clientdao.ajouterUnClient(client);

			logger.info("ClientService log : Nouveau Client ajoute, avec l id : " + client.getIdClient());

		} catch (ClientExistantException message) {
			logger.error("ClientService log : Impossible de creer ce Client dans la Bdd.");
			throw new AdresseExistanteException("ClientService Exception : Probleme avec l adresse.");
		}
	}

	public void modifduClient(Client client) throws ClientInexistantException {

		try {
			logger.info("ClientService log : Demande de modification du Client id : " + client.getIdClient()
					+ " dans la Bdd.");
			logger.info("ClientService log : Formatage de la string nom en Majuscule.");
			logger.info("ClientService log : Formatage de la string prenom en Majuscule.");
			String formatPrenom = client.getPrenomClient().substring(0, 1).toUpperCase()
					+ client.getPrenomClient().substring(1).toLowerCase();
			String formatNom = client.getNomClient().toUpperCase();
			client.setNomClient(formatNom);
			client.setPrenomClient(formatPrenom);

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

}
