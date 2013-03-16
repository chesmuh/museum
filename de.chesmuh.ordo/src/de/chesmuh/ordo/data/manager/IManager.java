package de.chesmuh.ordo.data.manager;

import java.awt.List;

import de.chesmuh.ordo.entity.DatabaseElement;

public interface IManager<Model extends DatabaseElement> {

	public List getAll();
	
	public Model get(Long id);
	
	public void insert(Model model);
	
	public void update(Model model);
	
	public void delete(Model model);
	
}
