package de.chesmuh.ordo.entity;

import java.sql.Timestamp;

/**
 * 
 * @author Chesmuh
 *
 */
public class Section extends DatabaseElement {

	private Long museum_id;
	private Long parent_id;
	private String name;
	private String description;
	
	public Section(Long id, Timestamp inserted, Timestamp deleted,
			Long museum_id, Long parent_id, String name, String description) {
		super(id, inserted, deleted);
		this.museum_id = museum_id;
		this.parent_id = parent_id;
		this.name = name;
		this.description = description;
	}

	public Section(Long museum_id, Long parent_id, String name,
			String description) {
		super();
		this.museum_id = museum_id;
		this.parent_id = parent_id;
		this.name = name;
		this.description = description;
	}

	public Long getMuseum_id() {
		return museum_id;
	}

	public void setMuseum_id(Long museum_id) {
		this.museum_id = museum_id;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
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
