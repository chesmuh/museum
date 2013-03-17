package de.chesmuh.ordo;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.chesmuh.ordo.data.manager.MuseumManager;
import de.chesmuh.ordo.entity.Museum;

public class Main {

	/**
	 * @author Chesmuh
	 * 
	 * @param args
	 */
	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) {
		LOGGER.setLevel(Level.FINEST);
		MuseumManager museumManager = new MuseumManager();
		Museum museum = new Museum("Museum Uni - Siegen", "Das Bildungshügelmuseum");
		museumManager.store(museum);
		museumManager.loadAll();
		Collection<Museum> collection = museumManager.getAll();
		for(Museum m : collection) {
			System.out.println(m.getId() + ": " + m.getName() + "\n\t" + m.getDescription());

		}
	}
}
