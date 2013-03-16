package de.chesmuh.ordo.entity;

import java.sql.Timestamp;

public class Museum extends DatabaseElement {
	
	private String name;
	private String description;
	
	public Museum(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public Museum(Long id, Timestamp deleted, String name, String description) {
		super(id, deleted);
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
