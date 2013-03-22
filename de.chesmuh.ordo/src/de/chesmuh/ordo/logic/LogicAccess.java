package de.chesmuh.ordo.logic;

import java.util.ArrayList;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.MuseumNotSetException;

public class LogicAccess {

	public static Section saveSection(long museum_id, Long parent_id,
			String name, String description) throws EmptyNameException, MuseumNotSetException {
		Section section = new Section(museum_id, parent_id, name, description);
		SectionLogic.saveSection(section);
		return section;
	}

	public static Exhibit saveExhibit(Long museumId, Long sectionId,
			String name, String description) {
		if (null == museumId && null == sectionId) {
			// No section set
		} else if (null != museumId
				&& null == (DataAccess.getInstance().getMuseumById(museumId))) {
			// Can't find Museum
		} else if (null != sectionId
				&& null == (DataAccess.getInstance().getSectionById(sectionId))) {
			// Can't find Section
		} else if (name.isEmpty()) {
			// Name is Empty
		}

		Exhibit exhibit = new Exhibit(museumId, sectionId, name, description);
		DataAccess.getInstance().saveExhibit(exhibit);
		return exhibit;
	}

	public static void deleteExhibits(ArrayList<Exhibit> toDelete) {
		DataAccess.getInstance().deleteExhibits(toDelete);
	}

}
