package de.chesmuh.ordo.data.manager;

import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.sql.SQLQueryExhibit;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Museum;
import de.chesmuh.ordo.entitys.Section;

/**
 * 
 * @author chesmuh
 *
 */
public class ExhibitManager extends AbstractManager<Exhibit> {

	public ExhibitManager() {
		super(SQLQueryExhibit.class);
	}

	public Collection<Exhibit> getBySection(Section section) {
		ArrayList<Exhibit> ret = new ArrayList<Exhibit>();
		for (Exhibit e : this.getAll()) {
			if (e.getSectionId() == section.getId()) {
				ret.add(e);
			}
		}
		return ret;
	}

	public Collection<Exhibit> getByMuseumWithSectionNull(Museum museum) {
		ArrayList<Exhibit> ret = new ArrayList<Exhibit>();
		for (Exhibit e : this.getAll()) {
			if (null == e.getSectionId() && museum.getId() == e.getMuseumId()) {
				ret.add(e);
			}
		}
		return ret;
	}
}
