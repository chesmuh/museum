package de.chesmuh.ordo.data.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;

import de.chesmuh.ordo.data.Database;
import de.chesmuh.ordo.data.Ordo;
import de.chesmuh.ordo.entitys.Tag;

/**
 * 
 * @author Chesmuh
 * 
 */
public class SQLQueryTag extends AbstractSQLQuery<Tag> {

	PreparedStatement updateList1;
	PreparedStatement updateList2;
	PreparedStatement deleteList;
	PreparedStatement updateList1Server;
	PreparedStatement updateList2Server;
	PreparedStatement deleteListServer;

	public SQLQueryTag() throws SQLException {
		super(new String[] {Ordo.Tag.CREATE_TABLE, Ordo.Tagged.CREATE_TABLE}, Ordo.Tag.ID, Ordo.Tag.TABLE_NAME,
				Ordo.Tag.NAME, Ordo.Tag.DELETED, Ordo.Tag.INSERTED);
	}

	@Override
	public void init() throws SQLException {
		super.init();
		String sql = "DELETE FROM " + Ordo.Tagged.TABLE_NAME + " WHERE "
				+ Ordo.Tagged.LABEL_ID + " = ?";
		this.updateList1 = Database.getInstance().getConnection()
				.prepareStatement(sql);
		sql = "INSERT INTO " + Ordo.Tagged.TABLE_NAME + "\n( "
				+ Ordo.Tagged.EXHIBIT_ID + "," + Ordo.Tagged.LABEL_ID + ")"
				+ "\nVALUES (?,?) \nON DUPLICATE KEY UPDATE "
				+ Ordo.Tagged.EXHIBIT_ID + "= VALUES("
				+ Ordo.Tagged.EXHIBIT_ID + ")";
		this.updateList2 = Database.getInstance().getConnection()
				.prepareStatement(sql);
		sql = "DELETE FROM " + Ordo.Tagged.TABLE_NAME + " WHERE "
				+ Ordo.Tagged.LABEL_ID + " = ?";
		this.deleteList = Database.getInstance().getConnection()
				.prepareStatement(sql);
	}

	@Override
	protected String prepareGetAll() {
		return "SELECT " + Ordo.Tag.ID + "," + Ordo.Tag.NAME + ","
				+ Ordo.Tag.DELETED + "," + Ordo.Tag.INSERTED + ","
				+ Ordo.Tagged.EXHIBIT_ID + "\nFROM " + Ordo.Tag.TABLE_NAME
				+ "\nLEFT JOIN " + Ordo.Tagged.TABLE_NAME + "\nON "
				+ Ordo.Tag.ID + " = " + Ordo.Tagged.LABEL_ID;
	}

	@Override
	public Collection<Tag> loadAll() throws SQLException {
		HashMap<Long, Tag> result = new HashMap<>();
		ResultSet resultSet = this.loadAll.executeQuery();

		while (resultSet.next()) {
			Tag label;
			Long id = resultSet.getLong(Ordo.Tag.ID);
			if (result.containsKey(id)) // label already added!
			{
				label = result.get(id);
				Long exhibit_id = resultSet.getLong(Ordo.Tagged.EXHIBIT_ID);
				if (exhibit_id != null) {
					label.addExhibit_id(exhibit_id);
				}
			} else {
				String name = resultSet.getString(Ordo.Tag.NAME);
				Timestamp deleted = resultSet.getTimestamp(Ordo.Tag.DELETED);
				Timestamp insert = resultSet.getTimestamp(Ordo.Tag.INSERTED);

				result.put(id, label = new Tag(id, insert, deleted, name));
				Long exhibit_id = resultSet.getLong(Ordo.Tagged.EXHIBIT_ID);
				if (exhibit_id != null) {
					label.addExhibit_id(exhibit_id);
				}
			}
		}
		return result.values();
	}

	@Override
	public void update(Tag model) throws SQLException {
		this.bindValues(this.update, model.getId(), model.getName(),
				model.getDeleted(), model.getInsert());

		this.update.executeUpdate();

		this.updateList1.setObject(1, model.getId());
		this.updateList1.execute();

		if (model.getExhibitIds().size() > 0) {
			for (Long exhibitId : model.getExhibitIds()) {
				this.updateList2.setObject(1, exhibitId);
				this.updateList2.setObject(2, model.getId());
				this.updateList2.addBatch();
			}
			this.updateList2.executeBatch();
		}
	}

	@Override
	public void store(Tag model) throws SQLException {
		this.bindValues(this.store, model.getName(), model.getDeleted(),
				model.getInsert());
		this.store.execute();
		this.setGeneratedKey(model);

		this.updateList1.setObject(1, model.getId());
		this.updateList1.execute();

		if (model.getExhibitIds().size() > 0) {
			for (Long exhibitId : model.getExhibitIds()) {
				this.updateList2.setObject(1, exhibitId);
				this.updateList2.setObject(2, model.getId());
				this.updateList2.addBatch();
			}
			this.updateList2.executeBatch();
		}
	}

	@Override
	public void delete(Tag model) throws SQLException {
		super.delete(model);
		this.bindValues(this.deleteList, model.getId());
		this.deleteList.execute();
	}
}
