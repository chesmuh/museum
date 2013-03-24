package de.chesmuh.ordo.data.manager;

import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.sql.SQLQueryTag;
import de.chesmuh.ordo.entitys.Exhibit;
import de.chesmuh.ordo.entitys.Tag;

/**
 * 
 * @author Chesmuh
 * 
 */
public class TagManager extends AbstractManager<Tag> {

	public TagManager() {
		super(SQLQueryTag.class);
	}

	public Collection<Tag> getByExhibit(Exhibit exhibit) {
		ArrayList<Tag> ret = new ArrayList<Tag>();
		for(Tag tag : this.values.values()) {
			if(tag.getExhibitIds().contains(exhibit.getId())) {
				ret.add(tag);
			}
		}
		return ret;
		
	}

}