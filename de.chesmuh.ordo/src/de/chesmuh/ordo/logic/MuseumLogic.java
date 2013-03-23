package de.chesmuh.ordo.logic;

import java.util.Collection;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.exceptions.EmptyNameException;

public class MuseumLogic {

	private MuseumLogic() {
		
	}
	
	
	public static void deleteMuseum(Museum museum) {
		Collection<Section> sectionByMuseumWithNoParent = DataAccess.getInstance().getSectionByMuseumWithNoParent(museum);
		for(Section section : sectionByMuseumWithNoParent) {
			deleteAll(section);
		}
		Collection<Exhibit> exhibitsByMuseumWithSectionNull = DataAccess.getInstance().getExhibitsByMuseumWithSectionNull(museum);
		DataAccess.getInstance().deleteExhibits(exhibitsByMuseumWithSectionNull);
		DataAccess.getInstance().deleteMuseum(museum);
		
	}
	
	private static void deleteAll(Section section) {
		Collection<Section> sectionBySection = DataAccess.getInstance().getSectionBySection(section);
		for(Section subSection : sectionBySection) {
			deleteAll(subSection);
		}
		Collection<Exhibit> exhibitBySection = DataAccess.getInstance().getExhibitBySection(section);
		DataAccess.getInstance().deleteExhibits(exhibitBySection);
		DataAccess.getInstance().deleteSection(section);
	}


	public static void saveMuseum(Museum museum) throws EmptyNameException {
		if (museum.getName().isEmpty()) {
			throw new EmptyNameException();
		}
		DataAccess.getInstance().saveMuseum(museum);
	}

}
