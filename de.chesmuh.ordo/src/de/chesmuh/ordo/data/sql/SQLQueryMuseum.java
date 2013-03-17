package de.chesmuh.ordo.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.Ordo;
import de.chesmuh.ordo.entity.Museum;

public class SQLQueryMuseum extends AbstractSQLQuery<Museum> {

	public SQLQueryMuseum() throws SQLException {
		super(new String[] { de.chesmuh.ordo.data.Ordo.Museum.CREATE_TABLE },
				Ordo.Museum.ID, Ordo.Museum.TABLE_NAME, //
				Ordo.Museum.NAME, //
				Ordo.Museum.DESCRIPTION, // 
				Ordo.Museum.DELETED, //
				Ordo.Museum.INSERTED);
	}

	@Override
	public Collection<Museum> loadAll() throws SQLException {
		ArrayList<Museum> result = new ArrayList<>();
		ResultSet resultSet = this.loadAll.executeQuery();
		while (resultSet.next()) {
			Long id = resultSet.getLong(Ordo.Museum.ID);
			Timestamp deleted = resultSet.getTimestamp(Ordo.Museum.DELETED);
			String name = resultSet.getString(Ordo.Museum.NAME);
			String description = resultSet.getString(Ordo.Museum.DESCRIPTION);
			Timestamp inserted = resultSet.getTimestamp(Ordo.Museum.INSERTED);
			result.add(new Museum(id, name, description, inserted, deleted));
		}
		return result;
	}

	@Override
	public void update(Museum model) throws SQLException {
		this.bindValues(this.update, model.getId(), model.getName(),
				model.getDescription(), model.getDeleted(), model.getInsert());
		this.update.executeUpdate();
	}

	@Override
	public void store(Museum model) throws SQLException {
		this.bindValues(this.store, //
				model.getName(), //
				model.getDescription(), //
				model.getDeleted(), //
				model.getInsert());
		this.store.execute();
		this.setGeneratedKey(model);
	}
}
