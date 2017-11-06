package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInexistantException;

@Stateless
@Transactional
public class GenreDao {

	static final Logger logger = LogManager.getLogger(GenreDao.class);

	@PersistenceContext(unitName = "dobyPUtest")
	private EntityManager em;

	public List<Genre> obtenirListeGenre() throws DaoException {

		try {
			logger.info("GenreDao log : Demande a la bdd la liste des Genres");
			String requete = "SELECT G FROM Genre G" + " ORDER BY idGenre asc";
			;

			TypedQuery<Genre> query = em.createQuery(requete, Genre.class);
			List<Genre> listeGenre = query.getResultList();

			logger.info("GenreDao log :  Envoi de la liste de Genres");
			return listeGenre;

		} catch (Exception message) {
			logger.error("GenreDao Exception : Probleme de la bdd.");
			throw new DaoException("GenreDao Exception : Probleme de la bdd.");
		}

	}

	public Genre obtenirGenre(final Integer idGenre) throws GenreInexistantException {

		logger.info("GenreDao log : Demande a la bdd le Prestations id : " + idGenre);
		Genre genre = null;
		genre = em.find(Genre.class, idGenre);

		if (Objects.isNull(genre)) {
			logger.error("GenreDao log : Le genre : " + idGenre + " demande est introuvable");
			throw new GenreInexistantException(
					"GenreDao Exception : L' Id : " + idGenre + " est introuvable dans la base");
		}

		logger.info("GenreDao log : Prestations " + idGenre + " trouve, envoie du Genre a PrestationsService");
		return genre;

	}

	public void ajouterUnGenre(Genre genre) throws GenreExistantException {

		try {
			logger.info("GenreDao log : Demande d ajout d un nouveau Genre dans la Bdd.");
			em.persist(genre);
			logger.info("GenreDao log : Nouveau type de Genre ajoute, avec l id : " + genre.getIdGenre());

		} catch (EntityExistsException message) {
			logger.error("GenreDao log : Impossible de creer ce nouveau Genre dans la Bdd.");
			throw new GenreExistantException("GenreDao Exception : Probleme, ce Genre a l air d'être deja persistee");

		}
	}

	public void modifieUnGenre(Genre genre) throws GenreInexistantException {

		logger.info("GenreDao log : Demande de modification genre id : " + genre.getIdGenre() + " a la Bdd.");
		Genre genreBdd = em.find(Genre.class, genre.getIdGenre());
		if (Objects.nonNull(genreBdd)) {
			em.merge(genre);
			logger.info("GenreDao log : Rdv id : " + genre.getIdGenre() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("GenreDao log : Rdv id : " + genre.getIdGenre() + " ne peut etre modifie dans la Bdd.");
			throw new GenreInexistantException("GenreDao log : Modification impossible,"
					+ "il n'y a pas de Client à modifier pour l'id : " + genre.getIdGenre() + " demande.");
		}
	}

	public void supprimeUnGenre(final Integer idGenre) throws GenreInexistantException {

		logger.info("GenreDao log : Demande de suppression du Genre id : " + idGenre + " dans la Bdd.");
		Genre genre = null;
		genre = em.find(Genre.class, idGenre);
		if (Objects.nonNull(genre)) {
			em.remove(genre);
			logger.info("GenreDao log : Type de Genre id : " + idGenre + " a bien ete supprime de la Bdd.");
		} else {
			logger.error(
					"GenreDao log : Genre id : " + idGenre + " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new GenreInexistantException(
					"GenreDao Exception : Ce Genre id : " + idGenre + " ne peut etre supprime de la Bdd.");
		}
	}

}
