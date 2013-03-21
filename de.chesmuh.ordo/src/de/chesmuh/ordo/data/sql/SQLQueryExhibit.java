package de.chesmuh.ordo.data.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import de.chesmuh.ordo.data.Ordo;
import de.chesmuh.ordo.entitys.Exhibit;

/**
 * 
 * @author Chesmuh
 *
 */
public class SQLQueryExhibit extends AbstractSQLQuery<Exhibit> {

	public SQLQueryExhibit() throws SQLException {
		super(new String[] { Ordo.Exhibit.CREATE_TABLE },
				Ordo.Exhibit.ID, Ordo.Exhibit.TABLE_NAME, Ordo.Exhibit.NAME,
				Ordo.Exhibit.DESCRIPTION, Ordo.Exhibit.SECTION_ID,
				Ordo.Exhibit.MUSEUM_ID, Ordo.Exhibit.DELETED,
				Ordo.Exhibit.INSERTED);
	}

	@Override
	public Collection<Exhibit> loadAll() throws SQLException {
		ArrayList<Exhibit> result = new ArrayList<>();
		ResultSet resultSet = this.loadAll.executeQuery();
		while (resultSet.next()) {
			Long id = resultSet.getLong(Ordo.Exhibit.ID);
			String name = resultSet.getString(Ordo.Exhibit.NAME);
			String description = resultSet.getString(Ordo.Exhibit.DESCRIPTION);
			Long sectionId = resultSet.getLong(Ordo.Exhibit.SECTION_ID);
			Long museumId = resultSet.getLong(Ordo.Exhibit.MUSEUM_ID);
			Timestamp deleted = resultSet.getTimestamp(Ordo.Exhibit.DELETED);
			Timestamp inserted = resultSet.getTimestamp(Ordo.Exhibit.INSERTED);
			
			if(0L == museumId)  {
				museumId = null;
			}
			if(0L == sectionId) {
				sectionId = null;
			}
			if(null == description) {
				description = "";
			}
			result.add(new Exhibit(id, inserted, deleted, museumId, sectionId,
					name, description));
		}
		return result;
	}

	@Override
	public void update(Exhibit model) throws SQLException {
		this.bindValues(this.update, model.getId(), model.getName(),
				model.getDescription(), model.getSectionId(),
				model.getMuseumId(), model.getDeleted(), model.getInsert());
		this.update.executeUpdate();
	}

	@Override
	public void store(Exhibit model) throws SQLException {
		this.bindValues(this.store, model.getName(), model.getDescription(),
				model.getSectionId(), model.getMuseumId(), model.getDeleted(),
				model.getInsert());
		this.store.execute();
		this.setGeneratedKey(model);
	}
}
