package de.chesmuh.ordo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.chesmuh.ordo.data.manager.MuseumManager;
import de.chesmuh.ordo.data.manager.SectionManager;
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
		MuseumManager museumManager = new MuseumManager();
		SectionManager sectionManager = new SectionManager(); 
		museumManager.loadAll();
		sectionManager.loadAll();
		
		Collection<Section> sections = new ArrayList<Section>();
		Collection<Museum> collection = museumManager.getAll();
		for(Museum m : collection) {
			Section s = new Section(m.getId(), null, "Section 1", "Das hier ist Secion 1");
			sections.add(s);
			sectionManager.store(s);
			System.out.println(s.getId() + ": " + s.getName() + "\n\t" + s.getDescription() + "\n" + s.getParent_id());
			Section s2 = new Section(m.getId(), s.getId(), "Section 2", "Das hier ist Section 2");
			sections.add(s2);
			sectionManager.store(s2);
			System.out.println(s2.getId() + ": " + s2.getName() + "\n\t" + s2.getDescription() + "\n" + s2.getParent_id());
		}
	}
}
