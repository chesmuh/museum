package de.chesmuh.ordo.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.Ordo;
import de.chesmuh.ordo.entity.Section;

/**
 * 
 * @author Chesmuh
 *
 */
public class SQLQuerySection extends AbstractSQLQuery<Section> {

	public SQLQuerySection() throws SQLException {
		super(new String[] { de.chesmuh.ordo.data.Ordo.Section.CREATE_TABLE },
				Ordo.Section.ID, Ordo.Section.TABLE_NAME, Ordo.Section.NAME,
				Ordo.Section.DESCRIPTION, Ordo.Section.PARENT_ID,
				Ordo.Section.MUSEUM_ID, Ordo.Section.DELETED,
				Ordo.Section.INSERTED);
	}

	@Override
	public Collection<Section> loadAll() throws SQLException {
		ArrayList<Section> result = new ArrayList<>();
		ResultSet resultSet = this.loadAll.executeQuery();
		while (resultSet.next()) {
			Long id = resultSet.getLong(Ordo.Section.ID);
			String name = resultSet.getString(Ordo.Section.NAME);
			String description = resultSet.getString(Ordo.Section.DESCRIPTION);
			Long parent_id = resultSet.getLong(Ordo.Section.PARENT_ID);
			Long museum_id = resultSet.getLong(Ordo.Section.MUSEUM_ID);
			Timestamp deleted = resultSet.getTimestamp(Ordo.Section.DELETED);
			Timestamp inserted = resultSet.getTimestamp(Ordo.Section.INSERTED);
			result.add(new Section(id, inserted, deleted, museum_id, parent_id,
					name, description));
		}
		return result;
	}

	@Override
	public void update(Section model) throws SQLException {
		this.bindValues(this.update, model.getId(), model.getName(),
				model.getDescription(), model.getParent_id(),
				model.getMuseum_id(), model.getDeleted(), model.getInsert());
		this.update.executeUpdate();
	}

	@Override
	public void store(Section model) throws SQLException {
		this.bindValues(this.store, model.getName(), model.getDescription(),
				model.getParent_id(), model.getMuseum_id(), model.getDeleted(),
				model.getInsert());
		this.store.execute();
		this.setGeneratedKey(model);
	}
}
