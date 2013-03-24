package de.chesmuh.ordo.logic;

import java.util.Collection;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Tag;
import de.chesmuh.ordo.exceptions.EmptyNameException;

public class TagLogic {

	private TagLogic() {

	}

	public static void saveTag(Tag tag) throws EmptyNameException {
		if (tag.getName().isEmpty()) {
			throw new EmptyNameException();
		}
		DataAccess.getInstance().saveTag(tag);
	}

	public static void addExhibitsToTag(Tag tag, Collection<Exhibit> exhibits) {
		for (Exhibit exhibit : exhibits) {
			if (!tag.getExhibit_ids().contains(exhibit.getId())) {
				DataAccess.getInstance().addExhibitToTag(tag, exhibit);
			}
		}
	}

	public static void addExhibitToTag(Tag tag, Exhibit exhibit) {
		if (!tag.getExhibit_ids().contains(exhibit.getId())) {
			DataAccess.getInstance().addExhibitToTag(tag, exhibit);
		}
	}

	public static void updateTag(Tag tag, String name) throws EmptyNameException {
		if (name.isEmpty()) {
			throw new EmptyNameException();
		}
		tag.setName(name);
		DataAccess.getInstance().updateTag(tag);
	}
}
