package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.Adresse;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientExistantException;
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
//			 String requeteCli = "SELECT c FROM Client c" + " ORDER BY idClient asc";
			 String requeteCli = "SELECT * FROM T_CLIENT JOIN T_ADRESSE ON CLIENT_IDCLIENT = ADRESSE_IDADRESSE";
//			String requete = "SELECT c.idClient, c.nomClient, c.prenomclient FROM Client c";
			Query query = em.createNativeQuery(requeteCli, Client.class);
			List<Client> listeClient = query.getResultList();
//			TypedQuery<Client> query = em.createQuery(requeteCli, Client.class);
//			List<Client> listeClient = query.getResultList();
			for (Client client : listeClient) {
				client.getAdresse();
			}

			logger.info("ClientDao log :  Envoi de la liste de Clients");
			return listeClient;

		} catch (Exception e) {
			logger.error("GenreDao Exception : Probleme de la bdd.");
			// throw new DaoException("GenreDao Exception : Probleme de la
			// bdd.");
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

	public void ajouterUnClient(Client client) throws ClientExistantException {

		try {
			logger.info("ClientDao log : Demande d ajout d un nouveau Client dans la Bdd.");
			// Creation d un nouvel objet Client et Adresse depuis les donnees
			// de l objet Client.
			Client cliSansAdresse = new Client();
			cliSansAdresse.setNomClient(client.getNomClient());
			cliSansAdresse.setPrenomClient(client.getPrenomClient());
			cliSansAdresse.setTelephoneClient(client.getTelephoneClient());
			cliSansAdresse.setTelephoneClient(client.getTelephoneClient());
			cliSansAdresse.setAdresseMailClient(client.getAdresseMailClient());
			cliSansAdresse.setDateAnniversaireClient(client.getDateAnniversaireClient());
			Adresse addrCli = client.getAdresse();
			// Persiste le Client sans adresse
			em.persist(cliSansAdresse);
			// Récupère l id du client et donne le meme a l adresse
			Integer idAdresseClient = cliSansAdresse.getIdClient();
			addrCli.setIdAdresse(idAdresseClient);
			// Persiste l adresse.
			em.persist(addrCli);

			logger.info("ClientDao log : Nouveau Client ajoute, avec l id : " + cliSansAdresse.getIdClient());

			logger.info("ClientDao log : adresse avec l id : " + addrCli.getIdAdresse());
			logger.info("ClientDao log : adresse avec l id : " + addrCli);
		} catch (EntityExistsException message) {
			logger.error("ClientDao log : Impossible de creer ce nouveeu Client dans la Bdd.");
			throw new ClientExistantException("ClientDao Exception : Probleme, ce Client a l air d'être deja persiste");

		}
	}

	public void modifieUnClient(Client client) throws ClientInexistantException {

		logger.info("ClientDao log : Demande de modification du Client id : " + client.getIdClient() + " a la Bdd.");
		Client cliSansAdresse = new Client();
		cliSansAdresse.setIdClient(client.getIdClient());
		cliSansAdresse.setNomClient(client.getNomClient());
		cliSansAdresse.setPrenomClient(client.getPrenomClient());
		cliSansAdresse.setTelephoneClient(client.getTelephoneClient());
		cliSansAdresse.setTelephoneClient(client.getTelephoneClient());
		cliSansAdresse.setAdresseMailClient(client.getAdresseMailClient());
		cliSansAdresse.setDateAnniversaireClient(client.getDateAnniversaireClient());
		Adresse addrCli = client.getAdresse();
		addrCli.setIdAdresse(addrCli.getIdAdresse());
//		Adresse adressBdd = em.find(Adresse.class, addrCli.getIdAdresse());
		Client clientBdd = em.find(Client.class, client.getIdClient());
		if (Objects.nonNull(clientBdd)) {
			em.merge(cliSansAdresse);
			// em.merge(addrCli);

			logger.info("ClientDao log : Rdv id : " + client.getIdClient() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("ClientDao log : Rdv id : " + client.getIdClient() + " ne peut etre modifie dans la Bdd.");
			throw new ClientInexistantException("ClientDao log : Modification impossible,"
					+ "il n'y a pas de Client à modifier pour l'id : " + client.getIdClient() + " demande.");
		}
	}

	public void supprimeUnClient(final Integer idClient) throws ClientInexistantException {

		logger.info("ClientDao log : Demande de suppression du Client id : " + idClient + " dans la Bdd.");
		Adresse adresse = null;
		Client client = null;
		adresse = em.find(Adresse.class, idClient);
		client = em.find(Client.class, idClient);
		if (Objects.nonNull(adresse)) {
			em.remove(adresse);
		}
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
