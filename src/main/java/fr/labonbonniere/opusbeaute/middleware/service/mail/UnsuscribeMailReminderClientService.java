package fr.labonbonniere.opusbeaute.middleware.service.mail;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.labonbonniere.opusbeaute.middleware.dao.ClientDao;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.ClientInexistantException;

@Stateless
public class UnsuscribeMailReminderClientService {

	@EJB
	private ClientDao clientdao;

	static final Logger logger = LogManager.getLogger(UnsuscribeMailReminderClientService.class);

	public Boolean unscuscribeRdvMailReminderClient(String adresseMailClient) throws Exception {
		
		logger.info("UnsuscribeRdvEmail log : Recerche d un client par son mail");
		
		boolean asChangedSuscribeRdvMailreminderState = false;
		String adresseMailClientToLowC = adresseMailClient.toLowerCase();
		
		try {
			
			logger.info("UnsuscribeRdvEmail log : Demande au Dao de retrouver un Client via l Email fourni");
			
			Client clientParMail = new Client();
			
			clientParMail = clientdao.retrouveUnClientViaEmail(adresseMailClientToLowC);
			
			logger.info("UnsuscribeRdvEmail log : Test si les Emails concordent");
			logger.info("UnsuscribeRdvEmail log : Test mail du Ws : " + adresseMailClientToLowC);
			logger.info("UnsuscribeRdvEmail log : Test mail de la bdd : " + clientParMail.getAdresseMailClient());
			if (clientParMail.getAdresseMailClient().contentEquals(adresseMailClientToLowC) == true ) {
				logger.info("UnsuscribeRdvEmail log : Les Emails concordent bien");
				
				logger.info("UnsuscribeRdvEmail log : Test si SuscribedRdvReminder est a True (T ou t).");
				logger.info("UnsuscribeRdvEmail log : Valeur de SuscribedRdvReminder : " + clientParMail.getSuscribedMailReminder());
				if (clientParMail.getSuscribedMailReminder().contentEquals("T") ==  true 
						|| clientParMail.getSuscribedMailReminder().contentEquals("t") == true ) {
				logger.info("UnsuscribeRdvEmail log : SuscribedRdvmail est a : " + clientParMail.getSuscribedMailReminder());
				
				logger.info("UnsuscribeRdvEmail log : Email present et compte souscri au reminder");
				logger.info("UnsuscribeRdvEmail log : Modification de suscribeMailReminder boolean true a false ");
				// definit SuscribedMailReminder sur False
				clientParMail.setSuscribedMailReminder("F");
				logger.info("UnsuscribeRdvEmail log : suscribeMailReminder positionne a false avant persistance");
				
				try {
					logger.info("UnsuscribeRdvEmail log : Demande au Dao Persistance de suscribeMailReminder boolean a false ");
					clientdao.modifieUnClient(clientParMail);
					asChangedSuscribeRdvMailreminderState = true;
					logger.info("UnsuscribeRdvEmail log : SuscribeMailReminder boolean a false persiste ");	
					
				} catch (ClientInexistantException message) {
					logger.error("UnsuscribeRdvEmail Exception : SuscribeMailReminder changement d etat non persiste ");
					throw new Exception("UnsuscribeRdvEmail log : SuscribeMailReminder changement d etat non persiste ");
				}

			}	else {
				asChangedSuscribeRdvMailreminderState = false;
				logger.error("UnsuscribeRdvEmail log : SuscribedRdvReminder deja a " + clientParMail.getSuscribedMailReminder());
								
			}
		
		} else {
			asChangedSuscribeRdvMailreminderState = false;
			logger.info("UnsuscribeRdvEmail log : Les Emails ne concordent pas concordent");
			throw new ClientInexistantException("UnsuscribeRdvEmail Exception : ");
		}
			
		
		} catch (ClientInexistantException message) {
			logger.error("UnsuscribeRdvEmail Exception : Les Emails ne concordent pas.");
			
		}

		return asChangedSuscribeRdvMailreminderState;
		
	}

}
