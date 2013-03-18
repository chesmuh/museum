package de.chesmuh.ordo.data.manager;

import de.chesmuh.ordo.data.manager.AbstractManager;
import de.chesmuh.ordo.data.sql.SQLQueryMuseum;
import de.chesmuh.ordo.entitys.Museum;

/**
 * 
 * @author Chesmuh
 *
 */
public class MuseumManager extends AbstractManager<Museum> {

	public MuseumManager() {
		super(SQLQueryMuseum.class);
	}
}
