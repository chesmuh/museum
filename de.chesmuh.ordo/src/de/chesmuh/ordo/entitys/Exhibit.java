package de.chesmuh.ordo.entitys;

import java.sql.Timestamp;

import de.chesmuh.ordo.data.DataAccess;


public class Exhibit extends DatabaseElement {
	
	private Long museumId;
	private Long sectionId;
	private String name;
	private String description;
	
	public Exhibit(Long museumId, Long sectionId, String name,
			String description) {
		super();
		this.museumId = museumId;
		this.sectionId = sectionId;
		this.name = name;
		this.description = description;
	}

	public Exhibit(Long id, Timestamp inserted, Timestamp deleted,
			Long museumId, Long sectionId, String name, String description) {
		super(id, inserted, deleted);
		this.museumId = museumId;
		this.sectionId = sectionId;
		this.name = name;
		this.description = description;
	}

	public Long getMuseumId() {
		return museumId;
	}

	public void setMuseumId(Long museumId) {
		this.museumId = museumId;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
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

	public Section getSection() {
		if(null == sectionId) {
			return null;
		}
		return DataAccess.getInstance().getSectionById(sectionId);
	}

	public Museum getMuseum() {
		if(null == museumId) {
			return null;
		}
		return DataAccess.getInstance().getMuseumById(museumId);
	}
	
}
