package fr.labonbonniere.opusbeaute.middleware.service;

import java.util.List;

import javax.ejb.EJB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.GenreDao;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInexistantException;

public class GenreService {
	static final Logger logger = LogManager.getLogger(GenreService.class);

	@EJB
	private GenreDao genredao;

	public List<Genre> recupereListeGenre() throws DaoException {

		try {

			logger.info("GenreService log : Demande au Dao la liste des Genres");
			List<Genre> lalistegenre = genredao.obtenirListeGenre();
			logger.info("GenreService - Liste des Genre recuperee");
			return lalistegenre;

		} catch (DaoException message) {
			logger.error("GenreService log : Probleme de la bdd.");
			throw new DaoException("GenreService Exception : Probleme de la bdd.");
		}
	}

	public Genre recupererUneGenre(final Integer idGenre) throws GenreInexistantException {

		try {
			logger.info("GenreService log : Demande a la bdd le Genre id : " + idGenre);
			Genre genre = genredao.obtenirGenre(idGenre);
			logger.info("GenreService log : Genre id : " + idGenre + " trouve, envoie de l objet Genre a GenreWS");
			return genre;

		} catch (GenreInexistantException message) {
			logger.error("GenreService log : Le Genre demandee est introuvable");
			throw new GenreInexistantException("GenreService Exception : l' Id est introuvable dans la base");
		}
	}

	public void ajoutGenre(Genre genre) throws GenreExistantException {

		try {
			logger.info("GenreService log : Demande d ajout d un nouveu Genre dans la Bdd.");
			genredao.ajouterUnGenre(genre);
			logger.info("GenreService log : Nouveau Genre ajoute, avec l id : " + genre.getIdGenre());

		} catch (GenreExistantException message) {
			logger.error("GenreService log : Impossible de creer ce Genre dans la Bdd.");
			throw new GenreExistantException("GenreService Exception : Impossible de creer ce Genre dans la Bdd.");
		}
	}

	public void modifDeLGenre(Genre genre) throws GenreInexistantException {

		try {
			logger.info(
					"GenreService log : Demande de modification du Genre id : " + genre.getIdGenre() + " dans la Bdd.");
			genredao.modifieUnGenre(genre);
			;
			logger.info("GenreService log : Genre id : " + genre.getIdGenre() + " a ete modifie dans la Bdd.");

		} catch (GenreInexistantException message) {
			logger.error("GenreService log : Genre id : " + genre.getIdGenre() + " ne peut etre modifie dans la Bdd.");
			throw new GenreInexistantException(
					"GenreService Exception : Genre avec l id : " + genre.getIdGenre() + " ne peut etre modifie.");
		}
	}

	public void suppressionddUnGenre(final Integer idGenre) throws GenreInexistantException {

		try {
			logger.info("GenreService log : Demande de suppression de Genre id : " + idGenre + " dans la Bdd.");
			genredao.supprimeUnGenre(idGenre);
			logger.info("GenreService log : Genre id : " + idGenre + " a bien ete supprime de la Bdd.");

		} catch (GenreInexistantException message) {
			logger.error("GenreService log : Genre id : " + idGenre + " ne peut etre supprime dans la Bdd.");
			throw new GenreInexistantException(
					"GenreService Exception : Genre id : " + idGenre + " ne peut etre supprime dans la Bdd.");
		}
	}

}
