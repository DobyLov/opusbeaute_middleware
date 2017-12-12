package fr.labonbonniere.opusbeaute.middleware.service.sms;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Stateless
public class SendSmsReminderClientService {
	
	private static final Logger logger = LogManager.getLogger(SendSmsReminderClientService.class);
	
	public void sendSmsClientTest() throws Exception {
		
		logger.info("SmsReminderClientWs Exception : Demande envoi Sms");
		
	URL url = new URL("http://192.168.1.50/RaspiSMS/smsAPI/?email=admin@example.fr&password=admin&numbers=0620790050&text=Bonjour,%20Jasmine%0d%0aLa%20Bonbonnière%20d'Audrey%0d%0avous%20rappèle%20votre%20rendez-vous%0d%0ademain%20A%20:%2015h30%0d%0aPour%20le%20soin%20:%0d%0aEpilation%20du%20nez.%0d%0aA%20l%20adresse%20suivante%20:%0d%0a13%20Rue%20Du%20Poil%20Rebel%0d%0a77000%20Peau-Lisse%0d%0a%0d%0aCordialement.");
	  HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	  httpCon.setDoOutput(true);
	  httpCon.setRequestMethod("GET");
	  OutputStreamWriter out = new OutputStreamWriter(
	      httpCon.getOutputStream());
	  logger.info("SmsReminderClientWs Exception : code reponse : " + httpCon.getResponseCode());
	  logger.info("SmsReminderClientWs Exception : code reponse : " + httpCon.getResponseMessage());
//	  System.out.println(httpCon.getResponseCode());
//	  System.out.println(httpCon.getResponseMessage());
	  out.close();
	  logger.info("SmsReminderClientWs Exception : Fin de procedure.");
	 }

}
