package de.chesmuh.ordo.logic;

import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.MuseumNotSetException;
import de.chesmuh.ordo.exceptions.SetChildAsParentException;

public class SectionLogic {

	private SectionLogic() {

	}

	public static void saveSection(Section section) throws EmptyNameException,
			MuseumNotSetException, SetChildAsParentException {
		testSection(section.getId(), section.getName(), section.getDescription(), section.getParentId(), section.getMuseumId());
		DataAccess.getInstance().saveSection(section);
	}

	public static void deleteSection(Section section) {
		Collection<Section> sectionBySection = DataAccess.getInstance()
				.getSectionBySectionWithSubSections(section);
		ArrayList<Exhibit> exhibits = DataAccess.getInstance()
				.getExhibitBySectionWithSubSections(section);
		for (Exhibit exhibit : exhibits) {
			if (section.getParentId() != null) {
				exhibit.setSectionId(section.getParentId());
			} else {
				exhibit.setMuseumId(section.getMuseumId());
				exhibit.setSectionId(null);
			}
			DataAccess.getInstance().saveExhibit(exhibit);
		}
		for(Section subSection : sectionBySection) {
			DataAccess.getInstance().deleteSection(subSection);
		}

	}

	public static void updataSection(Long museumId, Long parentId,
			String name, String description, Section section) throws EmptyNameException, MuseumNotSetException, SetChildAsParentException {
		testSection(section.getId(), name, description, parentId, museumId);		
		section.setDescription(description);
		section.setName(name);
		section.setMuseumId(museumId);
		section.setParentId(parentId);
		
		DataAccess.getInstance().updateSection(section);
	}

	private static void testSection(long id, String name, String description, Long parentId, Long museumId) throws SetChildAsParentException, MuseumNotSetException, EmptyNameException {
		if (name.isEmpty()) {
			throw new EmptyNameException();
		} else if (null == museumId) {
			throw new MuseumNotSetException();
		} else if(id == parentId) {
			throw new SetChildAsParentException();
		}
	}
}
