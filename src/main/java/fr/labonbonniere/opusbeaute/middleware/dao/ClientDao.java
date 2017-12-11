package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseClient;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;

@Stateless
@Transactional
public class ClientDao {

	static final Logger logger = LogManager.getLogger(ClientDao.class);

	@PersistenceContext(unitName = "dobyPUtest")
	private EntityManager em;

	public List<Client> obtenirListeClient() throws DaoException {

		try {
			logger.info("ClientDao log : Demande a la bdd la liste des Clients");
			String requeteCli = "SELECT c FROM Client c" + " ORDER BY idClient asc";
			TypedQuery<Client> query = em.createQuery(requeteCli, Client.class);
			List<Client> listeClient = query.getResultList();
			logger.info("ClientDao log :  Envoi de la liste de Clients");
			return listeClient;

		} catch (Exception e) {
			logger.error("GenreDao Exception : Probleme de la bdd.");
			throw new DaoException(e);
		}

	}

	public Client obtenirClient(final Integer idClient) throws ClientInexistantException {

		logger.info("ClientDAO log : Demande a la bdd le client id : " + idClient);
		Client client = null;
		client = em.find(Client.class, idClient);

		if (Objects.isNull(client)) {
			logger.error("ClientDAO log : Le Client : " + idClient + " demande est introuvable");
			throw new ClientInexistantException(
					"ClientDAO Exception : L' Id : " + idClient + " est introuvable dans la base");
		}

		logger.info("ClientDAO log : le client " + idClient + " trouve, envoie du Client a ClientService");
		return client;

	}

	public void ajouterUnClient(Client client) throws DaoException {
		
		try {
			logger.info("ClientDao log : Demande d ajout d un nouveau Client dans la Bdd.");
			em.persist(client);
			logger.info("ClientDao log : Client Id : " + client.getIdClient() + " enregistre dans la Bdd dans la Bdd.");
		} catch (Exception e) {
			logger.error("GenreDao Exception : Probleme de la bdd.");
			throw new DaoException();
		}
	}

	public void modifieUnClient(Client client) throws ClientInexistantException {

		logger.info("ClientDao log : Demande de modification du Client id : " + client.getIdClient() + " a la Bdd.");
		Client clientBdd = em.find(Client.class, client.getIdClient());
		if (Objects.nonNull(clientBdd)) {
			AdresseClient adresse = client.getAdresse();
			adresse.setIdAdresse(client.getIdClient());
			client.setAdresse(adresse);
			em.merge(client);
			logger.info("ClientDao log : Client id : " + client.getIdClient() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("ClientDao log : Client id : " + client.getIdClient() + " ne peut etre modifie dans la Bdd.");
			throw new ClientInexistantException("ClientDao log : Modification impossible,"
					+ "il n'y a pas de Client à modifier pour l'id : " + client.getIdClient() + " demande.");
		}
	}

	public void supprimeUnClient(final Integer idClient) throws ClientInexistantException {

		logger.info("ClientDao log : Demande de suppression du Client id : " + idClient + " dans la Bdd.");
		Client client = null;
		client = em.find(Client.class, idClient);
		if (Objects.nonNull(client)) {
			em.remove(client);
			logger.info("ClientDao log : Le Client et Adresse id : " + idClient + " ont bien ete supprimes de la Bdd.");

		} else {
			logger.error("ClientDao log : Le Client id : " + idClient
					+ " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new ClientInexistantException(
					"ClientDao Exception : Le Client id : " + idClient + " ne peut etre supprime de la Bdd.");
		}
	}

}
