package de.chesmuh.ordo.data.manager;

import java.util.Collection;
import java.util.HashSet;

import de.chesmuh.ordo.data.sql.SQLQuerySection;
import de.chesmuh.ordo.entity.Museum;
import de.chesmuh.ordo.entity.Section;

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
		HashSet<Section> ret = new HashSet<Section>();
		for (Section s : this.getAll()) {
			if (s.getMuseum_id() == museum.getId()) {
				ret.add(s);
			}
		}
		return ret;
	}

	public Collection<Section> getBySection(Section section) {
		HashSet<Section> ret = new HashSet<Section>();
		for (Section s : this.getAll()) {
			if (s.getParent_id() == section.getId()) {
				ret.add(s);
			}
		}
		return ret;
	}

	public Collection<Section> getByMuseumWithParentNull(Museum museum) {
		HashSet<Section> ret = new HashSet<Section>();
		for (Section s : this.getAll()) {
			if (s.getMuseum_id() == museum.getId()
					&& s.getParent_id() == null) {
				ret.add(s);
			}
		}
		return ret;
	}

}