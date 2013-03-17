package de.chesmuh.ordo.entity;

import java.sql.Timestamp;

public class DatabaseElement {

	private Long id;
	private Timestamp deleted;
	private Timestamp inserted;
	
	public DatabaseElement() {
		this.id = null;
		this.deleted = null;
		this.inserted = null;
	}
	
	public DatabaseElement(Long id, Timestamp inserted, Timestamp deleted) {
		this.id = id;
		this.deleted = deleted;
		this.inserted = inserted;
	}
	
	public Long getId() {
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
	
	public Timestamp getInsert() {
		return this.inserted;
	}
	
	public void setInsert(Timestamp insert) {
		this.inserted = insert;
	}
	
	public boolean isDeleted() {
		return this.deleted != null;
	}
	
}
