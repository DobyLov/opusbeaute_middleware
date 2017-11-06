package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.ClientDao;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;


public class ClientService {
	static final Logger logger = LogManager.getLogger(ClientService.class);

	@EJB
	private ClientDao clientdao;
	
	
	public List<Client> recupereListeClient() throws DaoException {		
		
		try {	
			
			logger.info("ClientService log : Demande au Dao la liste des Clients");
			List<Client> lalisteclient = clientdao.obtenirListeClient();
			logger.info("ClientService - Liste des Clients recuperee");			
			return lalisteclient;			
		
		} catch ( DaoException message ) {			
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
	
	
	public void ajoutClient(Client client) throws ClientExistantException {
		
		try {
			logger.info("ClientService log : Demande d ajout d un nouveau Client dans la Bdd.");
			clientdao.ajouterUnClient(client);;
			logger.info("ClientService log : Nouveau Client ajoute, avec l id : " + client.getIdClient());
			
		} catch (ClientExistantException message) {
			logger.error("ClientService log : Impossible de creer ce Client dans la Bdd.");
			throw new ClientExistantException("ClientService Exception : Impossible de creer ce Client dans la Bdd.");
		}		
	}
	
	
	 public void  modifduClient(Client client) throws ClientInexistantException {
		 
		 try {
			logger.info("ClientService log : Demande de modification du Client id : " + client.getIdClient() + " dans la Bdd.");
			clientdao.modifieUnClient(client);;
			logger.info("ClientService log : Client id : " + client.getIdClient() + " a ete modifie dans la Bdd.");
			
		 } catch (ClientInexistantException message) {
			logger.error("ClientService log : Client id : " + client.getIdClient() + " ne peut etre modifie dans la Bdd.");
			throw new ClientInexistantException("ClientService Exception : Client avec l Id : " + client.getIdClient() + " ne peut etre modifie.");
		 }				
	}
	
	 
	public void suppressionddUnClient (final Integer idClient) throws ClientInexistantException {
		
		try {
			logger.info("ClientService log : Demande de suppression du Client id : " + idClient + " dans la Bdd.");
			clientdao.supprimeUnClient(idClient);
			logger.info("ClientService log : Client id : " + idClient + " a bien ete supprime de la Bdd.");	
			
		} catch (ClientInexistantException message) {
			logger.error("ClientService log : Client id : " + idClient + " ne peut etre supprime dans la Bdd.");	
			throw new ClientInexistantException("ClientService Exception : Client id : " + idClient + " ne peut etre supprime dans la Bdd.");
		}
	}
	

}
