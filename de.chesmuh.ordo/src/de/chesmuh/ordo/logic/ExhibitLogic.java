package de.chesmuh.ordo.logic;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.SectionNotSetException;

public class ExhibitLogic {

	private ExhibitLogic() {

	}

	public static void saveExhibit(Exhibit exhibit) throws SectionNotSetException, EmptyNameException {
		if (null == exhibit.getMuseum() && null == exhibit.getSection()) {
			throw new SectionNotSetException();
		} else if (exhibit.getName().isEmpty()) {
			throw new EmptyNameException();
		}
		DataAccess.getInstance().saveExhibit(exhibit);
	}

	public static void updateExhibit(Long museumId, Long sectionId, String name, String description,
			Exhibit exhibit) throws EmptyNameException, SectionNotSetException {
		if (null == exhibit.getMuseum() && null == exhibit.getSection()) {
			throw new SectionNotSetException();
		} else if (exhibit.getName().isEmpty()) {
			throw new EmptyNameException();
		}
		exhibit.setName(name);
		exhibit.setDescription(description);
		DataAccess.getInstance().updateExhibit(exhibit);
		
	}
}
