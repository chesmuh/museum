package de.chesmuh.ordo.data.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.chesmuh.ordo.data.Database;
import de.chesmuh.ordo.entity.DatabaseElement;

public abstract class AbstractSQLQuery<Element extends DatabaseElement> implements
		ISqlQuery<Element> {

	protected PreparedStatement store;
	protected PreparedStatement delete;
	protected PreparedStatement update;
	protected PreparedStatement loadAll;
	protected PreparedStatement deleteBackup;
	protected PreparedStatement updateServer;
	protected PreparedStatement loadAllBackup;
	public final String[] createTableSQLs;
	private final String tableName;
	private final String keyName;
	private final String[] valueNames;

	public AbstractSQLQuery(String[] createTableSQLs, String key, String table,
			String... values) throws SQLException {
		this.createTableSQLs = createTableSQLs;
		this.keyName = key;
		this.tableName = table;
		this.valueNames = values;
		this.createTables();
		this.init();
	}

	@Override
	public void createTables() throws SQLException {
		for (String sql : createTableSQLs) {
			Database.getInstance().getConnection().createStatement()
					.execute(sql);
		}
	}

	@Override
	public void init() throws SQLException {
		this.prepareStatements();
	}

	@Override
	public void prepareStatements() throws SQLException {
		this.loadAll = Database.getInstance().getConnection()
				.prepareStatement(this.prepareGetAll());
		this.update = Database.getInstance().getConnection()
				.prepareStatement(this.prepareUpdate());
		this.store = Database
				.getInstance()
				.getConnection()
				.prepareStatement(this.prepareStore(),
						Statement.RETURN_GENERATED_KEYS);
		this.delete = Database.getInstance().getConnection()
				.prepareStatement(this.prepareDelete());
	}

	protected String prepareDelete() {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(tableName).append(" WHERE ")
				.append(keyName).append(" = ?");
		return sb.toString();
	}

	protected String prepareUpdate() {
		if (valueNames.length == 0) {
			throw new IllegalStateException("Values to update cannot be none!");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append("(\n").append("\t")
				.append(keyName);
		for (int i = 0; i < valueNames.length; ++i) {
			sb.append(",\n\t").append(valueNames[i]);
		}
		sb.append(")\nVALUES (?");
		for (int i = 0; i < valueNames.length; ++i) {
			sb.append(",?");
		}
		sb.append(") \nON DUPLICATE KEY UPDATE ").append(keyName)
				.append("=VALUES(").append(keyName).append(")");
		for (int i = 0; i < valueNames.length; ++i) {
			sb.append(",\n").append(valueNames[i]).append("=VALUES(")
					.append(valueNames[i]).append(')');
		}
		return sb.toString();
	}

	protected String prepareStore() {
		if (valueNames.length == 0) {
			throw new IllegalStateException("Values to store cannot be none!");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append("(\n").append("\t")
				.append(valueNames[0]);
		for (int i = 1; i < valueNames.length; ++i) {
			sb.append(",\n\t").append(valueNames[i]);
		}
		sb.append(")\nVALUES (?");
		for (int i = 1; i < valueNames.length; ++i) {
			sb.append(",?");
		}
		sb.append(") \nON DUPLICATE KEY UPDATE ").append(valueNames[0])
				.append("=VALUES(").append(valueNames[0]).append(")");
		for (int i = 1; i < valueNames.length; ++i) {
			sb.append(",\n").append(valueNames[i]).append("=VALUES(")
					.append(valueNames[i]).append(')');
		}
		return sb.toString();
	}

	protected String prepareGetAll() {
		return "SELECT * FROM " + tableName;
	}

	public void bindValues(PreparedStatement statement, Object... args)
			throws SQLException {
		for (int i = 0; i < args.length; ++i) {
			statement.setObject(i + 1, args[i]);
		}
	}

	protected void setGeneratedKey(Element element) throws SQLException {
		ResultSet resultSet = this.store.getGeneratedKeys();
		resultSet.next();
		element.setID(resultSet.getLong(1));
	}

	@Override
	public void delete(Element element) throws SQLException {
		this.bindValues(delete, element.getId());
		this.delete.execute();
	}

	@Override
	public Element getById(Long id) throws SQLException {
		throw new UnsupportedOperationException("Not supported.");
	}
}
