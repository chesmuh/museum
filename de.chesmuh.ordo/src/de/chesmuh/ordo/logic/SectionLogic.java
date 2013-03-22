package de.chesmuh.ordo.logic;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.MuseumNotSetException;

public class SectionLogic {
	
	private SectionLogic() {
		
	}
	
	public static void saveSection(Section section) throws EmptyNameException, MuseumNotSetException {
		if (section.getName().isEmpty()) {
			throw new EmptyNameException();
		} else if (null == section.getMuseum()) {
			throw new MuseumNotSetException();
		}
		
		DataAccess.getInstance().saveSection(section);
	}
	
}
