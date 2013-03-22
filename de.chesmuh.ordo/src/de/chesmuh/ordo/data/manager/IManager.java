package de.chesmuh.ordo.data.manager;

import java.util.Collection;

import de.chesmuh.ordo.entitys.DatabaseElement;

/**
 * 
 * @author Chesmuh
 *
 * @param <Element> {@link DatabaseElement}
 */
public interface IManager<Element extends DatabaseElement> {

	Collection<Element> getAll();
	
	void store(Element model);
	
	void update(Element model);
	
	void markAsDeleted(Element model);

	void loadAll();

	Collection<Element> getAllDeleted();

	Element getbyId(Long id);

	void delete(Element model);
	
}
