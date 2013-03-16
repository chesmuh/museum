package de.chesmuh.ordo.entity;

import java.sql.Timestamp;

public class DatabaseElement {

	private Long id;
	private Timestamp deleted;
	
	public DatabaseElement() {
		this.id = null;
		this.deleted = null;
	}
	
	public DatabaseElement(Long id, Timestamp deleted) {
		this.id = id;
		this.deleted = deleted;
	}
	
	public Long getID() {
		return this.id;
	}
	
	public Timestamp getDeleted() {
		return this.deleted;
	}
	
	public void setID(Long id) {
		this.id = id;
	}
	
	public void setDeleted(Timestamp deleted) {
		this.deleted = deleted;
	}
}
