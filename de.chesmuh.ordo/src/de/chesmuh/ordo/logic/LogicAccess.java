package de.chesmuh.ordo.logic;

import java.util.ArrayList;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.MuseumNotSetException;
import de.chesmuh.ordo.exceptions.SectionNotSetException;

public class LogicAccess {

	public static Section saveSection(long museum_id, Long parent_id,
			String name, String description) throws EmptyNameException, MuseumNotSetException {
		Section section = new Section(museum_id, parent_id, name, description);
		SectionLogic.saveSection(section);
		return section;
	}

	public static void deleteExhibits(ArrayList<Exhibit> toDelete) {
		DataAccess.getInstance().deleteExhibits(toDelete);
	}

	public static Exhibit saveExhibit(Long museumId, Long sectionId,
			String name, String description) throws SectionNotSetException, EmptyNameException {
		Exhibit exhibit = new Exhibit(museumId, sectionId, name, description);
		ExhibitLogic.saveExhibit(exhibit);
		return exhibit;
	}

	public static Tag saveTag(String name, ArrayList<Exhibit> exhibits) throws EmptyNameException {
		Tag tag = new Tag(name);
		TagLogic.saveTag(tag);
		TagLogic.addExhibitToTag(tag, exhibits);
		return tag;
	}

	public static void deleteTags(ArrayList<Tag> toDelete) {
		DataAccess.getInstance().deleteTags(toDelete);
	}

}
