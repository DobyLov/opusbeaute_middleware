package fr.labonbonniere.opusbeaute.middleware.service.wadl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.ejb.Stateless;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * transmet le fichier Wadl
 * 
 * @author fred
 *
 */
@Stateless
@XmlRootElement
public class WadlFileReaderService { 
	
	private static final Logger logger = LogManager.getLogger(WadlFileReaderService.class.getName());

    
	/**
	 * Recupere le Fichier Wadl 
	 *    
	 * @return String Fichier Web Application Document Language
	 */
    public String WadlFileReader() { 
        
//    	Path pathfile = Paths.get("../src/main/doc/application.wadl");
//    	String wadlPathFile = pathfile.toString();
    	File ressourceFile = new File ("E://Dev//Projets/opusbeaute//src//main//doc//application.wadl");
    	logger.info("WadlFileReaderService log : Chemin declare du fichier Wadl : \"" + ressourceFile + "\"");
    	BufferedReader br = null;
		FileReader fr = null;
		String wadlCurrentLine = "";
		String wadlDocument = "";
		
		try {
			
			logger.info("WadlFileReaderService Log : Ouverture du fichier Wadl." + ressourceFile);
			fr = new FileReader(ressourceFile);
			br = new BufferedReader(fr);

			
			logger.info("WadlFileReaderService Exception : Parcour du fichier Wadl");
			while ((wadlCurrentLine = br.readLine()) != null) {
//				System.out.println(sCurrentLine);
				wadlDocument = wadlDocument + wadlCurrentLine; 
			}
			

		} catch (IOException e) {
			
			logger.error("WadlFileReaderService Exception : Fichier Wadl introuvable.");
			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				logger.error("WadlFileReaderService Exception : Probbleme de fermeture du fichier Wadl");
				ex.printStackTrace();

			}

		}   
//		logger.info("WadlFileReaderService Log : Fichier Wadl : \n" + wadlDocument );
		logger.info("WadlFileReaderService Log : Fichier Wadl lu correctement :)");
		return wadlDocument;     	
    }

}
