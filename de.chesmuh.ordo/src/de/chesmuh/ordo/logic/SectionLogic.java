package de.chesmuh.ordo.logic;

import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.MuseumNotSetException;

public class SectionLogic {

	private SectionLogic() {

	}

	public static void saveSection(Section section) throws EmptyNameException,
			MuseumNotSetException {
		if (section.getName().isEmpty()) {
			throw new EmptyNameException();
		} else if (null == section.getMuseum()) {
			throw new MuseumNotSetException();
		}

		DataAccess.getInstance().saveSection(section);
	}

	public static void deleteSection(Section section) {
		Collection<Section> sectionBySection = DataAccess.getInstance()
				.getSectionBySectionWithSubSections(section);
		ArrayList<Exhibit> exhibits = DataAccess.getInstance()
				.getExhibitBySectionWithSubSections(section);
		for (Exhibit exhibit : exhibits) {
			if (section.getParent_id() != null) {
				exhibit.setSectionId(section.getParent_id());
			} else {
				exhibit.setMuseumId(section.getMuseum_id());
				exhibit.setSectionId(null);
			}
			DataAccess.getInstance().saveExhibit(exhibit);
		}
		for(Section subSection : sectionBySection) {
			DataAccess.getInstance().deleteSection(subSection);
		}

	}

}
