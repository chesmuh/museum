package de.chesmuh.ordo.data.manager;

import de.chesmuh.ordo.data.sql.SQLQueryTag;
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

}