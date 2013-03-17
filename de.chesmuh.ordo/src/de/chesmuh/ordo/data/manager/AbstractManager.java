package de.chesmuh.ordo.data.manager;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.chesmuh.ordo.data.sql.ISqlQuery;
import de.chesmuh.ordo.entity.DatabaseElement;
import de.chesmuh.ordo.exceptions.ModelAlreadyDeletedException;

public abstract class AbstractManager<Element extends DatabaseElement> implements
		IManager<Element> {

	protected final ISqlQuery<Element> sqlQuery;
	protected Map<Long, Element> values = new HashMap<Long, Element>();

	public AbstractManager(Class<? extends ISqlQuery<Element>> clazz) {
		try {
			this.sqlQuery = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new IllegalStateException("Could not create SqlQueryClass!");
		}
	}

	@Override
	public void update(Element model) {
		try {
			this.sqlQuery.update(model);
		} catch (SQLTimeoutException e) {
			// TODO
		} catch (SQLException e) {
			throw new IllegalStateException("Unexpected SQLError!", e);
		}

	}

	@Override
	public void store(Element model) {
		try {
			model.setInsert(new Timestamp(System.currentTimeMillis()));
			this.sqlQuery.store(model);
			this.values.put(model.getId(), model);

		} catch (SQLTimeoutException e) {
			// TODO
		} catch (SQLException e) {
			throw new IllegalStateException("Unexpected SQLError!", e);
		}
	}

	@Override
	public void markAsDeleted(Element model) throws ModelAlreadyDeletedException {
		try {
			if (model.isDeleted()) {
				throw new ModelAlreadyDeletedException("The model of "
						+ model.getClass().getName() + " was already deleted! ");
			}
			model.setDeleted(new Timestamp(System.currentTimeMillis()));
			this.sqlQuery.update(model);
		} catch (SQLTimeoutException e) {
			// TODO
		} catch (SQLException e) {
			throw new IllegalStateException("Unexpected SQLError!", e);
		}
	}

	public void loadAll() {
		try {
			this.values.clear();
			
			Collection<Element> collection = this.sqlQuery.loadAll();
			for (Element element :  collection) {
				this.values.put(element.getId(), element);
			}
		} catch (SQLTimeoutException e) {
			// TODO
		} catch (SQLException e) {
			throw new IllegalStateException("Unexpected SQLError!", e);
		}
	}

	@Override
	public Collection<Element> getAll() {
		HashSet<Element> result = new HashSet<>();
		for (Element step : this.values.values()) {
			if (!step.isDeleted()) {
				result.add(step);
			}
		}
		return result;
	}

	@Override
	public Collection<Element> getAllDeleted() {
		HashSet<Element> result = new HashSet<>();
			for (Element step : this.values.values()) {
				if (step.isDeleted()) {
					result.add(step);
				}
			}
		return result;
	}

	@Override
	public Element getbyId(Long id) {
		return this.values.get(id);
	}

	@Override
	public void delete(Element model) {
		try {
			this.sqlQuery.delete(model);
		} catch (SQLTimeoutException e) {
			// TODO
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
