package de.chesmuh.ordo.entitys;

import java.sql.Timestamp;

import de.chesmuh.ordo.data.DataAccess;

/**
 * 
 * @author Chesmuh
 *
 */
public class Section extends DatabaseElement implements Comparable<Section> {

	private long museum_id;
	private Long parent_id;
	private String name;
	private String description;
	
	public Section(Long id, Timestamp inserted, Timestamp deleted,
			long museum_id, Long parent_id, String name, String description) {
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

	@Override
	public int compareTo(Section o) {
		return this.name.compareTo(o.name);
		
	}

	public Museum getMuseum() {
		return DataAccess.getInstance().getMuseumById(this.museum_id);
	}

	public Section getParent() {
		if(parent_id == null) {
			return null;
		}
		return DataAccess.getInstance().getSectionById(parent_id);
	}
		
}
