package de.chesmuh.ordo.data.manager;

import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.sql.SQLQuerySection;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;

/**
 * 
 * @author Chesmuh
 * 
 */
public class SectionManager extends AbstractManager<Section> {

	public SectionManager() {
		super(SQLQuerySection.class);
	}

	public Collection<Section> getByMuseum(Museum museum) {
		ArrayList<Section> ret = new ArrayList<Section>();
		for (Section s : this.getAll()) {
			if (s.getMuseumId() == museum.getId() && !s.isDeleted()) {
				ret.add(s);
			}
		}
		return ret;
	}

	public Collection<Section> getBySection(Section section) {
		ArrayList<Section> ret = new ArrayList<Section>();
		for (Section s : this.getAll()) {
			if (s.getParentId() == section.getId() && !s.isDeleted()) {
				ret.add(s);
			}
		}
		return ret;
	}

	public Collection<Section> getByMuseumWithParentNull(Museum museum) {
		ArrayList<Section> ret = new ArrayList<Section>();
		for (Section s : this.getAll()) {
			if (s.getMuseumId() == museum.getId() && s.getParentId() == null && !s.isDeleted()) {
				ret.add(s);
			}
		}
		return ret;
	}

}