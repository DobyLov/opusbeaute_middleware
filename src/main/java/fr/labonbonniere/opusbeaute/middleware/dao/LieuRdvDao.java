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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvExistantException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdvInexistantException;

@Stateless
@Transactional
public class LieuRdvDao {
	static final Logger logger = LogManager.getLogger(LieuRdvDao.class);

	@PersistenceContext(unitName = "dobyPUtest")
	private EntityManager em;

	public List<LieuRdv> obtenirListeLieuRdv() throws DaoException {

		try {
			logger.info("LieuRdvDao log : Demande a la bdd la liste des LieuRdv");
			String requete = "SELECT L FROM LieuRdv L" + " ORDER BY idLieuRdv asc";
			;

			TypedQuery<LieuRdv> query = em.createQuery(requete, LieuRdv.class);
			List<LieuRdv> listeLieuRdv = query.getResultList();

			logger.info("LieuRdvDao log :  Envoi de la liste des LieuRdv");
			return listeLieuRdv;

		} catch (Exception message) {
			logger.error("LieuRdvDao Exception : Probleme de la bdd.");
			throw new DaoException("LieuRdvDao Exception : Probleme de la bdd.");
		}

	}

	public LieuRdv obtenirLieuRdv(final Integer idLieuRdv) throws LieuRdvInexistantException {

		logger.info("LieuRdvDao log : Demande a la bdd le LieuRdv id : " + idLieuRdv);
		LieuRdv lieurdv = null;
		lieurdv = em.find(LieuRdv.class, idLieuRdv);

		if (Objects.isNull(lieurdv)) {
			logger.error("LieuRdvDao log : Le LieuRdv : " + idLieuRdv + " demande est introuvable");
			throw new LieuRdvInexistantException(
					"LieuRdvDao Exception : L' Id : " + idLieuRdv + " est introuvable dans la base");
		}

		logger.info("LieuRdvDao log : LieuRdv " + idLieuRdv + " trouve, envoie du Genre a LieuRdvService");
		return lieurdv;

	}

	public void ajouterUnLieuRdv(LieuRdv lieurdv) throws LieuRdvExistantException {

		try {
			logger.info("LieuRdvDao log : Demande d ajout d un nouveau LieuRdv dans la Bdd.");
			em.persist(lieurdv);
			logger.info("LieuRdvDao log : Nouveau LieuRdv ajoute, avec l id : " + lieurdv.getIdLieuRdv());

		} catch (EntityExistsException message) {
			logger.error("LieuRdvDao log : Impossible de creer ce nouveau LieuRdv dans la Bdd.");
			throw new LieuRdvExistantException("LieuRdvDao Exception : Probleme, ce LieuRdv a l air d'être deja persistee");

		}
	}

	public void modifieUnLieuRdv(LieuRdv lieurdv) throws LieuRdvInexistantException {

		logger.info("LieuRdvDao log : Demande de modification du LieuRdv id : " + lieurdv.getIdLieuRdv() + " a la Bdd.");
		LieuRdv lieuRdvBdd = em.find(LieuRdv.class, lieurdv.getIdLieuRdv());
		if (Objects.nonNull(lieuRdvBdd)) {
			em.merge(lieurdv);
			logger.info("LieuRdvDao log : LieuRdv id : " + lieurdv.getIdLieuRdv() + " a ete modifie dans la Bdd.");
		} else {
			logger.error("LieuRdvDao log : LieuRdv id : " + lieurdv.getIdLieuRdv() + " ne peut etre modifie dans la Bdd.");
			throw new LieuRdvInexistantException("LieuRdvDao log : Modification impossible,"
					+ "il n'y a pas de LieuRdv à modifier pour l'id : " + lieurdv.getIdLieuRdv() + " demande.");
		}
	}

	public void supprimeUnLieuRdv(final Integer idLieuRdv) throws LieuRdvInexistantException {

		logger.info("LieuRdvDao log : Demande de suppression du LieuRdv id : " + idLieuRdv + " dans la Bdd.");
		LieuRdv lieurdv = null;
		lieurdv = em.find(LieuRdv.class, idLieuRdv);
		if (Objects.nonNull(lieurdv)) {
			em.remove(lieurdv);
			logger.info("LieuRdvDao log : LieuRdv id : " + idLieuRdv + " a bien ete supprime de la Bdd.");
		} else {
			logger.error(
					"LieuRdvDao log : LieuRdv id : " + idLieuRdv + " inexistant alors il ne peut etre supprime de la Bdd.");
			throw new LieuRdvInexistantException(
					"LieuRdvDao Exception : Ce LieuRdv id : " + idLieuRdv + " ne peut etre supprime de la Bdd.");
		}
	}

}
