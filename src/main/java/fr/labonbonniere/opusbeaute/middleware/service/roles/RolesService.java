package fr.labonbonniere.opusbeaute.middleware.service.roles;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.dao.RolesDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.Roles;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.RolesInexistantException;

@Stateless
public class RolesService {
	
	static final Logger logger = LogManager.getLogger(RolesService.class);
	
	@EJB
	RolesDao rolesDao;
	
	
	public List<Roles> recupereListeRoles() throws DaoException {

		try {

			logger.info("RolesService log : Demande au Dao la liste des Roles");
			List<Roles> laListeDesRoles = rolesDao.obtenirListeRoles();
			logger.info("RolesService - Liste des Roles recuperes");
			return laListeDesRoles;

		} catch (DaoException message) {
			logger.error("RolesService log : Probleme de la bdd.");
			throw new DaoException("RolesService Exception : Probleme de la bdd.");
		}
	}

	public Roles recupererUnRole(final Integer idRole) throws RoleInexistantException, RolesInexistantException {

		try {
			logger.info("RolesService log : Demande a la bdd le Role id : " + idRole);
			Roles role = rolesDao.obtenirRole(idRole);
			logger.info("RolesService log : Roles id : " + idRole + " trouve, envoie de l objet Genre a GenreWS");
			return role;

		} catch (Exception message) {
			logger.error("RolesService log : Le Role demandee est introuvable");
			throw new RoleInexistantException("RolesService Exception : l' Id est introuvable dans la base");
		}
		

	}

	public Roles ajoutRole(Roles role) throws DaoException, RolesUtilisateurNullException {

		try {
			logger.info("RolesService log : Demande d ajout d un nouveu Role dans la Bdd.");
			validationFormat(role);
			rolesDao.ajouterUnRoles(role);
			logger.info("RolesService log : Nouveau Genre ajoute, avec l id : " + role.getIdRoles());
			return role;
			
		} catch (DaoException message) {
			logger.error("RolesService log : Impossible de creer ce Genre dans la Bdd.");
			throw new DaoException("RolesService Exception : Impossible de creer ce Genre dans la Bdd.");
		}
	}

	public void modifDuRole(Roles role) throws RoleInexistantException, RolesUtilisateurNullException, RolesInexistantException {

		try {
			logger.info("RolesService log : Demande de modification du Genre id : " + role.getIdRoles() + " dans la Bdd.");
			validationFormat(role);
			rolesDao.modifieUnRoles(role);

			logger.info("RolesService log : Role id : " + role.getIdRoles() + " a ete modifie dans la Bdd.");

		} catch (Exception message) {
			logger.error("RolesService log : Role id : " + role.getIdRoles() + " ne peut etre modifie dans la Bdd.");
			throw new RoleInexistantException(
					"RolesService Exception : Role avec l id : " + role.getIdRoles() + " ne peut etre modifie.");
		}
		
	}

	public void suppressionddUnRole(final Integer idRole) throws RoleInexistantException, RolesInexistantException {

		try {
			logger.info("RolesService log : Demande de suppression de Genre id : " + idRole + " dans la Bdd.");
			rolesDao.supprimeUnRoles(idRole);
			logger.info("RolesService log : Genre id : " + idRole + " a bien ete supprime de la Bdd.");

		} catch (Exception message) {
			logger.error("RolesService log : Genre id : " + idRole + " ne peut etre supprime dans la Bdd.");
			throw new RoleInexistantException(
					"RolesService Exception : Genre id : " + idRole + " ne peut etre supprime dans la Bdd.");
		}
	}
	
	private Roles validationFormat (Roles role) throws RolesUtilisateurNullException {
				
		if (role.getRoles() != null && !role.getRoles().isEmpty()) {
			
			if (role.getRoles().length() > 5) {
				throw new RolesUtilisateurNullException(
						"RolesService Validation Exception : Genre.Genrehum depasse 5 caracteres.");
			} else {
				String checkSpaceAtStrBeginAndCharacSpec = role.getRoles();
				String StringWithoutSpaceAndCharspec =	checkSpaceAtStrBeginAndCharacSpec(checkSpaceAtStrBeginAndCharacSpec);
				role.setRoles(StringWithoutSpaceAndCharspec.toUpperCase());
			}
			
		} else {
			role.setRoles(null);
			throw new RolesUtilisateurNullException(
					"RolesService Validation Exception : Roles.Roleshum depasse 5 caracteres.");
		}
		return role;
	}
	
	
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
