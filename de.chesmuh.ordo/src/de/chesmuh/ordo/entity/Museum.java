package de.chesmuh.ordo.entity;

import java.sql.Timestamp;

/**
 * 
 * @author Chesmuh
 *
 */
public class Museum extends DatabaseElement {

	private String name;
	private String description;

	public Museum(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public Museum(Long id, String name, String description, Timestamp inserted,
			Timestamp deleted) {
		super(id, inserted, deleted);

		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
