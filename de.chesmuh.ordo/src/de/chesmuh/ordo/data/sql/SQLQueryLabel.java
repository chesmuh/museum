package de.chesmuh.ordo.data.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;

import de.chesmuh.ordo.data.Database;
import de.chesmuh.ordo.data.Ordo;
import de.chesmuh.ordo.entitys.Label;

/**
 * 
 * @author Chesmuh
 * 
 */
public class SQLQueryLabel extends AbstractSQLQuery<Label> {

	PreparedStatement updateList1;
	PreparedStatement updateList2;
	PreparedStatement deleteList;
	PreparedStatement updateList1Server;
	PreparedStatement updateList2Server;
	PreparedStatement deleteListServer;

	public SQLQueryLabel() throws SQLException {
		super(new String[] {Ordo.Label.CREATE_TABLE, Ordo.Labeled.CREATE_TABLE}, Ordo.Label.ID, Ordo.Label.TABLE_NAME,
				Ordo.Label.NAME, Ordo.Label.DELETED, Ordo.Label.INSERTED);
	}

	@Override
	public void init() throws SQLException {
		super.init();
		String sql = "DELETE FROM " + Ordo.Labeled.TABLE_NAME + " WHERE "
				+ Ordo.Labeled.LABEL_ID + " = ?";
		this.updateList1 = Database.getInstance().getConnection()
				.prepareStatement(sql);
		sql = "INSERT INTO " + Ordo.Labeled.TABLE_NAME + "\n( "
				+ Ordo.Labeled.EXHIBIT_ID + "," + Ordo.Labeled.LABEL_ID + ")"
				+ "\nVALUES (?,?) \nON DUPLICATE KEY UPDATE "
				+ Ordo.Labeled.EXHIBIT_ID + "= VALUES("
				+ Ordo.Labeled.EXHIBIT_ID + ")";
		this.updateList2 = Database.getInstance().getConnection()
				.prepareStatement(sql);
		sql = "DELETE FROM " + Ordo.Labeled.TABLE_NAME + " WHERE "
				+ Ordo.Labeled.LABEL_ID + " = ?";
		this.deleteList = Database.getInstance().getConnection()
				.prepareStatement(sql);
	}

	@Override
	protected String prepareGetAll() {
		return "SELECT " + Ordo.Label.ID + "," + Ordo.Label.NAME + ","
				+ Ordo.Label.DELETED + "," + Ordo.Label.INSERTED + ","
				+ Ordo.Labeled.EXHIBIT_ID + "\nFROM " + Ordo.Label.TABLE_NAME
				+ "\nLEFT JOIN " + Ordo.Labeled.TABLE_NAME + "\nON "
				+ Ordo.Label.ID + " = " + Ordo.Labeled.LABEL_ID;
	}

	@Override
	public Collection<Label> loadAll() throws SQLException {
		HashMap<Long, Label> result = new HashMap<>();
		ResultSet resultSet = this.loadAll.executeQuery();

		while (resultSet.next()) {
			Label label;
			Long id = resultSet.getLong(Ordo.Label.ID);
			if (result.containsKey(id)) // label already added!
			{
				label = result.get(id);
				Long exhibit_id = resultSet.getLong(Ordo.Labeled.EXHIBIT_ID);
				if (exhibit_id != null) {
					label.addExhibit_id(exhibit_id);
				}
			} else {
				String name = resultSet.getString(Ordo.Label.NAME);
				Timestamp deleted = resultSet.getTimestamp(Ordo.Label.DELETED);
				Timestamp insert = resultSet.getTimestamp(Ordo.Label.INSERTED);

				result.put(id, label = new Label(id, insert, deleted, name));
				Long exhibit_id = resultSet.getLong(Ordo.Labeled.EXHIBIT_ID);
				if (exhibit_id != null) {
					label.addExhibit_id(exhibit_id);
				}
			}
		}
		return result.values();
	}

	@Override
	public void update(Label model) throws SQLException {
		this.bindValues(this.update, model.getId(), model.getName(),
				model.getDeleted(), model.getInsert());

		this.update.executeUpdate();

		this.updateList1.setObject(1, model.getId());
		this.updateList1.execute();

		if (model.getExhibit_ids().size() > 0) {
			for (Long exhibitId : model.getExhibit_ids()) {
				this.updateList2.setObject(1, exhibitId);
				this.updateList2.setObject(2, model.getId());
				this.updateList2.addBatch();
			}
			this.updateList2.executeBatch();
		}
	}

	@Override
	public void store(Label model) throws SQLException {
		this.bindValues(this.store, model.getName(), model.getDeleted(),
				model.getInsert());
		this.store.execute();
		this.setGeneratedKey(model);

		this.updateList1.setObject(1, model.getId());
		this.updateList1.execute();

		if (model.getExhibit_ids().size() > 0) {
			for (Long exhibitId : model.getExhibit_ids()) {
				this.updateList2.setObject(1, exhibitId);
				this.updateList2.setObject(2, model.getId());
				this.updateList2.addBatch();
			}
			this.updateList2.executeBatch();
		}
	}

	@Override
	public void delete(Label model) throws SQLException {
		super.delete(model);
		this.bindValues(this.deleteList, model.getId());
		this.deleteList.execute();
	}
}
