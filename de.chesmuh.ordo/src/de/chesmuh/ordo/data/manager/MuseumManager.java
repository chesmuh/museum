package de.chesmuh.ordo.data.manager;

import java.util.HashMap;

import de.chesmuh.ordo.entity.Museum;

public class MuseumManager {
	
	private HashMap<Long, Museum> values = new HashMap<Long, Museum>();
	
	public void getAll() {
		
	}
	
	public Museum get(Long id) {
		return values.get(id);
	}
}
