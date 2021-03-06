package de.chesmuh.ordo.logic;

import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.exceptions.EmptyNameException;
import de.chesmuh.ordo.exceptions.MuseumNotSetException;
import de.chesmuh.ordo.exceptions.SectionNotSetException;
import de.chesmuh.ordo.exceptions.SetChildAsParentException;

public class LogicAccess {

	public static Section saveSection(long museum_id, Long parent_id,
			String name, String description) throws EmptyNameException,
			MuseumNotSetException, SetChildAsParentException {
		Section section = new Section(museum_id, parent_id, name, description);
		SectionLogic.saveSection(section);
		return section;
	}

	public static void deleteExhibits(ArrayList<Exhibit> toDelete) {
		DataAccess.getInstance().deleteExhibits(toDelete);
	}

	public static Exhibit saveExhibit(Long museumId, Long sectionId,
			String name, String description) throws SectionNotSetException,
			EmptyNameException {
		Exhibit exhibit = new Exhibit(museumId, sectionId, name, description);
		ExhibitLogic.saveExhibit(exhibit);
		return exhibit;
	}

	public static Tag saveTag(String name, Collection<Exhibit> exhibits)
			throws EmptyNameException {
		Tag tag = new Tag(name);
		TagLogic.saveTag(tag);
		TagLogic.addExhibitsToTag(tag, exhibits);
		return tag;
	}

	public static void deleteTags(ArrayList<Tag> toDelete) {
		DataAccess.getInstance().deleteTags(toDelete);
	}

	public static void deleteMuseum(Museum museum) {
		MuseumLogic.deleteMuseum(museum);
	}

	public static void tagExhibit(Exhibit exhibit, Tag tag) {
		TagLogic.addExhibitToTag(tag, exhibit);
	}

	public static void deleteSection(Section section) {
		SectionLogic.deleteSection(section);
	}

	public static Museum saveMuseum(String name, String description) throws EmptyNameException {
		Museum museum = new Museum(name, description);
		MuseumLogic.saveMuseum(museum);
		return museum;
	}

	public static Museum updateMuseum(String name, String description, Museum museum) throws EmptyNameException {
		MuseumLogic.updateMuseum(name, description, museum);
		return museum;
	}

	public static Tag updateTag(String name, Tag tag) throws EmptyNameException {
		TagLogic.updateTag(tag, name);
		return tag;
	}

	public static Section updateSection(Long museumId, Long sectionId, String name,
			String description, Section section) throws EmptyNameException, MuseumNotSetException, SetChildAsParentException {
		SectionLogic.updataSection(museumId, sectionId, name, description, section);
		return section;
	}

	public static Exhibit updateExhibit(Long museumId, Long sectionId, String name, String description,
			Exhibit exhibit) throws EmptyNameException, SectionNotSetException {
		ExhibitLogic.updateExhibit(museumId, sectionId, name, description, exhibit);
		return exhibit;
	}

}
