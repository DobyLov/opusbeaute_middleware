package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.UtilisateurDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.UtilisateurInexistantException;

public class UtilisateurService {
	static final Logger logger = LogManager.getLogger(UtilisateurService.class);

	@EJB
	private UtilisateurDao utilisateurdao;

	public List<Utilisateur> recupereListeUtilisateur() throws DaoException {

		try {

			logger.info("UtilistaeurService log : Demande au Dao la liste des Utilisateurs");
			List<Utilisateur> lalisteUtilisateur = utilisateurdao.obtenirListeUtilisateur();
			logger.info("UtilistaeurService - Liste des Utilisateurs recuperee");
			return lalisteUtilisateur;

		} catch (DaoException message) {
			logger.error("UtilistaeurService log : Probleme de la bdd.");
			throw new DaoException("UtilistaeurService Exception : Probleme de la bdd.");
		}
	}

	public Utilisateur recupererUnUtilisateur(final Integer idUtilisateur) throws UtilisateurInexistantException {

		try {
			logger.info("UtilistaeurService log : Demande a la bdd le Utilisateur id : " + idUtilisateur);
			Utilisateur utilisateur = utilisateurdao.obtenirUtilisateur(idUtilisateur);
			logger.info("UtilistaeurService log : Utilisateur id : " + idUtilisateur
					+ " trouve, envoie de l objet Utilisateur a UtilisateursWS");
			return utilisateur;

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilistaeurService log : L Utilisateur demande est introuvable");
			throw new UtilisateurInexistantException(
					"UtilistaeurService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutUnUtilisateur(Utilisateur utilisateur) throws UtilisateurExistantException {

		try {
			logger.info("UtilistaeurService log : Demande d ajout d un nouvel Utilisateur dans la Bdd.");
			logger.info("ClientService log : Formatage de la string nom en Majuscule.");
			logger.info("ClientService log : Formatage de la string prenom en Majuscule.");
			String nomformat = utilisateur.getNomUtilisateur().toUpperCase();
			String prenomformat = utilisateur.getPrenomUtilisateur().substring(0,0).toUpperCase() + 
					utilisateur.getPrenomUtilisateur().substring(1).toLowerCase();
			utilisateur.setNomUtilisateur(nomformat);
			utilisateur.setPrenomUtilisateur(prenomformat);
			utilisateurdao.ajouterUnutilisateur(utilisateur);
			logger.info("UtilistaeurService log : Nouvelle Utilisateur ajoutee, avec l id : " + utilisateur.getIdUtilisateur());

		} catch (UtilisateurExistantException message) {
			logger.error("UtilistaeurService log : Impossible de creer cet Utilisateur dans la Bdd.");
			throw new UtilisateurExistantException(
					"UtilistaeurService Exception : Impossible de creer cet Utilisateur dans la Bdd.");
		}
	}

	public void modifierUnUtilisateur(Utilisateur utilisateur) throws UtilisateurInexistantException {

		try {
			logger.info("UtilistaeurService log : Demande de modification du Utilisateur id : "
					+ utilisateur.getIdUtilisateur() + " dans la Bdd.");
			logger.info("ClientService log : Formatage de la string nom en Majuscule.");
			logger.info("ClientService log : Formatage de la string prenom en Majuscule.");
			String nomformat = utilisateur.getNomUtilisateur().toUpperCase();
			String prenomformat = utilisateur.getPrenomUtilisateur().substring(0,0).toUpperCase() + 
					utilisateur.getPrenomUtilisateur().substring(1).toLowerCase();
			utilisateur.setNomUtilisateur(nomformat);
			utilisateur.setPrenomUtilisateur(prenomformat);			
			utilisateurdao.modifieUnUtilisateur(utilisateur);
			logger.info("UtilistaeurService log : Utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " a ete modifie dans la Bdd.");

		} catch (UtilisateurInexistantException message) {
			logger.error("UtilistaeurService log : Utilisateur id : " + utilisateur.getIdUtilisateur()
					+ " ne peut etre modifie dans la Bdd.");
			throw new UtilisateurInexistantException("UtilistaeurService Exception : Utilisateur avec l id : "
					+ utilisateur.getIdUtilisateur() + " ne peut etre modifie.");
		}
	}

	public void suppressionDUnUtilisateur(final Integer idUtilisateur) throws UtilisateurInexistantException {

		try {
			logger.info(
					"UtilistaeurService log : Demande de suppression de l Utilisateur id : " + idUtilisateur + " dans la Bdd.");
			utilisateurdao.supprimeUnUtilisateur(idUtilisateur);
			logger.info("UtilistaeurService log : Utilisateur id : " + idUtilisateur + " a bien ete supprime de la Bdd.");

		} catch (UtilisateurInexistantException message) {
			logger.error(
					"UtilistaeurService log : Utilisateur id : " + idUtilisateur + " ne peut etre supprime dans la Bdd.");
			throw new UtilisateurInexistantException("UtilistaeurService Exception : Utilisateur id : " + idUtilisateur
					+ " ne peut etre supprime dans la Bdd.");
		}
	}

}
