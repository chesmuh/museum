package de.chesmuh.ordo.entitys;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.DataAccess;

public class Tag extends DatabaseElement {

	private String name;
	private Collection<Long> exhibit_ids;
	
	public Tag(String name) {
		super();
		this.name = name;
		this.exhibit_ids = new ArrayList<Long>();
	}
	
	public Tag(Long id, Timestamp inserted, Timestamp deleted, String name) {
		super(id, inserted, deleted);
		this.name = name;
		this.exhibit_ids = new ArrayList<Long>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Long> getExhibitIds() {
		return exhibit_ids;
	}

	public void setExhibit_ids(Collection<Long> exhibit_ids) {
		this.exhibit_ids = exhibit_ids;
	}
	
	public void addExhibit(Exhibit exhibit) {
		exhibit_ids.add(exhibit.getId());
	}
	
	public void removeExhibit(Exhibit exhibit) {
		exhibit_ids.remove(exhibit.getId());
	}
	
	public boolean containsExhibit(Exhibit exhibit) {
		return exhibit_ids.contains(exhibit.getId());
	}

	public void addExhibit_id(Long exhibit_id) {
		exhibit_ids.add(exhibit_id);		
	}

	public Collection<Exhibit> getExhibits() {
		ArrayList<Exhibit> ret = new ArrayList<Exhibit>();
		for(Long id : exhibit_ids) {
			if(id != null) {
				Exhibit exhibit = DataAccess.getInstance().getExhibitById(id);
				if(null != exhibit) {
					ret.add(exhibit);
				}
			}
		}
		return ret;
	}
	
}
