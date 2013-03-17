package de.chesmuh.ordo.data.manager;

import java.util.Collection;

import de.chesmuh.ordo.entity.DatabaseElement;
import de.chesmuh.ordo.exceptions.ModelAlreadyDeletedException;

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
	
	void markAsDeleted(Element model) throws ModelAlreadyDeletedException;

	void loadAll();

	Collection<Element> getAllDeleted();

	Element getbyId(Long id);

	void delete(Element model);
	
}
