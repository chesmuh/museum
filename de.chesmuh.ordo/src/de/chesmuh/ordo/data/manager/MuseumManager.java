package de.chesmuh.ordo.data.manager;

import de.chesmuh.ordo.data.sql.SQLQueryMuseum;
import de.chesmuh.ordo.entity.Museum;

public class MuseumManager extends AbstractManager<Museum> {

	public MuseumManager() {
		super(SQLQueryMuseum.class);
	}
}
