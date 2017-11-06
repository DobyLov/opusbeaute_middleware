package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;

import fr.labonbonniere.opusbeaute.middleware.dao.LieuRdvDao;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvInexistantException;

public class LieuRdvService {
	static final Logger logger = LogManager.getLogger(LieuRdvService.class);

	@EJB
	private LieuRdvDao lieurdvdao;

	public List<LieuRdv> recupereListeLieuRdv() throws DaoException {

		try {

			logger.info("LieuRdvService log : Demande au Dao la liste des Genres");
			List<LieuRdv> lalistelieurdv = lieurdvdao.obtenirListeLieuRdv();
			logger.info("LieuRdvService - Liste des Genre recuperee");
			return lalistelieurdv;

		} catch (DaoException message) {
			logger.error("LieuRdvService log : Probleme de la bdd.");
			throw new DaoException("LieuRdvService Exception : Probleme de la bdd.");
		}
	}

	public LieuRdv recupererUnLieuRdv(final Integer idLieuRdv) throws LieuRdvInexistantException {

		try {
			logger.info("LieuRdvService log : Demande a la bdd le Genre id : " + idLieuRdv);
			LieuRdv lieurdv = lieurdvdao.obtenirLieuRdv(idLieuRdv);
			logger.info("LieuRdvService log : Genre id : " + idLieuRdv + " trouve, envoie de l objet Genre a GenreWS");
			return lieurdv;

		} catch (LieuRdvInexistantException message) {
			logger.error("LieuRdvService log : Le Genre demandee est introuvable");
			throw new LieuRdvInexistantException("LieuRdvService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutLieuRdv(LieuRdv lieuRdv) throws LieuRdvExistantException {

		try {
			logger.info("LieuRdvService log : Demande d ajout d un nouveu Genre dans la Bdd.");
			lieurdvdao.ajouterUnLieuRdv(lieuRdv);
			logger.info("LieuRdvService log : Nouveau Genre ajoute, avec l id : " + lieuRdv);

		} catch (LieuRdvExistantException message) {
			logger.error("LieuRdvService log : Impossible de creer ce Genre dans la Bdd.");
			throw new LieuRdvExistantException("LieuRdvService Exception : Impossible de creer ce Genre dans la Bdd.");
		}
	}

	public void modifDeLLieuRdv(LieuRdv lieuRdv) throws LieuRdvInexistantException {

		try {
			logger.info("LieuRdvService log : Demande de modification du Genre id : " + lieuRdv.getIdLieuRdv()
					+ " dans la Bdd.");
			lieurdvdao.modifieUnLieuRdv(lieuRdv);
			logger.info("LieuRdvService log : Genre id : " + lieuRdv.getIdLieuRdv() + " a ete modifie dans la Bdd.");

		} catch (LieuRdvInexistantException message) {
			logger.error(
					"LieuRdvService log : Genre id : " + lieuRdv.getIdLieuRdv() + " ne peut etre modifie dans la Bdd.");
			throw new LieuRdvInexistantException("LieuRdvService Exception : Genre avec l id : "
					+ lieuRdv.getIdLieuRdv() + " ne peut etre modifie.");
		}
	}

	public void suppressionddUnLieuRdv(final Integer idLieuRdv) throws LieuRdvInexistantException {

		try {
			logger.info("LieuRdvService log : Demande de suppression de Genre id : " + idLieuRdv + " dans la Bdd.");
			lieurdvdao.supprimeUnLieuRdv(idLieuRdv);
			logger.info("LieuRdvService log : Genre id : " + idLieuRdv + " a bien ete supprime de la Bdd.");

		} catch (LieuRdvInexistantException message) {
			logger.error("LieuRdvService log : Genre id : " + idLieuRdv + " ne peut etre supprime dans la Bdd.");
			throw new LieuRdvInexistantException(
					"LieuRdvService Exception : Genre id : " + idLieuRdv + " ne peut etre supprime dans la Bdd.");
		}
	}

}
