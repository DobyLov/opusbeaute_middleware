package fr.labonbonniere.opusbeaute.middleware.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInexistantException;

/**
 * Gestion de la persistence du Genre
 * 
 * @author fred
 *
 */
@Stateless
@Transactional
public class GenreDao {

	static final Logger logger = LogManager.getLogger(GenreDao.class);

	@PersistenceContext(unitName = "opusBeautePU")
	private EntityManager em;
	
	/**
	 * Retourne le nombre d entree dans la base pour la table Genre
	 * @return
	 * @throws DaoException
	 */
	public long countGenre() throws DaoException {
		logger.info("GenreDao log : Compte le nombre de Genre dans la table Genre");
		String requete = "SELECT COUNT(G) FROM Genre g";
//		String requete = "SELECT COUNT(*) FROM T_CLIENT";
		Query query = em.createQuery(requete);
		long nbGenreRowsinBdd = (long) query.getSingleResult();
		return nbGenreRowsinBdd;
	}

	/**
	 * Retourne la liste des genres persistes
	 * 
	 * @return List
	 * @throws DaoException Exception
	 */
	public List<Genre> obtenirListeGenre() throws DaoException {

		try {
			logger.info("GenreDao log : Demande a la bdd la liste des Genres");
			String requete = "SELECT G FROM Genre G" + " ORDER BY idGenre asc";
			
			TypedQuery<Genre> query = em.createQuery(requete, Genre.class);
			List<Genre> listeGenre = query.getResultList();

			logger.info("GenreDao log :  Envoi de la liste de Genres");
			return listeGenre;

		} catch (Exception message) {
			logger.error("GenreDao Exception : Probleme de la bdd.");
			throw new DaoException("GenreDao Exception : Probleme de la bdd.");
		}

	}

	/**
	 * Retourne un Genre depuis son Id
	 * 
	 * @param idGenre Integer
	 * @return Genre
	 * @throws GenreInexistantException Exception
	 */
	public Genre obtenirGenre(final Integer idGenre) throws GenreInexistantException {

		logger.info("GenreDao log : Demande a la bdd le Prestations id : " + idGenre);
		Genre genre = null;
		genre = em.find(Genre.class, idGenre);

		if (Objects.isNull(genre)) {
			logger.error("GenreDao log : Le genre : " + idGenre + " demande est introuvable");
			throw new GenreInexistantException(
					"GenreDao Exception : L' Id : " + idGenre + " est introuvable dans la base");
		}

		logger.info("GenreDao log : Genre " + idGenre + " trouve, envoie du Genre a GenreService");
		return genre;

	}

	/**
	 * Persiste un nouveau Genre
	 * 
	 * @param genre Genre
	 * @throws DaoException Exception
	 */
	public void ajouterUnGenre(Genre genre) throws DaoException{

		try {
			logger.info("GenreDao log : Demande d ajout d un nouveau Genre dans la Bdd.");
			em.persist(genre);
			em.flush();
			logger.info("GenreDao log : Nouveau type de Genre ajoute, avec l id : " + genre.getIdGenre());

		} catch (Exception message) {
			logger.error("GenreDao log : Impossible de creer ce nouveau Genre dans la Bdd.");
			throw new DaoException("GenreDao Exception : Probleme, ce Genre a l air d'être deja persistee");

		}
	}

	/**
	 * Modifie un Genre deja persiste
	 * 
	 * @param genre Genre
	 * @throws GenreInexistantException Exception
	 */
	public void modifieUnGenre(Genre genre) throws GenreInexistantException {

		logger.info("GenreDao log : Demande de modification genre id : " + genre.getIdGenre() + " a la Bdd.");
		Genre genreBdd = em.find(Genre.class, genre.getIdGenre());
		if (Objects.nonNull(genreBdd)) {
			em.merge(genre);
			em.flush();
			logger.info("GenreDao log : Genre id : " + genre.getIdGenre() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("GenreDao log : Genre id : " + genre.getIdGenre() + " ne peut etre modifie dans la Bdd.");
			throw new GenreInexistantException("GenreDao log : Modification impossible,"
					+ "il n'y a pas de Genre à modifier pour l'id : " + genre.getIdGenre() + " demande.");
		}
	}

	/**
	 * Supprime un Genre persiste
	 * 
	 * @param idGenre Integer
	 * @throws GenreInexistantException Exception
	 */
	public void supprimeUnGenre(final Integer idGenre) throws GenreInexistantException {

		logger.info("GenreDao log : Demande de suppression du Genre id : " + idGenre + " dans la Bdd.");
		Genre genre = null;
		genre = em.find(Genre.class, idGenre);
		if (Objects.nonNull(genre)) {
			em.remove(genre);
			em.flush();
			logger.info("GenreDao log : Type de Genre id : " + idGenre + " a bien ete supprime de la Bdd.");
		} else {
			logger.error(
					"GenreDao log : Genre id : " + idGenre + " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new GenreInexistantException(
					"GenreDao Exception : Ce Genre id : " + idGenre + " ne peut etre supprime de la Bdd.");
		}
	}
}
