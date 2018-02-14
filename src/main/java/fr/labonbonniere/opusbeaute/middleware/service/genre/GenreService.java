package fr.labonbonniere.opusbeaute.middleware.service.genre;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.GenreDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.GenreInexistantException;

/**
 * Gere le Genre
 * 
 * @author fred
 *
 */
@Stateless
public class GenreService {
	static final Logger logger = LogManager.getLogger(GenreService.class);

	@EJB
	private GenreDao genredao;

	/**
	 * Recupere une liste d objet Genre
	 * 
	 * @return list
	 * @throws DaoException Exception
	 */
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

	/**
	 * Recuepre un genre par soin Id
	 * 
	 * @param idGenre Integer
	 * @return Genre
	 * @throws GenreInexistantException Exception
	 */
	public Genre recupererUnGenre(final Integer idGenre) throws GenreInexistantException {

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

	/**
	 * Ajoute un Genre a persister
	 * 
	 * @param genre Genre
	 * @throws DaoException Exception
	 * @throws GenreClientNullException Exception
	 */
	public void ajoutGenre(Genre genre) throws DaoException, GenreClientNullException {

		try {
			logger.info("GenreService log : Demande d ajout d un nouveu Genre dans la Bdd.");
			validationFormat(genre);
			genredao.ajouterUnGenre(genre);
			logger.info("GenreService log : Nouveau Genre ajoute, avec l id : " + genre.getIdGenre());

		} catch (DaoException message) {
			logger.error("GenreService log : Impossible de creer ce Genre dans la Bdd.");
			throw new DaoException("GenreService Exception : Impossible de creer ce Genre dans la Bdd.");
		}
		
	}

	/**
	 * Modifie un genre deja persiste 
	 * 
	 * @param genre Genre
	 * @throws GenreInexistantException Exception
	 * @throws GenreClientNullException Exception
	 */
	public void modifDeLGenre(Genre genre) throws GenreInexistantException, GenreClientNullException {

		try {
			logger.info("GenreService log : Demande de modification du Genre id : " + genre.getIdGenre() + " dans la Bdd.");
			validationFormat(genre);
			genredao.modifieUnGenre(genre);
			;

			logger.info("GenreService log : Genre id : " + genre.getIdGenre() + " a ete modifie dans la Bdd.");

		} catch (GenreInexistantException message) {
			logger.error("GenreService log : Genre id : " + genre.getIdGenre() + " ne peut etre modifie dans la Bdd.");
			throw new GenreInexistantException(
					"GenreService Exception : Genre avec l id : " + genre.getIdGenre() + " ne peut etre modifie.");
		}
	}

	/**
	 * supprime un genre deja persiste
	 * 
	 * @param idGenre Integer
	 * @throws GenreInexistantException Exception
	 */
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
	
	/**
	 * Valide les Champs de l objet Genre
	 * 
	 * @param genre Genre
	 * @return Genre
	 * @throws GenreClientNullException Exception
	 */
	private Genre validationFormat (Genre genre) throws GenreClientNullException {
				
		if (genre.getGenreHum() != null && !genre.getGenreHum().isEmpty()) {
			
			if (genre.getGenreHum().length() > 5) {
				throw new GenreClientNullException(
						"GenreService Validation Exception : Genre.Genrehum depasse 5 caracteres.");
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = genre.getGenreHum();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				genre.setGenreHum(StringWithoutSpaceAndCharspec.toUpperCase());
			}
			
		} else {
			genre.setGenreHum(null);
			throw new GenreClientNullException(
					"GenreService Validation Exception : Genre.Genrehum depasse 5 caracteres.");
		}
		return genre;
	}
	
	/**
	 * Verifie si il y a un espaceen debut de champs
	 * et verifie siu il y a des caracteres speciaux
	 * si oui suppression de ceux ci
	 * 
	 * @param checkSpaceAtBeginAndCharacSpec String
	 * @return String
	 */
	// Verifier la String Si elle commence par un espace ou possede des carcteres speciaux
	// Si c est le cas ne clash pas l appli mais reformate la String sans l espace en debut et sans les carac Spec.
	public String checkSpaceAtStrBeginAndCharacSpec(String checkSpaceAtBeginAndCharacSpec) {

		String strWithoutSpaceAtBegin = null;
		String strWithoutSpaceAtBeginCheckedCSpec = null;
		logger.info("StringBeginningSpaceCaraSpecDetector log : Check si la String debute avec un espace.");
		
		if (checkSpaceAtBeginAndCharacSpec.startsWith(" ")) {
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String debute avec un espace.");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str avant traitement _" + checkSpaceAtBeginAndCharacSpec);

			int nbLengthStr = checkSpaceAtBeginAndCharacSpec.length();
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.substring(1, nbLengthStr);
			strWithoutSpaceAtBeginCheckedCSpec = strWithoutSpaceAtBegin.replaceAll("[^\\s+^a-zA-Z^-]", "");
			strWithoutSpaceAtBegin = strWithoutSpaceAtBeginCheckedCSpec;
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBeginCheckedCSpec);
		
		} else {
			
			logger.info("StringBeginningSpaceCaraSpecDetector log : La String ne debute pas par un espace.");
			strWithoutSpaceAtBegin = checkSpaceAtBeginAndCharacSpec.replaceAll("[^\\s+^a-zA-Z^-]", "");
			logger.info("StringBeginningSpaceCaraSpecDetector log : Str apres traitement _" + strWithoutSpaceAtBegin);
		}
		
		return strWithoutSpaceAtBegin;
	}
}
