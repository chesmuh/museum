package de.chesmuh.ordo.logic;

import de.chesmuh.ordo.data.DataAccess;
import de.chesmuh.ordo.entitys.Section;


public class LogicAccess {

	public static Section addSection(long museum_id, Long section_id, String name, String description) {
		if(name.isEmpty()) {
			// Name is Empty
		} else if(null == DataAccess.getInstance().getMuseumById(museum_id)) {
			// Can't find Museum
		} else if(section_id != null && null == DataAccess.getInstance().getSectionById(section_id)) {
			// Cant' find Section
		} 
		Section section = new Section(museum_id, section_id, name, description);
		DataAccess.getInstance().saveSection(section);
		return section;
	}

}
