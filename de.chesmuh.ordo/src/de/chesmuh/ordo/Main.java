package de.chesmuh.ordo;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entity.Museum;
import de.chesmuh.ordo.entity.Section;

public class Main {

	/**
	 * @author Chesmuh
	 * 
	 * @param args
	 */
	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) {
		LOGGER.setLevel(Level.FINEST);
		Collection<Museum> allMuseum = DataAccess.getInstance().getAllMuseum();
				
		for(Museum m : allMuseum) {
			System.out.println(m.getName());
			Collection<Section> allSection = DataAccess.getInstance().getAllSectionByMuseumWithNoParent(m);
			for(Section s : allSection) {
				System.out.println(s.getName());
				listSubSections(s);
			}
		}
	}

	private static void listSubSections(Section s) {
		Collection<Section> allSection = DataAccess.getInstance().getAllSectionBySection(s);
		
		for(Section sec : allSection) {
			System.out.println(sec.getName());
			listSubSections(sec);
		}
	}
}
