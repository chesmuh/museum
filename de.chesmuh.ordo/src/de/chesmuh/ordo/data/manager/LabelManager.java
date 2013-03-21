package de.chesmuh.ordo.data.manager;

import de.chesmuh.ordo.data.sql.SQLQueryLabel;
import de.chesmuh.ordo.entitys.Label;

/**
 * 
 * @author Chesmuh
 * 
 */
public class LabelManager extends AbstractManager<Label> {

	public LabelManager() {
		super(SQLQueryLabel.class);
	}

}