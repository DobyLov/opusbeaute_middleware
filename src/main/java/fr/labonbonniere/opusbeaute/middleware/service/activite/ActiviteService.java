package fr.labonbonniere.opusbeaute.middleware.service.activite;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.ActiviteDao;
import fr.labonbonniere.opusbeaute.middleware.dao.DaoException;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.activite.Activite;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.activite.ActiviteInexistanteException;

/**
 ** Service Activite
 * Assure les regles Metier
 * 
 * @author fred
 *
 */
@Stateless
public class ActiviteService {
	
	static final Logger logger = LogManager.getLogger(ActiviteService.class);
	
	@EJB
	private ActiviteDao activitedao;
	
	
	/**
	 * Recupere la liste des Activites	
	 * @return List
	 * @throws DaoException
	 */
	public List<Activite> retourneLaListeActivite() throws DaoException {
		
		logger.info("ActiviteService log : Recupere la liste des activites");
		
		try {
			
			List<Activite> list = activitedao.recupereLaListeDesActivites();
			logger.info("ActiviteService log : liste des activites recuperee");
			
			return list;
		
		} catch (DaoException message) {
			
			logger.error("Activiteservice log: Probleme de Bdd");
			throw new DaoException("ActiviteService Exception : Probleme de Bdd");
		}
	}
	
	/**
	 * Recupere l Activite de puis son Id
	 * @param Integer idActivite
	 * @return Activite
	 * @throws ActiviteInexistanteException
	 */
	public Activite retourneLActiviteDepuisLId(Integer idActivite) throws ActiviteInexistanteException {
		
		logger.info("Activiteservice log: Recherche Activite id: " + idActivite);
		try {
			
			Activite activite = activitedao.obtenirActiviteParId(idActivite);
			logger.info("Activiteservice log: Recherche Activite trouvee");
			return activite;
			
		} catch (ActiviteInexistanteException message) {
			logger.error("ActiviteService log : Activite id: " 
						+ idActivite + " introuble dans la bdd");
			throw new ActiviteInexistanteException("ActiviteService Exception : Activite id: " 
						+ idActivite + " introuble dans la bdd");
		}
	}
	
	/**
	 * Ajoute une activite
	 * @param Activite activite 
	 * @throws DaoException
	 * @throws ActiviteInexistanteException
	 */
	public void ajouteUneactivite(Activite activite) throws DaoException, ActiviteInexistanteException {
		
		logger.info("ActiviteService log : Ajouter une Activite");
		try{
			
			checkValiditeActivite(activite);
			String formateActiviteNom = formateString(activite.getActiviteNom());
			activite.setActiviteNom(formateActiviteNom);
			activitedao.ajouterUneActivite(activite);
			logger.info("ActiviteService log : Activite ajoutee");
			
		} catch (ActiviteInvalideException message) {
			logger.error("ActiviteService log : Activite non ajoutee");
			throw new ActiviteInexistanteException("ActiviteService Exception : Impossible de persister cette Activite dans la bdd");
		}
		
	}
	
	/**
	 * Modifie un Activite
	 * @param activite
	 * @throws ActiviteInvalideException
	 * @throws ActiviteInexistanteException
	 */
	public void modifieUneActivite(Activite activite) throws ActiviteInvalideException, ActiviteInexistanteException {
		
		logger.info("ActiviteService log : Modifier une Activite");
		try {
			
			checkValiditeActivite(activite);
			String formateActiviteNom = formateString(activite.getActiviteNom());
			activite.setActiviteNom(formateActiviteNom);
			activitedao.modifieUneActivite(activite);
			logger.info("ActiviteService log : Activite modifiée");
			
		} catch (ActiviteInvalideException message) {
			logger.info("ActiviteService log : Activite non modifiée");
			throw new ActiviteInexistanteException("ActiviteService Exception : Impossible de modifier cette Activite dans la bdd");
		}
	
	}
	
	/**
	 * Supprime une activite
	 * @param idActivite
	 * @throws ActiviteInexistanteException
	 */
	public void supprimeUneActivite(Integer idActivite) throws ActiviteInexistanteException{
		
		logger.info("ActiviteService log : Supprimer une Activite");
		
		try {
			
			activitedao.supprimeUneActivite(idActivite);
			logger.info("ActiviteService log : Activite supprimee");
			
		} catch (ActiviteInexistanteException message) {
			logger.info("ActiviteService log : Activite non supprimee");
			throw new ActiviteInexistanteException("ActiviteService Exception : Impossible de supprimer cette Activite dans la bdd");
		}
	}
	
	/**
	 * Verifie ActiviteNom est bien de type string
	 * - Verifie de que ActiviteNom ne depasse pas 50 chararcxteres
	 * @param activite
	 * @throws ActiviteInvalideException
	 */
	private void checkValiditeActivite(Activite activite) throws ActiviteInvalideException {
		
		logger.info("ActiviteService log : Verifie si l activite est valide");
		if (activite.getActiviteNom() != null || !activite.getActiviteNom().isEmpty()) {
			
			logger.info("ActiviteService log : L activite n est pas nulle et non vide");
			if (activite.getActiviteNom() instanceof String) {
				
				logger.info("ActiviteService log : L activite est bien de type String");
				if (activite.getActiviteNom().length() > 50 ) {
					
					logger.error("ActiviteService log : Activite depasse 50 characteres");
					throw new ActiviteInvalideException("ActiviteService Exception : Le type n est pas de type String");
				
				} else {
					
					logger.info("ActiviteService log : Activite <50 characteres");

				}
				
			} else {
				
				logger.error("ActiviteService log : Activite Nulle ou vide.");
				throw new ActiviteInvalideException("ActiviteService Exception : Le type n est pas de type String");
			}
		}
		
	}
	
	/**
	 * Formate la string 
	 * Supprime les espaces au debut et a la fin
	 * supprime les characteres speciaux
	 * supprime des nombres
	 * remplace les characteres avec accent par des sans accent
	 * @param stringAFormater
	 * @return String
	 */
	private String formateString(String stringAFormater) {
		
		logger.info("ActiviteService log : Formatage de la String Activite.nom");
		String retirEspace = supprimerEspaceAuDebutEtALaFinDeLaString(stringAFormater);
		String retirChiffresEtCharSpec = supprimeLesChiffresEtCharacteresSpeciauxDeLaString(retirEspace);
		String remplaceAccentsParSansAccent = remplaceCharacteresAvecAccentsParDesCharacteresSansAccents(retirChiffresEtCharSpec);
		String formateLaPremiereLettreEnMaj = formateLaPremiereLettreEnMajLeResteEnMin(remplaceAccentsParSansAccent);
		
		return formateLaPremiereLettreEnMaj;
		
	}
	
	/**
	 * Supprimer TOUS les Espaces de la String donnée en paramètres
	 * @param stringAFiltrer
	 * @return String
	 */
	private String supprimerEspaceAuDebutEtALaFinDeLaString(String stringAFiltrer) {
		
		String stringFiltree = "";
		
		if (stringAFiltrer.startsWith(" ")) {
			
			stringFiltree = stringAFiltrer.substring(1, stringAFiltrer.length());
			
		} if (stringAFiltrer.endsWith(" ")) {
			
			stringFiltree = stringAFiltrer.substring(0, stringAFiltrer.length()-1);
		
		} else {
			
			stringFiltree = stringAFiltrer;
		}
		
		return stringFiltree;
	}
	
	/**
	 * Supprimer les Chiffres et characteres speciaux de la String donnée en paramètre
	 * @param stringAFiltrer
	 * @return String
	 */
	private String supprimeLesChiffresEtCharacteresSpeciauxDeLaString(String stringAFiltrer) { 
		
		return stringAFiltrer.replaceAll("[^\\s+^a-zA-Z^-^é^è^à^ù^û^ç]", "");		
	}
	
	/**
	 * Remplace les charactères avec accent cédilles
	 * par des charcateres sans accent ni cedille.
	 * @param stringAFlitrer
	 * @return
	 */
	private String remplaceCharacteresAvecAccentsParDesCharacteresSansAccents(String stringAFlitrer) {
		
		return stringAFlitrer.replaceAll("é", "e")
								.replaceAll("è", "e")
								.replaceAll("à", "a")
								.replaceAll("ç", "c")
								.replaceAll("û", "u")
								.replaceAll("ù", "u");
		
		
	}
	
	/**
	 * Formate la String donnée en majuscule
	 * @param stringAFormater
	 * @return
	 */
//	private String formateEnMajusculeLaString(String stringAFormater) {
//		
//		return stringAFormater.toUpperCase();
//	}
	
	/**
	 * Formate la premiere lettre de la String donnée en majuscule
	 * et le reste en minuscule
	 * @param stringAFormater
	 * @return
	 */
	private String formateLaPremiereLettreEnMajLeResteEnMin(String stringAFormater) {
		
		return WordUtils.capitalizeFully(stringAFormater);
		
	}

}
